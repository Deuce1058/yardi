package com.yardi.shared.userServices;

/**
 * A container which holds feedback data in response to a login request.<p>
 * A JSON object is constructed from the field values and returned to the browser. 
 */
public class LoginResponse {
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

	public LoginResponse() {
	}

	/**
	 * Constructor using all fields
	 * @param userName User name
	 * @param password Password
	 * @param newPassword New password. Supplied when user is changing their password.
	 * @param msgID Message ID
	 * @param msgDescription Message description
	 */
	public LoginResponse(String userName, String password, String newPassword,
			String msgID, String msgDescription) {
		this.userName = userName;
		this.password = password;
		this.newPassword = newPassword;
		this.msgID = msgID;
		this.msgDescription = msgDescription;
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
	 * Return New password. Supplied when user is changing their password.
	 * @return New password.
	 */
	public String getNewPassword() {
		return newPassword;
	}
	
	/**
	 * Return Password
	 * @return Password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Return User name
	 * @return User name
	 */
	public String getUserName() {
		return userName;
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
	 * Set user name to the value of the given String.
	 * @param userName user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "LoginResponse [userName=" + userName + ", password=" + password
				+ ", newPassword=" + newPassword + ", msgID=" + msgID
				+ ", msgDescription=" + msgDescription + "]";
	}
}
