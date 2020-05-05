package be.howest.ti.alhambra.logic;


import be.howest.ti.alhambra.logic.exceptions.AlhambraEntityNotFoundException;
import be.howest.ti.alhambra.logic.exceptions.AlhambraGameRuleException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Lobby {
    @JsonIgnore
    private static final int MAX_PLAYER_COUNT = 6;
    @JsonIgnore
    private static final int MIN_PLAYER_COUNT = 2;
    private final String id;
    private Map<String, Boolean> playersReady;
    private int playerCount;
    private int readyCount;

    public Lobby(String gameId) {
        this(gameId, new HashMap<>());
    }

    @JsonCreator
    public Lobby(@JsonProperty("id") String id, @JsonProperty("players") Map<String, Boolean> playersReady) {
        this.id = id;
        this.playersReady = playersReady;
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
        return playersReady.size();
    }

    public int countReady() {
        return (int) playersReady.values().stream().filter(status -> status).count();
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

    @JsonGetter("players")
    public Map<String, Boolean> getPlayersReady() {
        return playersReady;
    }

    public void addPlayer(String name) {
        if (countPlayer() < MAX_PLAYER_COUNT) {
            if (playersReady.containsKey(name)) {
                throw new AlhambraGameRuleException("Name already used");
            } else {
                playersReady.put(name, false);
            }
        } else {
            throw new AlhambraGameRuleException("The lobby is full");
        }
        updatePlayerCount();
    }

    public void removePlayer(String name) {
        if (!playersReady.containsKey(name)) throw new AlhambraEntityNotFoundException("Couldn't find that player: " + name);
        playersReady.remove(name);
        updatePlayerCount();
    }

    public boolean readyUpPlayer(String name) {
        if (playersReady.replace(name, true) == null) throw new AlhambraEntityNotFoundException("Couldn't find that player: " + name);
        updateReadyCount();
        return true;
    }

    public boolean unreadyPlayer(String name) {
        if (playersReady.replace(name, false) == null) throw new AlhambraEntityNotFoundException("Couldn't find that player: " + name);
        updateReadyCount();
        return true;
    }

    public void startGame() {
        if (countPlayer() > MIN_PLAYER_COUNT) {
            if (readyCount == playerCount) {
                //#todo
            } else {
                throw new AlhambraGameRuleException("All players need to be ready to start game");
            }
        } else {
            throw new AlhambraGameRuleException("You must be with 2 players to start a game");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playersReady);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lobby lobby = (Lobby) o;
        return Objects.equals(id, lobby.id) &&
                Objects.equals(playersReady, lobby.playersReady);
    }
}
