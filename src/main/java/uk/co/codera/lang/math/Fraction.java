package uk.co.codera.lang.math;

import java.math.BigInteger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Fraction {

    private final int nominator;
    private final int denominator;

    private Fraction(int nominator, int denominator) {
        int gcd = gcd(nominator, denominator);
        this.nominator = nominator / gcd;
        this.denominator = denominator / gcd;
    }

    public static Fraction from(String fraction) {
        String[] split = fraction.split("/");
        return from(Integer.valueOf(split[0]), Integer.parseInt(split[1]));
    }

    public static Fraction from(int numerator, int denominator) {
        return new Fraction(numerator, denominator);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.nominator).append(this.denominator).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Fraction)) {
            return false;
        }
        Fraction other = (Fraction) obj;
        return new EqualsBuilder().append(this.nominator, other.nominator).append(this.denominator, other.denominator)
                .isEquals();
    }

    @Override
    public String toString() {
        return this.nominator + "/" + this.denominator;
    }

    private int gcd(int nominator, int denominator) {
        return BigInteger.valueOf(nominator).gcd(BigInteger.valueOf(denominator)).intValue();
    }
}