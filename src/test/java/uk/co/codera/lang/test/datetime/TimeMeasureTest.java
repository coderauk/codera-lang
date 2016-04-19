package uk.co.codera.lang.test.datetime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.codera.lang.datetime.TimeMeasure.seconds;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import uk.co.codera.lang.datetime.TimeMeasure;

public class TimeMeasureTest {

    @Test
    public void testConvertToNewUnit() {
        TimeMeasure newTimeMeasure = seconds(10).convert(TimeUnit.MILLISECONDS);
        assertThat(newTimeMeasure.quantity(), is(10000L));
        assertThat(newTimeMeasure.unit(), is(TimeUnit.MILLISECONDS));
    }
}