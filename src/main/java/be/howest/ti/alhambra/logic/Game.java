package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {
    private final boolean ended;
    private final String currentPlayer;
    private final List<Player> players;
    private final Bank bank;
    private final Market market;
    @JsonIgnore
    private final List<Building> buildings;
    @JsonIgnore
    private final List<Coin> coins;


    public Game() {
        this(false, "", new ArrayList<>(), new Coin[4], new HashMap<>());
    }

    @JsonCreator
    public Game(@JsonProperty("ended") boolean ended, @JsonProperty("currentPlayer") String currentPlayer, @JsonProperty("players") List<Player> players, @JsonProperty("bank") Coin[] bank, @JsonProperty("market") Map<Currency, Building> market) {
        this.ended = ended;
        this.currentPlayer = currentPlayer;
        this.players = players;
        this.bank = new Bank(bank);
        this.market = new Market(market);
        buildings = loadFromFile();
        coins = Coin.allCoins();
        Collections.shuffle(buildings);
        Collections.shuffle(coins);
        addScoreRounds();

    }

    private List<Building> loadFromFile() {
        try (InputStream in = Game.class.getResourceAsStream("/buildings.json")) {
            return Arrays.asList(
                    Json.decodeValue(Buffer.buffer(in.readAllBytes()),
                            Building[].class)
            );
        } catch (IOException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Failed to load buildings", ex);
            return Collections.emptyList();
        }
    }

    private void addScoreRounds() {
        int firstScore = new Random().nextInt(20) + 20; // so between 20 and 40
        int secondScore = new Random().nextInt(20) + 60; // so between 60 and 80
        coins.add(firstScore, new Coin(null, 0));
        coins.add(secondScore, new Coin(null, 0));
    }


    public Game(Set<String> names) {
        this(false, "", convertNamesIntoPlayers(names), new Coin[4], new HashMap<>());
    }

    public static List<Player> convertNamesIntoPlayers(Set<String> keySet) {
        List<Player> newPlayers = new ArrayList<>();
        keySet.forEach(name -> newPlayers.add(new Player(name)));
        return newPlayers;
    }

    public boolean isEnded() {
        return ended;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Bank getBank() {
        return bank;
    }

    public Market getMarket() {
        return market;
    }

    @JsonProperty("market") //this method is solely used for JSON conversion
    public Map<Currency, Building> marketConvert() {
        return market.getMarket();
    }

    @JsonProperty("bank") //this method is solely used for JSON conversion
    public Coin[] bankConvert() {
        return bank.getBankCoins();
    }

    @Override
    public int hashCode() {
        return Objects.hash(ended, currentPlayer, players, bank, market);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return ended == game.ended &&
                Objects.equals(currentPlayer, game.currentPlayer) &&
                Objects.equals(players, game.players) &&
                Objects.equals(bank, game.bank) &&
                Objects.equals(market, game.market);
    }

    @Override
    public String toString() {
        return "Game{" +
                "ended=" + ended +
                ", CurrentPlayer='" + currentPlayer + '\'' +
                ", players=" + players +
                ", bank=" + bank +
                ", market=" + market +
                '}';
    }

    public List<Building> getBuildings() {
        return buildings;
    }
}
