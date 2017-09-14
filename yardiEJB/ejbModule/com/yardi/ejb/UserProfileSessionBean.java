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
	/*
	 * In the case of a RESOURCE_LOCAL, EntityManager.getTransaction().begin() and EntityManager.getTransaction().comit() 
	 * are allowed. Here, the container is managing begin and commit and an attempt to begin will throw Cannot use an 
	 * EntityTransaction while using JTA 
	 */
	@PersistenceContext(name = "yardiEJB")	
	private EntityManager entityManager;

    public UserProfileSessionBean() {
    }

    @Override
    public UserProfile find(String userName) {
    	UserProfile userProfile = null;
    	userProfile = entityManager.find(UserProfile.class, userName);
    	return userProfile;
    }
}
