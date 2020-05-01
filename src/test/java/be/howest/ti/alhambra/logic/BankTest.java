package be.howest.ti.alhambra.logic;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

    @Test
    void bankCoins() {
        Bank bank = new Bank();

        assertArrayEquals(new Coin[4], bank.getCoins());

        bank.addCoin(new Coin(Currency.GREEN, 5));
        Coin[] temp = new Coin[4];
        temp[0] = (new Coin(Currency.GREEN, 5));
        //check if  add coins works and countEmptyCoins works
        assertEquals(3, bank.countEmptyCoins());

        assertArrayEquals(temp, bank.getCoins());
        bank.addCoin(new Coin(Currency.GREEN, 5));
        bank.addCoin(new Coin(Currency.GREEN, 5));
        bank.addCoin(new Coin(Currency.GREEN, 5));

        assertThrows(IllegalStateException.class, () -> bank.addCoin(new Coin(Currency.GREEN, 5)));

        Coin[] temp2 = new Coin[2];
        temp2[0] = (new Coin(Currency.GREEN, 5));
        temp2[1] = (new Coin(Currency.GREEN, 5));


        bank.removeCoins(temp2);
        //check if removeCoins works
        assertEquals(2, bank.countEmptyCoins());

        Coin[] temp3 = new Coin[2];
        temp3[0] = (new Coin(Currency.GREEN, 5));
        temp3[1] = (new Coin(Currency.GREEN, 7));
        //throw error if it cant find the coin
        assertThrows(IllegalArgumentException.class, () -> bank.removeCoins(temp3));

        bank.addCoins(temp3);

        Coin[] temp4 = {new Coin(Currency.GREEN, 5), new Coin(Currency.GREEN, 7), null, new Coin(Currency.GREEN, 5)};
        assertArrayEquals(temp4,bank.getCoins());
    }
}
