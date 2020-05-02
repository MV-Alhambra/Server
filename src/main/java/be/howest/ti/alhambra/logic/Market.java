package be.howest.ti.alhambra.logic;

import java.util.Map;

public class Market
{
    //Replace String with Building
    private Map<Currency,String> market;

    public void addBuilding(Currency currency, String building)
    {
        market.put(currency, building);
    }

    public String getBuilding (Currency currency)
    {
        return market.get(currency);
    }

    public void removeBuilding(Currency currency)
    {
        market.remove(currency);
    }
}