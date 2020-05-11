package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PlayerInLobby {

    private final String name;
    private boolean status;

    @JsonCreator
    public PlayerInLobby(@JsonProperty("name") String name, @JsonProperty("status") boolean status) {
        this.name = name;
        this.status = status;
    }

    public PlayerInLobby(String name) {
        this(name,false);
    }

    public String getName() {
        return name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInLobby that = (PlayerInLobby) o;
        return Objects.equals(name, that.name);
    }
}
