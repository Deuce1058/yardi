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
		System.out.println("com.yardi.shared.userServices.LoginRequest() 0000 ");
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
		System.out.println("com.yardi.shared.userServices.LoginRequest.setChangePwd() 0001 ");

		if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
			System.out.println("com.yardi.shared.userServices.LoginRequest.setChangePwd() 0002 ");
			changePwd = Boolean.parseBoolean(s);
		} else {
			System.out.println("com.yardi.shared.userServices.LoginRequest.setChangePwd() 0003 ");
			changePwd = false;
		}
	}
	
	/**
	 * Set change password String to the value of the given String.
	 * @param chgPwd value to set
	 */
	public void setChgPwd(String chgPwd) {
		System.out.println("com.yardi.shared.userServices.LoginRequest.setChgPwd() 0004 ");
		this.chgPwd = chgPwd;
	}
	
	/**
	 * Set message description to the value of the given String.
	 * @param msgDescription message description value
	 */
	public void setMsgDescription(String msgDescription) {
		System.out.println("com.yardi.shared.userServices.LoginRequest.setMsgDescription() 0005 ");
		this.msgDescription = msgDescription;
	}
	
	/**
	 * Set the message ID to the value of the given String.
	 * @param msgID message ID value 
	 */
	public void setMsgID(String msgID) {
		System.out.println("com.yardi.shared.userServices.LoginRequest.setMsgID() 0006 ");
		this.msgID = msgID;
	}
	
	/**
	 * Set new password to the value of the given String.
	 * @param newPassword new password value
	 */
	public void setNewPassword(String newPassword) {
		System.out.println("com.yardi.shared.userServices.LoginRequest.setNewPassword() 0007 ");
		this.newPassword = newPassword;
	}
	
	/**
	 * Set password to the value of the given String.
	 * @param password password value
	 */
	public void setPassword(String password) {
		System.out.println("com.yardi.shared.userServices.LoginRequest.setPassword() 0008 ");
		this.password = password;
	}	
	
	/**
	 * Set session ID to the value of the given String.<p> 
 	 * Equivalent to <code>jakarta.servlet.http.HttpServletRequest.getSession().getId()</code>.
	 * @param sessionID session ID 
	 */
	public void setSessionID(String sessionID) {
		System.out.println("com.yardi.shared.userServices.LoginRequest.setSessionID() 0009 ");
		this.sessionID = sessionID;
	}

	/**
	 * Set user name to the value of the given String.
	 * @param userName user name
	 */
	public void setUserName(String userName) {
		System.out.println("com.yardi.shared.userServices.LoginRequest.setUserName() 000A ");
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
