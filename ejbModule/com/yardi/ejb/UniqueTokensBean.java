package com.yardi.ejb;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.yardi.shared.helpdesk.PwdHistory;
import com.yardi.shared.model.PasswordPolicyCopy;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.metamodel.EntityType;

/**
 * Session Bean implementation of methods for working with user tokens. 
 */
@Stateless
public class UniqueTokensBean implements UniqueTokens {
	@PersistenceContext(unitName="yardi")
	private EntityManager em;
	/**
	 * Reference to com.yardi.ejb.model.Pwd_Policy entity. Although com.yardi.ejb.UniqueTokensBean is stateless, Pwd_Policy is a singleton 
	 * so it can be safely stored on the bean.
	 */
	private PasswordPolicyCopy pwdPolicy;
	/**
	 * Injected reference to EJB com.yardi.ejb.PasswordPolicyBean.
	 */
	@EJB PasswordPolicy passwordPolicyBean;

    public UniqueTokensBean() {
    	System.out.println("com.yardi.ejb.UniqueTokensBean UniqueTokensBean() 0015 ");
    }

    /**
     * Find the Unique_Tokens entity by relative record number.
     * @param rrn relative record number of the entity to find. 
     * @return Unique_Tokens entity matching the given relative record number. Returns null if the persistence context does not contain a 
     * Unique_Tokens entity for the given relative record number and the UNIQUE_TOKENS database table has no row matching the given relative record number.
     */
    @Override
    public Unique_Tokens find(long rrn) {
    	Unique_Tokens uniqueToken = null;
    	TypedQuery<Unique_Tokens> qry = em.createQuery(
    		  "SELECT t from Unique_Tokens "
    		+ "WHERE t.up1Rrn = :rrn",
    		Unique_Tokens.class
    		);
    	try {
			uniqueToken = qry
				.setParameter("rrn", rrn)
				.getSingleResult();
		} catch (Exception e) {
			System.out.println("com.yardi.ejb.UniqueTokensBean find() 0004 exception ");
			e.printStackTrace();
		}
		System.out.println("com.yardi.ejb.UniqueTokensBean find() 0001 "
			+ "\n "
			+ "  rrn=" + rrn
			+ "\n "
			+ "  uniqueToken=" + uniqueToken
			);
    	return uniqueToken;
    }
    
    /**
     *  Find all the tokens for the given userName.<p>
     *  
     *  The query result is ordered by date added (descending) and rrn (descending) because date 2020 (newer) &gt; 2019 and on the same day rrn 7 (newer) &gt; 3. 
     *  Thus the returned Vector is ordered newest to oldest.
     *  @param userName specifies which tokens will be found.
     *  @return Vector of Unique_Tokens entities matching the given userName. Returns an empty Vector if the persistence context contains no Unique_Token 
     *  entities for the given userName and the UNIQUE_TOKENS database table has no rows for the given userName.  
     */
    @Override
    public Vector<Unique_Tokens> findTokens(String userName) {
		System.out.println("com.yardi.ejb.UniqueTokensBean findTokens() 0002 ");
		isJoined();
    	Vector<Unique_Tokens> userTokens = new Vector<Unique_Tokens>();
		TypedQuery<Unique_Tokens> qry = em.createQuery(
			  "SELECT t from Unique_Tokens t " 
			+ "WHERE t.up1UserName = :userName "
			+ "ORDER BY t.up1DateAdded DESC, t.up1Rrn DESC", 
			Unique_Tokens.class);
		userTokens = (Vector<Unique_Tokens>) qry
			.setParameter("userName", userName)
			.getResultList();
		System.out.println("com.yardi.ejb.UniqueTokensBean findTokens() 0000 "
				+ "\n "
				+ "  userName=" + userName
				+ "\n "
				);

		for (Unique_Tokens t : userTokens) {
			System.out.println("com.yardi.ejb.UniqueTokensBean findTokens() 0005 "
				+  "\n "
				+ "  uniqueToken=" 
				+ t
				);
			isManaged(t);			
		}
    	return userTokens;
    }
	
