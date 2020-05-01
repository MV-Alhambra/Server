package be.howest.ti.alhambra.logic;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.*;

public class Building {
    private final String type;
    private final int cost;
    private final Map<String, Boolean> walls;


    @JsonCreator
    public Building()
        {
        this.type = null;
        this.cost = -1;
        this.walls = null;
    }

    public String getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public Map<String, Boolean> getWalls() {
        return walls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;
        return cost == building.cost &&
                Objects.equals(type, building.type) &&
                Objects.equals(walls, building.walls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, cost, walls);
    }
}
