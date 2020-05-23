package be.howest.ti.alhambra.logic.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlhambraIllegalArgumentExceptionTest {

    @Test
    void testError(){
        assertThrows(AlhambraIllegalArgumentException.class, this::error);
    }

    private void error(){
        throw new AlhambraIllegalArgumentException("test");
    }

}