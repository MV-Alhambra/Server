package be.howest.ti.alhambra.logic;


import be.howest.ti.alhambra.logic.exceptions.AlhambraEntityNotFoundException;

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

    public String addLobby(String customGameName, int maxPlayerCount) {
        String gameId = String.format("%03d", id++); //001
        lobbies.add(new Lobby(gameId, customGameName, maxPlayerCount));
        return gameId;
    }

    public String joinLobby(String gameId, String name) {
        findLobby(gameId).addPlayer(name);
        return "playerToken";
    }

    private Lobby findLobby(String gameId) {
        return lobbies.stream().filter(lobby -> lobby.getId().equals(gameId)).findFirst().orElseThrow(() -> new AlhambraEntityNotFoundException("Lobby (" + gameId + ") not found!"));
    }

    public Boolean leaveLobby(String gameId, String name) {
        try {
            findLobby(gameId).removePlayer(name);
        } catch (AlhambraEntityNotFoundException exception) {
            //try leaving a game instead but that's for a different issue
            throw exception;//temp since not implemented yet
        }
        return true;
    }

    public boolean readyUp(String gameId, String playerName) {
        return findLobby(gameId).readyUpPlayer(playerName);

    }

    public boolean readyDown(String gameId, String playerName) {
        return findLobby(gameId).unreadyPlayer(playerName);

    }

    public boolean startLobby(String gameId) {
        Lobby lobby = findLobby(gameId);
        games.put(lobby.getId(), lobby.startGame());
        lobbies.remove(lobby);
        return true;
    }

    public Object getGame(String gameId) {
        Lobby lobby = findLobbyNoError(gameId);
        if (lobby == null) {
            return findGame(gameId);
        }
        return lobby;
    }

    private Lobby findLobbyNoError(String gameId) {
        return lobbies.stream().filter(lobby -> lobby.getId().equals(gameId)).findFirst().orElse(null);
    }

    private Game findGame(String gameId) {
        Game game = games.get(gameId);
        if (game == null) throw new AlhambraEntityNotFoundException("Can't find that game");
        return game;
    }

    public Game takeCoins(String gameId, String playerName, Coin[] coins) {
        return findGame(gameId).takeCoins(playerName, coins);
    }

    public Game buyBuilding(String gameId, String playerName, Currency currency, Coin[] coins) {
        return findGame(gameId).buyBuilding(playerName, currency, coins);
    }
}
