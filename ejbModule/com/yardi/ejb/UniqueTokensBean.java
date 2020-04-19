package com.yardi.ejb;

import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

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
	private EntityManager emgr;
	private Pwd_Policy pwdPolicy;
	@EJB PasswordPolicy passwordPolicyBean;

    public UniqueTokensBean() {
    	System.out.println("com.yardi.ejb UniqueTokensSessionBean UniqueTokensBean() 0015");
    }

    @PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb UniqueTokensSessionBean postConstructCallback() 0016");
    	getPwdPolicy();
    }

    public Vector<Unique_Tokens> findTokens(String userName) {
    	Vector<Unique_Tokens> userTokens = new Vector<Unique_Tokens>();
		TypedQuery<Unique_Tokens> qry = emgr.createQuery(
			  "SELECT t from Unique_Tokens t " 
			+ "WHERE t.up1UserName = :userName "
			+ "ORDER BY t.up1DateAdded DESC, t.up1Rrn DESC", 
			Unique_Tokens.class);
		userTokens = (Vector<Unique_Tokens>) qry
			.setParameter("userName", userName)
			.getResultList();
		//debug
		System.out.println("com.yardi.ejb UniqueTokenSessionBean findTokens() 0000 "
				+ "\n "
				+ "  userName=" + userName
				+ "\n "
				);
		System.out.println("com.yardi.ejb UniqueTokenSessionBean findTokens() 0005");
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

    public Unique_Tokens find(long rrn) {
    	Unique_Tokens uniqueToken = null;
    	//uniqueToken = emgr.find(Unique_Tokens.class, rrn);
    	TypedQuery<Unique_Tokens> qry = emgr.createQuery(
    		  "SELECT t from Unique_Tokens "
    		+ "WHERE t.up1Rrn = :rrn",
    		Unique_Tokens.class
    		);
    	try {
			uniqueToken = qry
				.setParameter("rrn", rrn)
				.getSingleResult();
		} catch (Exception e) {
			System.out.println("com.yardi.ejb UniqueTokenSessionBean find() 0004 exception");
			e.printStackTrace();
		}
		//debug
		System.out.println("com.yardi.ejb UniqueTokenSessionBean find() 0001 "
			+ "\n "
			+ "  rrn=" + rrn
			+ "\n "
			+ "  uniqueToken=" + uniqueToken
			);
		//debug
    	return uniqueToken;
    } 
    
    public int persist(String userName, String token, java.util.Date dateAdded) {
		System.out.println("com.yardi.ejb UniqueTokenSessionBean persist() 0011");
    	Query qry = emgr.createNativeQuery("INSERT INTO DB2ADMIN.UNIQUE_TOKENS "
    		+ "(UP1_USER_NAME, UP1_TOKEN, UP1_DATE_ADDED) "
    		+ "VALUES(?, ?, ?)");
    	int rows = qry
    		.setParameter(1, userName)
    		.setParameter(2, token)
    		.setParameter(3, dateAdded, TemporalType.DATE)
    		.executeUpdate();
		//debug
		System.out.println("com.yardi.ejb UniqueTokenSessionBean persist() 0002 "
			+ "\n "
			+ "  userName=" + userName
			+ "\n "
			+ "  token=" + token
			+ "\n "
			+ "  dateAdded=" + dateAdded
			+ "\n "
			+ "  rows=" + rows
			);
		//debug
    	return rows;
    }

    public int remove(long rrn) {
    	Query qry = emgr.createQuery("DELETE FROM Unique_Tokens "
    		+ "WHERE up1Rrn = :rrn");
    	int rows = qry
    		.setParameter("rrn", rrn)
    		.executeUpdate();
		//debug
		System.out.println("com.yardi.ejb UniqueTokenSessionBean remove() 0003 "
			+ "\n "
			+ "  rrn=" + rrn
			+ "\n "
			+ "  rows=" + rows
			);
		//debug
    	return rows;
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
		Unique_Tokens uniqueToken;
		short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
		
		if (pwdPolicy==null) {
			System.out.println("com.yardi.ejb UniqueTokensSessionBean removeExtraTokens() passwordPolicy==null 000C");
		}
		
		//debug
		if (!(userTokens==null)) {
			System.out.println("com.yardi.ejb UniqueTokensSessionBean removeExtraTokens() 0006"
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
				System.out.println("com.yardi.ejb UniqueTokensSessionBean removeExtraTokens() 0007");
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
					System.out.println("com.yardi.ejb UniqueTokensSessionBean removeExtraTokens() 0008 "
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
					uniqueToken = userTokens.get(tokenToRemove);
					long rrn = uniqueToken.getUp1Rrn();
					remove(rrn); // Delete the extra row. A new row will be inserted 
					userTokens.remove(tokenToRemove);
					// The vector elements after the one that was removed move up and occupy the position that was removed
				}
				//debug
				System.out.println("com.yardi.ejb UniqueTokensSessionBean removeExtraTokens() 0009"
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
		System.out.println("com.yardi.ejb UniqueTokensSessionBean removeOldestToken() 0014");
		//debug
    	//getPwdPolicy();
		Unique_Tokens uniqueToken;
		short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
		
		if (pwdPolicy==null) {
			System.out.println("com.yardi.ejb UniqueTokensSessionBean removeOldestToken() passwordPolicy==null 000D");
		}
		
		//debug
		if (!(userTokens==null)) {
			System.out.println("com.yardi.ejb UniqueTokensSessionBean removeOldestToken() 000A"
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
			System.out.println("com.yardi.ejb UniqueTokensSessionBean removeOldestToken() 0013");
			//debug
			int tokenToRemove = maxUniqueTokens - 1;
			uniqueToken = userTokens.get(tokenToRemove);
      		long rrn = uniqueToken.getUp1Rrn();
			remove(rrn); // Delete the extra row. A new row will be inserted 
			userTokens.remove(tokenToRemove);
			//debug
			System.out.println("com.yardi.ejb UniqueTokensSessionBean removeOldestToken() 000B"
					+ "\n"
					+ "   tokenToRemove="
					+ tokenToRemove
					+ "\n"
					+ "   rrn="
					+ rrn);
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

	private Pwd_Policy getPwdPolicy() {
		
		if (pwdPolicy==null) {
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb UniqueTokensBean getPwdPolicy() 000E"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy
			);
		//debug
		return pwdPolicy;
	}

	private void setPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb UniqueTokensBean setPwdPolicy() 0012"
				+ "\n"
				+ "   passwordPolicyBean="
				+ passwordPolicyBean
				);
		//debug
		pwdPolicy = passwordPolicyBean.getPwdPolicy();
		
		if (pwdPolicy == null) {
			System.out.println("com.yardi.ejb UniqueTokensBean setPwdPolicy() pwdPolicy==null 000F");
			return;
		}
		//debug
		System.out.println("com.yardi.ejb UniqueTokensBean setPwdPolicy() 0010"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy
			);
		//debug
	}

    public int updateToken(Long rrn, String token) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("yardi");
        EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
        Query qry = em.createQuery("UPDATE Unique_Tokens " 
        + "SET up1token = :token "
        + "WHERE up1rrn = :rrn");
        int rows = qry
                .setParameter("token", token)
                .setParameter("rrn", rrn)
                .executeUpdate();
        return rows;
    }
    
    public int updateDateAdded(Long rrn, java.util.Date addDate) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("yardi");
        EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
        Query qry = em.createQuery("UPDATE Unique_Tokens " 
        + "SET up1DateAdded = :addDate "
        + "WHERE up1rrn = :rrn");
        int rows = qry
                .setParameter("addDate", addDate)
                .setParameter("rrn", rrn)
                .executeUpdate();
        return rows;
    }
    
	public String stringify() {
		return "UniqueTokensBean [emgr=" + emgr + "]"
				+ "\n  "
				+ this;
	}
}
