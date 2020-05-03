package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MarketTest
{
    @Test
    void market()
    {
        //make market
        Market market = new Market();
        //make buildings
        Building building1 = new Building(BuildingType.valueOf("PAVILION"), 5, null);
        Building building2 = new Building(BuildingType.valueOf("PAVILION"), 5, null);
        Building building3 = new Building(BuildingType.valueOf("PAVILION"), 5, null);
        Building building4 = new Building(BuildingType.valueOf("PAVILION"), 5, null);

        //add buildings to market
        market.addBuilding(Currency.BLUE, building1);
        market.addBuilding(Currency.ORANGE, building2);
        market.addBuilding(Currency.YELLOW, building3);
        market.addBuilding(Currency.GREEN, building4);
        //test buildings in market
        assertEquals(building1,market.getBuilding(Currency.BLUE));
        assertEquals(building2,market.getBuilding(Currency.ORANGE));
        assertEquals(building3,market.getBuilding(Currency.YELLOW));
        assertEquals(building4,market.getBuilding(Currency.GREEN));

        //remove building
        market.removeBuilding(Currency.BLUE);
        //test building removed
        assertFalse(market.containsBuilding(Currency.BLUE));
    }
}
