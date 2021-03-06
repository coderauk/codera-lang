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
        this.encoder = new Pbkdf2PasswordEncoder();
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
        PasswordEncoder encoderWithIterationsSet = new Pbkdf2PasswordEncoder(50, 128);
        assertThat(encoderWithIterationsSet.encode("any"), startsWith("50:"));
    }

    @Test
    public void shouldNotMatchPasswordThatIsNotCorrect() {
        String encodedPassword = this.encoder.encode("password1");
        assertThat(this.encoder.matches("password2", encodedPassword), is(false));
    }

    @Test
    public void shouldMatchPasswordThatIsCorrect() {
        String encodedPassword = this.encoder.encode("password1");
        assertThat(this.encoder.matches("password1", encodedPassword), is(true));
    }
}