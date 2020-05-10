package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Coins {

    private final List<Coin> coinsBag;

    public Coins() {
        this(new ArrayList<>());
    }

    @JsonCreator
    public Coins(@JsonProperty("coins") List<Coin> coins) {
        this.coinsBag = coins;
    }

    public void addCoins(Coin[] coins) {
        for (Coin coin : coins) {
            addCoin(coin);
        }
    }

    private void addCoin(Coin coin) {
        coinsBag.add(coin);
    }

    public void removeCoins(Coin[] coins) {
        for (Coin coin : coins) {
            removeCoin(coin);
        }
    }

    private void removeCoin(Coin coin) {
        if (!coinsBag.remove(coin)) throw new IllegalArgumentException("Couldn't find the coin in the coins bag, coin: " + coin.toString());
    }

    public boolean containsCoins(Coin[] coins) {
        List<Coin> copyCoinsBag = new ArrayList<>(coinsBag);
        for (Coin coin : coins) {
            if (!copyCoinsBag.remove(coin)) return false;
        }
        return true;
    }

    public boolean containsCoin(Coin coin) {
        return coinsBag.contains(coin);
    }

    @JsonGetter("coins")
    public List<Coin> getCoinsBag() {
        return coinsBag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coinsBag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coins coins1 = (Coins) o;
        return Objects.equals(coinsBag, coins1.coinsBag);
    }
}
