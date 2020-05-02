package be.howest.ti.alhambra.logic.exceptions;

import be.howest.ti.alhambra.logic.Building;

public class City {

    private static final Building[][] defaultBuildings = { {null,null,null},{null,new Building(null,0),null},{null,null,null}};
    private Building[][] buildings;

    public City(Building[][] buildings) {
        this.buildings = buildings;
    }
    public City(){
        this(City.defaultBuildings);
    }
}
