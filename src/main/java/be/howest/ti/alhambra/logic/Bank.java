package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

public class Bank {
    private final Coin[] bankCoins;

    @JsonCreator
    public Bank() {
        this(new Coin[4]);
    }

    @JsonCreator
    public Bank(@JsonProperty("bank") Coin[] bank) {
        if (bank.length != 4) throw new IllegalArgumentException("Only arrays of length 4 allowed");
        this.bankCoins = bank;
    }

    public void removeCoins(Coin[] coins) {
        removeCoins(coins, false);
    }

    public void removeCoins(Coin[] coins, boolean dontActuallyRemove) {
        if (coins.length > 4) throw new IllegalArgumentException("Can only remove upto 4 coins");

        Coin[] actualCoins = dontActuallyRemove ? bankCoins.clone() : bankCoins; // now i can use it as a test if it contains or actually remove it

        for (Coin coin : coins) {
            boolean flag = true;
            for (int i = 0; i < actualCoins.length && flag; i++) {
                if (coin.equals(actualCoins[i])) {
                    actualCoins[i] = null;
                    flag = false;
                } else if (i == actualCoins.length - 1) {
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
        for (int i = 0; i < bankCoins.length; i++) {
            if (bankCoins[i] == null) {
                bankCoins[i] = coin;
                return;
            }
        }
        throw new IllegalStateException("The bank is full");
    }

    public void fillBank(Game game) {
        Arrays.stream(bankCoins).filter(Objects::isNull).forEach(coin -> addCoin(game.removeCoin()));
    }

    public int countEmptyCoins() {
        return (int) Arrays.stream(bankCoins).filter(Objects::isNull).count();
    }

    @JsonGetter("bank")
    public Coin[] getBankCoins() {
        return bankCoins;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bankCoins);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Arrays.equals(this.bankCoins, bank.bankCoins);
    }
}
