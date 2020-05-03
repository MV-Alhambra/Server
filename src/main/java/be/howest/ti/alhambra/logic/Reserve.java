package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Reserve {
    private final List<Building> buildings;

    @JsonCreator
    public Reserve(@JsonProperty("reserve") List<Building> buildings){
        this.buildings = buildings;
    }

    @JsonCreator
    public Reserve(){
        this(new ArrayList<>());
    }
    @JsonGetter("reserve")
    public List<Building> getBuildings() {
        return buildings;
    }

    public void addBuilding(Building building){
        buildings.add(building);
    }

    public void removeBuilding(Building building){
        if (!buildings.remove(building)) {
            throw new IllegalArgumentException("Couldn't find the building in your reserve");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserve reserve1 = (Reserve) o;
        return Objects.equals(buildings, reserve1.buildings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buildings);
    }
}
