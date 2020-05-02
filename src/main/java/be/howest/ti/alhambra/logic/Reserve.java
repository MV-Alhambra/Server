package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class Reserve {
    private final List<Building> buildings; //Temporarily with String values instead of real buildings

    @JsonCreator
    public Reserve(@JsonProperty("buildings") List<Building> buildings){
        this.buildings = buildings;
    }

    public List<Building> getBuildings() {
        return buildings;
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
