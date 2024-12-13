package com.yardi.ejb.helpdesk;

import com.yardi.shared.helpdesk.ResetPwdRequest;

/**
 * Interface for password reset controller
 */
public interface PwdResetCtrl {
	/**
	 * Find user profile details in database table USER_PROFILE and their password history from database table UNIQUE_TOKENS. The details are displayed on the 
	 * password reset page used by the help desk.  
	 * @return the response to a reset password request 
	 */
	ResetPwdRequest findUserDetails();
	/**
	 * Clients use this method to get feedback from the most recent operation that supplied feedback
	 * @return feedback from the most recent operation that supplies feedback
	 */
	String getFeedback();
	/** 
	 * remove bean
	 */
	void remove();
	/**
	 * Handle the password reset request. Tasks required to set a temporary password with a life defined in password policy.
	 * @return the response to a password reset request
	 */
	ResetPwdRequest resetPwd();
	/**
	 * Inject the password reset request
	 * @param resetPwdRequest a password reset request
	 */
	void setResetPwdRequest(ResetPwdRequest resetPwdRequest);
}
