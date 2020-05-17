package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CardinalDirection {
    NORTH, EAST, SOUTH, WEST; // clockwise order

    @JsonValue
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
