package be.howest.ti.alhambra.logic;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    @Test
    void currency(){
        // Create a currency ...
        Currency currency = Currency.GREEN;

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(currency, Json.decodeValue(Json.encode(currency), Currency.class));
    }

    @Test
    void coin() {
        // Create a coin ...
        Coin coin = new Coin(Currency.ORANGE, 10);

        // Turn it into a JsonObject
        JsonObject coinAsJsonObject = JsonObject.mapFrom(coin);

        // Assert that this object has the expected properties
        assertTrue(coinAsJsonObject.containsKey("currency"));
        assertTrue(coinAsJsonObject.containsKey("amount"));

        // Assert that you can convert it back to the same coin.
        assertEquals(coin, coinAsJsonObject.mapTo(Coin.class));

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(coin, Json.decodeValue(Json.encode(coin), Coin.class));
    }

    @Test
    void location() {
        // Create a location ...
        Location location = new Location(7, 10);

        // Turn it into a JsonObject
        JsonObject locationAsJsonObject = JsonObject.mapFrom(location);

        // Assert that this object has the expected properties
        assertTrue(locationAsJsonObject.containsKey("col"));
        assertTrue(locationAsJsonObject.containsKey("row"));

        // Assert that you can convert it back to the same location.
        assertEquals(location, locationAsJsonObject.mapTo(Location.class));

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(location, Json.decodeValue(Json.encode(location), Location.class));
    }

    @Test
    void bank() {
        // Create a bank ...
        Bank bank = new Bank();

        // Turn it into a JsonObject
        JsonObject bankAsJsonObject = JsonObject.mapFrom(bank);
        // Assert that this object has the expected properties
        assertTrue(bankAsJsonObject.containsKey("bank"));

        // Assert that you can convert it back to the same bank.
        assertEquals(bank, bankAsJsonObject.mapTo(Bank.class));

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(bank, Json.decodeValue(Json.encode(bank), Bank.class));

    }

    @Test
    void building() {
        // Create a building ...
        Building building = new Building(BuildingType.PAVILION, 5);

        // Turn it into a JsonObject
        JsonObject buildingAsJsonObject = JsonObject.mapFrom(building);
        // Assert that this object has the expected properties
        assertTrue(buildingAsJsonObject.containsKey("type"));
        assertTrue(buildingAsJsonObject.containsKey("cost"));
        assertTrue(buildingAsJsonObject.containsKey("walls"));

        // Assert that you can convert it back to the same Building.
        assertEquals(building, buildingAsJsonObject.mapTo(Building.class));
        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(building, Json.decodeValue(Json.encode(building), Building.class));
    }

    @Test
    void coins() {
        // Create a coins ...
        Coins coins = new Coins();

        // Turn it into a JsonObject
        JsonObject coinsAsJsonObject = JsonObject.mapFrom(coins);

        // Assert that this object has the expected properties
        assertTrue(coinsAsJsonObject.containsKey("coins"));

        // Assert that you can convert it back to the same coins.
        assertEquals(coins, coinsAsJsonObject.mapTo(Coins.class));

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(coins, Json.decodeValue(Json.encode(coins), Coins.class));

    }

    @Test
    void city() {
        // Create a city ...
        City city = new City();

        // Turn it into a JsonObject
        JsonObject cityAsJsonObject = JsonObject.mapFrom(city);

        // Assert that this object has the expected properties
        assertTrue(cityAsJsonObject.containsKey("city"));

        // Assert that you can convert it back to the same city.
        assertEquals(city, cityAsJsonObject.mapTo(City.class));

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(city, Json.decodeValue(Json.encode(city), City.class));
    }

    @Test
    void market() {
        // Create a market ...
        Market market = new Market();

        // Turn it into a JsonObject
        JsonObject marketAsJsonObject = JsonObject.mapFrom(market);

        // Assert that this object has the expected properties
        assertTrue(marketAsJsonObject.containsKey("market"));

        // Assert that you can convert it back to the same market.
        assertEquals(market, marketAsJsonObject.mapTo(Market.class));

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(market, Json.decodeValue(Json.encode(market), Market.class));
    }

    @Test
    void reserve() {
        // Create reserve
        Reserve reserve = new Reserve();

        // Turn it into a JsonObject
        JsonObject reserveAsJsonObject = JsonObject.mapFrom(reserve);

        // Assert that this object has the expected properties
        assertTrue(reserveAsJsonObject.containsKey("reserve"));

        // Assert that you can convert it back to the same reserve.
        assertEquals(reserve, reserveAsJsonObject.mapTo(Reserve.class));

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(reserve, Json.decodeValue(Json.encode(reserve), Reserve.class));
    }

    @Test
    void player() {
        // Create player
        Player player = new Player("Henk");

        // Turn it into a JsonObject
        JsonObject playerAsJsonObject = JsonObject.mapFrom(player);

        // Assert that this object has the expected properties
        assertTrue(playerAsJsonObject.containsKey("name"));
        assertTrue(playerAsJsonObject.containsKey("city"));
        assertTrue(playerAsJsonObject.containsKey("reserve"));
        assertTrue(playerAsJsonObject.containsKey("coins"));
        assertTrue(playerAsJsonObject.containsKey("buildings-in-hand"));
        assertTrue(playerAsJsonObject.containsKey("virtual-score"));
        assertTrue(playerAsJsonObject.containsKey("score"));

        // Assert that you can convert it back to the same player.
        assertEquals(player, playerAsJsonObject.mapTo(Player.class));

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(player, Json.decodeValue(Json.encode(player), Player.class));
    }

    @Test
    void lobby() {
        // Create lobby
        Lobby lobby = new Lobby("game21-002", "testLobby");

        // Turn it into a JsonObject
        JsonObject lobbyAsJsonObject = JsonObject.mapFrom(lobby);

        // Assert that this object has the expected properties
        assertTrue(lobbyAsJsonObject.containsKey("id"));
        assertTrue(lobbyAsJsonObject.containsKey("readyCount"));
        assertTrue(lobbyAsJsonObject.containsKey("players"));
        assertTrue(lobbyAsJsonObject.containsKey("playerCount"));
        assertTrue(lobbyAsJsonObject.containsKey("customNameLobby"));
        assertTrue(lobbyAsJsonObject.containsKey("maxNumberOfPlayers"));
        // Assert that you can convert it back to the same lobby.
        assertEquals(lobby, lobbyAsJsonObject.mapTo(Lobby.class));

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(lobby, Json.decodeValue(Json.encode(lobby), Lobby.class));
    }

    @Test
    void playerInLobby() {
        // Create player
        PlayerInLobby player = new PlayerInLobby("Carol");

        // Turn it into a JsonObject
        JsonObject playerAsJsonObject = JsonObject.mapFrom(player);

        // Assert that this object has the expected properties
        assertTrue(playerAsJsonObject.containsKey("name"));
        assertTrue(playerAsJsonObject.containsKey("status"));

        // Assert that you can convert it back to the same player.
        assertEquals(player, playerAsJsonObject.mapTo(PlayerInLobby.class));

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(player, Json.decodeValue(Json.encode(player), PlayerInLobby.class));
    }

    @Test
    void game(){
        // Create a game ...
        List<PlayerInLobby> names = new ArrayList<>();
        PlayerInLobby p1 = new PlayerInLobby("Carol");
        PlayerInLobby p2 = new PlayerInLobby("Joe");
        names.add(p1);
        names.add(p2);
        Game game = new Game(names);

        // Turn it into a JsonObject
        JsonObject gameAsJsonObject = JsonObject.mapFrom(game);

        // Assert that this object has the expected properties
        assertTrue(gameAsJsonObject.containsKey("players"));
        assertTrue(gameAsJsonObject.containsKey("currentPlayer"));
        assertTrue(gameAsJsonObject.containsKey("ended"));
        assertTrue(gameAsJsonObject.containsKey("bank"));
        assertTrue(gameAsJsonObject.containsKey("market"));

        // Assert that you can convert it back to the same game.
        assertEquals(game, gameAsJsonObject.mapTo(Game.class));

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(game, Json.decodeValue(Json.encode(game), Game.class));
    }

    @Test
    void playerToken(){
        // Create token
        PlayerToken token = new PlayerToken("Henk","001","henks game");

        // Turn it into a JsonObject
        JsonObject tokenAsJsonObject = JsonObject.mapFrom(token);
        // Assert that this object has the expected properties
        assertTrue(tokenAsJsonObject.containsKey("token"));

        // Assert that you can convert it back to the same token.
        assertEquals(token, tokenAsJsonObject.mapTo(PlayerToken.class));

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(token, Json.decodeValue(Json.encode(token), PlayerToken.class));

    }

}