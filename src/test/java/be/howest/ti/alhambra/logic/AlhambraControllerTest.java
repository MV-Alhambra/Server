package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlhambraControllerTest {

    @Test
    void AlhambraCont() {
        AlhambraController c = new AlhambraController();

        //Test addLobby function
        assertEquals("001", c.addLobby("Test", 6, false));

        assertEquals(new Building(null, -1, null), c.getBuildings());

        List<Lobby> lobbylist = new ArrayList<>();
        Lobby l = new Lobby("001" , "Test", 6, false);
        lobbylist.add(l);

        assertEquals(lobbylist, c.getLobbies());
        c.joinLobby("001", "mitch");

        //assertEquals(l, c.getGame("001"));

        assertTrue(c.leaveLobby("001", "mitch"));

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
                    AlhambraController c1 = new AlhambraController();
                    c1.addLobby("Test", 6, false);
                    c1.leaveLobby("001", "test");
                }
                );
        assertEquals("player not in lobby", exception.getMessage());
        c.joinLobby("001", "test1");
        c.joinLobby("001", "test2");
        c.readyUp("001", "test1");
        c.readyUp("001", "test2");
        c.readyDown("001", "test1");
        c.readyUp("001", "test1");
        assertTrue(c.startLobby("001"));
        assertFalse(c.startLobby("001"));
    }

    @Test
    void currencies(){
        AlhambraController c = new AlhambraController();
        assertArrayEquals(Currency.values(),c.getCurrencies());
    }
    @Test
    void buildingTypes(){
        AlhambraController c = new AlhambraController();
        assertArrayEquals(BuildingType.values(),c.getBuildingTypes());
    }
}
