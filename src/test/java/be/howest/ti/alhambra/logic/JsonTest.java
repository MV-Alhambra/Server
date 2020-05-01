package be.howest.ti.alhambra.logic;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

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
    void bank(){
        // Create a bank ...
        Bank bank = new Bank();

        // Turn it into a JsonObject
        JsonObject bankAsJsonObject = JsonObject.mapFrom(bank);
        // Assert that this object has the expected properties
        assertTrue(bankAsJsonObject.containsKey("coins"));

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
        assertEquals(BuildingType.valueOf("PAVILION").toString(), "pavilion" );
        assertEquals(BuildingType.valueOf("CHAMBERS").toString(), "chambers" );
    }

}