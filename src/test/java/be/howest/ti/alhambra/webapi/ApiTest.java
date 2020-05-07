package be.howest.ti.alhambra.webapi;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(VertxExtension.class)
class ApiTest {

    // config
    private static final int PORT = 8080;
    private static final String HOST = "localhost";


    // utils
    private Vertx vertx;
    private WebClient webClient;

    // Urls and url formats:
    public static final String URI_GAMES = "/games";
    public static final String URI_PLAYERS = "/games/%s/players";
    public static final String URI_CITY = "/games/%s/players/%s/city";


    // Parameters and headers
    private static final String DEFAULT_PLAYER_NAME = "alice";
    private static final String DEFAULT_AUTHORIZATION_HEADER = WebServer.AUTHORIZATION_TOKEN_PREFIX + AlhambraOpenAPI3TestBridge.DEFAULT_PLAYER_TOKEN;

    // request bodies
    private static final  JsonObject validLocation = new JsonObject().put("row", 0).put("col", 0);

    private static final  JsonObject validBuilding = new JsonObject()
            .put("type", "pavilion")
            .put("cost", 10)
            .put("walls", new JsonObject().put("north", true)
                    .put("east", true)
                    .put("south", true)
                    .put("west", true));

    private static JsonObject createValidBuildingAndLocation(JsonObject building, JsonObject location) {
        return new JsonObject().put("building", building).put("location", location);
    }

    private static final JsonArray validCoins = new JsonArray();

    // Response body validators
    private static final Predicate<String> IGNORE_BODY = body -> true;
    private static final JsonObject INVALID_BODY = new JsonObject().put("random", "data");

