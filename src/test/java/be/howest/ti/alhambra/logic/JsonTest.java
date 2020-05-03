package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.core.Version;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

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
        Building building = new Building(BuildingType.valueOf("PAVILION"), 5, null);

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
    void buildingType() {
        assertEquals(BuildingType.valueOf("PAVILION").toString(), "pavilion");
        assertEquals(BuildingType.valueOf("CHAMBERS").toString(), "chambers");
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
        System.out.println(playerAsJsonObject);
        // Assert that this object has the expected properties
        assertTrue(playerAsJsonObject.containsKey("name"));
        //assertTrue(playerAsJsonObject.containsKey("city")); temp
        assertTrue(playerAsJsonObject.containsKey("reserve"));
        assertTrue(playerAsJsonObject.containsKey("coins"));
        assertTrue(playerAsJsonObject.containsKey("buildings-in-hand"));
        assertTrue(playerAsJsonObject.containsKey("virtual-score"));
        assertTrue(playerAsJsonObject.containsKey("score"));
        assertFalse(playerAsJsonObject.containsKey("playerToken"));

        // Assert that you can convert it back to the same player.
        assertEquals(player, playerAsJsonObject.mapTo(Player.class));

        // Assert that you can go back and forth between Java-objects and Json (strings)
        assertEquals(player, Json.decodeValue(Json.encode(player), Player.class));
    }
}