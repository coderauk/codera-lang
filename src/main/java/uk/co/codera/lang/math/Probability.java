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

    public Probability add(Probability other) {
        return of(doAdd(other));
    }

    public int scale() {
        return this.probability.scale();
    }

    @Override
    public String toString() {
        return this.probability.toPlainString();
    }

    @Override
    public int hashCode() {
        return 37 * this.probability.stripTrailingZeros().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Probability)) {
            return false;
        }
        Probability other = (Probability) obj;
        return this.probability.compareTo(other.probability) == 0;
    }

    private BigDecimal doAdd(Probability other) {
        return this.probability.add(other.probability);
    }
}