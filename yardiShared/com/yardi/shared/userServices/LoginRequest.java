package com.yardi.shared.userServices;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * A container which represents the request by a user to login.<p>
 * Contains login credentials supplied by the user. Needed for mapping JSON 
 */

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class LoginRequest {
	/**
	 * User name
	 */
	private String userName;
	/**
	 * Password 
	 */
	private String password;
	/**
	 * New password. Supplied when user is changing their password.
	 */
	private String newPassword;
	/**
	 * Message ID
	 */
	private String msgID;
	/**
	 * Message description
	 */
	private String msgDescription;
	/**
	 * Change password indicator.<p>
	 * The value for this field comes from the web request and informs the application whether there is a change password request. This is the only way
	 * the application is aware that the password is being changed.
	 */
	private String chgPwd;
	/**
	 * Convenience boolean that indicates whether the user is requesting a password change.<p> 
	 * Value is based on the value of field <code>chgPwd</code>. 
	 */
	private boolean changePwd;
	/**
	 * Session ID.<p>
	 * Equivalent to <code>jakarta.servlet.http.HttpServletRequest.getSession().getId()</code>.
	 */
	private String sessionID;
	
	/**
	 * Default constructor
	 */
	public LoginRequest() {
	}

	/**
	 * Boolean indicating whether the user is requesting a password change. Provided for convenience.
	 * @return Boolean indicating whether password is being changed.
	 */
	public Boolean getChangePwd() {
		return changePwd;
	}
	
	/**
	 * The value from the change password form that indicates that the user is requesting a password change
	 * @return Value from the change password form indicating that user is requesting password change
	 */
	public String getChgPwd() {
		return chgPwd;
	}
	
	/**
	 * Return Message description
	 * @return Message description
	 */
	public String getMsgDescription() {
		return msgDescription;
	}
	
	/**
	 * Return Message ID
	 * @return Message ID
	 */
	public String getMsgID() {
		return msgID;
	}
	
	/**
	 * New password. Supplied when user is changing their password.
	 * @return New password.
	 */
	public String getNewPassword() {
		return newPassword;
	}
	
	/**
	 * Return password
	 * @return Password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Session ID.<p>
 	 * Equivalent to <code>jakarta.servlet.http.HttpServletRequest.getSession().getId()</code>. 
	 * @return Session ID.
	 */
	public String getSessionID() {
		return sessionID;
	}
	
	/**
	 * Return User name
	 * @return User name
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Set the convenience Boolean to inform the application whether the user is requesting a password change.<p>
	 * Pass the value of <code>getChgPwd()</code>. 
	 * @param s value of <code>getChgPwd()</code>
	 */
	public void setChangePwd(String s) {

		if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
			changePwd = Boolean.parseBoolean(s);
		} else {
			changePwd = false;
		}
	}
	
	/**
	 * Set change password String to the value of the given String.
	 * @param chgPwd value to set
	 */
	public void setChgPwd(String chgPwd) {
		this.chgPwd = chgPwd;
	}
	
	/**
	 * Set message description to the value of the given String.
	 * @param msgDescription message description value
	 */
	public void setMsgDescription(String msgDescription) {
		this.msgDescription = msgDescription;
	}
	
	/**
	 * Set the message ID to the value of the given String.
	 * @param msgID message ID value 
	 */
	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}
	
	/**
	 * Set new password to the value of the given String.
	 * @param newPassword new password value
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	/**
	 * Set password to the value of the given String.
	 * @param password password value
	 */
	public void setPassword(String password) {
		this.password = password;
	}	
	
	/**
	 * Set session ID to the value of the given String.<p> 
 	 * Equivalent to <code>jakarta.servlet.http.HttpServletRequest.getSession().getId()</code>.
	 * @param sessionID session ID 
	 */
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * Set user name to the value of the given String.
	 * @param userName user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "LoginRequest [userName=" + userName 
				+ ", password=" + password
				+ ", newPassword=" + newPassword 
				+ ", msgID=" + msgID
				+ ", msgDescription=" + msgDescription 
				+ ", chgPwd=" + chgPwd
				+ ", changePwd=" + changePwd 
				+ ", sessionID=" + sessionID
				+ "]";
	}
}
