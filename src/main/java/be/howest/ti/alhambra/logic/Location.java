package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

public class Location {
    private final int col;
    private final int row;

    @JsonCreator
    public Location(@JsonProperty("col") int col, @JsonProperty("row") int row) {
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return col == location.col && row == location.row;
    }
}
