package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class Player {
    private final String name;
    @JsonProperty("coins")
    private final Coins coins;
    private final Reserve reserve;
    private final City city;
    private final List<Building> buildingsInHand;

    private int score;
    private int virtualScore;


    public Player(String name) {
        this(name, new ArrayList<>(), new ArrayList<>(), City.getDefaultCity(), new ArrayList<>(), 0, 0);
    }

    @JsonCreator
    public Player(@JsonProperty("name") String name, @JsonProperty("coins") List<Coin> coins, @JsonProperty("reserve") List<Building> reserve, @JsonProperty("city") Building[][] city, @JsonProperty("buildings-in-hand") List<Building> buildingsInHand, @JsonProperty("score") int score, @JsonProperty("virtual-score") int virtualScore) {
        this.name = name;
        this.coins = new Coins(coins);
        this.reserve = new Reserve(reserve);
        this.city = new City(city);
        this.buildingsInHand = buildingsInHand;
        this.score = score;
        this.virtualScore = virtualScore;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("coins") //this method is solely used for JSON conversion
    public List<Coin> coinConvert() {
        return coins.getCoinsBag();
    }

    @JsonProperty("reserve") //this method is solely used for JSON conversion
    public List<Building> reserveConvert() {
        return reserve.getBuildings();
    }

    @JsonProperty("city") //this method is solely used for JSON conversion
    public Building[][] cityConvert() {
        return city.getBuildings();
    }

    public Coins getCoins() {
        return coins;
    }

    public Reserve getReserve() {
        return reserve;
    }

    public City getCity() {
        return city;
    }

    public int getScore(Game game, int round) {
        Map<BuildingType,Map<Player, Integer>> mostOfEachBuilding = new HashMap<>();
        mostOfEachBuilding.put(BuildingType.PAVILION, null);
        mostOfEachBuilding.put(BuildingType.SERAGLIO, null);
        mostOfEachBuilding.put(BuildingType.ARCADES, null);
        mostOfEachBuilding.put(BuildingType.CHAMBERS, null);
        mostOfEachBuilding.put(BuildingType.GARDEN, null);
        mostOfEachBuilding.put(BuildingType.TOWER, null);

        List<Player> players = game.getPlayers();

        for (Player player : players){
            Map<BuildingType, Integer> types = new HashMap<>();
            Building[][] buildings = player.getCity().getBuildings();
            for( int row = 0; row < buildings.length; row++){
                for( int col = 0; col < buildings.length; col++){
                    if(buildings[row][col] != null){
                        Building b = buildings[row][col];
                        BuildingType type = b.getType();
                        if(types.containsKey(type)){
                            types.put(type, 0);
                        }
                        else{
                            types.replace(type, types.get(type) +1);
                        }
                    }
                }
            }
            compareAmountOfBuildings(player, mostOfEachBuilding, types);
        }
        return score;
    }
    public void compareAmountOfBuildings(Player p, Map<BuildingType, Map<Player, Integer>> amounts, Map<BuildingType, Integer> types){
        for(Map.Entry<BuildingType, Integer> entry : types.entrySet()){
            Player player = amounts.entrySet().iterator().next().getValue().entrySet().iterator().next().getKey();
            if (entry.getValue() > amounts.get(entry.getKey()).get(player)){
                Map<Player, Integer> current = new HashMap<>();
                current.put(p, entry.getValue());
                amounts.replace(entry.getKey(), current);
            }
        }
    }

    public void setScore(int score) {
        this.score = score;
    }

    @JsonGetter("virtual-score")
    public int getVirtualScore() {
        return virtualScore;
    }

    public void setVirtualScore(int virtualScore) {
        this.virtualScore = virtualScore;
    }

    @JsonGetter("buildings-in-hand")
    public List<Building> getBuildingsInHand() {
        return buildingsInHand;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }
}
