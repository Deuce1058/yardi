package com.yardi.ejb;

import javax.ejb.Remote;

@Remote
public interface UserProfileSessionBeanRemote {
	UserProfile find(String userName);
	int setUpPwdAttempts(String userName, short pwdAttempts);
	int disable(String userName, java.sql.Timestamp disabledDate, short pwdAttempts);
	int loginSuccess(String userName);
	int changeUserToken(String userName, String token, java.util.Date pwdExpirationDate);
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
	String stringify();
}
