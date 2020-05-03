package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.*;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        System.out.println("@@@ Comparing inside equals of city @@@");
        System.out.println("mapSize is equal: "+ (mapSize == city.mapSize) + " buildings is equal: " + Arrays.equals(buildings, city.buildings));
        System.out.println("buildings: " +Arrays.deepToString(buildings));
        System.out.println("Compared to other buildings: " + Arrays.deepToString(city.buildings));
        for(int i =0;i<buildings.length;i++){
            System.out.println("Building[] is equal "+Arrays.equals(buildings[i], city.buildings[i]));
            for (int j = 0;j<buildings[i].length;j++){
                System.out.println("Cell is equal " + ( buildings[i][j] == null||buildings[i][j].equals(city.buildings[i][j])) + " Cell1: "+ buildings[i][j] + " Cell2: " + city.buildings[i][j] );
            }
        }
        System.out.println("@@@ The end of Comparing inside equals of city @@@");
        return mapSize == city.mapSize &&
                Arrays.equals(buildings, city.buildings);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mapSize);
        result = 31 * result + Arrays.hashCode(buildings);
        return result;
    }

    @Override
    public String toString() {
        return "City{" +
                "buildings=" + Arrays.deepToString(buildings) +
                ", mapSize=" + mapSize +
                '}';
    }
}
