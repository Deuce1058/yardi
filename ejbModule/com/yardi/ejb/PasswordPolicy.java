package com.yardi.ejb;

import jakarta.ejb.Local;

import com.yardi.ejb.model.Pwd_Policy;

/**
 * Template for working with password policy.
 */
@Local
public interface PasswordPolicy {
    /**
     * Retrieve password policy from the PWD_POLICY database table.
     * 
     * <pre style="font-family:times;">
     * Policy consists of:
     *   1 Complexity
     *      1.1 Upper case required Y/N
     *          1.1.1 Number of upper case required
     *      1.2 Lower case required Y/N
     *          1.2.1 Number of lower case required
     *      1.3 Special characters required Y/N
     *          1.3.1 Number of special characters required
     *      1.4 Digits required Y/N
     *          1.4.1 Number of digits required
     *      1.5 Cant contain user name
     *      1.6 Cant contain current password
     *      1.7 Maximum number of repeated characters
     *   2 Password length
     *      2a minimum length
     *      2b maximum length
     *   3 Number of days before password must be changed
     *   4 Number of unique passwords required specifies how many unique tokens are stored per user. This rule is applied when changing 
     *      the password and causes the new hashed password to be checked against token history for a match. The user is prevented from 
     *      reusing passwords
     *   5 Maximum number of login attempts allowed since the most recent successful login. When the number of invalid logins reaches this number
     *      the User_Profile entity is disabled preventing any additional login attempts until the password is reset by an admin
     * </pre>
     * 
     * These rules are applied when logging in and when changing the password either on demand or because the password expired. 
	 * 
	 * @param rrn the relative record number to find in the PWD_POLICY database table
	 * @return a Pwd_Policy entity
	 */
	Pwd_Policy find(Long rrn);
	/**
	 * Returns the status of the most recent method call that provides feedback.<p>
	 * Clients call <i>getFeedback()</i> to determine the status of the most recent method call that provides feedback.
	 * @return feedback from the most recent method call that provides feedback.
	 */
	String getFeedback();
	/**
	 * Return a reference to the Pwd_Policy entity.
	 * @return reference to the Pwd_Policy entity
	 */
	Pwd_Policy getPwdPolicy();
	String stringify();
}
