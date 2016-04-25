package uk.co.codera.lang.test.datetime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.codera.lang.datetime.TimeMeasure.milliseconds;
import static uk.co.codera.lang.datetime.TimeMeasure.seconds;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import uk.co.codera.lang.datetime.TimeMeasure;

public class TimeMeasureTest {

    @Test
    public void shouldConvertFromSecondsToMilliseconds() {
        TimeMeasure newTimeMeasure = seconds(10).convert(TimeUnit.MILLISECONDS);
        assertThat(newTimeMeasure.quantity(), is(10000L));
        assertThat(newTimeMeasure.unit(), is(TimeUnit.MILLISECONDS));
    }

    @Test
    public void shouldConvertFromMillisecondsToSeconds() {
        TimeMeasure newTimeMeasure = milliseconds(1000L).convert(TimeUnit.SECONDS);
        assertThat(newTimeMeasure.quantity(), is(1L));
        assertThat(newTimeMeasure.unit(), is(TimeUnit.SECONDS));
    }

    @Test
    public void shouldLosePrevisionWhenConvertingFromMillisecondsToSeconds() {
        assertThat(milliseconds(1230L).convert(TimeUnit.SECONDS).quantity(), is(1L));
    }
}