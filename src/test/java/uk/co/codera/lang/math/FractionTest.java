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
    public void shouldBeAbleToConstructNegativeFraction() {
        assertThat(Fraction.from("-7/4").toString(), is("-7/4"));
    }

    @Test
    public void shouldPrintFractionAsToString() {
        assertThat(Fraction.from("1/2").toString(), is("1/2"));
    }

    @Test
    public void shouldReduceToLowestTerm() {
        assertThat(Fraction.from("4/6").toString(), is("2/3"));
    }

    @Test
    public void shouldBeAbleToMultiplyTwoFractionsTogether() {
        assertThat(Fraction.from("2/4").multiply(Fraction.from("2/4")), is(Fraction.from("1/4")));
    }

    @Test
    public void shouldBeAbleToMultipleTwoNegativeFractionsTogether() {
        assertThat(Fraction.from("-1/4").multiply(Fraction.from("-1/4")), is(Fraction.from("1/16")));
    }

    @Test
    public void shouldBeAbleToMultipleOnePositiveAndOneNegativeFractionTogether() {
        assertThat(Fraction.from("-1/4").multiply(Fraction.from("1/4")), is(Fraction.from("-1/16")));
    }

    @Test
    public void shouldBeAbleToAddTwoFractionsTogether() {
        assertThat(Fraction.from("1/4").add(Fraction.from("2/5")), is(Fraction.from("13/20")));
    }

    @Test
    public void shouldBeAbleToAddTwoNegativeFractionsTogether() {
        assertThat(Fraction.from("-1/4").add(Fraction.from("-2/5")), is(Fraction.from("-13/20")));
    }

    @Test
    public void shouldBeAbleToAddPositiveAndNegativeFractionTogether() {
        assertThat(Fraction.from("1/4").add(Fraction.from("-2/5")), is(Fraction.from("-3/20")));
    }

    @Test
    public void shouldBeAbleToDivideOneFractionByAnother() {
        assertThat(Fraction.from("1/4").divide(Fraction.from("2/3")), is(Fraction.from("3/8")));
    }

    @Test
    public void shouldBeAbleToDividePositiveFractionByANegative() {
        assertThat(Fraction.from("1/4").divide(Fraction.from("-2/3")), is(Fraction.from("-3/8")));
    }

    @Test
    public void shouldBeAbleToDivideNegativeFractionByANegative() {
        assertThat(Fraction.from("-1/4").divide(Fraction.from("-2/3")), is(Fraction.from("3/8")));
    }

    @Test
    public void shouldBeAbleToObtainReciprocal() {
        assertThat(Fraction.from("2/5").reciprocal(), is(Fraction.from("5/2")));
    }

    @Test
    public void shouldBeAbleToObtainReciprocalOfNegativeFraction() {
        assertThat(Fraction.from("-2/5").reciprocal(), is(Fraction.from("-5/2")));
    }

    @Test
    public void shouldBeAbleToSubtractOneFractionFromAnother() {
        assertThat(Fraction.from("2/5").subtract(Fraction.from("1/4")), is(Fraction.from("3/20")));
    }

    @Test
    public void shouldBeAbleToSubtractOneNegativeFractionFromAnotherNegative() {
        assertThat(Fraction.from("-2/5").subtract(Fraction.from("-1/4")), is(Fraction.from("-3/20")));
    }

    @Test
    public void shouldBeAbleToSubtractOneNegativeFractionFromAPositive() {
        assertThat(Fraction.from("2/5").subtract(Fraction.from("-1/4")), is(Fraction.from("13/20")));
    }

    @Test
    public void shouldBeAbleToSubtractOnePositiveFractionFromANegative() {
        assertThat(Fraction.from("-2/5").subtract(Fraction.from("1/4")), is(Fraction.from("-13/20")));
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