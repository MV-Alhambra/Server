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



    }
}
