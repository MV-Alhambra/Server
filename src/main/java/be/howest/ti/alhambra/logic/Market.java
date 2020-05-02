package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Market
{
    private final Map<Currency,Building> buildingMarket;

    @JsonCreator
    public Market(@JsonProperty("market") Map<Currency,Building> market)
    {
        this.buildingMarket = market;
    }

    @JsonCreator
    public Market()
    {
        this(new HashMap<>());
    }

    public void addBuilding(Currency currency, Building building)
    {
        buildingMarket.put(currency, building);
    }

    public Building getBuilding (Currency currency)
    {
        return buildingMarket.get(currency);
    }

    @JsonGetter("market")
    public Map<Currency,Building> getMarket()
    {
        return buildingMarket;
    }

    public void removeBuilding(Currency currency)
    {
        buildingMarket.remove(currency);
    }

    public boolean containsBuilding(Currency currency)
    {
        return buildingMarket.containsKey(currency);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Market market1 = (Market) o;
        return Objects.equals(buildingMarket, market1.buildingMarket);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(buildingMarket);
    }
}