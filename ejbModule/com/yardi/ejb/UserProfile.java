package com.yardi.ejb;

import jakarta.ejb.Local;

import com.yardi.ejb.model.User_Profile;

@Local
public interface UserProfile {
	boolean authenticate(String userName, String password, boolean userIsChangingPassword);
	void changeUserToken(final char [] newPassword);
    /**
     * Return the User_Profile entity specified by userName or null if it is not in the persistence context or the database
     * @param userName specifies the entity ID
     * @return a User_Profile entity 
     */
	public User_Profile find(String userName);
	String getFeedback();
	User_Profile getUserProfile();
	void loginSuccess();
	void removeBean();
	void setUpPwdAttempts(short pwdAttempts);
	void setUserProfile(User_Profile userProfile);
}
