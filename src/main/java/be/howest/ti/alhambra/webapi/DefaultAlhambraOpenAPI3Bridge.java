package be.howest.ti.alhambra.webapi;

import be.howest.ti.alhambra.logic.*;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import java.util.Arrays;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class DefaultAlhambraOpenAPI3Bridge implements AlhambraOpenAPI3Bridge {


    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAlhambraOpenAPI3Bridge.class);
    private static final AlhambraController controller = new AlhambraController();
    private static final String LOCATION = "location";
    private static final String BUILDING = "building";


    public boolean verifyAdminToken(String token) {
        LOGGER.info("verifyPlayerToken");
        return true;
    }

    public boolean verifyPlayerToken(String token, String gameId, String playerName) {
        LOGGER.info("verifyPlayerToken");
        return controller.verifyToken(gameId, token);
    }

    public Object getBuildings(RoutingContext ctx) {
        LOGGER.info("getBuildings");
        return controller.getBuildings();
    }

    public Object getAvailableBuildLocations(RoutingContext ctx) {
        LOGGER.info("getAvailableBuildLocations");
        Map<CardinalDirection, Boolean> walls = Building.getDefaultWalls();
        //fills the wall with the correct values from the request
        Arrays.stream(CardinalDirection.values()).forEach(cardinalDirection -> walls.put(cardinalDirection, Boolean.valueOf(ctx.request().getParam(cardinalDirection.toString()))));

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
        return ScoringTable.getRoundTable(parseInt(ctx.request().getParam("round")));
    }

    public Object getGames(RoutingContext ctx) {
        LOGGER.info("getGames");
        return controller.getLobbies();
    }

    public Object createGame(RoutingContext ctx) { //added support for backwards compatible with previous server of DW
        LOGGER.info("createGame");
        if (!ctx.getBodyAsString().equals("")) { //if it has no body it cant be turned into JSON
            final boolean customName = ctx.getBodyAsJson().containsKey("customGameName"); //check if contains those keys
            final boolean customCap = ctx.getBodyAsJson().containsKey("maxNumberOfPlayers");
            final String lobbyName = customName ? ctx.getBodyAsJson().getValue("customGameName").toString() : "Alhambra lobby";
            final int playerCap = customCap ? ctx.getBodyAsJson().getInteger("maxNumberOfPlayers") : 6;
            return controller.addLobby(lobbyName, playerCap, !customCap || !customName);
        }
        return controller.addLobby("Alhambra lobby", 6, true);
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
        Currency currency = Currency.valueOf(ctx.getBodyAsJson().getString("currency").toUpperCase());
        Coin[] coins = Json.decodeValue(ctx.getBodyAsJson().getJsonArray("coins").toString(), Coin[].class);
        return controller.buyBuilding(getGameId(ctx), getPlayerName(ctx), currency, coins);
    }


    public Object redesign(RoutingContext ctx) {
        LOGGER.info("redesign");
        Location location = ctx.getBodyAsJson().containsKey(LOCATION) ? ctx.getBodyAsJson().getJsonObject(LOCATION).mapTo(Location.class) : null;
        Building building = ctx.getBodyAsJson().containsKey(BUILDING) ? ctx.getBodyAsJson().getJsonObject(BUILDING).mapTo(Building.class) : null;
        return controller.redesign(getGameId(ctx), getPlayerName(ctx), building, location);
    }

    public Object build(RoutingContext ctx) {
        LOGGER.info("build");
        Building building = ctx.getBodyAsJson().getJsonObject(BUILDING).mapTo(Building.class);
        Location location = ctx.getBodyAsJson().getJsonObject(LOCATION) == null ? null : ctx.getBodyAsJson().getJsonObject(LOCATION).mapTo(Location.class);
        return controller.build(getGameId(ctx), getPlayerName(ctx), building, location);
    }

    public Object getGame(RoutingContext ctx) {
        LOGGER.info("getGame");
        return controller.getGame(getGameId(ctx));
    }

    public Object startGame(RoutingContext ctx) {
        LOGGER.info("startGame");
        return controller.startLobby(getGameId(ctx));
    }

    @Override
    public Object giveDirk(RoutingContext ctx) {
        LOGGER.info("giveDirk");
        return controller.giveDirk(getGameId(ctx), getPlayerName(ctx), ctx.getBodyAsJson().getJsonObject(BUILDING).mapTo(Building.class));
    }

    private String getGameId(RoutingContext ctx) {
        return ctx.request().getParam("gameId");
    }

    private String getPlayerName(RoutingContext ctx) {
        return ctx.request().getParam("playerName");
    }

}
