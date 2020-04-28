package be.howest.ti.alhambra.logic;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Building {
    private final String type;
    private final int cost;
    private final Map<String, Boolean> walls;

    public Building(){
        this.type = "iets";
        this.cost = randomCost();
        this.walls = generateWalls();
    }

    private int randomCost(){
        return 0;
    }

    private Map<String, Boolean> generateWalls(){
        Random random = new Random();
        Map<String, Boolean> wallsGenerate = new HashMap<>();
        wallsGenerate.put("north", random.nextBoolean());
        wallsGenerate.put("east", random.nextBoolean());
        wallsGenerate.put("south", random.nextBoolean());
        wallsGenerate.put("west", random.nextBoolean());

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
