package be.howest.ti.alhambra.webapi;

import be.howest.ti.alhambra.exceptions.AlhambraException;
import be.howest.ti.alhambra.logic.exceptions.AlhambraEntityNotFoundException;
import be.howest.ti.alhambra.logic.exceptions.AlhambraGameRuleException;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import java.util.Collections;

class AlhambraOpenAPI3TestBridge implements AlhambraOpenAPI3Bridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlhambraOpenAPI3TestBridge.class);

    public static final String DEFAULT_GAME_ID = "game-000";
    public static final String THIEVE_PLAYER_NAME = "thieve";

    public static final String DEFAULT_ADMIN_TOKEN = "admin";
    public static final String DEFAULT_PLAYER_TOKEN = "player";

    public boolean verifyAdminToken(String token) {
        LOGGER.info("verifyPlayerToken");
        return DEFAULT_ADMIN_TOKEN.equals(token);
    }

    public boolean verifyPlayerToken(String token, String gameId, String playerName) {
        LOGGER.info("verifyPlayerToken");
        return DEFAULT_PLAYER_TOKEN.equals(token);
    }

    public Object getBuildings(RoutingContext ctx) {
        LOGGER.info("getBuildings");
        return Collections.emptyList();
    }

    public Object getAvailableBuildLocations(RoutingContext ctx) {
        LOGGER.info("getAvailableBuildLocations");
        return Collections.emptyList();
    }

    public Object getBuildingTypes(RoutingContext ctx) {
        LOGGER.info("getBuildingTypes");
        return Collections.emptyList();
    }

    public Object getCurrencies(RoutingContext ctx) {
        LOGGER.info("getCurrencies");
        return Collections.emptyList();
    }

    public Object getScoringTable(RoutingContext ctx) {
        LOGGER.info("getScoringTable");
        return null;
    }

    public Object getGames(RoutingContext ctx) {
        LOGGER.info("getGames");
        return Collections.emptyList();
    }



    public Object createGame(RoutingContext ctx) {
        LOGGER.info("createGame");
        return DEFAULT_GAME_ID;
    }

    public Object clearGames(RoutingContext ctx) {
        LOGGER.info("clearGames");
        return null;
    }

    public Object joinGame(RoutingContext ctx) {
        LOGGER.info("joinGame");

        if (!DEFAULT_GAME_ID.equals(ctx.request().getParam("gameId"))) {
            throw new AlhambraEntityNotFoundException("These Are Not the Droids You Are Looking For");
        }

        return DEFAULT_PLAYER_TOKEN;
    }


    public Object leaveGame(RoutingContext ctx) {
        LOGGER.info("leaveGame");
        return null;
    }

    public Object setReady(RoutingContext ctx) {
        LOGGER.info("setReady");
        return null;
    }

    public Object setNotReady(RoutingContext ctx) {
        LOGGER.info("setNotReady");
        return null;
    }

    public Object takeMoney(RoutingContext ctx) {
        LOGGER.info("takeMoney");

        if (THIEVE_PLAYER_NAME.equals(ctx.request().getParam("playerName"))) {
            throw new AlhambraGameRuleException("Once a thief always a thief");
        }

        return null;
    }

    public Object buyBuilding(RoutingContext ctx) {
        LOGGER.info("buyBuilding");
        return null;
    }


    public Object redesign(RoutingContext ctx) {
        LOGGER.info("redesign");
        return null;
    }

    public Object build(RoutingContext ctx) {
        LOGGER.info("build");
        return null;
    }


    public Object getGame(RoutingContext ctx) {
        LOGGER.info("getGame");
        return null;
    }
}
