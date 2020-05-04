package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class Game {
    private boolean started;
    private boolean ended;
    private String currentPlayer;
    private List<Player> players;
    private Bank bank;
    private Market market;

    public Game() {
        this(true, false, "", new ArrayList<>(), new Coin[4], new HashMap<>());
    }

    @JsonCreator
    public Game(@JsonProperty("started") boolean started, @JsonProperty("ended") boolean ended, @JsonProperty("currentPlayer") String currentPlayer, @JsonProperty("players") List<Player> players, @JsonProperty("bank") Coin[] bank, @JsonProperty("market") Map<Currency, Building> market) {
        this.started = started;
        this.ended = ended;
        this.currentPlayer = currentPlayer;
        this.players = players;
        this.bank = new Bank(bank);
        this.market = new Market(market);
    }

    public boolean isStarted() {
        return started;
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
        return Objects.hash(started, ended, currentPlayer, players, bank, market);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return started == game.started &&
                ended == game.ended &&
                Objects.equals(currentPlayer, game.currentPlayer) &&
                Objects.equals(players, game.players) &&
                Objects.equals(bank, game.bank) &&
                Objects.equals(market, game.market);
    }

    @Override
    public String toString() {
        return "Game{" +
                "started=" + started +
                ", ended=" + ended +
                ", CurrentPlayer='" + currentPlayer + '\'' +
                ", players=" + players +
                ", bank=" + bank +
                ", market=" + market +
                '}';
    }
}
