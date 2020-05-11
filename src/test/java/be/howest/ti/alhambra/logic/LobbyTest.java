package be.howest.ti.alhambra.logic;

import be.howest.ti.alhambra.logic.exceptions.AlhambraGameRuleException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {

    @Test
    void playerCount() {
        // checks lobby size
        Lobby lobby = new Lobby("game021-005", "testLobby");
        lobby.addPlayer("Joe");
        lobby.addPlayer("Carol");
        lobby.addPlayer("Jef");
        assertEquals(3, lobby.countPlayer());
        assertEquals("testLobby", lobby.getCustomNameLobby());

        // checks limit of lobby size (6)
        lobby.addPlayer("Howard");
        lobby.addPlayer("Travis");
        lobby.addPlayer("John");
        assertThrows(AlhambraGameRuleException.class, () -> lobby.addPlayer("Dillon"));

    }
    @Test
    void maxPlayerCount()
    {
        Lobby lobby = new Lobby("game021-005", "testLobby",4);
        lobby.addPlayer("Joe");
        lobby.addPlayer("Carol");
        lobby.addPlayer("Jef");
        lobby.addPlayer("Howard");
        assertThrows(AlhambraGameRuleException.class, () -> lobby.addPlayer("Dillon"));
    }
    @Test
    void playerName(){
        Lobby lobby = new Lobby("game021-005", "testLobby");
        lobby.addPlayer("Joe");
        lobby.addPlayer("Carol");
        lobby.addPlayer("Jef");

        //throw error if player name already exits
        assertThrows(AlhambraGameRuleException.class, () -> lobby.addPlayer("Carol"));
    }

    @Test
    void readyPlayers(){
        Lobby lobby = new Lobby("game021-005", "testLobby");
        lobby.addPlayer("Joe");
        lobby.addPlayer("Carol");
        lobby.addPlayer("Jef");

        //checks if one player is ready
        lobby.readyUpPlayer("Joe");
        assertEquals(1, lobby.countReady());

        //checks how many are ready
        lobby.readyUpPlayer("Carol");
        lobby.readyUpPlayer("Jef");
        assertEquals(3, lobby.countReady());

        //checks if player can unready
        lobby.unreadyPlayer("Joe");
        assertEquals(2, lobby.countReady());

    }

    @Test
    void startGame() {
        Lobby lobby = new Lobby("game021-005", "testLobby");
        lobby.addPlayer("Joe");

        //checks if the lobby is with more then 1 player
        assertThrows(AlhambraGameRuleException.class, lobby::startGame);

        lobby.addPlayer("Carol");
        lobby.addPlayer("Jef");
        lobby.readyUpPlayer("Joe");
        lobby.readyUpPlayer("Carol");

        // checks if all players are ready to start
        assertThrows(AlhambraGameRuleException.class, lobby::startGame);
    }

    @Test
    void checkPlayers() {
        Lobby lobby = new Lobby("game021-005");
        lobby.addPlayer("Joe");
       // assertEquals(new PlayerInLobby("Joe"),lobby.getPlayerClass("Joe"));
        //assertThrows(IllegalArgumentException.class, ()->lobby.getPlayerClass("henk"));
    }


}