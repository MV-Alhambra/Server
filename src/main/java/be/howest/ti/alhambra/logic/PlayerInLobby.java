package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PlayerInLobby {

    private final String name;
    private boolean status;
    private final String playerToken;

    @JsonCreator
    public PlayerInLobby(@JsonProperty("name") String name, @JsonProperty("status") boolean status, @JsonProperty("token") String playerToken ) {
        this.name = name;
        this.status = status;
        this.playerToken = playerToken;
    }


    public PlayerInLobby(String name) {
        this.name = name;
        this.status = false;
        this.playerToken = "token";
    }




    public String getName() {
        return name;
    }

    public boolean isStatus() {
        return status;
    }

    public String getPlayerToken() {
        return playerToken;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInLobby that = (PlayerInLobby) o;
        return status == that.status &&
                Objects.equals(name, that.name) &&
                Objects.equals(playerToken, that.playerToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status, playerToken);
    }
}
