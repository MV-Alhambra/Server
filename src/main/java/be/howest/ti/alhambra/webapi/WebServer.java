package be.howest.ti.alhambra.webapi;

import be.howest.ti.alhambra.exceptions.AlhambraException;
import be.howest.ti.alhambra.logic.exceptions.AlhambraEntityNotFoundException;
import be.howest.ti.alhambra.logic.exceptions.AlhambraGameRuleException;
import be.howest.ti.alhambra.webapi.exceptions.UrlTokenFormatException;
import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.api.validation.ValidationException;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WebServer extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebServer.class);

    private static final int PORT_FALLBACK = 8080;
    private static final String OPEN_API3_FILE_FALLBACK = "alhambra-spec.yaml";

    public static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";

    private final AlhambraOpenAPI3Bridge bridge;


    WebServer(AlhambraOpenAPI3Bridge bridge) {
        this.bridge = bridge;
    }

    public WebServer() {
        this(new DefaultAlhambraOpenAPI3Bridge());
    }

    @Override
    public void start(Future<Void> startFuture) {
        ConfigRetriever retriever = ConfigRetriever.create(vertx);

        retriever.getConfig(ar -> {
            if (ar.failed()) {
                LOGGER.warn("Can not read config");
                // Failed to retrieve the configuration
            } else {
                JsonObject config = ar.result();
                //config.put("openApi3Spec","src/main/resources/alhambra-spec.yaml");//seems to be working, it loads our custom spec in
                int port = config.getInteger("port", PORT_FALLBACK);
                String spec = config.getString("openApi3Spec", OPEN_API3_FILE_FALLBACK);
                LOGGER.info("Starting web server with config: port {0} (fallback: {1})", "" + port, "" + PORT_FALLBACK);
                LOGGER.info("Starting web server with config: spec {0} (fallback: {1})", "" + spec, "" + OPEN_API3_FILE_FALLBACK);

                OpenAPI3RouterFactory.create(vertx,
                        spec,
                        evt -> {
                            if (evt.succeeded()) {
                                LOGGER.info("Successfully loaded API specification");
                                vertx.createHttpServer()
                                        .requestHandler(createRequestHandler(evt.result()))
                                        .listen(port,
                                                x -> listen(startFuture, x));
                            } else {
                                LOGGER.error("Failed to load API specification", evt.cause());
                                LOGGER.info("Shutting down");
                                vertx.close();
                            }
                        });
            }
        });
    }

    private void listen(Future<Void> startFuture, AsyncResult<HttpServer> evt) {
        if (evt.succeeded()) {
            LOGGER.info(String.format("Listening at port %d", evt.result().actualPort()));
            startFuture.complete();
        } else {
            LOGGER.error("Web server failed to start listening", evt.cause());
            LOGGER.info("Shutting down");
            vertx.close();
        }
    }

    public static boolean isAPIOperationMethod(Method method) {
        return method.getParameterTypes().length == 1 &&
                method.getParameterTypes()[0] == RoutingContext.class;
    }

    private Router createRequestHandler(OpenAPI3RouterFactory factory) {
        // Log all incoming requests
        factory.addGlobalHandler(LoggerHandler.create(LoggerFormat.TINY));

        // all expected responses are JSON
        factory.addGlobalHandler(ctx -> {
            ctx.response().putHeader("Content-Type", "application/json");
            ctx.next();
        });

        // Allow Cross-Origin Resource Sharing (CORS) from all clients
        factory.addGlobalHandler(createCorsHandler());

        // Verify the player's token for all secured operations
        factory.addSecurityHandler("playerToken", this::verifyPlayerToken);

        // Verify the player's token for all secured operations
        factory.addSecurityHandler("adminToken", this::verifyAdminToken);

        // Install all operation handlers
        Stream.of(bridge.getClass().getMethods())
                .filter(WebServer::isAPIOperationMethod)
                .forEach(operationMethod -> this.addOperationMethod(factory, operationMethod));

        Router router = factory.getRouter();

        return router
                .errorHandler(400, this::onBadRequest)
                .errorHandler(401, this::onUnAuthorised)
                .errorHandler(403, this::onForbidden)
                .errorHandler(404, this::onNotFound)
                .errorHandler(500, this::onInternalServerError);
    }

    private void addOperationMethod(OpenAPI3RouterFactory factory, Method operationMethod) {
        factory.addHandlerByOperationId(
                operationMethod.getName(),
                ctx -> ctx.response()
                        .setStatusCode(200)
                        .end(Json.encodePrettily(invokeOperationWithContext(operationMethod, ctx)))
        );
    }

    private Object invokeOperationWithContext(Method operationMethod, RoutingContext ctx) {
        try {

            return operationMethod.invoke(bridge, ctx);

        } catch (IllegalAccessException ex) {
            LOGGER.error("Failed to execute an API operation method (IllegalAccessException)", ex);
            throw new AlhambraException("Failed to execute an API operation method");
        } catch (InvocationTargetException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                LOGGER.error("Failed to execute an API operation method (InvocationTargetException) without a clear cause", ex);
                throw new AlhambraException("Failed to execute an API operation method");
            }
        }
    }


    private void verifyAdminToken(RoutingContext ctx) {
        verifyToken(ctx, bridge::verifyAdminToken);
    }

    private void verifyPlayerToken(RoutingContext ctx) {
        String gameId = ctx.request().getParam("gameId");
        String playerName = ctx.request().getParam("playerName");
        verifyToken(ctx, token -> bridge.verifyPlayerToken(token, gameId, playerName));
    }

    private void verifyToken(RoutingContext ctx, Predicate<String> check) {
        String token = getBearerToken(ctx);

        if (token == null) {
            ctx.fail(401); // Unauthorized  due to wrong or absent header format
        } else if (check.test(token)) {
            ctx.next();
        } else {
            ctx.fail(403); // forbidden
        }
    }

    private CorsHandler createCorsHandler() {
        return CorsHandler.create("*")
                .allowedHeaders(Set.of(
                        "Authorization", // needed for bearer token
                        "Content-Type"   // needed for body content-type
                ))
                .allowedMethods(Set.of(HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH, HttpMethod.PUT));
    }

    private void onBadRequest(RoutingContext ctx) {
        ValidationException ex = (ValidationException) ctx.failure();
        String cause = String.format("%s in %s", ex.getMessage(), ex.parameterName());
        LOGGER.info("onBadRequest {0}: {1}", ctx.request().absoluteURI(), cause);
        replyWithFailure(ctx, 400, "Bad Request", cause);
    }

    private void onNotFound(RoutingContext ctx) {
        LOGGER.info("onNotFound at {0}", ctx.failure(), ctx.request().absoluteURI());
        replyWithFailure(ctx, 404, "Not Found", null);
    }

    private void onUnAuthorised(RoutingContext ctx) {
        replyWithFailure(ctx, 401, "Unauthorised", null);
    }

    private void onForbidden(RoutingContext ctx) {
        replyWithFailure(ctx, 403, "Forbidden", null);
    }

    private void onInternalServerError(RoutingContext ctx) {
        try {
            throw ctx.failure();
        } catch (UrlTokenFormatException failure) {
            LOGGER.info("Wrong player name of prefix format", failure);
            replyWithFailure(ctx, 400, "Bad Request", failure.getMessage());
        } catch (AlhambraEntityNotFoundException failure) {
            LOGGER.info("Failing with AlhambraEntityNotFoundException 422", failure);
            replyWithFailure(ctx, 422, "Unable to find the entity", failure.getMessage());
        } catch (AlhambraGameRuleException failure) {
            LOGGER.info("Failing with AlhambraGameRuleException 409", failure);
            replyWithFailure(ctx, 409, "Alhambra rules violated (Conflict)", failure.getMessage());
        } catch (Throwable failure) { // NOSONAR
            LOGGER.error("onInternalServerError at {0}", failure, ctx.request().absoluteURI());
            replyWithFailure(ctx, 500, "Internal Server Error", null);
        }
    }

    private void replyWithFailure(RoutingContext ctx, int statusCode, String message, String cause) {
        JsonObject reply = context2json(ctx)
                .put("failed", true)
                .put("status", statusCode)
                .put("message", message);

        if (cause != null) {
            reply.put("cause", cause);
        }

        ctx.response().setStatusCode(statusCode).end(Json.encodePrettily(reply));
    }

    private JsonObject context2json(RoutingContext ctx) {
        return new JsonObject()
                .put("method", ctx.request().method())
                .put("url", ctx.request().absoluteURI())
                .put("authorization", ctx.request().getHeader(HttpHeaders.AUTHORIZATION))
                .put("content-type", ctx.request().getHeader(HttpHeaders.CONTENT_TYPE))
                .put("body", ctx.getBodyAsString());
    }

    private String getBearerToken(RoutingContext ctx) {
        String header = ctx.request().getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(AUTHORIZATION_TOKEN_PREFIX)) {
            return null;
        } else {
            return header.substring(AUTHORIZATION_TOKEN_PREFIX.length());
        }
    }


}
