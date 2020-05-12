package be.howest.ti.alhambra.logic;

import be.howest.ti.alhambra.logic.exceptions.AlhambraGameRuleException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
import java.util.stream.Collectors;

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

    public Building getBuilding(Location location) {
        return buildings[location.getRow()][location.getCol()];
    }

    public void placeBuilding(Building building, Location location) { // places a building in the city
        if (getAvailableLocations(building.getWalls()).contains(location)) { //check if it is a valid location
            location = Location.convertLocationToStaticLocation(location, mapSize);
            buildings[location.getRow()][location.getCol()] = building;
        } else {
            throw new AlhambraGameRuleException("You can't place a building here");
        }
        checkMapSize();
    }

    /*
     * Available location is a location that is null, is next to a not null location ( so i had also i had to check that i dont try to check tiles that arent inside the ),
     *  check if walls allow it: check if giving walls allow it and check walls of the building next to it allow it
     *  Remove duplicates
     *  #todo even more validation: only walls on walls so no building next to another can have a wall and no wall
     * */
    public List<Location> getAvailableLocations(Map<String, Boolean> walls) {
        List<Location> locations = new ArrayList<>();

        for (int row = 0; row < mapSize; row++) {
            for (int col = 0; col < mapSize; col++) {
                if (buildings[row][col] != null) {
                    if (!walls.get("south") && !buildings[row][col].getWalls().get("north") && row - 1 >= 0 && buildings[row - 1][col] == null) { // check above the current location
                        locations.add(Location.convertStaticLocationToLocation(new Location(row - 1, col), mapSize));
                    }
                    if (!walls.get("east") && !buildings[row][col].getWalls().get("west") && col - 1 >= 0 && buildings[row][col - 1] == null) { // check left of the current location
                        locations.add(Location.convertStaticLocationToLocation(new Location(row, col - 1), mapSize));
                    }
                    if (!walls.get("north") && !buildings[row][col].getWalls().get("south") && row + 1 < mapSize && buildings[row + 1][col] == null) { // check below the current location
                        locations.add(Location.convertStaticLocationToLocation(new Location(row + 1, col), mapSize));
                    }
                    if (!walls.get("west") && !buildings[row][col].getWalls().get("east") && col + 1 < mapSize && buildings[row][col + 1] == null) { // check right of the current location
                        locations.add(Location.convertStaticLocationToLocation(new Location(row, col + 1), mapSize));
                    }
                }
            }
        }
        return locations.stream().distinct().collect(Collectors.toList()); // remove duplicates
    }

    private void checkMapSize() { //checks if the city needs to be expanded
        for (int row = 0; row < buildings.length; row++) {
            for (int col = 0; col < buildings.length; col++) {
                if (buildings[row][col] != null && ((row == 0 || row == mapSize - 1) || (col == 0 || col == mapSize - 1))) { //checks if there is a building on the outer ring
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
            System.arraycopy(buildings[row], 0, newBuildings[row + 1], 1, buildings.length); //basically copies a complete row and puts in the new array with one offset so its in the middle
        }
        buildings = newBuildings;
    }

    public Building removeBuilding(Location location) {
        location = Location.convertLocationToStaticLocation(location, mapSize);
        Building building = buildings[location.getRow()][location.getCol()];

        if (building == null) {
            throw new IllegalArgumentException("Location is already empty");
        } else {
            buildings[location.getRow()][location.getCol()] = null;
            return building;
        }
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
