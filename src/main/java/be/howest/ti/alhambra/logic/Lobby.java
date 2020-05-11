package be.howest.ti.alhambra.logic;


import be.howest.ti.alhambra.logic.exceptions.AlhambraGameRuleException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lobby {
    @JsonIgnore
    private static final int MAX_PLAYER_COUNT = 6;
    @JsonIgnore
    private static final int MIN_PLAYER_COUNT = 2;
    private final String id;
    private List<PlayerInLobby> players;
    private int playerCount;
    private int readyCount;

    public Lobby(String gameId) {
        this(gameId, new ArrayList<>());
    }

    @JsonCreator
    public Lobby(@JsonProperty("id") String id, @JsonProperty("players") List<PlayerInLobby> players) {
        this.id = id;
        this.players = players;
        updatePlayerCount();
        updateReadyCount();
    }

    private void updatePlayerCount() {
        playerCount = countPlayer();
    }

    private void updateReadyCount() {
        readyCount = countReady();
    }

    public int countPlayer() {
        return players.size();
    }

    public int countReady() {
        return (int) players.stream().filter(PlayerInLobby::isStatus).count();
    }

    public String getId() {
        return id;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getReadyCount() {
        return readyCount;
    }

    public List<PlayerInLobby> getPlayers() {
        return players;
    }

    public void addPlayer(String name) {
        if (countPlayer() < MAX_PLAYER_COUNT) {
            if (checkInLobby(name))
                throw new AlhambraGameRuleException("Name already used");
            else {
                players.add(new PlayerInLobby(name));
            }
        } else {
            throw new AlhambraGameRuleException("The lobby is full");
        }
        updatePlayerCount();
    }

    private boolean checkInLobby(String name) {
        for (PlayerInLobby p : players) {
            if (name.equals(p.getName())) {
                return true;
            }
        }
        return false;
    }

    public void removePlayer(String name) {
        PlayerInLobby player = getPlayerClass(name);
        players.remove(player);
        updatePlayerCount();
    }

    private PlayerInLobby getPlayerClass(String name) {
        for (PlayerInLobby p : players) {
            if (name.equals(p.getName())) {
                return p;
            }
        }
        throw new IllegalArgumentException("player not in lobby");
    }

    public boolean readyUpPlayer(String name) {
        getPlayerClass(name).setStatus(true);
        updateReadyCount();
        return true;
    }

    public boolean unreadyPlayer(String name) {
        getPlayerClass(name).setStatus(false);
        updateReadyCount();
        return true;
    }

    public Game startGame() {
        if (countPlayer() >= MIN_PLAYER_COUNT) {
            if (readyCount == playerCount) {
                return new Game(players);
            } else {
                throw new AlhambraGameRuleException("All players need to be ready to start the game");
            }
        } else {
            throw new AlhambraGameRuleException("You must be with 2 players to start a game");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, players);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lobby lobby = (Lobby) o;
        return Objects.equals(id, lobby.id);
    }
}
