package be.howest.ti.alhambra.logic;


import be.howest.ti.alhambra.logic.exceptions.AlhambraEntityNotFoundException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlhambraController {

    private final List<Lobby> lobbies = new ArrayList<>();
    private final Map<String, Game> games = new HashMap<>();
    private int id = 1;

    public Building getBuildings() {
        return new Building(null, -1, null);
    }

    public Currency[] getCurrencies() {
        return Currency.values();
    }

    public BuildingType[] getBuildingTypes() {
        return BuildingType.values();
    }

    public List<Lobby> getLobbies() {
        return lobbies;
    }

    public String addLobby(String customGameName, int maxPlayerCount, boolean autoStart) {
        String gameId = String.format("%03d", id++); //001
        lobbies.add(new Lobby(gameId, customGameName, maxPlayerCount, autoStart));
        return gameId;
    }

    public byte[] joinLobby(String gameId, String name) {
        return findLobby(gameId).addPlayer(name).getToken();
    }

    private Lobby findLobby(String gameId) {
        return lobbies.stream().filter(lobby -> lobby.getId().equals(gameId)).findFirst().orElseThrow(() -> new AlhambraEntityNotFoundException("Lobby (" + gameId + ") not found!"));
    }

    public Boolean leaveLobby(String gameId, String name) {
        try {
            findLobby(gameId).removePlayer(name);
        } catch (AlhambraEntityNotFoundException exception) {
            findGame(gameId).removePlayer(name);
        }
        return true;
    }

    private Game findGame(String gameId) {
        Game game = games.get(gameId);
        if (game == null) throw new AlhambraEntityNotFoundException("Can't find that game");
        return game;
    }

    public boolean readyUp(String gameId, String playerName) {
        return findLobby(gameId).readyUpPlayer(playerName, this);

    }

    public boolean readyDown(String gameId, String playerName) {
        return findLobby(gameId).unreadyPlayer(playerName);

    }

    public boolean startLobby(String gameId) {
        Lobby lobby = findLobbyNoError(gameId);
        if (lobby == null) { //checks if game is already started
            findGame(gameId);
            return false;
        }
        games.put(lobby.getId(), lobby.startGame());
        lobbies.remove(lobby);
        return true;
    }

    private Lobby findLobbyNoError(String gameId) {
        return lobbies.stream().filter(lobby -> lobby.getId().equals(gameId)).findFirst().orElse(null);
    }

    public Object getGame(String gameId) {
        Lobby lobby = findLobbyNoError(gameId);
        if (lobby == null) return findGame(gameId);
        return lobby;
    }

    public Game takeCoins(String gameId, String playerName, Coin[] coins) {
        return findGame(gameId).takeCoins(playerName, coins);
    }

    public Game buyBuilding(String gameId, String playerName, Currency currency, Coin[] coins) {
        return findGame(gameId).buyBuilding(playerName, currency, coins);
    }

    public List<Location> getAvailableBuildLocations(String gameId, String playerName, Map<CardinalDirection, Boolean> walls) {
        return findGame(gameId).findPlayer(playerName).getCity().getAvailableLocations(walls);
    }

    public Game build(String gameId, String playerName, Building building, Location location) {
        return findGame(gameId).build(playerName, building, location);
    }

    public Game redesign(String gameId, String playerName, Building building, Location location) {
        return findGame(gameId).redesign(playerName, building, location);
    }

    public boolean verifyToken(String gameId, String token) {
        PlayerToken playerToken = Json.decodeValue(new JsonObject().put("token", token).toString(), PlayerToken.class);
        try { //tried implementing an interface to reduce this code but it didnt end up working
            return findLobby(gameId).getPlayers().stream().anyMatch(playerInLobby -> playerInLobby.getToken().equals(playerToken));
        } catch (AlhambraEntityNotFoundException e) {
            return findGame(gameId).getPlayers().stream().anyMatch(player -> player.getToken().equals(playerToken));
        }
    }

    public Boolean giveDirk(String gameId, String playerName, Building building) {
        return findGame(gameId).giveBuildingToDirk(building, playerName);
    }
}
