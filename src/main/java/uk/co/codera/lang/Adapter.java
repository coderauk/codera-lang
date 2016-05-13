package uk.co.codera.lang;

public interface Adapter<I, O> {

    O adapt(I input);
}