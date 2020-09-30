package com.yardi.ejb;

import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.yardi.ejb.model.Pwd_Policy;

/**
 * Session Bean implementation class UniqueTokensBean
 */
@Stateless
public class UniqueTokensBean implements UniqueTokens {
	/*
	 * In the case of a RESOURCE_LOCAL, EntityManager.getTransaction().begin() and EntityManager.getTransaction().comit() 
	 * are allowed. Here, the container is managing begin and commit and an attempt to begin will throw Cannot use an 
	 * EntityTransaction while using JTA 
	 */
	@PersistenceContext(unitName="yardi")
	private EntityManager em;
	private Pwd_Policy pwdPolicy;
	@EJB PasswordPolicy passwordPolicyBean;

    public UniqueTokensBean() {
    	System.out.println("com.yardi.ejb.UniqueTokensBean UniqueTokensBean() 0015 ");
    }

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
		//debug
		System.out.println("com.yardi.ejb.UniqueTokensBean find() 0001 "
			+ "\n "
			+ "  rrn=" + rrn
			+ "\n "
			+ "  uniqueToken=" + uniqueToken
			);
		//debug
    	return uniqueToken;
    }

    public Vector<Unique_Tokens> findTokens(String userName) {
    	Vector<Unique_Tokens> userTokens = new Vector<Unique_Tokens>();
		TypedQuery<Unique_Tokens> qry = em.createQuery(
			  "SELECT t from Unique_Tokens t " 
			+ "WHERE t.up1UserName = :userName "
			+ "ORDER BY t.up1DateAdded DESC, t.up1Rrn DESC", 
			Unique_Tokens.class);
		userTokens = (Vector<Unique_Tokens>) qry
			.setParameter("userName", userName)
			.getResultList();
		//debug
		System.out.println("com.yardi.ejb.UniqueTokensBean findTokens() 0000 "
				+ "\n "
				+ "  userName=" + userName
				+ "\n "
				);
		System.out.println("com.yardi.ejb.UniqueTokensBean findTokens() 0005 ");
		for (Unique_Tokens t : userTokens) {
			System.out.println(
				  "\n "
				+ "  uniqueToken=" 
				+ t
				);
		}
		//debug
    	return userTokens;
    }

    private Pwd_Policy getPwdPolicy() {
		//debug
		System.out.println("com.yerdi.ejb.UniqieTokensBean getPwdPolicy() 0017 ");
		//debug

		if (pwdPolicy==null) {
			//debug
			System.out.println("com.yerdi.ejb.UniqieTokensBean getPwdPolicy() 0018 pwdPolicy==null ");
			//debug
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb.UniqueTokensBean getPwdPolicy() 000E "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			);
		//debug
		return pwdPolicy;
	} 
    
    public void persist(String userName, String token, java.util.Date dateAdded) {
		//debug
		System.out.println("com.yardi.ejb.UniqueTokensBean persist() 0011 "
			+ "\n "
			+ "  userName=" + userName
			+ "\n "
			+ "  token=" + token
			+ "\n "
			+ "  dateAdded=" + dateAdded
			);
		//debug
		em.persist(new Unique_Tokens(userName, token, dateAdded));
    }

    @PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.UniqueTokensBean postConstructCallback() 0016 ");
    	getPwdPolicy();
    }

	public void remove(long rrn) {
		//debug
		System.out.println("com.yardi.ejb.UniqueTokensBean remove() 0003 "
			+ "\n "
			+ "  rrn=" + rrn
			);
		//debug
		em.remove(em.find(Unique_Tokens.class, rrn));
    }

	/**
	 * Remove extra tokens in token history when the number of stored tokens exceeds the maximum defined in password 
	 * policy. This can happen if the maximum number of tokens to store is changed. 
	 * 
	 * @param userTokens
	 * @param uniqueToken
	 * @param maxUniqueTokens
	 * @param userName
	 */
	public Vector<Unique_Tokens> removeExtraTokens(Vector<Unique_Tokens> userTokens) {
    	//getPwdPolicy();
		short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
		
		if (pwdPolicy==null) {
			System.out.println("com.yardi.ejb.UniqueTokensBean removeExtraTokens() passwordPolicy==null 000C ");
		}
		
		//debug
		if (!(userTokens==null)) {
			System.out.println("com.yardi.ejb.UniqueTokensBean removeExtraTokens() 0006 "
					+ "\n"
					+ "   Vector<Unique_Tokens>="
					+ userTokens
					);
		}
		//debug
		
		if (maxUniqueTokens > 0) { // is unique passwords being enforced? 
			int nbrOfStoredTokens = 0;
			
			if (!(userTokens == null)) { // do they have any stored tokens?
				//debug
				System.out.println("com.yardi.ejb.UniqueTokensBean removeExtraTokens() 0007 ");
				for (Unique_Tokens u : userTokens) {
					System.out.println(
						  "\n"
						+ "   userTokens="
						+ u
						);
				}
				//debug
				nbrOfStoredTokens = userTokens.size();
				for(int i=maxUniqueTokens, tokenToRemove=maxUniqueTokens; i<nbrOfStoredTokens; i++) {
					//debug
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
					//debug
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
				//debug
				System.out.println("com.yardi.ejb.UniqueTokensBean removeExtraTokens() 0009 "
						+ "\n"
						+ "   Vector<Unique_Tokens>="
						+ userTokens
						);
				//debug
			}
		}
		return userTokens;
	}

	/**
	 * Remove the oldest stored token in token history so that the number of stored tokens in token history will not
	 * exceed the maximum defined in password policy when the new token is stored  
	 * 
	 * @param userTokens
	 * @param maxUniqueTokens
	 * @param uniqueToken
	 */
	public void removeOldestToken(Vector<Unique_Tokens> userTokens) {
		//debug
		System.out.println("com.yardi.ejb.UniqueTokensBean removeOldestToken() 0014 ");
		//debug
		short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
		
		if (pwdPolicy==null) {
			System.out.println("com.yardi.ejb.UniqueTokensBean removeOldestToken() passwordPolicy==null 000D ");
		}
		
		//debug
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
		//debug
		
		if (!(userTokens==null) && maxUniqueTokens > 0 && userTokens.size() >= maxUniqueTokens) { 
			/*
			 *  if there are stored tokens and
			 *  unique tokens is being enforced and
			 *  the number of stored tokens is greater or equal to the maximum number of unique tokens to store
			 *  then remove oldest token 
			 */
			//debug
			System.out.println("com.yardi.ejb.UniqueTokensBean removeOldestToken() 0013 ");
			//debug
			int tokenToRemove = maxUniqueTokens - 1;
			remove(userTokens.get(tokenToRemove).getUp1Rrn()); // Delete the extra row. A new row will be inserted 
			//debug
			System.out.println("com.yardi.ejb.UniqueTokensBean removeOldestToken() 000B "
					+ "\n"
					+ "   tokenToRemove="
					+ tokenToRemove
					+ "\n"
					+ "   userTokens.get(tokenToRemove).getUp1Rrn()="
					+ userTokens.get(tokenToRemove).getUp1Rrn());
			//debug
			userTokens.remove(tokenToRemove);

			//debug
			for (Unique_Tokens u : userTokens) {
				System.out.println(
					  "\n"
					+ "   uniqueToken="
					+ u
					);
			}
			//debug
		}
	}

	private void setPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb.UniqueTokensBean setPwdPolicy() 0012 ");
		//debug
		pwdPolicy = passwordPolicyBean.getPwdPolicy();
		
		if (pwdPolicy == null) {
			System.out.println("com.yardi.ejb.UniqueTokensBean setPwdPolicy() 000F pwdPolicy==null ");
			return;
		}
		//debug
		System.out.println("com.yardi.ejb.UniqueTokensBean setPwdPolicy() 0010 "
			+ "\n   "
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			);
		//debug
	}

    public String stringify() {
		return "UniqueTokensBean [emgr=" + em + "]"
				+ "\n  "
				+ this;
	}
    
	public int updateToken(Long up1Rrn, String up1Token, Long time) {
		//debug
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
		//debug
        Unique_Tokens t = em.find(Unique_Tokens.class, up1Rrn);
        t.setUp1Token(up1Token);
        t.setUp1DateAdded(new java.util.Date(time));
        return 1;
    }
}
