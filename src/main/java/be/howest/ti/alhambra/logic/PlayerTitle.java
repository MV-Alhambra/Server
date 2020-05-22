package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerTitle implements Comparable<PlayerTitle> {

    private final String role;
    private final String description;
    @JsonIgnore
    private int value;
    @JsonProperty("value")
    private String unit;

    @JsonCreator
    public PlayerTitle(@JsonProperty("role") String role, @JsonProperty("description") String description, @JsonProperty("value") String unit) {
        this(role, description, 0, unit);
    }

    public PlayerTitle(String role, String description, int value, String unit) {
        this.role = role;
        this.description = description;
        this.value = value;
        this.unit = unit;
    }

    public static List<PlayerTitle> getAllPlayerTitles() {
        List<PlayerTitle> titles = new ArrayList<>();
        titles.add(new PlayerTitle("The hoarder", "Has the most value of coins left over", "coin value"));
        titles.add(new PlayerTitle("Richie Rich", "Spent the most coins", "value of coins"));
        titles.add(new PlayerTitle("Bob the builder", "Has the most buildings in his city", "buildings"));
        titles.add(new PlayerTitle("The Great Wall of China", "Has the longest wall", "wall pieces"));
        titles.add(new PlayerTitle("Mr. Perfect", "Has the most redesigns", "redesigns"));
        titles.add(new PlayerTitle("The stalker", "Viewed the most opponents cities", "cities"));
        titles.add(new PlayerTitle("The Collector", "has the most buildings in reserve", "buildings"));
        return titles;
    }

    public static PlayerTitle getDefault() {
        return new PlayerTitle("Nothing special", "A title is not everything", "");
    }

    public String getRole() {
        return role;
    }

    public String getDescription() {
        return description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JsonProperty("value")
    public String getValueWithUnit() {
        return value + " " + unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }

    @Override
    public int compareTo(PlayerTitle playerTitle) {
        if (!this.equals(playerTitle)) throw new IllegalArgumentException("can only compare with equal titles");
        return playerTitle.getValue() - this.value;
    }

    @Override
    public boolean equals(Object o) { // title defines uniqueness
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerTitle that = (PlayerTitle) o;
        return Objects.equals(role, that.role);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PlayerTitle{" +
                "title='" + role + '\'' +
                ", value=" + value +
                '}';
    }
}
