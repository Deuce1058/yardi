package com.yardi.userServices;

public class InvalidSessionException extends Exception {
	private static final long serialVersionUID = 1L;
	private String expected;
	private String found;

	public InvalidSessionException(String expected, String found) {
		this.expected = expected;
		this.found = found;
		System.out.println("com.yardi.userServices InvalidSessionException()");
	}

	@Override
	public String toString() {
		return com.yardi.rentSurvey.YardiConstants.YRD0013 
			+ "\n"
			+ "   Expceted: "
			+ expected
			+ "    Found: "
			+ found;
	}

}
