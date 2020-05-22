package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
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
        return Objects.hash(title);
    }

    @Override
    public boolean equals(Object o) { // title defines uniqueness
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerTitle that = (PlayerTitle) o;
        return Objects.equals(title, that.title);
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

    public static List<PlayerTitle> getAllPlayerTitles(){
        List<PlayerTitle> titles = new ArrayList<>();
        titles.add(new PlayerTitle("The hoarder","Has the most value of coins left over","coin value"));
        titles.add(new PlayerTitle("Richie Rich","Spent the most coins","value of coins"));
        titles.add(new PlayerTitle("Bob the builder","Build the most buildings","buildings"));
        titles.add(new PlayerTitle("The wall of China","Has the longest wall", "wall pieces"));
        titles.add(new PlayerTitle("Mr. Perfect","Has the most redesigns","redesigns"));
        titles.add(new PlayerTitle("The stalker","Viewed the most opponents cities","cities"));
        return  titles;
    }
}
