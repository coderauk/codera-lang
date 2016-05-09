package uk.co.codera.lang.math;

import java.math.BigDecimal;

public class Probability {

	private final BigDecimal probability;
	
	private Probability(BigDecimal probability) {
		this.probability = probability;
	}
	
	public static Probability of(double probability) {
		return of(BigDecimal.valueOf(probability));
	}
	
	public static Probability of(String probability) {
		return of(new BigDecimal(probability));
	}
	
	public static Probability of(BigDecimal probability) {
		return new Probability(probability);
	}
	
	@Override
	public String toString() {
		return this.probability.toPlainString();
	}
}