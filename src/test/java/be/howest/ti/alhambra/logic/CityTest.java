package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertThrows(IllegalArgumentException.class, () -> city1.placeBuilding(building, new Location(-1, 0)));
        //needs further expanding when further validation is completed
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

    //@Test
    void availableLocations() {
        City city1 = new City();
        Map<String, Boolean> walls = Building.getDefaultWalls();
        System.out.println(walls);
        walls.put("north", true);
        walls.put("west", true);
        walls.put("east", true);

        System.out.println(city1.getAvailableLocations(walls));
        city1.placeBuilding(new Building(BuildingType.ARCADES, 5, walls), new Location(-1, 0));
        System.out.println(Arrays.deepToString(city1.getBuildings()));
        System.out.println(city1.getAvailableLocations(Building.getDefaultWalls()));
        city1.placeBuilding(new Building(BuildingType.ARCADES, 5), new Location(-1, 1));
        System.out.println(Arrays.deepToString(city1.getBuildings()));
        System.out.println(city1.getAvailableLocations(Building.getDefaultWalls()));

    }
}
