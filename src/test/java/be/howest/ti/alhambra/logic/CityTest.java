package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CityTest {

    @Test
    void place(){
        //setup
        City city1 = new City();
        Building building = new Building(BuildingType.ARCADES,8);
        Building[][] tempBuildings = new Building[5][5];
        tempBuildings[2][2] = new Building(null,0);
        tempBuildings[1][2] = building;
        //auto tests if expanding works
        System.out.println(Arrays.deepToString(city1.getBuildings()));
        city1.placeBuilding(building,new Location(0,-1));
        System.out.println(Arrays.deepToString(city1.getBuildings()));
        //tests if the building was actually placed and on the right place
        assertArrayEquals(tempBuildings,city1.getBuildings());
        //tests if it throws error when placing building on a building
        assertThrows(IllegalArgumentException.class,()->city1.placeBuilding(building,new Location(0,-1)));
        //needs further expanding when further validation is completed
    }
    @Test
    void remove(){
        //setup
        City city2 = new City();
        Building building = new Building(BuildingType.ARCADES,8);
        Building[][] tempBuildings = new Building[5][5];
        tempBuildings[2][2] = new Building(null,0);

        city2.placeBuilding(building,new Location(0,-1));
        city2.removeBuilding( new Location(0,-1));
        //tests if the building was actually removed and on the right place
        assertArrayEquals(tempBuildings,city2.getBuildings());
        //tests if it throws error when removing a building that doesnt exist
        assertThrows(IllegalArgumentException.class,()->city2.removeBuilding(new Location(0,-1)));
    }
}
