package be.howest.ti.alhambra.logic;

import be.howest.ti.alhambra.logic.exceptions.AlhambraEntityNotFoundException;
import be.howest.ti.alhambra.logic.exceptions.AlhambraGameRuleException;
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

        Market market = game.getMarket();
        Coin coin = new Coin(Currency.ORANGE,5);
        while (coin !=null){
            coin = game.removeCoin();
        }
        assertNull(game.removeCoin());
        assertThrows(AlhambraEntityNotFoundException.class,()->game.giveBuildingToDirk(null,null));

        //test endGame
        game.removePlayer("Joe");
        assertTrue(game.isEnded());

        assertEquals(market,game.getMarket());;

        Game game2 = new Game(names);

        assertThrows(AlhambraGameRuleException.class,()->  game2.buyBuilding("Joe",Currency.ORANGE,new Coin[]{new Coin(Currency.ORANGE,48)}));

       Building building = new Building(BuildingType.GARDEN,5);
        while (building !=null){
           building = game2.removeBuilding();
        }
        assertNull(game2.removeBuilding());


    }
}
