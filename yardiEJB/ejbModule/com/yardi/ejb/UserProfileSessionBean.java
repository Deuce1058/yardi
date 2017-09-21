package com.yardi.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TemporalType;

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

    public UserProfile find(String userName) {
    	UserProfile userProfile = null;
    	userProfile = entityManager.find(UserProfile.class, userName);
    	return userProfile;
    }
    
    public int setUpPwdAttempts(String userName, short pwdAttempts) {
    	//upPwdAttempts
    	Query qry = updateEmgr.createQuery("UPDATE UserProfile " 
    		+ "SET upPwdAttempts = :pwdAttempts " 
    		+ "WHERE upUserid    = :userName");
    	int rows = qry.setParameter("pwdAttempts", pwdAttempts).setParameter("userName", userName).executeUpdate();
    	return rows;
    }
    
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
    
    public int loginSuccess(String userName) {
    	//upDisabledDate, upPwdAttempts, upLastLoginDate
    	java.sql.Timestamp loginDate = new java.sql.Timestamp(new java.util.Date().getTime());
    	Query qry = updateEmgr.createQuery("UPDATE UserProfile " 
    		+ "SET upPwdAttempts =  0, "
    		+ "upDisabledDate    =  null, "
    		+ "upLastLoginDate   = :loginDate "
    		+ "WHERE upUserid    = :userName");
    	int rows = qry
    		.setParameter("loginDate", loginDate)
    		.setParameter("userName",  userName)
    		.executeUpdate();
    	return rows;
    }
    
    public int changeUserToken(String token, java.util.Date pwdExpirationDate) {
    	Query qry = updateEmgr.createQuery("UPDATE UserProfile " 
        		+ "SET uptoken    = :token,"
        		+ "upPwdexpd      = :pwdExpirationDate "
        		+ "WHERE upUserid = :userName");
        int rows = qry
        	.setParameter("token", token)
        	.setParameter("pwdExpirationDate", pwdExpirationDate, TemporalType.DATE)
        	.executeUpdate();
        return rows;
    }
}
