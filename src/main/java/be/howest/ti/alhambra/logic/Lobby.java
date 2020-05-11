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
    private static final int MIN_PLAYER_COUNT = 2;
    private final int maxNumberOfPlayers;
    private final String id;
    private final String customNameLobby;
    private List<PlayerInLobby> players;
    private int playerCount;
    private int readyCount;

    public Lobby(String gameId, String customNameLobby, int maxNumberOfPlayers) {
        this(gameId, new ArrayList<>(), customNameLobby, maxNumberOfPlayers);
    }

    @JsonCreator
    public Lobby(@JsonProperty("id") String id, @JsonProperty("players") List<PlayerInLobby> playersReady, @JsonProperty("customNameLobby") String customNameLobby, @JsonProperty("maxNumberOfPlayers") int maxNumberOfPlayers) {
        this.id = id;
        this.players = playersReady;
        this.customNameLobby = customNameLobby;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
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

    public int countReady() { // counts how many players are ready
        return (int) players.stream().filter(PlayerInLobby::isStatus).count();
    }


    public Lobby(String gameId, String customNameLobby) {
        this(gameId, new ArrayList<>(), customNameLobby, 6);
    }

    public String getId() {
        return id;
    }

    public String getCustomNameLobby() {
        return customNameLobby;
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

    public int getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }


    public void addPlayer(String name) {
        if (countPlayer() < maxNumberOfPlayers) {
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
        return players.stream().anyMatch(player -> player.getName().equals(name));
    }

    public void removePlayer(String name) {
        players.remove(getPlayerClass(name));
        updatePlayerCount();
    }

    private PlayerInLobby getPlayerClass(String name) { // find the player or throws an error if it cant find it
        return players.stream().filter(player -> player.getName().equals(name)).findFirst().orElseThrow(() -> new IllegalArgumentException("player not in lobby"));
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
        return Objects.hash(maxNumberOfPlayers, id, customNameLobby, players, playerCount, readyCount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lobby lobby = (Lobby) o;
        return maxNumberOfPlayers == lobby.maxNumberOfPlayers &&
                playerCount == lobby.playerCount &&
                readyCount == lobby.readyCount &&
                Objects.equals(id, lobby.id) &&
                Objects.equals(customNameLobby, lobby.customNameLobby) &&
                Objects.equals(players, lobby.players);
    }
}
