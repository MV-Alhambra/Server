package be.howest.ti.alhambra.logic;


import java.util.List;

public class AlhambraController {

    private List<Lobby> lobbies;

    public Building getBuildings(){
        return new Building(null, -1, null);
    }

    public Currency[] getCurrencies(){
        return  Currency.values();
    }

    public BuildingType[] getBuildingTypes(){ return BuildingType.values(); }

    public List<Lobby> getLobbies(){
        return lobbies;
    }


}
