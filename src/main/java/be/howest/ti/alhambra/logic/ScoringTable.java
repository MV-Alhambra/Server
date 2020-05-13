package be.howest.ti.alhambra.logic;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toMap;

//Utility class to calc the score, everything the do with score should be put here
public class ScoringTable {

    private ScoringTable() {
    } //bc you aren't allowed to make instances of utility classes

    public static Map<Player, Integer> calcScoreBuildings(List<Player> players, int round) {

        Map<BuildingType, Map<Player, Integer>> totalTypeEachPlayer = new LinkedHashMap<>();    //map with for each type another map with how key player and then how many they have  of that type, linked bc order matters

        for (BuildingType type : BuildingType.values()) {
            totalTypeEachPlayer.put(type, new LinkedHashMap<>()); //order matters
            for (Player player : players) { //fill the map with players and count for each type
                totalTypeEachPlayer.get(type).put(player, player.getCity().countType(type));
            }
            totalTypeEachPlayer.put(type, totalTypeEachPlayer.get(type).entrySet().stream() //sort players by count of each type
                    .filter(k -> k.getValue() > 0)
                    .sorted(Map.Entry.comparingByValue((i1, i2) -> i2 - i1)) //sort them by custom comparator cuz i wanted it reversed, that code in the parameter is a comparator
                    .limit(round) //only hold amount of players equal to scores available
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)) //back to map
            );
        }

        Map<Player, Integer> scores = new HashMap<>();

        for (Player player : players) {
            scores.put(player, 0);
        }
        Map<BuildingType, List<Integer>> roundTable = getRoundTable(round);

        for (Map.Entry<BuildingType, Map<Player, Integer>> type : totalTypeEachPlayer.entrySet()) {
            List<Player> keysOfType = new ArrayList<>(type.getValue().keySet());
            for (int i = 0; i < round; i++) {
                if (keysOfType.size() != i) { //prevent NPE
                    Player player = keysOfType.get(i);
                    scores.put(player, scores.get(player) + roundTable.get(type.getKey()).get(i)); //gets old score and adds new score
                }
            }
        }
        return scores;
    }

    public static Map<BuildingType, List<Integer>> getRoundTable(int round) { //Dynamically calculate the round table, it's round + increasing by 1 for each BuildingType and an extra 1 for last round

        Map<BuildingType, List<Integer>> roundTable = new LinkedHashMap<>(); //order matters, fill them expects them to be in the correct order
        Arrays.stream(BuildingType.values()).forEach(type -> roundTable.put(type, new ArrayList<>())); // set the initial values

        AtomicInteger index = new AtomicInteger();
        for (int i = 1; i <= round; i++) { //fill them
            int finalI = i;
            if (i == 3) index.getAndIncrement();
            roundTable.forEach((key, value) -> value.add(0, index.getAndIncrement() + finalI)); //add round and index
        }
        return roundTable;
    }
}
