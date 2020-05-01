package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

public class Bank {
    private final Coin[] coins;

    @JsonCreator
    public Bank() {
        this(new Coin[4]);
    }

    @JsonCreator
    public Bank(@JsonProperty("coins") Coin[] coins) {
        if (coins.length != 4) {
            throw new IllegalArgumentException("Only arrays of length 4 allowed");
        }
        this.coins = coins;
    }

    public void removeCoins(Coin[] coins) {
        if (coins.length > 4) {
            throw new IllegalArgumentException("Can only remove upto 4 coins");
        }
        for (Coin coin : coins) {
            for (int i = 0; i < this.coins.length; i++) {
                if (coin.equals(this.coins[i])) {
                    this.coins[i] = null;
                    i = this.coins.length;//prevent multiple coins from being removed
                } else if (i == this.coins.length - 1) {
                    throw new IllegalArgumentException("Couldn't find the coin in the bank");
                }
            }
        }
    }

    public void addCoins(Coin[] coins) {
        for (Coin coin : coins) {
            addCoin(coin);
        }
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

    public int countEmptyCoins() {
        return (int) Arrays.stream(coins).filter(Objects::isNull).count();
    }

    public Coin[] getCoins() {
        return coins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Arrays.equals(coins, bank.coins);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coins);
    }
}
