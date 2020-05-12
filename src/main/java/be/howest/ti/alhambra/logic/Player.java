package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLOutput;
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

    public int calcScore(Game game, int round) {
        Map<BuildingType,Map<Player, Integer>> mostOfEachBuilding = new HashMap<>();

        for(BuildingType type : BuildingType.values()){
            mostOfEachBuilding.put(type, new HashMap<>());
        }
        List<Player> players = game.getPlayers();

        for (Player player : players){
            Building[][] buildings = player.getCity().getBuildings();
            for (Building[] building : buildings) {
                for (int col = 0; col < buildings.length; col++) {
                    if (building[col] != null && building[col].getType() != null) {
                        Building b = building[col];
                        BuildingType type = b.getType();
                        Map<Player, Integer> typeInMap = mostOfEachBuilding.get(type);
                        if (typeInMap.containsKey(player)) {
                            typeInMap.replace(player, typeInMap.get(player) + 1);
                        } else{
                            typeInMap.put(player, 1);
                        }
                    }
                }
            }
        }
        return giveScore(this, mostOfEachBuilding, round);
    }
    public int giveScore(Player p, Map<BuildingType, Map<Player, Integer>> mostOfEachBuilding, int round){
        if (round == 1){
            for(Map.Entry<BuildingType, Map<Player, Integer>> entry : mostOfEachBuilding.entrySet()){
                int value = 0;
                Player most = null;
                for(Map.Entry<Player, Integer> playerEntry : entry.getValue().entrySet()){
                    if(playerEntry.getValue() > value){
                        value = playerEntry.getValue();
                        most = playerEntry.getKey();
                    }
                }
                switch (entry.getKey()){
                    case PAVILION:
                        if(most == p){
                            p.setScore(p.getScore() + 1);
                        }
                        break;
                    case SERAGLIO:
                        if(most == p){
                            p.setScore(p.getScore() + 2);
                        }
                        break;
                    case ARCADES:
                        if(most == p){
                            p.setScore(p.getScore() + 3);
                        }
                        break;
                    case CHAMBERS:
                        if(most == p){
                            p.setScore(p.getScore() + 4);
                        }
                        break;
                    case GARDEN:
                        if(most == p){
                            p.setScore(p.getScore() + 5);
                        }
                        break;
                    case TOWER:
                        if(most == p){
                            p.setScore(p.getScore() + 6);
                        }
                        break;
                }
            }
        }
        else if (round == 2){
            for(Map.Entry<BuildingType, Map<Player, Integer>> entry : mostOfEachBuilding.entrySet()){
                int value = 0;
                Player most = null;
                Player sec = null;
                for(Map.Entry<Player, Integer> playerEntry : entry.getValue().entrySet()){
                    if(playerEntry.getValue() > value){
                        sec = most;
                        most = playerEntry.getKey();
                    }
                }
                switch (entry.getKey()){
                    case PAVILION:
                        if(most == p){
                            p.setScore(p.getScore() + 8);
                        }
                        else if(sec == p){
                            p.setScore(p.getScore() + 1);
                        }
                        break;
                    case SERAGLIO:
                        if(most == p){
                            p.setScore(p.getScore() + 9);
                        }
                        else if(sec == p){
                            p.setScore(p.getScore() + 2);
                        }
                        break;
                    case ARCADES:
                        if(most == p){
                            p.setScore(p.getScore() + 10);
                        }
                        else if(sec == p){
                            p.setScore(p.getScore() + 3);
                        }
                        break;
                    case CHAMBERS:
                        if(most == p){
                            p.setScore(p.getScore() + 11);
                        }
                        else if(sec == p){
                            p.setScore(p.getScore() + 4);
                        }
                        break;
                    case GARDEN:
                        if(most == p){
                            p.setScore(p.getScore() + 12);
                        }
                        else if(sec == p){
                            p.setScore(p.getScore() + 5);
                        }
                        break;
                    case TOWER:
                        if(most == p){
                            p.setScore(p.getScore() + 13);
                        }
                        else if(sec == p){
                            p.setScore(p.getScore() + 6);
                        }
                        break;
                }
            }
        }
        else if (round == 3){
            for(Map.Entry<BuildingType, Map<Player, Integer>> entry : mostOfEachBuilding.entrySet()){
                int value = 0;
                Player most = null;
                Player sec = null;
                Player third = null;
                for(Map.Entry<Player, Integer> playerEntry : entry.getValue().entrySet()){
                    if(playerEntry.getValue() >= value){
                        third = sec;
                        sec = most;
                        most = playerEntry.getKey();
                    }
                }
                switch (entry.getKey()){
                    case PAVILION:
                        if(most == p){
                            p.setScore(p.getScore() + 16);
                        }
                        else if(sec == p){
                            p.setScore(p.getScore() + 8);
                        }
                        else if(third == p){
                            p.setScore(p.getScore() + 1);
                        }
                        break;
                    case SERAGLIO:
                        if(most == p){
                            p.setScore(p.getScore() + 17);
                        }
                        else if(sec == p){
                            p.setScore(p.getScore() + 9);
                        }
                        else if(third == p){
                            p.setScore(p.getScore() + 2);
                        }
                        break;
                    case ARCADES:
                        if(most == p){
                            p.setScore(p.getScore() + 18);
                        }
                        else if(sec == p){
                            p.setScore(p.getScore() + 10);
                        }
                        else if(third == p){
                            p.setScore(p.getScore() + 3);
                        }
                        break;
                    case CHAMBERS:
                        if(most == p){
                            p.setScore(p.getScore() + 19);
                        }
                        else if(sec == p){
                            p.setScore(p.getScore() + 11);
                        }
                        else if(third == p){
                            p.setScore(p.getScore() + 4);
                        }
                        break;
                    case GARDEN:
                        if(most == p){
                            p.setScore(p.getScore() + 20);
                        }
                        else if(sec == p){
                            p.setScore(p.getScore() + 12);
                        }
                        else if(third == p){
                            p.setScore(p.getScore() + 5);
                        }
                        break;
                    case TOWER:
                        if(most == p){
                            p.setScore(p.getScore() + 21);
                        }
                        else if(sec == p){
                            p.setScore(p.getScore() + 13);
                        }
                        else if(third == p){
                            p.setScore(p.getScore() + 6);
                        }
                        break;
                }
            }
        }
        return score;
    }

    public int getScore() {
        return score;
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

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }
}
