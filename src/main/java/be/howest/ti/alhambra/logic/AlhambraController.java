package be.howest.ti.alhambra.logic;


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

    public Lobby addLobby() {
        String gameId = String.format("%03d", id++); //001
        Lobby lobby = new Lobby(gameId);
        lobbies.add(lobby);
        return lobby;
    }


}
