package be.howest.ti.alhambra.logic;

import java.util.ArrayList;
import java.util.List;

public class Coins {

    private final List<Coin> coins;


    public Coins(List<Coin> coins) {
        this.coins = coins;
    }

    public Coins() {
        this(new ArrayList<>());
    }

    public void addCoins(Coin[] coins) {
        for (Coin coin : coins) {
            addCoin(coin);
        }
    }

    private void addCoin(Coin coin) {
        coins.add(coin);
    }

    public void removeCoins(Coin[] coins) {
        for (Coin coin : coins) {
            removeCoin(coin);
        }
    }

    private void removeCoin(Coin coin) {
        if (!coins.remove(coin)) {
            throw new IllegalArgumentException("Couldn't find the coin in the coins bag, coin: " + coin.toString());
        }
    }

    public boolean containsCoin(Coin coin) {
        return coins.contains(coin);
    }

    public List<Coin> getCoins() {
        return coins;
    }
}
