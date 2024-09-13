package com.yardi.QSECOFR;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a request for a new token. 
 * @author Jim
 */

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonPropertyOrder({"msgID", "msgDescription", "password"})
public class TokenRequest {
	/**
	 * New password
	 */
	private String password;
	/**
	 * Saved password for checking that the password was changed
	 */
	@JsonIgnore
	private String passwordSave;
	/**
	 * Message ID
	 */
	private String msgID;
	/**
	 * Message description
	 */
	private String msgDescription;
	
	/**
	 * Default constructor
	 */
	public TokenRequest() {
	}

	/**
	 * Constructor using new password, message ID and message description
	 * @param password new password
	 * @param msgID message ID
	 * @param msgDescription message description
	 */
	public TokenRequest(String password, String msgID, String msgDescription) {
		this.password = password;
		this.msgID = msgID;
		this.msgDescription = msgDescription;
	}

	/**
	 * Return message description
	 * @return message description
	 */
	public String getMsgDescription() {
		return msgDescription;
	}

	/**
	 * Return message ID
	 * @return message ID
	 */
	public String getMsgID() {
		return msgID;
	}

	/**
	 * Return new password
	 * @return new password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Return saved password
	 * @return saved password
	 */
	public String getPasswordSave() {
		return passwordSave;
	}

	/**
	 * Return new password as <code>char[]</code>
	 * @return new password as <code>char[]</code>
	 */
	public char[] passwordToChar() {
		return password.toCharArray();
	}

	/**
	 * Set message description
	 * @param msgDescription message description
	 */
	public void setMsgDescription(String msgDescription) {
		this.msgDescription = msgDescription;
	}

	/**
	 * Set message ID
	 * @param msgID message ID
	 */
	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}
	
	/**
	 * Set new password
	 * @param password new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Set saved password
	 * @param passwordSave saved password
	 */
	public void setPasswordSave(String passwordSave) {
		this.passwordSave = passwordSave;
	}
	
	/**
	 * Return string containing all fields of this container
	 */
	public String toString() {
		return "TokenRequest [password=" + password + ", passwordSave=" + passwordSave + ", msgID="
				+ msgID + ", msgDescription=" + msgDescription + "]";
	}
}
