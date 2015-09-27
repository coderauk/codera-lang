package uk.co.bssd.hank.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiMap<K, V> {

	private final Map<K, List<V>> values; 
	
	public MultiMap() {
		this.values = new HashMap<>();
	}

	public void put(K key, V value) {
		if (!this.values.containsKey(key)) {
			this.values.put(key, new ArrayList<V>());
		}
		this.values.get(key).add(value);
	}
	
	public boolean remove(K key, V value) {
		return get(key).remove(value);
	}
	
	public Collection<V> get(K key) {
		if (this.values.containsKey(key)) {
			return this.values.get(key);
		}
		return Collections.emptyList();
	}

}