    /**
	 * Find all the user's tokens and count the number of tokens for each date.<p>
	 * This list appears on the password reset page used by the help desk. It shows each time the user has reset their password and the number of times on each 
	 * day that the user reset their password 
	 * @param userID user ID
	 * @return List of the user's tokens with a count of the number of tokens there are on each date
	 */
    @Override
	public List<PwdHistory> findTokensWithCount(String userID) {
		System.out.println("com.yardi.ejb.UniqueTokensBean findTokensWithCount() 0021 ");
		isJoined();
		TypedQuery<PwdHistory> qry = em.createQuery(
				"SELECT NEW com.yardi.shared.helpdesk.PwdHistory(u.up1DateAdded, COUNT(u)) " + 
	    		"FROM Unique_Tokens u " + 
	    		"WHERE u.up1UserName = :userID " + 
	    		"GROUP BY u.up1DateAdded " +
	    		"ORDER BY u.up1DateAdded DESC ", 
	    		PwdHistory.class);
		qry.setParameter("userID", userID);
	    return qry.getResultList(); 
	}
			
	/**
     * Returns a reference to the Pwd_Policy entity from com.yardi.ejb.PasswordPolicyBean.<p>
     * 
     * postConstructCallback() calls this method. On the initial call the <i>pwdPolicy</i> field is null so setPwdPolicy() is called to obtain a
     * reference to password policy from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy().
     * 
     * @return reference to Pwd_Policy entity.
     */
	private PasswordPolicyCopy getPwdPolicy() {
		System.out.println("com.yerdi.ejb.UniqieTokensBean getPwdPolicy() 0017 ");

		if (pwdPolicy==null) {
			System.out.println("com.yerdi.ejb.UniqieTokensBean getPwdPolicy() 0018 pwdPolicy==null ");
			setPwdPolicy();
		}
		
		System.out.println("com.yardi.ejb.UniqueTokensBean getPwdPolicy() 000E "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			);
		return pwdPolicy;
	}

	/**
	 * Test whether the instance is an entity.
	 * 
	 * @param clazz the instance to test.
	 * @return boolean indicating whether the instance is an entity.
	 */
	private boolean isEntity(Class<?> clazz) {
		System.out.println("com.yardi.ejb.UniqueTokensBean.isEntity() 001C ");
	    boolean foundEntity = false;
	    Set<EntityType<?>> entities = em.getMetamodel().getEntities();
	    
	    for(EntityType<?> entityType :entities) {
	        Class<?> entityClass = entityType.getJavaType();
	        
	        if(entityClass.equals(clazz)) {
	            foundEntity = true;
	        }
	    }
	    
		System.out.println("com.yardi.ejb.UniqueTokensBean.isEntity() 001D " + foundEntity);
	    return foundEntity;
	}

    /**
     * Test whether the entity manager is participating in a transaction.
     * 
     * @return boolean indicating whether the entity manager is joined to the current transaction. 
     */
	private boolean isJoined() {
  		System.out.println("com.yardi.ejb.UniqueTokensBean isJoined() 001A "
  				+ "\n"
  				+ "   isJoined="
  				+ em.isJoinedToTransaction()
  				);
		return em.isJoinedToTransaction();
	} 
    
	/**
	 * Test whether the given Unique_Tokens entity managed.
	 * 
	 * @param token the entity to test.
	 * @return boolean indicating whether the Unique_Tokens entity is being managed. Returns false if the given instance is null or if the given 
	 * instance is not an entity.
	 */
	private boolean isManaged(Unique_Tokens token) {
  		System.out.println("com.yardi.ejb.UniqueTokensBean isManaged() 001B ");
  		
  		if (token==null) {
  	  		System.out.println(
  	  				  "com.yardi.ejb.UniqueTokensBean.isManaged() 001F "
	  				+ "\n"
	  				+ "   em.contains(Unique_Tokens)=false"
	  				);
	  		return false;
  		} 
  		
  		if (isEntity(token.getClass())==false) {
  	  		System.out.println(
	  				  "com.yardi.ejb.UniqueTokensBean.isManaged() 0020 "
	  				+ "\n"
	  				+ "   em.contains(sessionsTable)=false"
	  				);
	  		return false;
  		}

	  	System.out.println(
	  			  "com.yardi.ejb.UniqueTokensBean.isManaged() 001E "
	  			+ "\n"
	  			+ "   em.contains(Unique_Tokens)="
	  			+ em.contains(token)
	  			);
		return em.contains(token);  			
	}

    /**
	 * Persist a new Unique_Tokens entity constructed from the supplied parms.
	 * @param userName user's name
	 * @param token hashed password
	 * @param dateAdded date token was added
	 */
	@Override
    public void persist(String userName, String token, java.util.Date dateAdded) {
		System.out.println("com.yardi.ejb.UniqueTokensBean persist() 0011 "
			+ "\n "
			+ "  userName=" + userName
			+ "\n "
			+ "  token=" + token
			+ "\n "
			+ "  dateAdded=" + dateAdded
			);
		em.persist(new Unique_Tokens(userName, token, dateAdded));
    }

