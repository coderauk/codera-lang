package uk.co.codera.lang.crypto;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class Pbkdf2PasswordEncoderTest {

    private PasswordEncoder encoder;

    @Before
    public void before() {
        this.encoder = new Pbkdf2PasswordEncoder(50, 128);
    }

    @Test
    public void shouldEncodeNonNullPasswordToNonNullResult() {
        assertThat(this.encoder.encode("password1"), is(notNullValue()));
    }

    @Test
    public void shouldNotEncodePasswordToParameterPassedIn() {
        assertThat(this.encoder.encode("password"), is(not(equalTo("password"))));
    }

    @Test
    public void shouldNotEncodeThePasswordTheSameTwice() {
        String firstHash = this.encoder.encode("password");
        String secondHash = this.encoder.encode("password");
        assertThat(firstHash, is(not(equalTo(secondHash))));
    }

    @Test
    public void shouldEncodeNumberIterationsAsFirstPartOfResult() {
        assertThat(this.encoder.encode("any"), startsWith("50:"));
    }
}