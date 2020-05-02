package be.howest.ti.alhambra.logic.exceptions;

import be.howest.ti.alhambra.logic.Building;
import com.fasterxml.jackson.annotation.*;

public class City {

    private static final Building[][] defaultCity = {{null, null, null}, {null, new Building(null, 0), null}, {null, null, null}};
    private Building[][] buildings;

    @JsonCreator
    public City(@JsonProperty("city") Building[][] buildings) {
        this.buildings = buildings;
    }

    public City() {
        this(City.defaultCity);
    }

    @JsonGetter("city")
    public Building[][] getBuildings() {
        return buildings;
    }
}
