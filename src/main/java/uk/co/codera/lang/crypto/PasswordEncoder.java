package uk.co.codera.lang.crypto;

public interface PasswordEncoder {

    String encode(String password);

    boolean matches(CharSequence rawPassword, String encodedPassword);
}