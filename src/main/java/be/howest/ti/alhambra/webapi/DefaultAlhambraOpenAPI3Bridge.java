package be.howest.ti.alhambra.webapi;

import be.howest.ti.alhambra.logic.AlhambraController;
import be.howest.ti.alhambra.logic.Building;
import be.howest.ti.alhambra.logic.Coin;
import be.howest.ti.alhambra.logic.Currency;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

public class DefaultAlhambraOpenAPI3Bridge implements AlhambraOpenAPI3Bridge {


    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAlhambraOpenAPI3Bridge.class);
    private static final AlhambraController controller = new AlhambraController();


    public boolean verifyAdminToken(String token) {
        LOGGER.info("verifyPlayerToken");
        return true;
    }

    public boolean verifyPlayerToken(String token, String gameId, String playerName) {
        LOGGER.info("verifyPlayerToken");
        return true;
    }

    public Object getBuildings(RoutingContext ctx) {
        LOGGER.info("getBuildings");
        return controller.getBuildings();
    }

    public Object getAvailableBuildLocations(RoutingContext ctx) {
        LOGGER.info("getAvailableBuildLocations");
        Map<String, Boolean> walls = Building.getDefaultWalls();
        walls.put("north", Boolean.valueOf(ctx.request().getParam("north")));
        walls.put("west", Boolean.valueOf(ctx.request().getParam("west")));
        walls.put("east", Boolean.valueOf(ctx.request().getParam("east")));
        walls.put("south", Boolean.valueOf(ctx.request().getParam("south")));
        return controller.getAvailableBuildLocations(getGameId(ctx), getPlayerName(ctx), walls);
    }

    public Object getBuildingTypes(RoutingContext ctx) {
        LOGGER.info("getBuildingTypes");
        return controller.getBuildingTypes();
    }

    public Object getCurrencies(RoutingContext ctx) {
        LOGGER.info("getCurrencies");
        return controller.getCurrencies();
    }

    public Object getScoringTable(RoutingContext ctx) {
        LOGGER.info("getScoringTable");
        return null;
    }

    public Object getGames(RoutingContext ctx) {
        LOGGER.info("getGames");
        return controller.getLobbies();
    }

    public Object createGame(RoutingContext ctx) {
        LOGGER.info("createGame");
        return controller.addLobby(ctx.getBodyAsJson().getValue("customGameName").toString(),ctx.getBodyAsJson().getInteger("maxNumberOfPlayers"));
    }

    public Object clearGames(RoutingContext ctx) {
        LOGGER.info("clearGames");
        return null;
    }

    public Object joinGame(RoutingContext ctx) {
        LOGGER.info("joinGame");
        return controller.joinLobby(getGameId(ctx), ctx.getBodyAsJson().getValue("playerName").toString());
    }


    public Object leaveGame(RoutingContext ctx) {
        LOGGER.info("leaveGame");
        return controller.leaveLobby(getGameId(ctx), getPlayerName(ctx));
    }

    public Object setReady(RoutingContext ctx) {
        LOGGER.info("setReady");
        return controller.readyUp(getGameId(ctx), getPlayerName(ctx));
    }

    public Object setNotReady(RoutingContext ctx) {
        LOGGER.info("setNotReady");
        return controller.readyDown(getGameId(ctx), getPlayerName(ctx));
    }

    public Object takeMoney(RoutingContext ctx) {
        LOGGER.info("takeMoney");
        return controller.takeCoins(getGameId(ctx), getPlayerName(ctx), Json.decodeValue(ctx.getBodyAsString(), Coin[].class));
    }

    public Object buyBuilding(RoutingContext ctx) {
        LOGGER.info("buyBuilding");
        Currency currency = Json.decodeValue("\"" + ctx.getBodyAsJson().getString("currency") + "\"", Currency.class);
        Coin[] coins = Json.decodeValue(ctx.getBodyAsJson().getJsonArray("coins").toString(), Coin[].class);
        return controller.buyBuilding(getGameId(ctx), getPlayerName(ctx), currency, coins);
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
        return controller.getGame(getGameId(ctx));
    }

    public Object startGame(RoutingContext ctx) {
        LOGGER.info("startGame");
        return controller.startLobby(getGameId(ctx));
    }

    private String getGameId(RoutingContext ctx) {
        return ctx.request().getParam("gameId");
    }

    private String getPlayerName(RoutingContext ctx) {
        return ctx.request().getParam("playerName");
    }

}
