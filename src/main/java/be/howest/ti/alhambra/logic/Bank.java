package be.howest.ti.alhambra.logic;

import java.util.Arrays;

public class Bank {
    private final Coin[] coins;

    public Bank() {
        this(new Coin[4]);
    }

    public Bank(Coin[] coins) {
        if (coins.length != 4) {
            throw new IllegalArgumentException("Only arrays of length 4 allowed");
        }
        this.coins = coins;
    }

    public void grabCoins() {

    }

    public void addCoin(Coin coin) {
        for (int i = 0; i < coins.length; i++) {
            if (coins[i] == null) {
                coins[i] = coin;
                return;
            }
        }
        throw new IllegalStateException("The bank is full");
    }

    @Override
    public String toString() {
        return "Bank{" +
                "coins=" + Arrays.toString(coins) +
                '}';
    }

    public Coin[] getCoins() {
        return coins;
    }
}
