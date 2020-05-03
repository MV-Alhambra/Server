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

    @Test
    void readyPlayers(){
        Lobby lobby = new Lobby("game021-005");
        lobby.addPlayer("Joe");
        lobby.addPlayer("Carol");
        lobby.addPlayer("Jef");

        //checks if one player is ready
        lobby.readyUpPlayer("Joe");
        assertEquals(1, lobby.amountReady());

        //checks how many are ready
        lobby.readyUpPlayer("Carol");
        lobby.readyUpPlayer("Jef");
        assertEquals(3, lobby.amountReady());

        //checks if player can unready
        lobby.unreadyPlayer("Joe");
        assertEquals(2, lobby.amountReady());

    }

    @Test
    void startGame() {
        Lobby lobby = new Lobby("game021-005");
        lobby.addPlayer("Joe");
        lobby.addPlayer("Carol");
        lobby.addPlayer("Jef");
        lobby.readyUpPlayer("Joe");
        lobby.readyUpPlayer("Carol");

        // checks if all players are ready to start
        assertThrows(IllegalStateException.class, lobby::startGame);

        // starting game
        lobby.readyUpPlayer("Jef");
        lobby.startGame();
        assertTrue(lobby.isStarted());

    }


}