package be.howest.ti.alhambra.logic;


public class AlhambraController {

    public Building getBuildings(){
        return new Building(null, -1, null);
    }

    public Currency[] getCurrencies(){
        return  Currency.values();
    }

    public BuildingType[] getBuildingTypes(){ return BuildingType.values(); }

}
