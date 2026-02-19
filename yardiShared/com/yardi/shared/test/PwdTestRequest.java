package com.yardi.shared.test;

public class PwdTestRequest {
	private String msgID; //out
	private String msgDescription; //out
	private String pwdCompositionRulesBeanStatus; //out
	private String userName; //in
	private String password; //in
	private String newPassword; //in
	private String token; //out

	public PwdTestRequest() {
	}

	public PwdTestRequest(String msgID, String msgDescription, String pwdCompositionRulesBeanStatus, String password, String newPassword) {
		this.msgID = msgID;
		this.msgDescription = msgDescription;
		this.pwdCompositionRulesBeanStatus = pwdCompositionRulesBeanStatus;
		this.password = password;
		this.newPassword = newPassword;
	}

	public String getMsgDescription() {
		return msgDescription;
	}

	public String getMsgID() {
		return msgID;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public String getPassword() {
		return password;
	}
	
	public String getPwdCompositionRulesBeanStatus() {
		return pwdCompositionRulesBeanStatus;
	}

	public String getToken() {
		return token;
	}

	public String getUserName() {
		return userName;
	}

	public char[] passwordToChar() {
		return password.toCharArray();
	}

	public void setMsgDescription(String msgDescription) {
		this.msgDescription = msgDescription;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPwdCompositionRulesBeanStatus(String pwdCompositionRulesBeanStatus) {
		this.pwdCompositionRulesBeanStatus = pwdCompositionRulesBeanStatus;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "PwdTestRequest [msgID=" + msgID + ", msgDescription=" + msgDescription
				+ ", pwdCompositionRulesBeanStatus=" + pwdCompositionRulesBeanStatus + ", userName=" + userName
				+ ", password=" + password + ", newPassword=" + newPassword + ", token=" + token + "]";
	}
	
}
