package uk.co.codera.lang.measure;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum UnitOfLength {

	METRES {
		public BigDecimal toMetres(BigDecimal length) { return length; }
		public BigDecimal toMiles(BigDecimal length) { return length.divide(new BigDecimal("1609.34"), RoundingMode.HALF_UP); }
	},
	
	MILES {
		public BigDecimal toMetres(BigDecimal length) { return length.multiply(new BigDecimal("1609.34")); }
		public BigDecimal toMiles(BigDecimal length) { return length; }
	};
	
	public BigDecimal toMetres(BigDecimal length) {
		throw new AbstractMethodError();
	}
	
	public BigDecimal toMiles(BigDecimal length) {
		throw new AbstractMethodError();
	}
}