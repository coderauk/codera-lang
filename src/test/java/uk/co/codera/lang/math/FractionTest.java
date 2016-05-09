package uk.co.codera.lang.math;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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

    @Test
    public void shouldNotBeEqualIfOtherObjectIsNull() {
        assertThat(Fraction.from("1/2"), is(not(equalTo(null))));
    }

    @Test
    public void shouldNotBeEqualIfOtherObjectIsNotAFraction() {
        assertThat(Fraction.from("1/2"), is(not(equalTo(new Object()))));
    }

    @Test
    public void shouldNotBeEqualIfFractionDifferent() {
        assertThat(Fraction.from("1/2"), is(not(equalTo(Fraction.from("1/3")))));
    }

    @Test
    public void shouldBeEqualIfFractionTheSame() {
        assertThat(Fraction.from("1/2"), is(equalTo(Fraction.from("1/2"))));
    }

    @Test
    public void shouldHaveSameHashCodeIfFractionsAreEqual() {
        assertThat(Fraction.from("1/2").hashCode(), is(equalTo(Fraction.from("1/2").hashCode())));
    }

    @Test
    public void shouldHaveDifferentHashCodeIfFractionsAreNotEqual() {
        assertThat(Fraction.from("1/2").hashCode(), is(not(equalTo(Fraction.from("1/3").hashCode()))));
    }
}