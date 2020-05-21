package be.howest.ti.alhambra.logic;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Building {

    private final BuildingType type;
    private final int cost;
    private final Map<CardinalDirection, Boolean> walls;


    public Building(BuildingType type, int cost) { //chaining
        this(type, cost, getDefaultWalls());
    }

    @JsonCreator
    public Building(
            @JsonProperty("type") BuildingType type,
            @JsonProperty("cost") int cost,
            @JsonProperty("walls") Map<CardinalDirection, Boolean> walls
    ) {
        this.type = type;
        this.cost = cost;
        this.walls = walls;
    }

    public static Map<CardinalDirection, Boolean> getDefaultWalls() {
        Map<CardinalDirection, Boolean> tempWalls = new HashMap<>();
        Arrays.stream(CardinalDirection.values()).forEach(cardinalDirection -> tempWalls.put(cardinalDirection, false));
        return tempWalls;
    }

    public Building(Building building) { // constructor for my stream in city
        this.type = building.type;
        this.cost = building.cost;
        this.walls = new HashMap<>(building.getWalls()); // no reference but copy
    }

    public Map<CardinalDirection, Boolean> getWalls() {
        return walls;
    }

    public BuildingType getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public boolean getWall(CardinalDirection cardinalDirection) {
        return walls.get(cardinalDirection);
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
