package be.howest.ti.alhambra.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Building {
    private final String type;
    private final int cost;
    private final Map<String, Boolean> walls;


    Random random = new Random(); //so you can reuse it and need to make a new object each time


    public Building(){
        this.type = "iets";
        this.cost = randomCost();
        this.walls = generateWalls();
    }

    private int randomCost(){
        return  random.nextInt(9) + 1; // nextInt is from 0 to bound, the +1 makes it goes from 1 incl to 10 excl
    }

    private Map<String, Boolean> generateWalls(){

        Map<String, Boolean> wallsGenerate = new HashMap<>();
        int currentWalls = 0;

        for (WindDirection direction  : WindDirection.values()) {  //this makes it so there are no more then 3 walls
            boolean wall = random.nextBoolean();
            if (wall && (currentWalls < 3)){
                currentWalls ++;
                wallsGenerate.put(direction.toString(), true);
            }
            else {
                wallsGenerate.put(direction.toString(), false);
            }

        }
        return wallsGenerate;
    }

    public String getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public Map<String, Boolean> getWalls() {
        return walls;
    }
}
