package be.howest.ti.alhambra.logic;

import be.howest.ti.alhambra.logic.exceptions.AlhambraGameRuleException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CityTest {

    @Test
    void place() {
        //setup
        City city1 = new City();
        Building building = new Building(BuildingType.ARCADES, 8);
        Building[][] tempBuildings = new Building[5][5];
        tempBuildings[2][2] = new Building(null, 0);
        tempBuildings[1][2] = building;
        //auto tests if expanding works
        city1.placeBuilding(building, new Location(-1, 0));
        //tests if the building was actually placed and on the right place
        assertArrayEquals(tempBuildings, city1.getBuildings());
        //tests if it throws error when placing building on a building
        assertThrows(AlhambraGameRuleException.class, () -> city1.placeBuilding(building, new Location(-1, 0)));
    }

    @Test
    void remove() {
        //setup
        City city2 = new City();
        Building building = new Building(BuildingType.ARCADES, 8);
        Building[][] tempBuildings = new Building[5][5];
        tempBuildings[2][2] = new Building(null, 0);

        city2.placeBuilding(building, new Location(-1, 0));
        city2.removeBuilding(new Location(-1, 0));
        //tests if the building was actually removed and on the right place
        assertArrayEquals(tempBuildings, city2.getBuildings());
        //tests if it throws error when removing a building that doesnt exist
        assertThrows(IllegalArgumentException.class, () -> city2.removeBuilding(new Location(-1, 0)));
    }

    @Test
    void availableLocations() {
        City city1 = new City();
        Map<CardinalDirection, Boolean> walls = Building.getDefaultWalls();
        walls.put(CardinalDirection.NORTH, true);
        walls.put(CardinalDirection.WEST, true);
        walls.put(CardinalDirection.EAST, true);

        //test if building is placed
        Building b1 = new Building(BuildingType.ARCADES, 5, walls);
        city1.placeBuilding(b1, new Location(-1, 0));
        Location loc = new Location(-1 , 0);
        assertEquals(b1, city1.getBuilding(loc));

        //test so you cant place a building next to a wall
        assertThrows(AlhambraGameRuleException.class, () -> city1.placeBuilding(new Building(BuildingType.ARCADES, 5), new Location(-1, 1)));

        //test so you cant place it if i doesnt connect to the city
        assertThrows(AlhambraGameRuleException.class, () -> city1.placeBuilding(new Building(BuildingType.ARCADES, 5), new Location(-2, 2)));

        //test to check if there is no building on the location you want to place the building
        assertThrows(AlhambraGameRuleException.class, () -> city1.placeBuilding(new Building(BuildingType.ARCADES, 5), new Location(-1, 0)));
    }
}
