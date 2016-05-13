package uk.co.codera.lang;

@FunctionalInterface
public interface Adapter<I, O> {

    O adapt(I input);
}