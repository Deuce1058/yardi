package com.yardi.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

/**
 * Session Bean implementation class UserProfileSessionBean
 */
@Stateless
public class UserProfileSessionBean implements UserProfileSessionBeanRemote {
	@PersistenceContext(name = "yardiEJB")	
	private EntityManager entityManager;

    public UserProfileSessionBean() {
    }

    @Override
    public UserProfile findUser(String userName) {
    	UserProfile userProfile = null;
    	userProfile = entityManager.find(UserProfile.class, userName);
    	return userProfile;
    }
    
    @Override
    public void beginTransaction() {
    	entityManager.getTransaction().begin();
    }
    
    @Override
    public void commitTransaction() {
    	entityManager.getTransaction().commit();
    }
}
