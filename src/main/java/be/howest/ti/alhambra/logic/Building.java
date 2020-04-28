package be.howest.ti.alhambra.logic;

import java.util.*;

public class Building {
    private final BuildingType type;
    private final int cost;
    private final Map<WindDirection, Boolean> walls;



    public Building(){
        this.type = null;
        this.cost = 0;
        this.walls = null;
    }

    public BuildingType getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public Map<WindDirection, Boolean> getWalls() {
        return walls;
    }
}
