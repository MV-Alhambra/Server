package be.howest.ti.alhambra.logic;

import be.howest.ti.alhambra.logic.exceptions.AlhambraGameRuleException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
import java.util.stream.Collectors;

import static be.howest.ti.alhambra.logic.CardinalDirection.*;

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
        Building[][] defaultCity = new Building[3][3]; //on purpose creating a new object
        defaultCity[1][1] = new Building(null, 0);
        return defaultCity;
    }

    public Integer countType(BuildingType type) {
        return (int) Arrays.stream(buildings)
                .flatMap(Arrays::stream) //pulls the items out of the array, so essentially flatten the data structure
                .filter(Objects::nonNull) //remove nulls
                .filter(building -> type.equals(building.getType())) //filter the types based on parameter
                .count();
    }

    @JsonGetter("city")
    public Building[][] getBuildings() {
        return buildings;
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

    /*
     * Available location is a location that is null, is next to a not null location,
     *  check if walls allow it: check if giving walls allow it and check walls of the building next to it allow it
     * then checks the surroundings of that location (walls match)
     * checks if the placed building wont create a hole(empty space surrounded with buildings)
     * remove duplicates
     */
    public List<Location> getAvailableLocations(Map<CardinalDirection, Boolean> walls) {
        List<Location> locations = new ArrayList<>();

        for (int row = 0; row < mapSize; row++) {
            for (int col = 0; col < mapSize; col++) {
                if (buildings[row][col] != null) { //checks the available locations of each building
                    logicAvailableLocations(walls, locations, row, col);
                }
            }
        }
        return locations.stream().distinct().collect(Collectors.toList()); // remove duplicates
    }

    private void logicAvailableLocations(Map<CardinalDirection, Boolean> walls, List<Location> locations, int row, int col) {
        Location.getSurroundingLocationsWithCD(new Location(row, col)).entrySet().stream() //gets the locations around the current location
                .filter(entry -> checkAvailableLocation(walls, getOppositeCD(entry.getKey()), row, col, entry.getValue())) //checks if the location is available
                .forEach(entry -> locations.add(Location.convertStaticLocationToLocation(entry.getValue(), mapSize))); //if so then add it the locations
    }


    // check if no walls are blocking ( from giving walls and from checked location's building), check if the walls match and check if the location hasn't been used yet
    private boolean checkAvailableLocation(Map<CardinalDirection, Boolean> walls, CardinalDirection wall, int row, int col, Location staticLocation) {
        return !walls.get(wall) && !buildings[row][col].getWall(getOppositeCD(wall)) && buildings[staticLocation.getRow()][staticLocation.getCol()] == null && checkSurroundings(walls, staticLocation) && checkForEmptySpots(staticLocation);
    }

    private boolean checkSurroundings(Map<CardinalDirection, Boolean> walls, Location staticLocation) { //check if the surroundings have matching walls as the giving walls
        return Location.getSurroundingLocationsWithCD(staticLocation).entrySet().stream()
                .filter(entry -> checkNotIOBorNPE(entry.getValue())) // filter outs locations that might give IOB or an NPE, more readable if separate checked else i would put it in the noneMatch too
                .noneMatch(entry -> getBuildingStatic(entry.getValue()).getWall(getOppositeCD(entry.getKey())) != walls.get(entry.getKey())); //checks if the surrounding locations opposite wall not the same as given wall if they all the same it returns true
    }

    private boolean checkForEmptySpots(Location staticLocation) {  // checks if there isn't gonna be an empty hole when you place that building (returns true if there is no blocking)
        return Location.getSurroundingLocations(staticLocation).stream() //gets the locations around the current locations
                .filter(location -> checkNotIOB(location) && getBuildingStatic(location) == null) //removes the locations around it that are buildings -> cuz it can only block an empty location
                .map(Location::getSurroundingLocations) //for each location it gets all their surrounding locations and maps the old location to the new list of surrounding locations
                .noneMatch(locations -> locations.stream().filter(subLocation -> checkNotIOB(subLocation) && getBuildingStatic(subLocation) != null).count() == 3); //if 3 locations around the sub location are buildings then it would block that location, if it cant find any then it doesnt block(returns true then, the building about to placed would be the fourth)
    }

    private boolean checkNotIOBorNPE(Location staticLocation) { //check if the giving location wouldn't throw a NPE (NullPointerException) or an IOB (IndexOutOfBounds)
        return checkNotIOB(staticLocation) && getBuildingStatic(staticLocation) != null;
    }

    private boolean checkNotIOB(Location staticLocation) { //checks if the building isn't outside the map
        return staticLocation.getCol() < mapSize && staticLocation.getCol() >= 0 && staticLocation.getRow() < mapSize && staticLocation.getRow() >= 0;
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
    private Building getBuildingStatic(Location staticLocation) {
        return buildings[staticLocation.getRow()][staticLocation.getCol()];
    }

    public Building getBuilding(Location location) {
        return getBuildingStatic(Location.convertLocationToStaticLocation(location, mapSize));
    }


    public static CardinalDirection getOppositeCD(CardinalDirection cardinalDirection) { //get the opposite cardinal direction, N->S W->E
        switch (cardinalDirection) {
            case NORTH:
                return SOUTH;
            case WEST:
                return EAST;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            default:
                return null;
        }
    }
}
