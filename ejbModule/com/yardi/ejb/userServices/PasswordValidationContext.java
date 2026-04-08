package com.yardi.ejb.userServices;

import java.util.Vector;

import com.yardi.ejb.Unique_Tokens;
import com.yardi.ejb.crypto.Jargon2Bean;
import com.yardi.shared.model.PasswordPolicyCopy;
import com.yardi.shared.userServices.PasswordStatistics;

/**
 * Container of objects required for password validation 
 */
public class PasswordValidationContext {
	/**
	 * User's current password
	 */
    private final String currentPassword;
    /**
     * User's new password
     */
    private final String newPassword;
    /**
     * User name
     */
    private final String userName;
    /**
     * User's hashed password history
     */
    private final Vector<Unique_Tokens> userTokens;
    /**
     * Compiled password statistics 
     */
    private final PasswordStatistics stats;
    /**
     * The immutable password policy from {@link com.yardi.ejb.PasswordPolicyBean#getPasswordPolicyCopy() PasswordPolicyBean.getPasswordPolicyCopy()}
     */
    private final PasswordPolicyCopy pwdPolicy;
    /**
     * Argon2 hashing. Required to enforce unique passwords
     */
    private final Jargon2Bean jargon2Bean;
    
    
	/**
	 * Constructor using all fields
	 * @param currentPassword user's current password
	 * @param newPassword user's new password
	 * @param userName user name
	 * @param userTokens user's hashed password history
	 * @param stats compiled statistics for the password
	 * @param pwdPolicy the immutable password policy
	 * @param jargon2Bean reference to {@link com.yardi.ejb.crypto.Jargon2Bean Jargon2Bean}
	 */
	public PasswordValidationContext(String currentPassword, String newPassword, String userName,
			Vector<Unique_Tokens> userTokens, PasswordStatistics stats,
			PasswordPolicyCopy pwdPolicy, Jargon2Bean jargon2Bean) {
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
		this.userName = userName;
		this.userTokens = userTokens;
		this.stats = stats;
		this.pwdPolicy = pwdPolicy;
		this.jargon2Bean = jargon2Bean;
	}

	/**
	 * Returns the user's current password 
	 * @return current password
	 */
	public String getCurrentPassword() {
		return currentPassword;
	}

	/**
	 * Returns the {@link com.yardi.ejb.crypto.Jargon2Bean Jargon2Bean} reference.  
	 * @return reference to <code>Jargon2Bean</code>
	 */
	public Jargon2Bean getJargon2Bean() {
		return jargon2Bean;
	}

	/**
	 * Returns the user's new password
	 * @return new password
	 */
	public String getNewPassword() {
		return newPassword;
	}


	/**
	 * Return the immutable password policy from 
     * {@link com.yardi.ejb.PasswordPolicyBean#getPasswordPolicyCopy() PasswordPoilcyBean.getPasswordPolicyCopy()}
	 * @return an immutable copy of password policy
	 */
	public PasswordPolicyCopy getPwdPolicy() {
		return pwdPolicy;
	}

	/**
	 * Returns a reference to compiled statistics about the new password 
	 * @return reference to {@link com.yardi.shared.userServices.PasswordStatistics PasswordStatistics}
	 */
	public PasswordStatistics getStats() {
		return stats;
	}

	/**
	 * Returns the user name
	 * @return user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Return user's hashed password history
	 * @return hashed password history
	 */
	public Vector<Unique_Tokens> getUserTokens() {
		return userTokens;
	}


	@Override
	public String toString() {
		return "PasswordValidationContext ["
				+ "\n    "
				+ "currentPassword=" 
				+ currentPassword 
				+ "\n    "
				+ "newPassword=" 
				+ newPassword
				+ "\n    "
				+ "userName=" 
				+ userName 
				+ "\n    "
				+ "userTokens=" 
				+ userTokens 
				+ "\n    "
				+ "stats="
				+ stats.toString()				
				+ "\n    "
				+ "pwdPolicy=" 
				+ pwdPolicy.toString() 
				+ "]";
	}
}
