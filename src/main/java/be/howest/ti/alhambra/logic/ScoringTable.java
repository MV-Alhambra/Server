package be.howest.ti.alhambra.logic;

import be.howest.ti.alhambra.logic.exceptions.AlhambraIllegalArgumentException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toMap;

//Utility class to calc the score, everything the do with score should be put here
public class ScoringTable {

    private ScoringTable() {
    } //bc you aren't allowed to make instances of utility classes

    public static void main(String[] args) {
        List<PlayerInLobby> names = new ArrayList<>();
        PlayerInLobby p1 = new PlayerInLobby("P1", true);
        PlayerInLobby p2 = new PlayerInLobby("P2", true);
        PlayerInLobby p3 = new PlayerInLobby("P3", true);

        names.add(p1);
        names.add(p2);
        names.add(p3);
        Game g = new Game(names);

        Building b1 = new Building(BuildingType.PAVILION, 5);
        Building b2 = new Building(BuildingType.CHAMBERS, 9);
        Building b3 = new Building(BuildingType.CHAMBERS, 4);
        Building b4 = new Building(BuildingType.TOWER, 8);
        Building b5 = new Building(BuildingType.SERAGLIO, 8);
        Building b6 = new Building(BuildingType.GARDEN, 8);
        Building b7 = new Building(BuildingType.CHAMBERS, 5);

        Player pl1 = g.getPlayers().get(0);
        Player pl2 = g.getPlayers().get(1);
        Player pl3 = g.getPlayers().get(2);

        pl1.getCity().placeBuilding(b1, new Location(1, 0));
        pl1.getCity().placeBuilding(b2, new Location(0, 1));
        pl1.getCity().placeBuilding(b3, new Location(1, 1));

        pl2.getCity().placeBuilding(b4, new Location(1, 0));
        pl2.getCity().placeBuilding(b5, new Location(0, 1));

        pl3.getCity().placeBuilding(b6, new Location(1, 0));
        pl3.getCity().placeBuilding(b7, new Location(0, 1));

        getScoreBuildings(g.getPlayers(), 2);
    }

    public static Map<Player, Integer> getScoreBuildings(List<Player> players, int round) {
        //map with for each total amount

        Map<BuildingType, Map<Player, Integer>> totalTypeEachPlayer = new HashMap<>();

        for (BuildingType type : BuildingType.values()) {
            totalTypeEachPlayer.put(type, new LinkedHashMap<>()); //order matters
            for (Player player : players) { //fill the map with players and count for each type
                totalTypeEachPlayer.get(type).put(player, player.getCity().countType(type));
            }
            totalTypeEachPlayer.put(type, totalTypeEachPlayer.get(type).entrySet().stream() //sort players by count of each type
                    .sorted(Map.Entry.comparingByValue((i1, i2) -> i2 - i1)) //sort them by custom comparator cuz i wanted it reversed, that code in the parameter is a comparator
                    .limit(round) //only hold amount of players equal to scores available
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)) //back to map
            );
        }

        Map<Player, Integer> scores = new HashMap<>();


        totalTypeEachPlayer.forEach((k, v) -> System.out.println(k + " " + v));


        return null;
    }

    public static Map<BuildingType, List<Integer>> getRoundTable(int round) {
        if (1 > round || round > 3) throw new AlhambraIllegalArgumentException("only rounds between 1 and 3 allowed"); //defensive programming

        Map<BuildingType, List<Integer>> roundTable = new HashMap<>();
        //Dynamically calculate the round table, it's round + increasing by 1 for each BuildingType and an extra 1 for last round
        Arrays.stream(BuildingType.values()).forEach(type -> roundTable.put(type, new ArrayList<>())); // set the initial values

        AtomicInteger index = new AtomicInteger();
        for (int i = 1; i <= round; i++) { //fill them
            int finalI = i;
            if (i == 3) index.getAndIncrement();
            roundTable.forEach((key, value) -> value.add(0, index.getAndIncrement() + finalI));
        }
        return roundTable;
    }


}
