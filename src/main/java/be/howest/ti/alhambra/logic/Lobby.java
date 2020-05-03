package be.howest.ti.alhambra.logic;


import java.util.LinkedHashMap;
import java.util.Map;

public class Lobby {
    private boolean started;
    private final String gameId;
    private Map<String, Boolean> playersReady;

    
    public Lobby(String gameId) {
        this.started = false;
        this.gameId = gameId;
        this.playersReady = new LinkedHashMap<>();
    }



}
