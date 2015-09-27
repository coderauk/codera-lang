package uk.co.bssd.hank.test.collection;

public class ValueObject {
	private final String id;
	private final String nonUniqueField;
	private final String nullableField;

	public static ValueObject create(String id, String nonUniqueField, String nullableField) {
		return new ValueObject(id, nonUniqueField, nullableField);
	}

	private ValueObject(String id, String nonUniqueField, String nullableField) {
		this.id = id;
		this.nonUniqueField = nonUniqueField;
		this.nullableField = nullableField;
	}

	public String id() {
		return this.id;
	}

	public String nonUniqueField() {
		return this.nonUniqueField;
	}
	
	public String nullableField() {
		return this.nullableField;
	}
}