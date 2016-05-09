package uk.co.codera.lang.math;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

public class ProbabilityTest {

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotBeAbleToConstructFromNullBigDecimalValue() {
		Probability.of((BigDecimal) null);
	}

	@Test
	public void shouldBeAbleToConstructFromDouble() {
		assertThat(Probability.of(0.5d), is(notNullValue()));
	}

	@Test
	public void shouldBeAbleToConstructFromString() {
		assertThat(Probability.of("0.5"), is(notNullValue()));
	}

	@Test
	public void shouldBeAbleToConstructFromBigDecimal() {
		assertThat(Probability.of(new BigDecimal("0.5")), is(notNullValue()));
	}

	@Test
	public void shouldPrintValueOfProbabilityAsToString() {
		assertThat(Probability.of(0.75d).toString(), is("0.75"));
	}

	@Test
	public void shouldPreserveScale() {
		assertThat(Probability.of("0.5000").scale(), is(4));
	}

	@Test
	public void shouldNotBeEqualIfOtherObjectIsNull() {
		assertThat(Probability.of("0.5"), is(not(equalTo(null))));
	}

	@Test
	public void shouldNotBeEqualIfOtherObjectIsNotAProbability() {
		assertThat(Probability.of("0.5"), is(not(equalTo(new Object()))));
	}

	@Test
	public void shouldNotBeEqualIfProbabilityDifferent() {
		assertThat(Probability.of("0.5"), is(not(equalTo(Probability.of(0.6d)))));
	}

	@Test
	public void shouldBeEqualIfProbabilityTheSame() {
		assertThat(Probability.of("0.5"), is(equalTo(Probability.of(0.5d))));
	}

	@Test
	public void shouldHaveSameHashCodeIfProbabilitiesAreEqual() {
		assertThat(Probability.of("0.5").hashCode(), is(equalTo(Probability.of("0.5").hashCode())));
	}

	@Test
	public void shouldHaveSameHashCodeIfProbabilitiesAreEqualWithDifferentScale() {
		assertThat(Probability.of("0.5000").hashCode(), is(equalTo(Probability.of("0.5").hashCode())));
	}

	@Test
	public void shouldHaveDifferentHashCodeIfProbabilitiesAreNotEqual() {
		assertThat(Probability.of("0.5").hashCode(), is(not(equalTo(Probability.of("0.75").hashCode()))));
	}

	@Test
	public void shouldBeEqualIfProbabilityTheSameWithDifferentScale() {
		assertThat(Probability.of("0.5"), is(equalTo(Probability.of("0.5000"))));
	}

	@Test
	public void shouldBeAbleToAddTwoProbabilitiesTogether() {
		assertThat(Probability.of("0.5").add(Probability.of("0.25")), is(Probability.of("0.75")));
	}

	@Test
	public void shouldBeAbleToMultiplyTwoProbabilitiesTogether() {
		assertThat(Probability.of("0.5").multiply(Probability.of("0.5")), is(Probability.of("0.25")));
	}

	@Test
	public void shouldBeAbleToCalculateComplement() {
		assertThat(Probability.of("0.25").complement(), is(Probability.of("0.75")));
	}
}