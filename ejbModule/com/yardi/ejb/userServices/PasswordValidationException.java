package com.yardi.ejb.userServices;

public class PasswordValidationException extends Exception {
	private final PasswordRule failedRule;
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 1L;

	public PasswordValidationException(PasswordRule failedRule, String message) {
		super(message);
		 this.failedRule = failedRule;
	}
	
    public PasswordRule getFailedRule() {
        return failedRule;
    }

    public String getMsgId() {
        return failedRule.getMsgId();
    }
}
