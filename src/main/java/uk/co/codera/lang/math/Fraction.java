package uk.co.codera.lang.math;

public class Fraction {

    private final int nominator;
    private final int denominator;

    private Fraction(int nominator, int denominator) {
        this.nominator = nominator;
        this.denominator = denominator;
    }

    public static Fraction from(String fraction) {
        String[] split = fraction.split("/");
        return from(Integer.valueOf(split[0]), Integer.parseInt(split[1]));
    }

    public static Fraction from(int numerator, int denominator) {
        return new Fraction(numerator, denominator);
    }

    @Override
    public String toString() {
        return this.nominator + "/" + this.denominator;
    }
}