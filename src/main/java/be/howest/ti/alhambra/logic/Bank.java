package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

public class Bank {
    private final Coin[] bank;

    @JsonCreator
    public Bank() {
        this(new Coin[4]);
    }

    @JsonCreator
    public Bank(@JsonProperty("bank") Coin[] bank) {
        if (bank.length != 4) {
            throw new IllegalArgumentException("Only arrays of length 4 allowed");
        }
        this.bank = bank;
    }

    public void removeCoins(Coin[] coins) {
        if (coins.length > 4) {
            throw new IllegalArgumentException("Can only remove upto 4 coins");
        }
        for (Coin coin : coins) {
            for (int i = 0; i < this.bank.length; i++) {
                if (coin.equals(this.bank[i])) {
                    this.bank[i] = null;
                    i = this.bank.length;//prevent multiple coins from being removed
                } else if (i == this.bank.length - 1) {
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
        for (int i = 0; i < bank.length; i++) {
            if (bank[i] == null) {
                bank[i] = coin;
                return;
            }
        }
        throw new IllegalStateException("The bank is full");
    }

    public int countEmptyCoins() {
        return (int) Arrays.stream(bank).filter(Objects::isNull).count();
    }

    public Coin[] getBank() {
        return bank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Arrays.equals(this.bank, bank.bank);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bank);
    }
}
