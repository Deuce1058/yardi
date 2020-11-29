package com.yardi.ejb;

import javax.ejb.Remote;

import com.yardi.ejb.model.Login_User_Profile;

@Remote
public interface LoginUserProfile {
	boolean authenticate(String userName, String password, boolean userIsChangingPassword);
	void changeUserToken(final char [] newPassword);
	String getFeedback();
	Login_User_Profile getUserProfile();
	void loginSuccess();
	void removeBean();
	void setUpPwdAttempts(short pwdAttempts);
	void setUserProfile(Login_User_Profile userProfile);
}
