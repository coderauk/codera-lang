package uk.co.codera.lang.math;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class FractionTest {

    @Test
    public void shouldBeAbleToConstructFromString() {
        assertThat(Fraction.from("7/4"), is(notNullValue()));
    }

    @Test
    public void shouldBeAbleToConstructFromNumeratorAndDenominator() {
        assertThat(Fraction.from(7, 4).toString(), is("7/4"));
    }

    @Test
    public void shouldPrintFractionAsToString() {
        assertThat(Fraction.from("1/2").toString(), is("1/2"));
    }
}