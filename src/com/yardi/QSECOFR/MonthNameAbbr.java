package com.yardi.QSECOFR;

/**
 * Enum for converting an abbreviated month name to its ordinal value
 * @author Jim
 *
 */
public enum MonthNameAbbr {
	JAN(1),
	FEB(2),
	MAR(3),
	APR(4), 
	MAY(5), 
	JUN(6), 
	JUL(7), 
	AUG(8), 
	SEP(9), 
	OCT(10), 
	NOV(11), 
	DEC(12);
	
	private int ordinal;
	
	MonthNameAbbr(int n) {
		ordinal = n;
	}
	
	int getOrdinal() {
		return ordinal;
	}
}
