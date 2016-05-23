package uk.co.codera.lang.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Pbkdf2PasswordEncoder implements PasswordEncoder {

    private static final int DEFAULT_NUMBER_ITERATIONS = 1000;
    private static final int DEFAULT_KEY_LENGTH = 64 * 8;

    private static final String ALGORITHM_PBKDF2 = "PBKDF2WithHmacSHA1";
    private static final String ALGORITHM_SHA1_RANDOM = "SHA1PRNG";

    private final int iterations;
    private final int keyLength;

    public Pbkdf2PasswordEncoder() {
        this(DEFAULT_NUMBER_ITERATIONS, DEFAULT_KEY_LENGTH);
    }

    public Pbkdf2PasswordEncoder(int iterations, int keyLength) {
        this.iterations = iterations;
        this.keyLength = keyLength;
    }

    @Override
    public String encode(String password) {
        byte[] salt = randomSalt();
        byte[] hash = encode(password, salt, this.iterations, this.keyLength);
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String[] parts = encodedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);
        byte[] testHash = encode(rawPassword.toString(), salt, iterations, hash.length * 8);
        return Arrays.equals(hash, testHash);
    }

    private byte[] encode(String password, byte[] salt, int iterations, int keyLength) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM_PBKDF2);
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] randomSalt() {
        SecureRandom sr = secureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private SecureRandom secureRandom() {
        try {
            return SecureRandom.getInstance(ALGORITHM_SHA1_RANDOM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] fromHex(String hex) {
        return DatatypeConverter.parseHexBinary(hex);
    }

    private String toHex(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }
}