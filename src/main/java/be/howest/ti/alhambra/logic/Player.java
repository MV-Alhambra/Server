package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Player {
    private final String name;

    @JsonCreator
    public Player(@JsonProperty("player") String name){
        this.name = name;
    }

    @JsonGetter("player")
    public String getName() {
        return name;
    }
    

}
