package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlhambraControllerTest {

    @Test
    void AlhambraCont() {
        AlhambraController c = new AlhambraController();

        assertEquals("001", c.addLobby("Test", 6, false));

    }
}
