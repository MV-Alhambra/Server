package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.*;

public class City {

    private static final Building[][] defaultCity = {{null, null, null}, {null, new Building(null, 0), null}, {null, null, null}};
    private Building[][] buildings;
    private int mapSize;

    @JsonCreator
    public City(@JsonProperty("city") Building[][] buildings) {
        this.buildings = buildings;
        this.mapSize = buildings.length;
    }

    public City() {
        this(City.defaultCity);
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
        checkMapSize();
    }

    public void removeBuilding(Location location) {
        location = Location.convertLocationToStaticLocation(location, mapSize);

        if (buildings[location.getCol()][location.getRow()] == null) {
            throw new IllegalArgumentException("Location is already empty");
        } else {
            buildings[location.getCol()][location.getRow()] = null;
        }
    }

    private void checkMapSize() {
        for (int row = 0; row < buildings.length; row++) {
            for (int col = 0; col < buildings.length; col++) {
                if (buildings[col][row] != null && ((row == 0 || row == mapSize) || (col == 0 || col == mapSize))) {
                    updateMapSize();
                    return;// stop/exit
                }
            }
        }
    }

    private void updateMapSize() {
        mapSize += 2;

        Building[][] newBuildings = new Building[mapSize][mapSize];

        for (int row = 0; row < buildings.length; row++) {
            for (int col = 0; col < buildings.length; col++) {
                newBuildings[col + 2][row + 2] = buildings[col][row];
            }
        }
        buildings = newBuildings;
    }
}
