package uk.co.codera.lang.measure;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum UnitOfLength {

    METRES {
        @Override
        public BigDecimal toMetres(BigDecimal length) {
            return length;
        }

        @Override
        public BigDecimal toMiles(BigDecimal length) {
            return length.divide(new BigDecimal("1609.34"), RoundingMode.HALF_UP);
        }
    },

    MILES {
        @Override
        public BigDecimal toMetres(BigDecimal length) {
            return length.multiply(new BigDecimal("1609.34"));
        }

        @Override
        public BigDecimal toMiles(BigDecimal length) {
            return length;
        }
    };

    public abstract BigDecimal toMetres(BigDecimal length);

    public abstract BigDecimal toMiles(BigDecimal length);
}