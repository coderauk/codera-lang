package uk.co.codera.lang.test.measure;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

import uk.co.codera.lang.measure.UnitOfLength;

public class UnitOfLengthTest {

    @Test
    public void shouldConvertMetresToMetres() {
        assertThat(UnitOfLength.METRES.toMetres(new BigDecimal("1.0")), comparesEqualTo(new BigDecimal("1.0")));
    }

    @Test
    public void shouldConvertMetresToMiles() {
        assertThat(UnitOfLength.METRES.toMiles(new BigDecimal("1609.34")), comparesEqualTo(new BigDecimal("1")));
    }

    @Test
    public void shouldConvertMilesToMiles() {
        assertThat(UnitOfLength.MILES.toMiles(new BigDecimal("1.25")), comparesEqualTo(new BigDecimal("1.25")));
    }

    @Test
    public void shouldConvertMilesToMetres() {
        assertThat(UnitOfLength.MILES.toMetres(new BigDecimal("1")), comparesEqualTo(new BigDecimal("1609.34")));
    }
}