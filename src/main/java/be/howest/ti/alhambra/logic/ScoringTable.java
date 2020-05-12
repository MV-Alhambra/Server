package be.howest.ti.alhambra.logic;

import java.util.List;
import java.util.Map;

//Utility class to calc the score, everything the do with score should be put here
public class ScoringTable {

    private ScoringTable() {
    } //bc you aren't allowed to make instances of utility classes

    public static Map<BuildingType, List<Integer>> getRoundTable(int round) {
        if (1 > round || round > 3) throw new IllegalArgumentException("only rounds between 1 and 3 allowed"); //defense programming

        //Dynamically calculate the round table, it's round + increasing by 1 for each BuildingType
        for (BuildingType type: BuildingType.values()){
            System.out.println(type);
        }

        return null;
    }

    public static void main(String[] args) {
        ScoringTable.getRoundTable(1);
    }

}
