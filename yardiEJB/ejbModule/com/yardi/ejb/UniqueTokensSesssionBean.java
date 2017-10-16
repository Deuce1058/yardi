package com.yardi.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.TemporalType;

/**
 * Session Bean implementation class UniqueTokensSesssionBean
 */
@Stateless
public class UniqueTokensSesssionBean implements UniqueTokensSesssionBeanRemote {
	/*
	 * In the case of a RESOURCE_LOCAL, EntityManager.getTransaction().begin() and EntityManager.getTransaction().comit() 
	 * are allowed. Here, the container is managing begin and commit and an attempt to begin will throw Cannot use an 
	 * EntityTransaction while using JTA 
	 */
	@PersistenceContext(name="yardiEJB")
	private EntityManager emgr;

    /**
     * Default constructor. 
     */
    public UniqueTokensSesssionBean() {
    }

    public Vector<UniqueToken> findTokens(String userName) {
    	Vector<UniqueToken> userTokens = new Vector<UniqueToken>();
		TypedQuery<UniqueToken> qry = emgr.createQuery(
			  "SELECT t from UniqueToken t " 
			+ "WHERE t.up1UserName = :userName "
			+ "ORDER BY t.up1DateAdded, t.up1Rrn", 
			UniqueToken.class);
		userTokens = (Vector<UniqueToken>) qry
			.setParameter("userName", userName)
			.getResultList();
    	return userTokens;
    }

    public UniqueToken find(long rrn) {
    	UniqueToken uniqueToken = null;
    	uniqueToken = emgr.find(UniqueToken.class, rrn);
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
    	return rows;
    }

    public int remove(long rrn) {
    	Query qry = emgr.createQuery("DELETE FROM UniqueToken "
    		+ "WHERE up1Rrn = :rrn");
    	int rows = qry
    		.setParameter("rrn", rrn)
    		.executeUpdate();
    	return rows;
    }
}
