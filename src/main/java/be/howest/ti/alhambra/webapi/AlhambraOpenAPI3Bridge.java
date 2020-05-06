package be.howest.ti.alhambra.webapi;

import io.vertx.ext.web.RoutingContext;

interface AlhambraOpenAPI3Bridge {

    boolean verifyAdminToken(String token);

    boolean verifyPlayerToken(String token, String gameId, String playerName);

    Object getBuildings(RoutingContext ctx);

    Object getAvailableBuildLocations(RoutingContext ctx);

    Object getBuildingTypes(RoutingContext ctx);

    Object getCurrencies(RoutingContext ctx);

    Object getScoringTable(RoutingContext ctx);

    Object getGames(RoutingContext ctx);


    Object createGame(RoutingContext ctx);

    Object clearGames(RoutingContext ctx);

    Object joinGame(RoutingContext ctx);


    Object leaveGame(RoutingContext ctx);

    Object setReady(RoutingContext ctx);

    Object setNotReady(RoutingContext ctx);

    Object takeMoney(RoutingContext ctx);

    Object buyBuilding(RoutingContext ctx);

    Object redesign(RoutingContext ctx);

    Object build(RoutingContext ctx);

    Object getGame(RoutingContext ctx);

    Object getTest(RoutingContext ctx);

}
