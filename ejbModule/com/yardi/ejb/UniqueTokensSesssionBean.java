package com.yardi.ejb;

import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

/**
 * Session Bean implementation class UniqueTokensSesssionBean
 */
@Stateless
@LocalBean
public class UniqueTokensSesssionBean implements UniqueTokensSesssionBeanRemote {
	/*
	 * In the case of a RESOURCE_LOCAL, EntityManager.getTransaction().begin() and EntityManager.getTransaction().comit() 
	 * are allowed. Here, the container is managing begin and commit and an attempt to begin will throw Cannot use an 
	 * EntityTransaction while using JTA 
	 */
	@PersistenceContext(unitName="yardi")
	private EntityManager emgr;

    /**
     * Default constructor. 
     */
    public UniqueTokensSesssionBean() {
    }

    public Vector<UniqueTokens> findTokens(String userName) {
    	Vector<UniqueTokens> userTokens = new Vector<UniqueTokens>();
		TypedQuery<UniqueTokens> qry = emgr.createQuery(
			  "SELECT t from UniqueTokens t " 
			+ "WHERE t.up1UserName = :userName "
			+ "ORDER BY t.up1DateAdded DESC, t.up1Rrn DESC", 
			UniqueTokens.class);
		userTokens = (Vector<UniqueTokens>) qry
			.setParameter("userName", userName)
			.getResultList();
		//debug
		System.out.println("com.yardi.ejb UniqueTokenSessionBean findTokens() 0000 "
				+ "\n "
				+ "  userName=" + userName
				+ "\n "
				);
		System.out.println("com.yardi.ejb UniqueTokenSessionBean findTokens() 0005");
		for (UniqueTokens t : userTokens) {
			System.out.println(
				  "\n "
				+ "  uniqueToken=" 
				+ t
				);
		}
		//debug
    	return userTokens;
    }

    public UniqueTokens find(long rrn) {
    	UniqueTokens uniqueToken = null;
    	//uniqueToken = emgr.find(UniqueTokens.class, rrn);
    	TypedQuery<UniqueTokens> qry = emgr.createQuery(
    		  "SELECT t from UniqueTokens "
    		+ "WHERE t.up1Rrn = :rrn",
    		UniqueTokens.class
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
    	Query qry = emgr.createQuery("DELETE FROM UniqueTokens "
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

	public String stringify() {
		return "UniqueTokensSesssionBean [emgr=" + emgr + "]"
				+ "\n  "
				+ this;
	}
}
