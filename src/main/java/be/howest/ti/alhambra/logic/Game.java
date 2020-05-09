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
    private final List<Player> players;
    private final Bank bank;
    private final Market market;
    @JsonIgnore
    private final List<Building> buildings;
    @JsonIgnore
    private final List<Coin> coins;
    private String currentPlayer;
    @JsonIgnore
    private int index;

    public Game(Set<String> names) {
        this(false, "", convertNamesIntoPlayers(names), new Coin[4], new HashMap<>());
    }

    @JsonCreator
    public Game(@JsonProperty("ended") boolean ended, @JsonProperty("currentPlayer") String currentPlayer, @JsonProperty("players") List<Player> players, @JsonProperty("bank") Coin[] bank, @JsonProperty("market") Map<Currency, Building> market) {
        this.ended = ended;
        this.currentPlayer = currentPlayer;
        this.players = players;
        this.bank = new Bank(bank);
        this.market = new Market(market);
    }

    public Game(List<PlayerInLobby> names) {
        this(false, "", convertNamesIntoPlayers(names), new Coin[4], new HashMap<>());
        index = 0;
        nextPlayer();
        buildings = new ArrayList<>(loadFromFile()); //loadFromFile returns a fixed size list
        coins = Coin.allCoins();
        Collections.shuffle(buildings);
        Collections.shuffle(coins);
        addScoreRounds();//must before all other methods that might remove Coins
        givePlayersStarterCoins();
        this.market.fillMarkets(this);
        this.bank.fillBank(this);
    }

    public static List<Player> convertNamesIntoPlayers(List<PlayerInLobby> allPlayers) {
        List<Player> newPlayers = new ArrayList<>();
        allPlayers.forEach(player-> newPlayers.add(new Player( player.getName())));
        keySet.forEach(name -> newPlayers.add(new Player(name)));
        return newPlayers;
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

    public void scoreRound() {
        //for a different issue
        //but basically here should every player his score be updated its get calculated in city
        players.forEach(player -> player.setScore(player.getScore() + 1));//temp replaced with above
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

    public Coin removeCoin() { //removes an coin and returns it
        try {
            Coin coin = coins.remove(0);
            if (coin.getAmount() == 0) { //calls an scoreRound
                scoreRound();
                return removeCoin();
            }
            return coin;
        } catch (IndexOutOfBoundsException e) {
            // end game bc game is over, used up all the coins
            // game also ends when buildings are up
            // that is for another issue
            // might also keep going and only stop game when buildings are gone
            // this shouldn't throw an error since bank.fillBank works with nulls
            return null;
        }
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

    public Building removeBuilding() {
        try {
            return buildings.remove(0);
        } catch (IndexOutOfBoundsException e) {
            // end game bc game is over, used up all the buildings
            // game might also end when coins are up, depends on implementation
            // that is for another issue
            // this shouldn't throw an error since market.fillMarket works with nulls
            return null;
        }
    }

    private void givePlayersStarterCoins() {
        players.forEach(player -> {
            int sum = 0;
            List<Coin> bag = new ArrayList<>();
            while (sum < 20) {
                Coin coin = removeCoin();
                sum += coin.getAmount();
                bag.add(coin);
            }
            player.getCoins().addCoins(bag.toArray(Coin[]::new));
        });
    }

    private void nextPlayer() {
        currentPlayer = players.get(index).getName();
        if (++index >= players.size()) index = 0;
    }
}
