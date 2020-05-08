package be.howest.ti.alhambra.logic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Coin {
    private final Currency currency;
    private final int amount;

    @JsonCreator
    public Coin(@JsonProperty("currency") Currency currency, @JsonProperty("amount") int amount) {
        this.currency = currency;
        this.amount = amount;
    }

    static List<Coin> allCoins() {
        return Stream.of(Currency.values())//create a stream of each currency value
                .flatMap(currency -> IntStream.rangeClosed(1, 9).mapToObj(value -> new Coin(currency, value))) // foreach currency create 9 coins from 1 to 9
                .flatMap(coin -> Stream.of(coin, coin, coin)) // triples the amount of coins
                .collect(Collectors.toList()); //returns a list of 108 coins (1->9->27)*4(for each currency)
    }

    public Currency getCurrency() {
        return currency;
    }

    @JsonProperty("amount")
    int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coin coin = (Coin) o;

        if (amount != coin.amount) return false;
        return currency == coin.currency;
    }

    @Override
    public int hashCode() {
        int result = currency.hashCode();
        result = 31 * result + amount;
        return result;
    }

    @Override
    public String toString() {
        return String.format("(%d %s)", amount, currency);
    }
}
