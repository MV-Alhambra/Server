package be.howest.ti.alhambra.logic;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Lobby {
    private boolean started;
    private final String gameId;
    private Map<String, Boolean> playersReady;

    @JsonCreator
    public Lobby(
           @JsonProperty("gameId") String gameId) {
        this.started = false;
        this.gameId = gameId;
        this.playersReady = new LinkedHashMap<>();
    }

    public boolean isStarted() {
        return started;
    }

    public String getGameId() {
        return gameId;
    }

    public Map<String, Boolean> getPlayersReady() {
        return playersReady;
    }

    public void addPlayer(String name){
        if (playerCount() < 6) {
            if (playersReady.containsKey(name)) {
                throw new IllegalArgumentException("Name already used");
            } else {
                playersReady.put(name, false);
            }
        }
        else {
            throw new IllegalStateException("The lobby is full");
        }
    }

    public int playerCount() {
        return playersReady.size();
    }

    public void readyUpPlayer(String name){
        playersReady.replace(name, true);
    }

    public void unreadyPlayer(String name){
        playersReady.replace(name, false);
    }

    public int amountReady(){
        int amountReady = 0;
        for (boolean player : playersReady.values()) {
            if (player){
                amountReady ++;
            }
        }
        return amountReady;
    }

    public void startGame(){
        if (playerCount() > 2){
            if (amountReady() == playerCount()){
                this.started = true;
            }
            else {
                throw new IllegalStateException("All players need to be ready to start game");
            }
        }
        else {
            throw new IllegalStateException("You must be with 2 players to start a game");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lobby lobby = (Lobby) o;
        return started == lobby.started &&
                Objects.equals(gameId, lobby.gameId) &&
                Objects.equals(playersReady, lobby.playersReady);
    }

    @Override
    public int hashCode() {
        return Objects.hash(started, gameId, playersReady);
    }
}
