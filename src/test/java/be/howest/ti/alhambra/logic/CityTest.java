package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CityTest {

    @Test
    void place(){
        //setup
        City city = new City();
        Building building = new Building(BuildingType.ARCADES,8);
        Building[][] tempBuildings = new Building[5][5];
        tempBuildings[2][2] = new Building(null,0);
        tempBuildings[1][2] = building;
        //auto tests if expanding works
        city.placeBuilding(building,new Location(0,-1));
        //tests if the building was actually placed and on the right place
        assertArrayEquals(tempBuildings,city.getBuildings());
        //tests if it throws error when placing building on a building
        assertThrows(IllegalArgumentException.class,()->city.placeBuilding(building,new Location(0,-1)));
        //needs further expanding when further validation is completed
    }
    @Test
    void remove(){
        //setup
        City city = new City();
        Building building = new Building(BuildingType.ARCADES,8);
        Building[][] tempBuildings = new Building[5][5];
        tempBuildings[2][2] = new Building(null,0);

        city.placeBuilding(building,new Location(0,-1));
        city.removeBuilding( new Location(0,-1));
        //tests if the building was actually removed and on the right place
        assertArrayEquals(tempBuildings,city.getBuildings());
        //tests if it throws error when removing a building that doesnt exist
        assertThrows(IllegalArgumentException.class,()->city.removeBuilding(new Location(0,-1)));
    }
}
