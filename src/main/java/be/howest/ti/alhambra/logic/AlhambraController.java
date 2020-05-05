package be.howest.ti.alhambra.logic;


import be.howest.ti.alhambra.logic.exceptions.AlhambraEntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class AlhambraController {

    private final List<Lobby> lobbies = new ArrayList<>();
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

    public String addLobby() {
        String gameId = String.format("%03d", id++); //001
        Lobby lobby = new Lobby(gameId);
        lobbies.add(lobby);
        return gameId;
    }

    public String joinLobby(String gameId,String name){
        findLobby(gameId).addPlayer(name);
        return "playerToken";
    }

    public Boolean leaveLobby(String gameId,String name){
        try {
            findLobby(gameId).removePlayer(name);
        }catch (AlhambraEntityNotFoundException exception){
            //try leaving a game instead but that's for a different issue
            throw exception;//temp since not implemented yet
        }
        return true;
    }

    private Lobby findLobby(String gameId){
      return lobbies.stream().filter(lobby -> lobby.getId().equals(gameId)).findFirst().orElseThrow(()-> new AlhambraEntityNotFoundException("Lobby ("+ gameId+") not found!"));
    }

    public boolean readyUp(String gameId, String playerName){
        return findLobby(gameId).readyUpPlayer(playerName);
    }

    public boolean readyDown(String gameId, String playerName){
        return findLobby(gameId).unreadyPlayer(playerName);
    }

}
