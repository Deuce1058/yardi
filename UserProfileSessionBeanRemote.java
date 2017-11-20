package com.yardi.ejb;

import javax.ejb.Remote;

@Remote
public interface UserProfileSessionBeanRemote {
	UserProfile find(String userName);
	int setUpPwdAttempts(String userName, short pwdAttempts);
	int disable(String userName, java.sql.Timestamp disabledDate, short pwdAttempts);
	int loginSuccess(String userName);
	int changeUserToken(String userName, String token, java.util.Date pwdExpirationDate);
	String stringify();
}
