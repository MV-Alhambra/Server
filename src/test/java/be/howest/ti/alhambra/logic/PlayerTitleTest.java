package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTitleTest {

    @Test
    void getValue() {
    }

    @Test
    void getUnit() {
    }

    @Test
    void setUnit() {
    }

    @Test
    void setValue() {
    }

    @Test
    void getAllPlayerTitles() {
        List<PlayerTitle> titles = new ArrayList<>();
        titles.add(new PlayerTitle("The hoarder", "Has the most value of coins left over", "coin value"));
        titles.add(new PlayerTitle("Richie Rich", "Spent the most coins", "value of coins"));
        titles.add(new PlayerTitle("Bob the builder", "Has the most buildings in his city", "buildings"));
        titles.add(new PlayerTitle("The Great Wall of China", "Has the longest wall", "wall pieces"));
        titles.add(new PlayerTitle("Mr. Perfect", "Has the most redesigns", "redesigns"));
        titles.add(new PlayerTitle("The stalker", "Viewed the most opponents cities", "cities"));
        titles.add(new PlayerTitle("The Collector", "has the most buildings in reserve", "buildings"));
        assertEquals(titles,PlayerTitle.getAllPlayerTitles());
    }

    @Test
    void getDefault() {
        assertEquals(new PlayerTitle("Nothing special", "A title is not everything", ""),PlayerTitle.getDefault());
    }

    @Test
    void getValueWithUnit() {
    }

    @Test
    void compareTo() {
    }

    @Test
    void testEquals() {
        PlayerTitle test =new PlayerTitle("Nothing special", "A title is not everything", "");
        assertEquals(test, test);
    }
}