package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.Test;


public class BankTest {

    @Test
    void addCoins(){
        Bank bank = new Bank();
        bank.addCoin(new Coin(Currency.GREEN,5));
    }
}
