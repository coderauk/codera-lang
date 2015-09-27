package uk.co.bssd.hank.test.collection;

import java.util.Arrays;
import java.util.List;

import uk.co.bssd.hank.collection.AttributeSelector;
import uk.co.bssd.hank.collection.BaseCollection;

public class ValueObjects extends BaseCollection<ValueObjects, ValueObject> {

	private static class IdSelector implements AttributeSelector<ValueObject> {
		@Override
		public String value(ValueObject instance) {
			return instance.id();
		}
	}

	private static class NonUniqueFieldSelector implements
			AttributeSelector<ValueObject> {
		@Override
		public String value(ValueObject instance) {
			return instance.nonUniqueField();
		}
	}

	private static class NullableFieldSelector implements
			AttributeSelector<ValueObject> {
		@Override
		public String value(ValueObject instance) {
			return instance.nullableField();
		}
	}

	public static ValueObjects over(ValueObject... valueObjects) {
		return new ValueObjects(Arrays.asList(valueObjects));
	}

	private ValueObjects(List<ValueObject> collection) {
		super(collection);
	}

	protected ValueObjects newCollection(List<ValueObject> collection) {
		return new ValueObjects(collection);
	}

	public ValueObject findById(String id) {
		return find(idSelector(), id);
	}

	public ValueObjects listWithNonUniqueField(String nonUniqueField) {
		return list(nonUniqueFieldSelector(), nonUniqueField);
	}

	public ValueObjects listWithNullableField(String nullableField) {
		return list(nullableFieldSelector(), nullableField);
	}

	private static AttributeSelector<ValueObject> idSelector() {
		return new IdSelector();
	}

	private static AttributeSelector<ValueObject> nonUniqueFieldSelector() {
		return new NonUniqueFieldSelector();
	}

	private static AttributeSelector<ValueObject> nullableFieldSelector() {
		return new NullableFieldSelector();
	}
}
