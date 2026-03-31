package com.yardi.ejb.userServices;

import java.util.Vector;

import com.yardi.ejb.Unique_Tokens;
import com.yardi.ejb.crypto.Jargon2Bean;
import com.yardi.shared.model.PasswordPolicyCopy;
import com.yardi.shared.userServices.PasswordStatistics;

public class PasswordValidationContext {
    private final String currentPassword;
    private final String newPassword;
    private final String userName;
    private final Vector<Unique_Tokens> userTokens;
    private final PasswordStatistics stats;
    private final PasswordPolicyCopy pwdPolicy;
    private final Jargon2Bean jargon2Bean;
    
    
	/**
	 * @param currentPassword
	 * @param newPassword
	 * @param userName
	 * @param userTokens
	 * @param stats
	 * @param pwdPolicy
	 * @param jargon2Bean reference to com.yardi.ejb.crypto.Jargon2bean
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


	public String getCurrentPassword() {
		return currentPassword;
	}


	public Jargon2Bean getJargon2Bean() {
		return jargon2Bean;
	}


	public String getNewPassword() {
		return newPassword;
	}


	public PasswordPolicyCopy getPwdPolicy() {
		return pwdPolicy;
	}


	public PasswordStatistics getStats() {
		return stats;
	}


	public String getUserName() {
		return userName;
	}


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
