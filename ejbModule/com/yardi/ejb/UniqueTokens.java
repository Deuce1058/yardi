package com.yardi.ejb;

import java.util.Vector;

import javax.ejb.Remote;

@Remote
public interface UniqueTokens {
	Unique_Tokens find(long rrn);
	int persist(String userName, String token, java.util.Date dateAdded);
	int remove(long rrn);
	Vector<Unique_Tokens> findTokens(String userName);
	String stringify();
	/**
	 * Remove extra tokens in token history when the number of stored tokens exceeds the maximum defined in password 
	 * policy. This can happen if the maximum number of tokens to store is changed. 
	 * 
	 * @param userTokens
	 * @param uniqueToken
	 * @param maxUniqueTokens
	 * @param userName
	 */
	Vector<Unique_Tokens> removeExtraTokens(Vector<Unique_Tokens> userTokens);
	/**
	 * Remove the oldest stored token in token history so that the number of stored tokens in token history will not
	 * exceed the maximum defined in password policy when the new token is stored  
	 * 
	 * @param userTokens
	 * @param maxUniqueTokens
	 * @param uniqueToken
	 */
	void removeOldestToken(Vector<Unique_Tokens> userTokens);
	}
