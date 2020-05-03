package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.*;
import java.util.*;

public class Coins {

    private final List<Coin> coinsBag;

    @JsonCreator
    public Coins(@JsonProperty("coins") List<Coin> coins) {
        this.coinsBag = coins;
    }

    @JsonCreator
    public Coins() {
        this(new ArrayList<>());
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

    public boolean containsCoin(Coin coin) {
        return coinsBag.contains(coin);
    }

    @JsonGetter("coins")
    public List<Coin> getCoinsBag() {
        return coinsBag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coins coins1 = (Coins) o;
        return Objects.equals(coinsBag, coins1.coinsBag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coinsBag);
    }
}
