package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {

    @Test
    void playerCount() {
        Lobby lobby = new Lobby("game021-005");
        lobby.addPlayer("Joe");
        lobby.addPlayer("Carol");
        lobby.addPlayer("Jef");
        assertEquals(3, lobby.playerCount());
    }

    @Test
    void playerName(){
        Lobby lobby = new Lobby("game021-005");
        lobby.addPlayer("Joe");
        lobby.addPlayer("Carol");
        lobby.addPlayer("Jef");

        //throw error if player name already exits
        assertThrows(IllegalArgumentException.class, () -> lobby.addPlayer("Carol"));
    }

}