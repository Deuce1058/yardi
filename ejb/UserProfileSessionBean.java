package com.yardi.ejb;

import java.util.Date;
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

	public int persist(
		String userId, 
		String token,
		short homeMarket,
		String firstName,
		String lastName,
		String address1,
		String address2,
		String city,
		String state,
		String zip,
		String zip4,
		String phone,
		String fax,
		String email,
		String ssn,
		java.util.Date birthdate,
		String activeYN,
		java.util.Date passwordExpirationDate,
		java.util.Date disabledDate,
		java.util.Date lastLoginDate,
		int pwdAttempts
		) {
		String disabledYN = "N";
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("userProfile");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
    	Query qry = em.createNativeQuery("INSERT INTO DB2ADMIN.USER_PROFILE "
    		+ "("
    		+ "UP_USERID, "
    		+ "UPTOKEN, "
    		+ "UP_HOME_MARKET, "
    		+ "UP_FIRST_NAME, "
    		+ "UP_LAST_NAME, "
    		+ "UP_ADDRESS1, "
    		+ "UP_ADDRESS2, "
    		+ "UP_CITY, "
    		+ "UP_STATE, "
    		+ "UP_ZIP, "
    		+ "UP_ZIP4, "
    		+ "UP_PHONE, "
    		+ "UP_FAX, "
    		+ "UP_EMAIL, "
    		+ "UPSSN, "
    		+ "UPDOB, "
    		+ "UP_ACTIVE_YN, "
    		+ "UP_PWDEXPD, "
    		+ "UP_DISABLED_YN, "
    		+ "UP_DISABLED_DATE, "
    		+ "UP_LAST_LOGIN_DATE, "
    		+ "UP_PWD_ATTEMPTS"
    		+ ") VALUES("
    		+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
    		+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
    		+ ")"
    		);
    	int rows = qry
       		.setParameter(1, userId)
       		.setParameter(2, token)
       		.setParameter(3, homeMarket)
       		.setParameter(4, firstName)
       		.setParameter(5, lastName)
       		.setParameter(6, address1)
       		.setParameter(7, address2)
       		.setParameter(8, city)
       		.setParameter(9, state)
       		.setParameter(10, zip)
       		.setParameter(11, zip4)
       		.setParameter(12, phone)
       		.setParameter(13, fax)
       		.setParameter(14, email)
       		.setParameter(15, ssn)
       		.setParameter(16, birthdate, TemporalType.DATE)
       		.setParameter(17, activeYN)
       		.setParameter(18, passwordExpirationDate, TemporalType.DATE)
       		.setParameter(19, disabledYN)
       		.setParameter(20, disabledDate, TemporalType.TIMESTAMP)
       		.setParameter(21, lastLoginDate, TemporalType.TIMESTAMP)
       		.setParameter(22, pwdAttempts)
       		.executeUpdate();
    	return rows;
	}
    
    public String stringify() {
		return "UserProfileSessionBean="
				+ this;
	}
}
