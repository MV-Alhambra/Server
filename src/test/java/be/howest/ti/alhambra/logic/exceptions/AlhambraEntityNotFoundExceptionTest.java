package be.howest.ti.alhambra.logic.exceptions;

import be.howest.ti.alhambra.exceptions.AlhambraException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlhambraEntityNotFoundExceptionTest {

    @Test
    void testError(){
        assertThrows(AlhambraEntityNotFoundException.class, this::error);
    }

    private void error(){
        throw new AlhambraEntityNotFoundException("test");
    }

}