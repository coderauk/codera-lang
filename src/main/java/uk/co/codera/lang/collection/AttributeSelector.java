package uk.co.bssd.hank.collection;

public interface AttributeSelector<T> {

	String value(T instance);
}