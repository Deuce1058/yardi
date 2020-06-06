package com.yardi.userServices;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * Container for login credentials supplied by the user. Needed for mapping JSON 
 * @author Jim
 *
 */

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class LoginRequest {
	private String userName;
	private String password;
	private String newPassword;
	private String msgID;
	private String msgDescription;
	private String chgPwd;
	private boolean changePwd;
	private String sessionID;
	
	public LoginRequest() {
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
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
	public String getChgPwd() {
		return chgPwd;
	}
	public void setChgPwd(String chgPwd) {
		this.chgPwd = chgPwd;
	}
	public Boolean getChangePwd() {
		return changePwd;
	}
	
	void setChangePwd(String s) {

		if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
			changePwd = Boolean.parseBoolean(s);
		} else {
			changePwd = false;
		}
	}	
	
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
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