    @BeforeEach
    void deploy(final VertxTestContext testContext) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(
                new WebServer(new AlhambraOpenAPI3TestBridge()),
                testContext.completing());
        webClient = WebClient.create(vertx);
    }

    @AfterEach
    void close(final VertxTestContext testContext) {
        vertx.close(testContext.completing());
        webClient.close();
    }


    private HttpRequest<Buffer> makeRequest(HttpMethod method, String requestURI, String authorizationHeader) {
        HttpRequest<Buffer> request = webClient.request(method, PORT, HOST, requestURI);
        if (authorizationHeader != null) {
            request.putHeader(
                    HttpHeaders.AUTHORIZATION.toString(),
                    authorizationHeader
            );
        }
        return request;
    }

    private void testRequest(
            final VertxTestContext testContext,
            HttpMethod method, String requestURI, String authorizationHeader, Object body,
            int expectedStatusCode, Predicate<String> isExpectedBody
    ) {
        // Do as expected:
        makeRequest(method, requestURI, authorizationHeader).sendJson(body, testContext.succeeding(response ->
                testContext.verify(() -> {
                    assertEquals(expectedStatusCode, response.statusCode());
                    assertTrue(isExpectedBody.test(response.bodyAsString()));
                    testContext.completeNow();
                })
        ));
    }

    private void testRequest(
            final VertxTestContext testContext,
            HttpMethod method, String requestURI, String authorizationHeader,
            int expectedStatusCode, Predicate<String> isExpectedBody
    ) {
        makeRequest(method, requestURI, authorizationHeader).send(testContext.succeeding(response ->
                testContext.verify(() -> {
                    assertEquals(expectedStatusCode, response.statusCode());
                    String body = response.bodyAsString();
                    assertTrue(
                            isExpectedBody.test(body),
                            () -> String.format("Unexpected body: %s", body)
                    );
                    testContext.completeNow();
                })
        ));
    }


    @Test
    void startWebServer(final VertxTestContext testContext) {
        testContext.completeNow();
    }

    @Test
    void getBuildingTypes(final VertxTestContext testContext) {
        String requestURI = "/buildings/types";
        testRequest(
                testContext,
                HttpMethod.GET, requestURI, null,
                200,
                body -> (Json.decodeValue(body, String[].class).length == 0)
        );
    }

    @Test
    void getCurrencies(final VertxTestContext testContext) {
        String requestURI = "/currencies";
        testRequest(
                testContext,
                HttpMethod.GET, requestURI, null,
                200,
                body -> (Json.decodeValue(body, String[].class).length == 0)
        );
    }

    @Test
    void getScoringTable(final VertxTestContext testContext) {
        String requestURI = "/scoring/1";
        testRequest(
                testContext,
                HttpMethod.GET, requestURI, null,
                200,
                IGNORE_BODY
        );
    }

    @Test
    void getScoringTableWithString(final VertxTestContext testContext) {
        String requestURI = "/scoring/txt";
        testRequest(
                testContext,
                HttpMethod.GET, requestURI, null,
                400,
                IGNORE_BODY
        );
    }

    @Test
    void createGame(final VertxTestContext testContext) {
        testRequest(
                testContext,
                HttpMethod.POST, URI_GAMES, null,
                200,
                body -> Json.decodeValue(body, String.class).equals(AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID)
        );
    }

    @Test
    void createGameWithPrefix(final VertxTestContext testContext) {
        JsonObject body = new JsonObject().put("prefix", "game");
        testRequest(
                testContext,
                HttpMethod.POST, URI_GAMES, null, body,
                200,
                responseBody -> Json.decodeValue(responseBody, String.class).equals(AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID)
        );
    }

    //@Test createGame doesnt use a body anymore
    void createGameWithInvalidBody(final VertxTestContext testContext) {
        testRequest(
                testContext,
                HttpMethod.POST, URI_GAMES, null, INVALID_BODY,
                400,
                IGNORE_BODY
        );
    }

    @Test
    void joinExistingGame(final VertxTestContext testContext) {
        JsonObject body = new JsonObject().put("playerName", "jos");
        String requestURI = String.format(URI_PLAYERS, AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID);
        testRequest(
                testContext,
                HttpMethod.POST, requestURI, null, body,
                200,
                responseBody -> Json.decodeValue(responseBody, String.class).equals(AlhambraOpenAPI3TestBridge.DEFAULT_PLAYER_TOKEN)
        );
    }

    @Test
    void joinExistingGameWithInvalidBody(final VertxTestContext testContext) {
        String requestURI = String.format(URI_PLAYERS, AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID);
        testRequest(
                testContext,
                HttpMethod.POST, requestURI, null, INVALID_BODY,
                400,
                IGNORE_BODY
        );
    }

    @Test
    void joinNonExistingGame(final VertxTestContext testContext) {
        String requestURI = String.format(URI_PLAYERS, "fake-game");
        testRequest(
                testContext,
                HttpMethod.POST, requestURI, null,
                404,
                IGNORE_BODY
        );
    }

    @Test
    void leaveGame(final VertxTestContext testContext) {
        String requestURI = String.format("/games/%s/players/%s", AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID, DEFAULT_PLAYER_NAME);
        testRequest(
                testContext,
                HttpMethod.DELETE, requestURI, DEFAULT_AUTHORIZATION_HEADER,
                200,
                IGNORE_BODY
        );
    }

    @Test
    void setReady(final VertxTestContext testContext) {
        String requestURI = String.format("/games/%s/players/%s/ready", AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID, DEFAULT_PLAYER_NAME);
        testRequest(
                testContext,
                HttpMethod.PUT, requestURI, DEFAULT_AUTHORIZATION_HEADER,
                200,
                IGNORE_BODY
        );
    }

    @Test
    void setNotReady(final VertxTestContext testContext) {
        String requestURI = String.format("/games/%s/players/%s/ready", AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID, DEFAULT_PLAYER_NAME);
        testRequest(
                testContext,
                HttpMethod.DELETE, requestURI, DEFAULT_AUTHORIZATION_HEADER,
                200,
                IGNORE_BODY
        );
    }

    @Test
    void getGames(final VertxTestContext testContext) {
        testRequest(
                testContext,
                HttpMethod.GET, URI_GAMES, null,
                200,
                IGNORE_BODY
        );
    }

    @Test
    void getGame(final VertxTestContext testContext) {
        String requestURI = String.format("/games/%s", AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID);
        testRequest(
                testContext,
                HttpMethod.GET, requestURI, DEFAULT_AUTHORIZATION_HEADER,
                200,
                IGNORE_BODY
        );
    }

    @Test
    void takeMoney(final VertxTestContext testContext) {
        JsonArray body = new JsonArray();
        String requestURI = String.format("/games/%s/players/%s/money", AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID, DEFAULT_PLAYER_NAME);
        testRequest(
                testContext,
                HttpMethod.POST, requestURI, DEFAULT_AUTHORIZATION_HEADER, body,
                200,
                IGNORE_BODY
        );
    }

    @Test
    void stealMoney(final VertxTestContext testContext) {
        JsonArray body = new JsonArray();
        String requestURI = String.format("/games/%s/players/%s/money", AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID, AlhambraOpenAPI3TestBridge.THIEVE_PLAYER_NAME);
        testRequest(
                testContext,
                HttpMethod.POST, requestURI, DEFAULT_AUTHORIZATION_HEADER, body,
                409,
                IGNORE_BODY
        );
    }

    @Test
    void buy(final VertxTestContext testContext) {
        JsonObject body = new JsonObject()
                .put("currency", "blue")
                .put("coins", validCoins);

        String requestURI = String.format("/games/%s/players/%s/buildings-in-hand", AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID, DEFAULT_PLAYER_NAME);
        testRequest(
                testContext,
                HttpMethod.POST, requestURI, DEFAULT_AUTHORIZATION_HEADER, body,
                200,
                IGNORE_BODY
        );
    }

    @Test
    void redesignAddAndSwap(final VertxTestContext testContext) {
        JsonObject body = createValidBuildingAndLocation(validBuilding, validLocation);

        String requestURI = String.format(URI_CITY, AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID, DEFAULT_PLAYER_NAME);
        testRequest(
                testContext,
                HttpMethod.PATCH, requestURI, DEFAULT_AUTHORIZATION_HEADER, body,
                200,
                IGNORE_BODY
        );
    }

    @Test
    void redesignDelete(final VertxTestContext testContext) {
        JsonObject body = new JsonObject().put("location", validLocation);

        String requestURI = String.format(URI_CITY, AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID, DEFAULT_PLAYER_NAME);
        testRequest(
                testContext,
                HttpMethod.PATCH, requestURI, DEFAULT_AUTHORIZATION_HEADER, body,
                200,
                IGNORE_BODY
        );
    }

    @Test
    void build(final VertxTestContext testContext) {
        JsonObject body = createValidBuildingAndLocation(validBuilding, validLocation);

        String requestURI = String.format(URI_CITY, AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID, DEFAULT_PLAYER_NAME);
        testRequest(
                testContext,
                HttpMethod.POST, requestURI, DEFAULT_AUTHORIZATION_HEADER, body,
                200,
                IGNORE_BODY
        );
    }

    @Test
    void buildToReserve(final VertxTestContext testContext) {
        JsonObject body = createValidBuildingAndLocation(validBuilding, null);

        String requestURI = String.format(URI_CITY, AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID, DEFAULT_PLAYER_NAME);

        testRequest(
                testContext,
                HttpMethod.POST, requestURI, DEFAULT_AUTHORIZATION_HEADER, body,
                200,
                IGNORE_BODY
        );
    }

    @Test
    void buildWithInvalidBody(final VertxTestContext testContext) {
        String requestURI = String.format(URI_CITY, AlhambraOpenAPI3TestBridge.DEFAULT_GAME_ID, DEFAULT_PLAYER_NAME);

        testRequest(
                testContext,
                HttpMethod.POST, requestURI, DEFAULT_AUTHORIZATION_HEADER, INVALID_BODY,
                400,
                IGNORE_BODY
        );
    }

}
