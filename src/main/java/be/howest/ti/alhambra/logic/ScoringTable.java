package be.howest.ti.alhambra.logic;

import be.howest.ti.alhambra.logic.exceptions.AlhambraIllegalArgumentException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

//Utility class to calc the score, everything the do with score should be put here
public class ScoringTable {

    private ScoringTable() {
    } //bc you aren't allowed to make instances of utility classes

    public static void main(String[] args) {
        System.out.println(ScoringTable.getRoundTable(2));
    }

    public static Map<BuildingType, List<Integer>> getRoundTable(int round) {
        if (1 > round || round > 3) throw new AlhambraIllegalArgumentException("only rounds between 1 and 3 allowed"); //defense programming

        Map<BuildingType, List<Integer>> roundTable = new HashMap<>();
        //Dynamically calculate the round table, it's round + increasing by 1 for each BuildingType
        for (BuildingType type : BuildingType.values()) { // set the initial values
            roundTable.put(type, new ArrayList<>());
        }
        AtomicInteger index = new AtomicInteger();
        for (int i = 1; i <= round; i++) { //fill them
            int finalI = i;
            if (i == 3) index.getAndIncrement();
            roundTable.forEach((key, value) -> value.add(0, index.getAndIncrement() + finalI));
        }
        return roundTable;
    }



}
