package com.yardi.userServices;

/**
 * An invalid session exception is thrown when the session ID stored as a session attribute does not match the session ID of the incoming request.   
 */
public class InvalidSessionException extends Exception {
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Expected session ID
	 */
	private String expected;
	/**
	 * Found session ID
	 */
	private String found;

	/**
	 * Constructor using the expected session ID and the found session ID
	 * @param expected the expected session ID
	 * @param found the found session ID
	 */
	public InvalidSessionException(String expected, String found) {
		this.expected = expected;
		this.found = found;
		System.out.println("com.yardi.userServices InvalidSessionException()");
	}

	/**
	 * Show expected and found session ID for logging
	 */
	@Override
	public String toString() {
		return com.yardi.shared.rentSurvey.YardiConstants.YRD0013 
			+ "\n"
			+ "   Expceted: "
			+ expected
			+ "    Found: "
			+ found;
	}

}
