package be.howest.ti.alhambra.logic;

import be.howest.ti.alhambra.logic.exceptions.AlhambraEntityNotFoundException;
import be.howest.ti.alhambra.logic.exceptions.AlhambraGameRuleException;
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
    private final List<Player> players;
    private final Bank bank;
    private final Market market;
    @JsonIgnore
    private final List<Building> buildings;
    @JsonIgnore
    private final List<Coin> coins;
    private boolean ended;
    private String currentPlayer;
    @JsonIgnore
    private int index;
    @JsonIgnore
    private int round;

    public Game(List<PlayerInLobby> names) {
        this(false, "", convertNamesIntoPlayers(names), new Coin[4], new HashMap<>());
    }

    @JsonCreator
    public Game(@JsonProperty("ended") boolean ended, @JsonProperty("currentPlayer") String currentPlayer, @JsonProperty("players") List<Player> players, @JsonProperty("bank") Coin[] bank, @JsonProperty("market") Map<Currency, Building> market) {
        this.ended = ended;
        this.currentPlayer = currentPlayer;
        this.players = players;
        this.bank = new Bank(bank);
        this.market = new Market(market);
        index = 0;
        round = 1;
        buildings = new ArrayList<>(loadFromFile()); //loadFromFile returns a fixed size list
        coins = Coin.allCoins();
        Collections.shuffle(buildings);
        Collections.shuffle(coins);
        addScoreRounds();//must before all other methods that might remove Coins
        givePlayersStarterCoins();
        nextPlayer();
    }

    public static List<Player> convertNamesIntoPlayers(List<PlayerInLobby> allPlayers) {
        List<Player> newPlayers = new ArrayList<>();
        allPlayers.forEach(player -> newPlayers.add(new Player(player.getName()).setToken(player.getToken())));
        return newPlayers;
    }

    public void removePlayer(String name) {
        index = players.size() - 1 == index ? 0 : index; // reset index to prevent IOB when nextPlayer is called
        players.remove(findPlayer(name));
        if (currentPlayer.equals(name)) nextPlayer(); // cant have a person that left as current Player
        if (players.size() == 2) /*activate two Player system, so add dirk*/ ;
        else if (players.size() == 1) endGame();
    }

    public Player findPlayer(String name) {
        return players.stream().filter(player -> player.getName().equals(name)).findFirst().orElseThrow(() -> new AlhambraEntityNotFoundException("Couldn't find that player: " + name));
    }

    private void nextPlayer() { // when called it sets the next current Player
        ScoringTable.calcScoreBuildings(players, round++).forEach(Player::setVirtualScore); // set the virtual score
        bank.fillBank(this);
        market.fillMarkets(this);
        currentPlayer = players.get(index).getName(); // gets the name of the currentPlayer
        if (++index >= players.size()) index = 0; // add one to the index and set it to zero when max is reached
    }

    private void endGame() { //end the game
        scoreRound(); // last score round
        ended = true;
    }

    public void scoreRound() { //each time called it does an score round
        ScoringTable.calcScoreBuildings(players, round++).forEach((player, score) -> player.setScore(player.getScore() + score)); //adds the new Score to the old score
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
            // game ends when no buildings are left
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
            endGame();
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

    public Game takeCoins(String playerName, Coin[] coins) {
        checkTurn(playerName);
        try {
            bank.removeCoins(coins, true);
            bank.removeCoins(coins); //now i actually remove them
            findPlayer(playerName).getCoins().addCoins(coins); // now add them to the player
            nextPlayer();
        } catch (IllegalArgumentException exception) {
            throw new AlhambraEntityNotFoundException("Couldn't find those coins: " + Arrays.toString(coins));
        }

        return this;
    }

    private void checkTurn(String playerName) {
        if (!currentPlayer.equals(playerName)) throw new AlhambraGameRuleException("It's not your turn");
    }

    /* Checks if the turn of this person, all coins are same currency,the sum of coins is enough
     * and then either changes the turn or not depending on same cost as sum
     * Then moves that building from market to buildingsInHand and removes the coins from the player */
    public Game buyBuilding(String playerName, Currency currency, Coin[] coins) {
        checkTurn(playerName);
        int sum = Coin.getSumCoins(coins);
        int cost = market.getBuilding(currency).getCost();
        Player player = findPlayer(playerName);

        if (!player.getCoins().containsCoins(coins)) throw new AlhambraGameRuleException("Player doesn't own those coins");
        else if (!Coin.coinsSameCurrency(coins)) throw new AlhambraGameRuleException("Coins must have the same currency");
        else if (sum < cost) throw new AlhambraGameRuleException("Not enough coins were given");
        else {
            player.getBuildingsInHand().add(market.removeBuilding(currency)); //remove and add it to the hand
            player.getCoins().removeCoins(coins);
            if (sum != cost) nextPlayer();
        }
        return this;
    }

    public Game build(String playerName, Building building, Location location) {
        Player player = findPlayer(playerName);

        if (!player.getBuildingsInHand().remove(building)) throw new AlhambraEntityNotFoundException("Couldn't find that building in the hand");
        if (location == null) {
            player.getReserve().addBuilding(building);
        } else {
            player.getCity().placeBuilding(building, location);
        }
        return this;
    }

    /* check if its the player turn and check if the player has that building in reserve
     *
     * */
    public Game redesign(String playerName, Building building, Location location) {
        checkTurn(playerName);
        Player player = findPlayer(playerName);
        if (building != null && !player.getReserve().contains(building)) throw new AlhambraEntityNotFoundException("Couldn't that find building in reserve");

        else if (building == null && location != null) { //city to reserve
            cityToReserve(player, location);
        } else if (location != null) { //reserve to city or swap if there is a building on the location
            Building oldBuilding = player.getCity().getBuilding(location);
            if (oldBuilding != null) cityToReserve(player, location);
            try { //try and swap the building
                player.getCity().placeBuilding(building, location);
            } catch (AlhambraGameRuleException e) { //put the building back in the city that was removed
                player.getCity().placeBuilding(oldBuilding, location);
                throw e;
            }
            player.getReserve().removeBuilding(building);
        } else {
            throw new AlhambraGameRuleException("Incorrect usage of redesign api");
        }
        nextPlayer();
        return this;
    }

    private void cityToReserve(Player player, Location location) {
        Building relocateBuilding = player.getCity().getBuilding(location);

        if (relocateBuilding != null && relocateBuilding.getType() != null) { //check if not fountain or empty
            player.getReserve().addBuilding(player.getCity().removeBuilding(location));
        } else {
            throw new AlhambraEntityNotFoundException("Wrong location given for city to reserve " + location);
        }
    }
}
