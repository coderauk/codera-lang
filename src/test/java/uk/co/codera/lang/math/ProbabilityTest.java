package uk.co.codera.lang.math;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

public class ProbabilityTest {

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
}