package com.yardi.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class UniqueTokensSesssionBean
 */
@Stateless
@LocalBean
public class UniqueTokensSesssionBean implements UniqueTokensSesssionBeanRemote {
	@PersistenceContext(name="yardiEJB")
	private EntityManager emgr;

    /**
     * Default constructor. 
     */
    public UniqueTokensSesssionBean() {
    }

    @Override
    public UniqueToken exists(long rrn) {
    	UniqueToken uniqueToken = null;
    	uniqueToken = emgr.find(UniqueToken.class, rrn);
    	return uniqueToken;
    }
    
    @Override
    public void beginTransaction() {
    	emgr.getTransaction().begin(); //step 1 start transaction
    }
    
    @Override
    public void commitTransaction() {
    	emgr.getTransaction().commit(); //step 3 commit transaction
    }
    
    @Override
    public void persist(UniqueToken uniqueToken) {
    	emgr.persist(uniqueToken); //step 2 insert
    }
    
    @Override
    public void remove(UniqueToken uniqueToken) {
    	emgr.remove(uniqueToken); //step 2 delete
    }
    
    @Override
    public EntityManager getEntityManager() {
    	return emgr;
    }
}
