package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.Test;

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReserveTest {

    @Test
    void reserve(){
        Building building1 = new Building(BuildingType.valueOf("PAVILION"), 5, null);
        Building building2 = new Building(BuildingType.valueOf("CHAMBERS"), 9, null);
        Building building3 = new Building(BuildingType.valueOf("GARDEN"), 11, null);
        List<Building> buildings = new ArrayList<>();
        buildings.add(building1);
        buildings.add(building2);
        buildings.add(building3);

        Reserve reserve = new Reserve(buildings);
        Reserve reserve2 = new Reserve(buildings);

        assertEquals(building1, reserve.getBuildings().get(0));
        assertEquals(building2, reserve.getBuildings().get(1));
        assertEquals(building3, reserve.getBuildings().get(2));

        assertEquals(reserve, reserve2);
    }
    void testEqualsAndHashcode(){
        Building building1 = new Building(BuildingType.valueOf("PAVILION"), 5, null);
        List<Building> buildings = new ArrayList<>();
        buildings.add(building1);

        Reserve reserve = new Reserve(buildings);
        Reserve reserve2 = new Reserve(buildings);

        assertTrue(reserve.equals(reserve2) && reserve2.equals(reserve));
        assertEquals(reserve.hashCode(), reserve2.hashCode());
    }

}
