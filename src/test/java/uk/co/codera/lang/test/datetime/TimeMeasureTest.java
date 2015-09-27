package uk.co.bssd.hank.test.datetime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.bssd.hank.datetime.TimeMeasure.seconds;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import uk.co.bssd.hank.datetime.TimeMeasure;

public class TimeMeasureTest {

	@Test
	public void testConvertToNewUnit() {
		TimeMeasure newTimeMeasure = seconds(10).convert(TimeUnit.MILLISECONDS);
		assertThat(newTimeMeasure.quantity(), is(10000L));
		assertThat(newTimeMeasure.unit(), is(TimeUnit.MILLISECONDS));
	}
}