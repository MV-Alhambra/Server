package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PlayerTitle {

    private final String title;
    private final String description;
    private String value;

    @JsonCreator
    public PlayerTitle(@JsonProperty("title") String title,@JsonProperty("description") String description, @JsonProperty("value") String unit) {
        this.title = title;
        this.description = description;
        this.value = "X " + unit;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = this.value.replace("X", String.valueOf(value));
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
    public int hashCode() {
        return Objects.hash(title, description, value);
    }
}
