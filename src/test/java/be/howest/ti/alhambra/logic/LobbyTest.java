package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {

    @Test
    void playerCount() {
        // checks lobby size
        Lobby lobby = new Lobby("game021-005");
        lobby.addPlayer("Joe");
        lobby.addPlayer("Carol");
        lobby.addPlayer("Jef");
        assertEquals(3, lobby.playerCount());

        // checks limit of lobby size (6)
        lobby.addPlayer("Howard");
        lobby.addPlayer("Travis");
        lobby.addPlayer("John");
        assertThrows(IllegalStateException.class, () -> lobby.addPlayer("Dillon"));

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