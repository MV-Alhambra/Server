package be.howest.ti.alhambra.logic;

public class PlayerInLobby {

    private final String name;
    private boolean status;
    private final String playerToken;

    public PlayerInLobby(String name) {
        this.name = name;
        this.status = false;
        this.playerToken = "token";
    }
}
