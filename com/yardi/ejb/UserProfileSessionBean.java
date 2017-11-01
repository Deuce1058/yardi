package com.yardi.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

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

    public UserProfile find(String userName) {
    	UserProfile userProfile = null;
    	//userProfile = entityManager.find(UserProfile.class, userName);
    	TypedQuery<UserProfile> qry = entityManager.createQuery(
    		  "SELECT u from UserProfile u "
    		+ "WHERE u.upUserid = :userName ",
    		UserProfile.class);
    	userProfile = qry
    		.setParameter("userName", userName)
    		.getSingleResult();
		//debug
		System.out.println("com.yardi.ejb UserProfileSessionBean find() 0000 "
				+ "\n "
				+ "  userName=" + userName
				+ "\n "
				+ "  userProfile=" + userProfile
				);
		//debug
    	return userProfile;
    }
    
    public int setUpPwdAttempts(String userName, short pwdAttempts) {
    	//upPwdAttempts
    	Query qry = entityManager.createQuery("UPDATE UserProfile " 
    		+ "SET upPwdAttempts = :pwdAttempts " 
    		+ "WHERE upUserid    = :userName");
    	int rows = qry
    			.setParameter("pwdAttempts", pwdAttempts)
    			.setParameter("userName"   , userName)
    			.executeUpdate();
		//debug
		System.out.println("com.yardi.ejb UserProfileSessionBean setUpPwdAttempts() 0001 "
				+ "\n "
				+ "  userName=" + userName
				+ "\n "
				+ "  pwdAttempts=" + pwdAttempts
				+ "\n "
				+ "  rows=" + rows
				);
		//debug
    	return rows;
    }
    
    public int disable(String userName, java.sql.Timestamp disabledDate, short pwdAttempts) {
    	//upDisabledDate, upPwdAttempts
    	Query qry = entityManager.createQuery("UPDATE UserProfile "  
    	    + "SET upPwdAttempts = :pwdAttempts, "
    		+ "upDisabledDate    = :disabledDate "
    		+ "WHERE upUserid    = :userName");
    	int rows = qry
    		.setParameter("pwdAttempts" , pwdAttempts)
    		.setParameter("disabledDate", disabledDate)
    		.setParameter("userName"    , userName)
    		.executeUpdate();
		//debug
		System.out.println("com.yardi.ejb UserProfileSessionBean disable() 0002 "
				+ "\n "
				+ "  userName=" + userName
				+ "\n "
				+ "  disabledDate=" + disabledDate
				+ "\n "
				+ "  pwdAttempts=" + pwdAttempts
				+ "\n "
				+ "  rows=" + rows
				);
		//debug
    	return rows;
    }
    
    public int loginSuccess(String userName) {
    	//upDisabledDate, upPwdAttempts, upLastLoginDate
    	java.sql.Timestamp loginDate = new java.sql.Timestamp(new java.util.Date().getTime());
    	Query qry = entityManager.createQuery("UPDATE UserProfile " 
    		+ "SET upPwdAttempts =  0, "
    		+ "upDisabledDate    =  null, "
    		+ "upLastLoginDate   = :loginDate "
    		+ "WHERE upUserid    = :userName");
    	int rows = qry
    		.setParameter("loginDate", loginDate)
    		.setParameter("userName" , userName)
    		.executeUpdate();
		//debug
		System.out.println("com.yardi.ejb UserProfileSessionBean loginSuccess() 0003 "
				+ "\n "
				+ "  userName=" + userName
				+ "\n "
				+ "  upPwdAttempts =0"
				+ "\n "
				+ "  upDisabledDate=null"
				+ "\n "
				+ "  loginDate=" + loginDate
				+ "\n "
				+ "  rows=" + rows
				);
		//debug
    	return rows;
    }
    
    public int changeUserToken(String userName, String token, java.util.Date pwdExpirationDate) {
    	Query qry = entityManager.createQuery("UPDATE UserProfile " 
        		+ "SET uptoken    = :token,"
        		+ "upPwdexpd      = :pwdExpirationDate "
        		+ "WHERE upUserid = :userName");
        int rows = qry
        	.setParameter("token", token)
        	.setParameter("pwdExpirationDate", pwdExpirationDate, TemporalType.DATE)
        	.setParameter("userName", userName)
        	.executeUpdate();
		//debug
		System.out.println("com.yardi.ejb UserProfileSessionBean changeUserToken() 0004 "
				+ "\n "
				+ "  userName=" + userName
				+ "\n "
				+ "  uptoken=" + token
				+ "\n "
				+ "  upPwdexpd=" + pwdExpirationDate
				+ "\n "
				+ "  rows=" + rows
				);
		//debug
        return rows;
    }
}
