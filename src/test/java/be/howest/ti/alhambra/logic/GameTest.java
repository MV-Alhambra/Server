package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.Test;

public class GameTest {

    @Test
    void loadBuildings(){
        Game game = new Game();
        System.out.println(game.getBuildings());
    }

    @Test
    void allCoins(){
        System.out.println(Coin.allCoins());
    }

}
