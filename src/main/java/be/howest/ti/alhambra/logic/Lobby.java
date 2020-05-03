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
        if (amountReady() == playerCount()){
            this.started = true;
        }
        else {
            throw new IllegalStateException("All players need to be ready to start game");
        }
    }

}
