package be.howest.ti.alhambra.logic;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Building {

    private final BuildingType type;
    private final int cost;
    private final Map<String, Boolean> walls;


    public Building(BuildingType type, int cost) { //chaining
        this(type, cost, getDefaultWalls());
    }

    @JsonCreator
    public Building(
            @JsonProperty("type") BuildingType type,
            @JsonProperty("cost") int cost,
            @JsonProperty("walls") Map<String, Boolean> walls
    ) {
        this.type = type;
        this.cost = cost;
        this.walls = walls;
    }

    public static Map<String, Boolean> getDefaultWalls() {
        Map<String, Boolean> tempWalls = new HashMap<>();
        tempWalls.put("north", false);
        tempWalls.put("east", false);
        tempWalls.put("south", false);
        tempWalls.put("west", false);
        return tempWalls;
    }

    public BuildingType getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public Map<String, Boolean> getWalls() {
        return walls;
    }

    public boolean getWall(String key) {
        return walls.get(key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, cost, walls);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;
        return cost == building.cost && Objects.equals(type, building.type) && Objects.equals(walls, building.walls);
    }

    @Override
    public String toString() {
        return "Building{" +
                "type=" + type +
                ", cost=" + cost +
                ", walls=" + walls +
                '}';
    }
}
