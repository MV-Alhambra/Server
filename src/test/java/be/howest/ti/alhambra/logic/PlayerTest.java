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

}
