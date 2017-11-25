package com.yardi.ejb;

import java.util.ArrayList;
import java.util.Vector;

import javax.ejb.Remote;
import javax.persistence.EntityManager;

@Remote
public interface UniqueTokensSesssionBeanRemote {
	UniqueToken find(long rrn);
	int persist(String userName, String token, java.util.Date dateAdded);
	int remove(long rrn);
	Vector<UniqueToken> findTokens(String userName);
	String stringify();
	int persist(
		String userId, 
		String token,
		int homeMarket,
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
		java.util.Date disabledDate,
		java.util.Date lastLoginDate,
		int pwdAttempts
		);
}
