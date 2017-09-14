package com.yardi.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

    /**
     * Default constructor. 
     */
    public UniqueTokensSesssionBean() {
    }

    @Override
    public ArrayList<UniqueToken> findTokens(String userName) {
		List<UniqueToken> userTokens = new ArrayList<UniqueToken>(50);
		Query qry = emgr.createNativeQuery(
			"SELECT * FROM DB2ADMIN.UNIQUE_TOKENS WHERE UP1_USER_NAME = ? ORDER BY UP1_DATE_ADDED, UP1_RRN", 
			UniqueToken.class);
		userTokens = (ArrayList<UniqueToken>)qry.setParameter(1, userName).getResultList();
    	return (ArrayList<UniqueToken>) userTokens;
    }

    @Override
    public UniqueToken find(long rrn) {
    	UniqueToken uniqueToken = null;
    	uniqueToken = emgr.find(UniqueToken.class, rrn);
    	return uniqueToken;
    } 
    
    @Override
    public void persist(UniqueToken uniqueToken) {
    	emgr.persist(uniqueToken); //step 2 insert
    }

    @Override
    public void remove(UniqueToken uniqueToken) {
    	emgr.remove(uniqueToken); //step 2 delete
    }
}
