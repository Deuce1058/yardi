package com.yardi.userServices;

import com.yardi.ejb.PasswordPolicySessionBeanRemote;
import com.yardi.ejb.UniqueTokensSesssionBeanRemote;
import com.yardi.ejb.UserProfileSessionBeanRemote;

public class LoginResponse {
	private String userName;
	private String password;
	private String newPassword;
	private String msgID;
	private String msgDescription;

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
}
