package com.yardi.shared.QSECOFR;

/**
 * Enum for converting an abbreviated month name to its ordinal value.
 * @author Jim
 */
public enum MonthNameAbbr {
	/**
	 * January
	 */
	JAN(1),
	/**
	 * February
	 */
	FEB(2),
	/**
	 * March
	 */
	MAR(3),
	/**
	 * April
	 */
	APR(4), 
	/**
	 * May
	 */
	MAY(5), 
	/**
	 * June
	 */
	JUN(6), 
	/**
	 * July
	 */
	JUL(7), 
	/**
	 * August
	 */
	AUG(8),
	/**
	 * September
	 */
	SEP(9), 
	/**
	 * October
	 */
	OCT(10), 
	/**
	 * November
	 */
	NOV(11), 
	/**
	 * December
	 */
	DEC(12);
	
	/**
	 * Ordinal value of abbreviated month name.
	 */
	private int ordinal;
	
	MonthNameAbbr(int n) {
		ordinal = n;
	}
	
	/** 
	 * Return the ordinal of the matched abbreviated month name.
	 * @return ordinal of the matched abbreviated month name
	 */
	public int getOrdinal() {
		return ordinal;
	}
}
