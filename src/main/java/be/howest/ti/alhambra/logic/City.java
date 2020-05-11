package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class City {

    private Building[][] buildings;
    private int mapSize;

    public City() {
        this(City.getDefaultCity());
    }

    @JsonCreator
    public City(@JsonProperty("city") Building[][] buildings) {
        this.buildings = buildings;
        this.mapSize = buildings.length;
    }

    public static Building[][] getDefaultCity() {
        Building[][] defaultCity = new Building[3][3];
        defaultCity[1][1] = new Building(null, 0);
        return defaultCity;
    }

    @JsonGetter("city")
    public Building[][] getBuildings() {
        return buildings;
    }

    public void placeBuilding(Building building, Location location) { // #todo add validation for building allowed to be placed ivm walls,
        location = Location.convertLocationToStaticLocation(location, mapSize);

        if (buildings[location.getRow()][location.getCol()] != null) { // atm i only check if  the location is already used
            throw new IllegalArgumentException("Location is already used by another building");
        } else {
            buildings[location.getRow()][location.getCol()] = building;
        }
        checkMapSize();
    }

    private void checkMapSize() { //checks if the city needs to be expanded
        for (int row = 0; row < buildings.length; row++) {
            for (int col = 0; col < buildings.length; col++) {
                if (buildings[col][row] != null && ((row == 0 || row == mapSize - 1) || (col == 0 || col == mapSize - 1))) {
                    updateMapSize();
                    return;// stop/exit
                }
            }
        }
    }

    private void updateMapSize() { //expands the city
        mapSize += 2;

        Building[][] newBuildings = new Building[mapSize][mapSize];

        for (int row = 0; row < buildings.length; row++) {
            for (int col = 0; col < buildings.length; col++) {
                newBuildings[col + 1][row + 1] = buildings[col][row];
            }
        }
        buildings = newBuildings;
    }

    public void removeBuilding(Location location) {
        location = Location.convertLocationToStaticLocation(location, mapSize);

        if (buildings[location.getRow()][location.getCol()] == null) {
            throw new IllegalArgumentException("Location is already empty");
        } else {
            buildings[location.getRow()][location.getCol()] = null;
        }
    }

    /*
     * Available location is a location that is null, is next to a not null location
     *
     * */
    public List<Location> getAvailableLocations(Map<String, Boolean> walls) {
        List<Location> locations = new ArrayList<>();

        for (int row = 0; row < mapSize; row++) {
            for (int col = 0; col < mapSize; col++) {
                if (buildings[col][row] != null) {
                    if (row - 1 >= 0 && buildings[col][row - 1] == null) {
                        locations.add(new Location(col, row - 1));
                    } if (col - 1 >= 0 && buildings[col - 1][row] == null) {
                        locations.add(new Location(col - 1, row));
                    } if (row + 1 < mapSize && buildings[col][row + 1] == null) {
                        locations.add(new Location(col, row + 1));
                    }  if (col + 1 < mapSize && buildings[col + 1][row] == null) {
                        locations.add(new Location(col + 1, row));
                    }
                }
            }
        }

        return locations;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mapSize);
        result = 31 * result + Arrays.hashCode(buildings);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return mapSize == city.mapSize &&
                Arrays.deepEquals(buildings, city.buildings); //fk i just wasted two hours of my time on this. why does Arrays.equals not properly work with 2 dim arrays
    }

    @Override
    public String toString() {
        return "City{" +
                "buildings=" + Arrays.deepToString(buildings) +
                ", mapSize=" + mapSize +
                '}';
    }
}
