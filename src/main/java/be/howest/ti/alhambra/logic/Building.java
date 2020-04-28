package be.howest.ti.alhambra.logic;

import java.util.*;

public class Building {
    private final BuildingType type;
    private final int cost;
    private final Map<WindDirection, Boolean> walls;


    Random random = new Random(); //so you can reuse it and need to make a new object each time


    public Building(){
        this.type = randomType();
        this.cost = randomCost();
        this.walls = generateWalls();
    }

    private BuildingType randomType() {
        List<BuildingType> allTypes = Arrays.asList(BuildingType.values());
        int randomBuildingIndex = random.nextInt(allTypes.size());
        return allTypes.get(randomBuildingIndex);
    }

    private int randomCost(){
        return  random.nextInt(9) + 1; // nextInt is from 0 to bound, the +1 makes it goes from 1 incl to 10 excl
    }

    private Map<WindDirection, Boolean> generateWalls(){

        Map<WindDirection, Boolean> wallsGenerate = new HashMap<>();
        int currentWalls = 0;

        for (WindDirection direction  : WindDirection.values()) {  //this makes it so there are no more then 3 walls
            boolean wall = random.nextBoolean();
            if (wall && (currentWalls < 3)){
                currentWalls ++;
                wallsGenerate.put(direction, true);
            }
            else {
                wallsGenerate.put(direction, false);
            }

        }
        return wallsGenerate;
    }

    public BuildingType getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public Map<WindDirection, Boolean> getWalls() {
        return walls;
    }
}
