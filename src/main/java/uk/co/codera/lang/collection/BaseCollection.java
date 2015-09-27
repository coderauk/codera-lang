package uk.co.bssd.hank.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class BaseCollection<C, V> implements Iterable<V> {

	private final List<V> collection;

	public BaseCollection(List<V> collection) {
		this.collection = Collections.unmodifiableList(collection);
	}
	
	protected abstract C newCollection(List<V> collection);

	@Override
	public Iterator<V> iterator() {
		return this.collection.iterator();
	}

	public int size() {
		return this.collection.size();
	}

	protected V find(AttributeSelector<V> attributeSelector, String value) {
		for (V instance : this) {
			if (attributeSelector.value(instance).equals(value)) {
				return instance;
			}
		}
		throw new IllegalStateException("No object could be found with value="
				+ value);
	}

	protected C list(AttributeSelector<V> attributeSelector, String value) {
		List<V> filtered = new ArrayList<V>();
		for (V instance : this) {
			if (isEqual(value, attributeSelector.value(instance))) {
				filtered.add(instance);
			}
		}
		return newCollection(filtered);
	}
	
	private boolean isEqual(String expected, String actual) {
		if (expected != null) {
			return expected.equals(actual);
		}
		return actual == null;
	}
}