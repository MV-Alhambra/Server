package be.howest.ti.alhambra.logic.exceptions;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Random;

public class PlayerToken {
    private final byte[] token;


    public PlayerToken(String playerName, String id, String gameName) {
        String toBeHashed = playerName + id + gameName + new Random().nextInt(5000); //truly unique
        token = hashToken(toBeHashed);
    }


    public byte[] hashToken(String token) { //according to internet PBKDF2 is more secure than SHA-512 since its slower to crack
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(token.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return factory.generateSecret(spec).getEncoded();
    }

    public byte[] getToken() {
        return token;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(token);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerToken that = (PlayerToken) o;
        return Arrays.equals(token, that.token);
    }
}