    @PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.UniqueTokensBean postConstructCallback() 0016 ");
    	getPwdPolicy();
    }

	/**
     * Remove Unique_Tokens entities by relative record number.
     * @param rrn the relative record number of the entity to remove.
     */
    @Override
	public void remove(long rrn) {
		System.out.println("com.yardi.ejb.UniqueTokensBean remove() 0003 "
			+ "\n "
			+ "  rrn=" + rrn
			);
		isJoined();
		Unique_Tokens t = em.find(Unique_Tokens.class, rrn);
		em.remove(t);
		isManaged(t);
    }

	/**
	 * Remove Unique_Tokens entities when the number of stored tokens the user has exceeds the maximum defined in password policy.<p>
	 * 
	 * Field <i>ppNbrUnique</i> in the Pwd_Policy entity defines the maximum number of stored tokens a user can have in UNIQUE_TOKENS database table. 
	 * If the number of stored tokens the user has exceeds this number, remove these extra tokens. 
	 * This can happen if the maximum number of tokens to store, as defined in password policy, is changed. 
	 * 
	 * @param userTokens Vector containing Unique_Tokens entities to remove. 
	 * @return Vector containing, at most, the maximum number of stored tokens the user can have.
	 */
	@Override
	public Vector<Unique_Tokens> removeExtraTokens(Vector<Unique_Tokens> userTokens) {
		short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
		
		if (pwdPolicy==null) {
			System.out.println("com.yardi.ejb.UniqueTokensBean removeExtraTokens() passwordPolicy==null 000C ");
		}
		
		if (!(userTokens==null)) {
			System.out.println("com.yardi.ejb.UniqueTokensBean removeExtraTokens() 0006 "
					+ "\n"
					+ "   Vector<Unique_Tokens>="
					+ userTokens
					);
		}
		
		if (maxUniqueTokens > 0) { // is unique passwords being enforced? 
			int nbrOfStoredTokens = 0;
			
			if (!(userTokens == null)) { // do they have any stored tokens?
				System.out.println("com.yardi.ejb.UniqueTokensBean removeExtraTokens() 0007 ");
				for (Unique_Tokens u : userTokens) {
					System.out.println(
						  "\n"
						+ "   userTokens="
						+ u
						);
				}
				nbrOfStoredTokens = userTokens.size();
				for(int i=maxUniqueTokens, tokenToRemove=maxUniqueTokens; i<nbrOfStoredTokens; i++) {
					System.out.println("com.yardi.ejb.UniqueTokensBean removeExtraTokens() 0008 "
							+ "\n "
							+ "   uniqueToken="
							+ userTokens.get(tokenToRemove).toString()
							+ "\n "
							+ "   rrn="
							+ userTokens.get(tokenToRemove).getUp1Rrn()
							+ "\n "
							+ "   i="
							+ i
							+ "\n "
							+ "   maxUniqueTokens="
							+ maxUniqueTokens
							+ "\n "
							+ "   nbrOfStoredTokens="
							+ nbrOfStoredTokens
							+ "\n "
							+ "   tokenToRemove="
							+ tokenToRemove
							);
					/*
					 * More tokens are being stored than the current max. These are extra rows
					 * First delete all of the extra tokens so that the check for unique tokens can just check all the 
					 * remaining tokens. 
					 * Next, remove the oldest stored token
					 * Finally, insert the new token
					 */
					remove(userTokens.get(tokenToRemove).getUp1Rrn()); // Delete the extra row. A new row will be inserted 
					userTokens.remove(tokenToRemove);
					// The vector elements after the one that was removed move up and occupy the position that was removed
				}
				System.out.println("com.yardi.ejb.UniqueTokensBean removeExtraTokens() 0009 "
						+ "\n"
						+ "   Vector<Unique_Tokens>="
						+ userTokens
						);
			}
		}
		return userTokens;
	}

	/**
	 * Remove the oldest stored token in token history so that the number of stored tokens in token history will not
	 * exceed the maximum defined in password policy when the new token is stored.<p>
	 * 
	 * When the user is changing their password, this is one of two times that tokens stored in UNIQUE_TOKENS database table are removed.<br><br>
	 * First, if the user has more stored tokens in UNIQUE_TOKENS than the maximum defined in password policy, then remove these extra tokens. 
	 * See removeExtraTokens().<br><br>
	 * Second, this method will remove the oldest stored token so that when the next token is stored, the number of stored tokens the user has does not 
	 * exceed the maximum number of stored tokens as defined in password policy.<br><br>
	 * Field <i>ppNbrUnique</i> in the Pwd_Policy entity defines the maximum number of stored tokens a user can have.  
	 * 
	 * 
	 * @param userTokens Vector containing Unique_Token entities to remove.
	 */
	@Override
	public void removeOldestToken(Vector<Unique_Tokens> userTokens) {
		System.out.println("com.yardi.ejb.UniqueTokensBean removeOldestToken() 0014 ");
		short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
		
		if (pwdPolicy==null) {
			System.out.println("com.yardi.ejb.UniqueTokensBean removeOldestToken() passwordPolicy==null 000D ");
		}
		
		if (!(userTokens==null)) {
			System.out.println("com.yardi.ejb.UniqueTokensBean removeOldestToken() 000A "
					+ "\n"
					+ "   maxUniqueTokens="
					+ maxUniqueTokens
					+ "\n"
					+ "   userTokens.size()="
					+ userTokens.size()
					);
			for (Unique_Tokens u : userTokens) {
				System.out.println(
						  "\n"
						+ "   userTokens="
						+ u
						);
			} 
		}
		
		if (!(userTokens==null) && maxUniqueTokens > 0 && userTokens.size() >= maxUniqueTokens) { 
			/*
			 *  if there are stored tokens and
			 *  unique tokens is being enforced and
			 *  the number of stored tokens is greater or equal to the maximum number of unique tokens to store
			 *  then remove oldest token 
			 */
			System.out.println("com.yardi.ejb.UniqueTokensBean removeOldestToken() 0013 ");
			int tokenToRemove = maxUniqueTokens - 1;
			remove(userTokens.get(tokenToRemove).getUp1Rrn()); // Delete the extra row. A new row will be inserted 
			System.out.println("com.yardi.ejb.UniqueTokensBean removeOldestToken() 000B "
					+ "\n"
					+ "   tokenToRemove="
					+ tokenToRemove
					+ "\n"
					+ "   userTokens.get(tokenToRemove).getUp1Rrn()="
					+ userTokens.get(tokenToRemove).getUp1Rrn());
			userTokens.remove(tokenToRemove);

			for (Unique_Tokens u : userTokens) {
				System.out.println(
					  "\n"
					+ "   uniqueToken="
					+ u
					);
			}
		}
	}
	
	/**
	 * Set Pwd_Policy entity to the reference obtained from com.yardi.ejb.PasswordPoilcyBean
	 */
	private void setPwdPolicy() {
		System.out.println("com.yardi.ejb.UniqueTokensBean setPwdPolicy() 0012 ");
		pwdPolicy = passwordPolicyBean.getPasswordPolicyCopy();
		
		if (pwdPolicy == null) {
			System.out.println("com.yardi.ejb.UniqueTokensBean setPwdPolicy() 000F pwdPolicy==null ");
			return;
		}
		System.out.println("com.yardi.ejb.UniqueTokensBean setPwdPolicy() 0010 "
			+ "\n   "
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			);
	}
    
    /**
	 * Log the string representation of the class instance. 
	 * @return string representation of the class instance.
	 */
	@Override
    public String stringify() {
		return "UniqueTokensBean [emgr=" + em + "]"
				+ "\n  "
				+ this;
	}
	
	/**
     * Update the Unique_Tokens entity with the specified relative record number using the given parms.
     * @param up1Rrn relative record number to update.
     * @param up1Token new hashed password value.
     * @param time new date added value in mills.
     * @return 1
     */
	public int updateToken(Long up1Rrn, String up1Token, Long time) {
		System.out.println("com.yardi.ejb.UniqueTokensBean updateToken() 0019 "
				+ "\n "
				+ "   up1rrn="
				+ up1Rrn
				+ "\n "
				+ "   up1Token="
				+ up1Token
				+ "\n "
				+ "   time="
				+ time
				);
        Unique_Tokens t = em.find(Unique_Tokens.class, up1Rrn);
        t.setUp1Token(up1Token);
        t.setUp1DateAdded(new java.util.Date(time));
        return 1;
    } 
}
