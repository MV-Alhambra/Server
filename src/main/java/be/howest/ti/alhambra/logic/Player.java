package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class Player {
    private final String name;
    @JsonProperty("coins")
    private final Coins coins;
    private final Reserve reserve;
    @JsonIgnore //temp since city doesnt exist yet
    private final City city;
    private final int score;
    private final int virtualScore;
    private final List<Building> buildingsInHand;
    @JsonIgnore
    private final String playerToken;


    public Player(String name) {
        this(name, new ArrayList<>(), new ArrayList<>(),/* new City(),*/ new ArrayList<>(), 0, 0);
    }

    @JsonCreator
    public Player(@JsonProperty("name") String name, @JsonProperty("coins") List<Coin> coins, @JsonProperty("reserve") List<Building> reserve,/* @JsonProperty("city") City city, */@JsonProperty("buildings-in-hand") List<Building> buildingsInHand, @JsonProperty("score") int score, @JsonProperty("virtual-score") int virtualScore) {
        this.name = name;
        this.coins = new Coins(coins);
        this.reserve = new Reserve(reserve);
        /*this.city = new City(city); */
        this.city = new City();//temp
        this.buildingsInHand = buildingsInHand;
        this.score = score;
        this.virtualScore = virtualScore;
        this.playerToken = "";//later generate this through generatePlayerToken -> see issue
    }

    public String getName() {
        return name;
    }

    @JsonProperty("coins") //this method is solely used for JSON conversion
    public List<Coin> coinConvert(){
        return coins.getCoinsBag();
    }

    @JsonProperty("reserve") //this method is solely used for JSON conversion
    public List<Building> reserveConvert(){
        return reserve.getBuildings();
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

    public int getScore() {
        return score;
    }

    public String getPlayerToken() {
        return playerToken;
    }

    @JsonGetter("virtual-score")
    public int getVirtualScore() {
        return virtualScore;
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
