package uk.co.bssd.hank.test.collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import uk.co.bssd.hank.collection.MultiMap;

public class MultiMapTest {

	@Test
	public void testKeyWithNoValuesReturnsEmptyCollection() {
		assertThat(multiMap().get("key"), is(empty()));
	}
	
	@Test
	public void testKeyWithValuesReturnsThemInOrderAdded() {
		String key = "pil";
		String value1 = "This ain't";
		String value2 = "a love song";
		
		MultiMap<String, String> multimap = multiMap();
		multimap.put(key, value1);
		multimap.put(key, value2);
		
		assertThat(multimap.get(key), contains(value1, value2));
	}
	
	@Test 
	public void testCanAddAndRemoveValueForKey() {
		String key = "nothing";
		String value = "remove me";
		
		MultiMap<String, String> multimap = multiMap();
		multimap.put(key, value);
		
		assertThat(multimap.remove(key, value), is(true));
		assertThat(multimap.get(key), is(empty()));
	}
	
	@Test
	public void testRemoveOnKeyThatDoesNotExist() {
		assertThat(multiMap().remove("not", "me"), is(false));
	}
	
	private MultiMap<String, String> multiMap() {
		return new MultiMap<>();
	}
}