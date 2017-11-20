package com.yardi.ejb;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.sun.javafx.collections.MappingChange.Map;

/**
 * Session Bean implementation class UserProfileSessionBean
 */
@Stateless
public class UserProfileSessionBean implements UserProfileSessionBeanRemote {
	/*
	 * In the case of a RESOURCE_LOCAL, EntityManager.getTransaction().begin() and EntityManager.getTransaction().comit() 
	 * are allowed. Here, the container is managing begin and commit and an attempt to begin will throw Cannot use an 
	 * EntityTransaction while using JTA
	 * 
	 * 2017 1104 Instead of a single instance of EntityManager move it to the method level. Each method has its own 
	 * EntityManager. Use EntityManagerFactory.createEntityManager(SynchronizationType synchronizationType)
	 * see https://stackoverflow.com/questions/27881058/jpa-get-updated-synced-entities-across-entitymanagers-refresh
	 * second comment after the answer "EntityManagers are meant to be light weight"
	 * 
	 * examples of EntityManager.setProperty()
	 * examples of EntityManagerFactory.createEntityManager(SynchronizationType synchronizationType, Map map)
	 */
	@PersistenceContext(unitName = "userProfile")	
	private EntityManager entityManager;

	public UserProfileSessionBean() {
    }

    public UserProfile find(String userName) {
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("userProfile");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
    	UserProfile userProfile = null;
    	//userProfile = entityManager.find(UserProfile.class, userName);
    	TypedQuery<UserProfile> qry = em.createQuery(
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
				+ "\n "
				+ "  em="
				+ em
				);
		//debug
		em.flush();
		em.clear();
    	return userProfile;
    }
    
    public int setUpPwdAttempts(String userName, short pwdAttempts) {
    	//upPwdAttempts
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("userProfile");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
    	Query qry = em.createQuery("UPDATE UserProfile " 
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
				+ "  rows=" 
				+ rows
				+ "\n "
				+ "  em="
				+ em
				);
		//debug
		em.flush();
		em.clear();
    	return rows;
    }
    
    public int disable(String userName, java.sql.Timestamp disabledDate, short pwdAttempts) {
    	//upDisabledDate, upPwdAttempts
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("userProfile");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
    	Query qry = em.createQuery("UPDATE UserProfile "  
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
				+ "  rows=" 
				+ rows
				+ "\n "
				+ "  em="
				+ em
				);
		//debug
		em.flush();
		em.clear();
    	return rows;
    }
    
    public int loginSuccess(String userName) {
    	//upDisabledDate, upPwdAttempts, upLastLoginDate
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("userProfile");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
    	java.sql.Timestamp loginDate = new java.sql.Timestamp(new java.util.Date().getTime());
    	Query qry = em.createQuery("UPDATE UserProfile " 
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
				+ "  rows=" 
				+ rows
				+ "\n "
				+ "  em="
				+ em
				);
		//debug
		em.flush();
		em.clear();
    	return rows;
    }
    
    public int changeUserToken(String userName, String token, java.util.Date pwdExpirationDate) {
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("userProfile");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
    	Query qry = em.createQuery("UPDATE UserProfile " 
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
				+ "  rows=" 
				+ rows
				+ "\n "
				+ "  em="
				+ em
				);
		//debug
		em.flush();
		em.clear();
        return rows;
    }

	public String stringify() {
		return "UserProfileSessionBean="
				+ this;
	}
}
