package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Reserve {
    private final List<String> reserve;

    @JsonCreator
    public Reserve(@JsonProperty("reserve") List<String> reserve){
        this.reserve = reserve;
    }

}
