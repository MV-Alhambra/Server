package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BuildingType {
    PAVILION, SERAGLIO, ARCADES, CHAMBERS, GARDEN, TOWER; //removed FOUNTAIN and sorted on score for ScoringTable

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
