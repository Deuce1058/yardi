package com.yardi.ejb;

import java.util.Vector;

import jakarta.ejb.Local;

/**
 * Template for working with user tokens.
 */
@Local
public interface UniqueTokens {
    /**
     * Find the Unique_Tokens entity by relative record number.<p>
     * @param rrn relative record number of the entity to find. 
     * @return Unique_Tokens entity matching the given relative record number. Returns null if the persistence context does not contain a 
     * Unique_Tokens entity for the given relative record number and the UNIQUE_TOKENS database table has no row matching the given relative record number.
     */
	Unique_Tokens find(long rrn);
    /**
     *  Find all the tokens for the given userName.<p>
     *  
     *  The query result is ordered by date added (descending) and rrn (descending) because date 2020 (newer) &gt; 2019 and on the same day rrn 7 (newer) &gt; 3. 
     *  Thus the returned Vector is ordered newest to oldest.
     *  @param userName specifies which tokens will be found.
     *  @return Vector of Unique_Tokens entities matching the given userName. Returns an empty Vector if the persistence context contains no Unique_Token 
     *  entities for the given userName and the UNIQUE_TOKENS database table has no rows for the given userName.  
     */
	Vector<Unique_Tokens> findTokens(String userName);
	/**
	 * Persist a new Unique_Tokens entity constructed from the supplied parms.<p>
	 * @param userName user's name
	 * @param token hashed password
	 * @param dateAdded date token was added
	 */
	void persist(String userName, String token, java.util.Date dateAdded);
    /**
     * Remove Unique_Tokens entities by relative record number.<p>
     * @param rrn the relative record number of the entity to remove.
     */
	void remove(long rrn);
	/**
	 * Remove extra tokens in token history when the number of stored tokens exceeds the maximum defined in password 
	 * policy. This can happen if the maximum number of tokens to store is changed. 
	 * 
	 * @param userTokens Vector containing Unique_Tokens entities to remove. 
	 * @return Vector containing, at most, the maximum number of stored tokens the user can have.
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
	/**
	 * Remove the oldest stored token in token history so that the number of stored tokens in token history will not
	 * exceed the maximum defined in password policy when the new token is stored.<p>
	 * 
	 * When the user is changing their password, this is one of two times that tokens stored in UNIQUE_TOKENS database table are removed.<br><br>
	 * First, if the user has more stored tokens in UNIQUE_TOKENS than the maximum defined in password policy, then remove these extra tokens. 
	 * See <i>removeExtraTokens()</i>.<p>
	 * Second, this method will remove the oldest stored token so that when the next token is stored, the number of stored tokens the user has does not 
	 * exceed the maximum number of stored tokens as defined in password policy.<br><br>
	 * 
	 * @param userTokens Vector containing Unique_Token entities to remove.
	 */

	void removeOldestToken(Vector<Unique_Tokens> userTokens);
	/**
	 * Log the string representation of the class instance. 
	 * @return string representation of the class instance.
	 */
	String stringify();
    /**
     * Update the Unique_Tokens entity with the specified relative record number using the given parms.<p>
     * @param rrn relative record number to update.
     * @param token new hashed password value.
     * @param time new date added value in mills.
     * @return 1
     */
	int updateToken(Long rrn, String token, Long time);
	}
