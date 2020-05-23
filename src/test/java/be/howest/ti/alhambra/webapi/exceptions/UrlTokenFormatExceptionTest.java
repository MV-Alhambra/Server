package be.howest.ti.alhambra.webapi.exceptions;

import be.howest.ti.alhambra.logic.exceptions.AlhambraEntityNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlTokenFormatExceptionTest {
    @Test
    void testError(){
        assertThrows(UrlTokenFormatException.class, this::error);
    }

    private void error(){
        throw new UrlTokenFormatException("test");
    }

}