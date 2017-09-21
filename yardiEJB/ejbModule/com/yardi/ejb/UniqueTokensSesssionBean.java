package com.yardi.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

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
	@PersistenceContext(name="yardiEJB")
	private EntityManager emgr;

	@PersistenceContext(name="yardiEJB")
	private EntityManager emgrUpdate;

    /**
     * Default constructor. 
     */
    public UniqueTokensSesssionBean() {
    }

    @Override
    public ArrayList<UniqueToken> findTokens(String userName) {
		List<UniqueToken> userTokens = new ArrayList<UniqueToken>(50);
		Query qry = emgr.createQuery(
			  "SELECT UniqueToken " 
			+ "WHERE up1UserName = :userName "
			+ "ORDER BY up1DateAdded, up1Rrn", 
			UniqueToken.class);
		userTokens = (ArrayList<UniqueToken>)qry.setParameter("userName", userName).getResultList();
    	return (ArrayList<UniqueToken>) userTokens;
    }

    @Override
    public UniqueToken find(long rrn) {
    	UniqueToken uniqueToken = null;
    	uniqueToken = emgr.find(UniqueToken.class, rrn);
    	return uniqueToken;
    } 
    
    @Override
    public int persist(String userName, String token, java.util.Date dateAdded) {
    	Query qry = emgr.createNativeQuery("INSERT INTO DB2ADMIN.UNIQUE_TOKENS "
    		+ "VALUES(?, ?, ?)");
    	int rows = qry
    		.setParameter(1, userName)
    		.setParameter(2, token)
    		.setParameter(3, dateAdded, TemporalType.DATE)
    		.executeUpdate();
    	return rows;
    }

    @Override
    public int remove(long rrn) {
    	Query qry = emgrUpdate.createQuery("DELETE FROM UniqueToken "
    		+ "WHERE up1Rrn = :rrn");
    	int rows = qry.setParameter("rrn", rrn)
    	.executeUpdate();
    	return rows;
    }
}
