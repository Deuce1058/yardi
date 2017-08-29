package com.yardi.rentSurvey;

/**
 * Container for login credentials supplied by the user. Needed for mapping JSON 
 * @author Jim
 *
 */

public class LoginData {
	private String userName;
	private String password;
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
	@Override
	public String toString() {
		return "LoginData [userName=" + userName + ", password=" + password
				+ "]";
	}
}
