package be.howest.ti.alhambra.logic;

import be.howest.ti.alhambra.logic.exceptions.AlhambraEntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest
{
    @Test
    void game()
    {
        List<PlayerInLobby> names = new ArrayList<>();
        PlayerInLobby p1 = new PlayerInLobby("Carol");
        PlayerInLobby p2 = new PlayerInLobby("Joe");
        names.add(p1);
        names.add(p2);
        Game game = new Game(names);

        //test remove player
        assertEquals(p1.getName(), game.findPlayer("Carol").getName());
        game.removePlayer("Carol");
        assertThrows(AlhambraEntityNotFoundException.class, ()->game.findPlayer("Carol"));

        //test endGame
        game.removePlayer("Joe");
        assertTrue(game.isEnded());

    }
}
