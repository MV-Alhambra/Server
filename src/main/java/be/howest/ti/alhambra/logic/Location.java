package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class Location {
    private final int row;
    private final int col;


    @JsonCreator
    public Location(@JsonProperty("row") int row, @JsonProperty("col") int col) {
        this.row = row;
        this.col = col;
    }

    public static Location convertLocationToStaticLocation(Location location, int mapSize) { //turns the dynamic location ( location based around fountain ) into static location (based on top left)
        int mapRadius = (mapSize - 1) / 2;
        return new Location(location.getRow() + mapRadius, location.getCol() + mapRadius);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public static Location convertStaticLocationToLocation(Location location, int mapSize) { //turns the static location ( based on top left ) into dynamic location (based on fountain position)
        int mapRadius = (mapSize - 1) / 2;
        return new Location(location.getRow() - mapRadius, location.getCol() - mapRadius);
    }

    public static List<Location> getSurroundingLocations(Location location) { //works with static locations too, gets the locations directly next to the given location
        return new ArrayList<>(getSurroundingLocationsWithCD(location).values());
    }

    public static Map<CardinalDirection, Location> getSurroundingLocationsWithCD(Location location) { //works with static locations too, gets the locations directly next to the given location and binds it to a CD
        Map<CardinalDirection, Location> locations = new LinkedHashMap<>();
        locations.put(CardinalDirection.NORTH, new Location(location.getRow() - 1, location.getCol())); //up
        locations.put(CardinalDirection.EAST, new Location(location.getRow(), location.getCol() + 1)); //right
        locations.put(CardinalDirection.SOUTH, new Location(location.getRow() + 1, location.getCol())); //down
        locations.put(CardinalDirection.WEST, new Location(location.getRow(), location.getCol() - 1)); //left
        return locations;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return col == location.col && row == location.row;
    }

    @Override
    public String toString() {
        return String.format("(%d;%d)", row, col);
    }
}
