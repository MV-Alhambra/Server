package be.howest.ti.alhambra.logic;

import java.util.Map;

public class Market
{
    private Map<Currency,Building> market;

    public void addBuilding(Currency currency, Building building)
    {
        market.put(currency, building);
    }

    public Building getBuilding (Currency currency)
    {
        return market.get(currency);
    }

    public void removeBuilding(Currency currency)
    {
        market.remove(currency);
    }
}