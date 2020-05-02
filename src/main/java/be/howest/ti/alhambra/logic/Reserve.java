package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class Reserve {
    private final List<String> reserve; //Temporarily with String values instead of real buildings

    @JsonCreator
    public Reserve(@JsonProperty("reserve") List<String> reserve){
        this.reserve = reserve;
    }

    public List<String> getReserve() {
        return reserve;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserve reserve1 = (Reserve) o;
        return Objects.equals(reserve, reserve1.reserve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reserve);
    }
}
