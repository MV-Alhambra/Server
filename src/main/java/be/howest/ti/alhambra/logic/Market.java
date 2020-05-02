package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Market
{
    private final Map<Currency,Building> market;

    @JsonCreator
    public Market(@JsonProperty("market") Map<Currency,Building> market)
    {
        this.market = market;
    }

    @JsonCreator
    public Market()
    {
        this(new HashMap<>());
    }

    public void addBuilding(Currency currency, Building building)
    {
        market.put(currency, building);
    }

    public Building getBuilding (Currency currency)
    {
        return market.get(currency);
    }

    @JsonGetter("market")
    public Map<Currency,Building> getMarket()
    {
        return market;
    }

    public void removeBuilding(Currency currency)
    {
        market.remove(currency);
    }

    public boolean containsBuilding(Currency currency)
    {
        return market.containsKey(currency);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Market market1 = (Market) o;
        return Objects.equals(market, market1.market);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(market);
    }
}