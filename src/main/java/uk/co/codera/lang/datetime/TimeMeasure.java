package uk.co.codera.lang.datetime;

import java.util.concurrent.TimeUnit;

/**
 * Represents a measure of time, i.e. a quantity and a unit, e.g. 10 seconds.
 */
public final class TimeMeasure {
	
	private long quantity;
	private TimeUnit unit;
	
	private TimeMeasure(long quantity, TimeUnit unit) {
		this.quantity = quantity;
		this.unit = unit;
	}

    public static TimeMeasure milliseconds(long milliseconds) {
        return create(milliseconds, TimeUnit.MILLISECONDS);
    }

    public static TimeMeasure seconds(long seconds) {
        return create(seconds, TimeUnit.SECONDS);
    }

    public static TimeMeasure create(long quantity, TimeUnit unit) {
        return new TimeMeasure(quantity, unit);
    }

    public long quantity() {
        return this.quantity;
    }

    public TimeUnit unit() {
        return this.unit;
    }

    public TimeMeasure convert(TimeUnit newUnit) {
        return create(newUnit.convert(this.quantity, this.unit), newUnit);
    }
}