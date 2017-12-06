package com.yardi.QSECOFR;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Represents a request for a new token. 
 * @author Jim
 *
 */

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonPropertyOrder({"msgID", "msgDescription", "password"})
public class TokenRequest {
	private String password;
	@JsonIgnore
	private String passwordSave;
	private String msgID;
	private String msgDescription;
	
	public TokenRequest() {
	}

	public TokenRequest(String password, String msgID, String msgDescription) {
		this.password = password;
		this.msgID = msgID;
		this.msgDescription = msgDescription;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public String getMsgDescription() {
		return msgDescription;
	}

	public void setMsgDescription(String msgDescription) {
		this.msgDescription = msgDescription;
	}

	public String getPasswordSave() {
		return passwordSave;
	}

	public void setPasswordSave(String passwordSave) {
		this.passwordSave = passwordSave;
	}

	public char[] passwordToChar() {
		return password.toCharArray();
	}
	
	public String toString() {
		return "TokenRequest [password=" + password + ", passwordSave=" + passwordSave + ", msgID="
				+ msgID + ", msgDescription=" + msgDescription + "]";
	}
}
