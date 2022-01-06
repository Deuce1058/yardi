package com.yardi.ejb;

import jakarta.ejb.Local;

@Local
public interface UserProfile {
	boolean authenticate(String userName, String password, boolean userIsChangingPassword);
	int changeUserToken(String userName, char [] newPassword);
	User_Profile find(String userName);
	String getFeedback();
	int loginSuccess(String userName);
	int persist(
			String userId, 
			String token,
			short homeMarket,
			String firstName,
			String lastName,
			String address1,
			String address2,
			String city,
			String state,
			String zip,
			String zip4,
			String phone,
			String fax,
			String email,
			String ssn,
			java.util.Date birthdate,
			String activeYN,
			java.util.Date passwordExpirationDate,
			java.sql.Timestamp disabledDate,
			java.sql.Timestamp lastLoginDate,
			short pwdAttempts
			);
	int remove(String userID);
	void removeBean();
	int setUpPwdAttempts(String userName, short pwdAttempts);
	String stringify();
	int updateAll(
			String userId, 
			String token,
			short homeMarket,
			String firstName,
			String lastName,
			String address1,
			String address2,
			String city,
			String state,
			String zip,
			String zip4,
			String phone,
			String fax,
			String email,
			String ssn,
			java.util.Date birthdate,
			String activeYN,
			java.util.Date passwordExpirationDate,
			java.sql.Timestamp disabledDate,
			java.sql.Timestamp lastLoginDate,
			short pwdAttempts
			);
}
