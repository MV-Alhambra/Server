package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.*;

public class City {

    private static final Building[][] defaultCity = {{null, null, null}, {null, new Building(null, 0), null}, {null, null, null}};
    private Building[][] buildings;
    private int mapSize;

    @JsonCreator
    public City(@JsonProperty("city") Building[][] buildings, int mapSize) {
        this.buildings = buildings;
        this.mapSize = mapSize;
    }

    public City() {
        this(City.defaultCity, 3);
    }

    @JsonGetter("city")
    public Building[][] getBuildings() {
        return buildings;
    }

    public void placeBuilding(Building building, Location location) {
        location = Location.convertLocationToStaticLocation(location, mapSize);

        if (buildings[location.getCol()][location.getRow()] == null) {
            throw new IllegalArgumentException("Location is already used by another building");
        } else {
            buildings[location.getCol()][location.getRow()] = building;
        }
    }

    public void removeBuilding(Location location){
        location = Location.convertLocationToStaticLocation(location, mapSize);

        if (buildings[location.getCol()][location.getRow()] == null) {
            throw new IllegalArgumentException("Location is already empty");
        } else {
            buildings[location.getCol()][location.getRow()] = null;
        }

    }
}
