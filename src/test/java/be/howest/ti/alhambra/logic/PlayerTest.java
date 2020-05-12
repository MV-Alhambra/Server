package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    void player(){
        Player player = new Player("p1");
        assertEquals("p1", player.getName());
    }
    @Test
    void testEqualsAndHashcode(){
        Player p1 = new Player("test");
        Player p2 = new Player("test");
        Player p3 = new Player("Tset");


        assertTrue(p1.equals(p2) && p2.equals(p1));
        assertFalse(p1.equals(p3) && p3.equals(p1));
        assertEquals(p1.hashCode(), p2.hashCode());
    }
    @Test
    void score(){
        List<PlayerInLobby> names = new ArrayList<>();
        PlayerInLobby p1 = new PlayerInLobby("P1", true);
        PlayerInLobby p2 = new PlayerInLobby("P2", true);
        PlayerInLobby p3 = new PlayerInLobby("P3", true);

        names.add(p1);
        names.add(p2);
        names.add(p3);
        Game g = new Game(names);

        Building b1 = new Building(BuildingType.PAVILION, 5);
        Building b2 = new Building(BuildingType.CHAMBERS, 9);
        Building b3 = new Building(BuildingType.CHAMBERS, 4);
        Building b4 = new Building(BuildingType.TOWER, 8);
        Building b5 = new Building(BuildingType.SERAGLIO, 8);
        Building b6 = new Building(BuildingType.GARDEN, 8);
        Building b7 = new Building(BuildingType.ARCADES, 5);

        Player pl1 = g.getPlayers().get(0);
        Player pl2 = g.getPlayers().get(1);
        Player pl3 = g.getPlayers().get(2);

        pl1.getCity().placeBuilding(b1, new Location(1,0));
        pl1.getCity().placeBuilding(b2, new Location(0,1));
        pl1.getCity().placeBuilding(b3, new Location(1,1));

        pl2.getCity().placeBuilding(b4, new Location(1,0));
        pl2.getCity().placeBuilding(b5, new Location(0,1));

        pl3.getCity().placeBuilding(b6, new Location(1,0));
        pl3.getCity().placeBuilding(b7, new Location(0,1));

        assertEquals(3 , pl1.calcScore(g, 1));
        
    }
}
