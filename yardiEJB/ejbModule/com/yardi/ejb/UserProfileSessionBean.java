package com.yardi.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

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

	@PersistenceContext(name = "yardiEJB")	
	private EntityManager updateEmgr;

    public UserProfileSessionBean() {
    }

    @Override
    public UserProfile find(String userName) {
    	UserProfile userProfile = null;
    	userProfile = entityManager.find(UserProfile.class, userName);
    	return userProfile;
    }
    
    @Override
    public int setUpPwdAttempts(String userName, short pwdAttempts) {
    	//upPwdAttempts
    	Query qry = updateEmgr.createQuery("UPDATE UserProfile " 
    		+ "SET upPwdAttempts = :pwdAttempts " 
    		+ "WHERE upUserid    = :userName");
    	int rows = qry.setParameter("pwdAttempts", pwdAttempts).setParameter("userName", userName).executeUpdate();
    	return rows;
    }
    
    @Override
    public int disable(String userName, java.sql.Timestamp disabledDate, short pwdAttempts) {
    	//upDisabledDate, upPwdAttempts
    	Query qry = updateEmgr.createQuery("UPDATE UserProfile "  
    	    + "SET upPwdAttempts = :pwdAttempts, "
    		+ "upDisabledDate    = :disabledDate "
    		+ "WHERE upUserid    = :userName");
    	int rows = qry.setParameter("pwdAttempts", pwdAttempts)
    		.setParameter("disabledDate", disabledDate)
    		.setParameter("userName", userName).executeUpdate();
    	return rows;
    }
    
    @Override
    public int loginSuccess(String userName, java.sql.Timestamp disabledDate, 
    	short pwdAttempts, java.sql.Timestamp loginDate) {
    	//upDisabledDate, upPwdAttempts, upLastLoginDate 
    	Query qry = updateEmgr.createQuery("UPDATE UserProfile " 
    		+ "SET upPwdAttempts = :pwdAttempts, "
    		+ "upDisabledDate    = :disabledDate, "
    		+ "upLastLoginDate   = :loginDate "
    		+ "WHERE upUserid    = :userName");
    	int rows = qry.setParameter("pwdAttempts", pwdAttempts)
       		.setParameter("disabledDate", disabledDate)
    		.setParameter("loginDate", loginDate)
    		.setParameter("userName", userName).executeUpdate();
    	return rows;
    }
}
