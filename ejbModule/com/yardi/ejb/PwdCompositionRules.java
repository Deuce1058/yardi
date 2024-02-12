package com.yardi.ejb;

import java.util.Vector;

import jakarta.ejb.Local;

/**
 * Enforce password policy complexity and reuse rules.
 */
@Local
public interface PwdCompositionRules {
    /**
     * Enforce the password policy on the new password<p>
     * <strong>The following feedback is provided:</strong><br>
     * <span style="font-family:consolas;">  YRD0000 Process completed normally</span><br>
     * <span style="font-family:consolas;">  YRD0005 Password must be at least %n characters long</span><br>
     * <span style="font-family:consolas;">  YRD0006 Password must contain at least 1 upper case</span><br>
     * <span style="font-family:consolas;">  YRD0007 Password must contain at least 1 lower case</span><br>
     * <span style="font-family:consolas;">  YRD0008 Password must contain at least 1 number</span><br>
     * <span style="font-family:consolas;">  YRD0009 Password must contain at least 1 special character</span><br>
     * <span style="font-family:consolas;">  YRD000A Password matches a password that was previously used</span><br>
     * <span style="font-family:consolas;">  YRD000B Password policy is missing</span><br>
     * <span style="font-family:consolas;">  YRD0010 New password must not contain current password</span><br>
     * <span style="font-family:consolas;">  YRD0011 New password must not contain user name in any case</span><br>
     * <span style="font-family:consolas;">  YRD0015 Password must not be longer than %n characters</span><br>
     * <span style="font-family:consolas;">  YRD0016 Password contains more than %n repeated characters</span><br>
     * <span style="font-family:consolas;">  YRD0017 Password must contain at least %n numbers</span><br>
     * <span style="font-family:consolas;">  YRD0018 Password must contain at least %n upper case characters</span><br>
     * <span style="font-family:consolas;">  YRD0019 Password must contain at least %n lower case characters</span><br>
     * <span style="font-family:consolas;">  YRD001A Password must contain at least %n special characters</span>
     * @param password new password in plain text
     * @param userName user ID
     * @param userToken current hashed password 
     * @param userTokens all of the stored tokens for the user
     * @return boolean indicating whether new password conforms to password policy
     */
	boolean enforce(final String password, final String userName, final String userToken, final Vector<Unique_Tokens> userTokens);
	/**
	 * Returns the status of the most recent method call that provides feedback.<p>
	 * Clients call <i>getFeedback()</i> to determine the status of the most recent method call that provides feedback.
	 * @return feedback from the most recent method call that provides feedback.
	 */
	String getFeedback();
	/**
	 * Stateful session bean remove method.<p>
	 * Clients call this method so that com.yardi.ejb.PwdCompositionRulesBean can release resources it has before being removed.
	 */
	void removeBean();
}
