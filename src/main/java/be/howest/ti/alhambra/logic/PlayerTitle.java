package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PlayerTitle implements Comparable<PlayerTitle> {

    private final String title;
    private final String description;
    @JsonIgnore
    private int value;
    @JsonProperty("value")
    private String valueWithUnit;

    @JsonCreator
    public PlayerTitle(@JsonProperty("title") String title, @JsonProperty("description") String description, @JsonProperty("value") String unit) {
        this(title, description, 0, unit);
    }

    public PlayerTitle(String title, String description, int value, String unit) {
        this.title = title;
        this.description = description;
        this.value = value;
        this.valueWithUnit = value + " " + unit;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty("value")
    public String getValueWithUnit() {
        return valueWithUnit;
    }

    public void setValueWithUnit(String valueWithUnit) {
        this.valueWithUnit = valueWithUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerTitle that = (PlayerTitle) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int compareTo(PlayerTitle playerTitle) {
        if (!this.equals(playerTitle)) throw new IllegalArgumentException("can only compare with equal titles");
        return playerTitle.getValue() - this.value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
