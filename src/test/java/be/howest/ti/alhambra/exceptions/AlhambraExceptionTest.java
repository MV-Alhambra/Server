package be.howest.ti.alhambra.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlhambraExceptionTest {

    @Test
    void exception(){

        assertThrows(AlhambraException.class, this::error);
    }

    private void error(){
        throw new AlhambraException("test");
    }
}