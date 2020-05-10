package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Market {
    private final Map<Currency, Building> markets;

    @JsonCreator
    public Market() {
        this(new HashMap<>());
    }

    @JsonCreator
    public Market(@JsonProperty("market") Map<Currency, Building> market) {
        this.markets = market;
        initMarkets();
    }

    private void initMarkets() { // adds a market for each currency
        if (markets.isEmpty()) Arrays.stream(Currency.values()).forEach(currency -> markets.put(currency, null));
    }

    public void addBuilding(Currency currency, Building building) {
        markets.put(currency, building);
    }

    public Building getBuilding(Currency currency) {
        return markets.get(currency);
    }

    @JsonGetter("market")
    public Map<Currency, Building> getMarket() {
        return markets;
    }

    public Building removeBuilding(Currency currency) {
        return markets.put(currency, null);
    }

    public boolean containsBuilding(Currency currency) {
        return markets.containsKey(currency);
    }

    public void fillMarkets(Game game) {
        markets.keySet().stream()
                .filter(currency -> markets.get(currency) == null)
                .forEach(currency -> markets.put(currency, game.removeBuilding()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(markets);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Market market1 = (Market) o;
        return Objects.equals(markets, market1.markets);
    }
}