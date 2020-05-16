package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public static List<Location> getSurroundingLocations(Location location) { //works with static locations too
        List<Location> locations = new ArrayList<>();
        locations.add(new Location(location.getRow(), location.getCol() - 1)); //left
        locations.add(new Location(location.getRow() - 1, location.getCol())); //up
        locations.add(new Location(location.getRow(), location.getCol() + 1)); //right
        locations.add(new Location(location.getRow() + 1, location.getCol())); //down
        return locations;
    }

   /* public static Map<String,> getSurroundingLocationsWithCD(Location location){ //
#todo do this
    } */

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
