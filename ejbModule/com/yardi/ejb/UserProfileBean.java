package com.yardi.ejb;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.shared.userServices.PasswordAuthentication;

/**
 * Session Bean implementation class UserProfileBean
 */
@Stateful
public class UserProfileBean implements UserProfile {
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
	@PersistenceContext(unitName = "yardi", type=PersistenceContextType.EXTENDED)	
	private EntityManager em;
	private String feedback = "";
	private Pwd_Policy pwdPolicy;
	@EJB PasswordPolicy passwordPolicyBean;

    public UserProfileBean() {
    	System.out.println("com.yardi.ejb.UserProfileBean UserProfileBean() 0015");
    }

    @PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.UserProfileBean postConstructCallback() 0016");
    	getPwdPolicy();
    }

    public User_Profile find(String userName) {
    	User_Profile userProfile = null;
    	TypedQuery<User_Profile> qry = em.createQuery(
    		  "SELECT u from User_Profile u "
    		+ "WHERE u.upUserid = :userName ",
    		User_Profile.class);
    	try {
			userProfile = qry
				.setParameter("userName", userName)
				.getSingleResult();
		} catch (Exception e) {
			System.out.println("com.yardi.ejb.UserProfileBean find() 0005 exception");
	    	userProfile = null;
			e.printStackTrace();
		}
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean find() 0000 "
				+ "\n "
				+ "  userName=" + userName
				+ "\n "
				+ "  userProfile=" + userProfile
				+ "\n "
				+ "  em="
				+ em
				);
		//debug
    	return userProfile;
    }
    
    public int setUpPwdAttempts(String userName, short pwdAttempts) {
    	//upPwdAttempts
    	Query qry = em.createQuery("UPDATE User_Profile " 
    		+ "SET upPwdAttempts = :pwdAttempts " 
    		+ "WHERE upUserid    = :userName");
    	int rows = qry
    			.setParameter("pwdAttempts", pwdAttempts)
    			.setParameter("userName"   , userName)
    			.executeUpdate();
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean setUpPwdAttempts() 0001 "
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
    	return rows;
    }
    
    private int disable(String userName) {
    	//upDisabledDate, upPwdAttempts
    	Query qry = em.createQuery("UPDATE User_Profile "  
    	    + "SET upPwdAttempts = :pwdAttempts, "
    		+ "upDisabledDate    = :disabledDate "
    		+ "WHERE upUserid    = :userName");
    	int rows = qry
    		.setParameter("pwdAttempts" , pwdPolicy.getPpMaxSignonAttempts())
    		.setParameter("disabledDate", new java.sql.Timestamp(new java.util.Date().getTime()), TemporalType.TIMESTAMP)
    		.setParameter("userName"    , userName)
    		.executeUpdate();
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean disable() 0002 "
				+ "\n "
				+ "  userName=" + userName
				+ "\n "
				+ "  disabledDate=" + new java.util.Date().getTime()
				+ "\n "
				+ "  pwdAttempts=" + pwdPolicy.getPpMaxSignonAttempts()
				+ "\n "
				+ "  rows=" 
				+ rows
				+ "\n "
				+ "  em="
				+ em
				);
		//debug
    	return rows;
    }
    
    /**
     * Update the user profile to reflect successful login. Password attempts is set to zero, disabled date is set to null,
     * last login date is today. 
     * 
     * @param userName
     */
    public int loginSuccess(String userName) {
    	//upDisabledDate, upPwdAttempts, upLastLoginDate
    	java.sql.Timestamp loginDate = new java.sql.Timestamp(new java.util.Date().getTime());
    	Query qry = em.createQuery("UPDATE User_Profile " 
    		+ "SET upPwdAttempts =  0, "
    		+ "upDisabledDate    =  null, "
    		+ "upLastLoginDate   = :loginDate "
    		+ "WHERE upUserid    = :userName");
    	int rows = qry
    		.setParameter("loginDate", loginDate, TemporalType.TIMESTAMP)
    		.setParameter("userName" , userName)
    		.executeUpdate();
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean loginSuccess() 0003 "
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
    	return rows;
    }
    
    public int changeUserToken(String userName, final char [] newPassword) {
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		String userToken="";
		try {
			userToken = passwordAuthentication.hash(newPassword);  //hash of new password
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.add(Calendar.DAY_OF_MONTH, new Short(pwdPolicy.getPpDays()).intValue()); //new password expiration date
    	Query qry = em.createQuery("UPDATE User_Profile " 
        		+ "SET uptoken    = :token,"
        		+ "upPwdexpd      = :pwdExpirationDate "
        		+ "WHERE upUserid = :userName");
        int rows = qry
        	.setParameter("token", userToken)
        	.setParameter("pwdExpirationDate", new java.util.Date(gc.getTimeInMillis()), TemporalType.DATE)
        	.setParameter("userName", userName)
        	.executeUpdate();
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean changeUserToken() 0004 "
				+ "\n "
				+ "  userName=" + userName
				+ "\n "
				+ "  uptoken=" + userToken
				+ "\n "
				+ "  upPwdexpd=" + gc
				+ "\n "
				+ "  rows=" 
				+ rows
				);
		//debug
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
		java.sql.Timestamp disabledDate,
		java.sql.Timestamp lastLoginDate,
		short pwdAttempts
		) {
		String disabledYN = "N";
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
  
	public int remove(String userID) {
    	Query qry = em.createQuery("DELETE FROM User_Profile "
    		+ "WHERE upUserid = :userID");
    	int rows = qry
    		.setParameter("userID", userID)
    		.executeUpdate();
    	return rows;
	}
	
	public int updateAll(
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
		java.sql.Timestamp disabledDate,
		java.sql.Timestamp lastLoginDate,
		short pwdAttempts
		) {
		String disabledYN = "N";
	   	Query qry = em.createQuery("UPDATE User_Profile "
    	    + "SET uptoken     = :token, "
    		+ "upHomeMarket    = :homeMarket, "
    		+ "upFirstName     = :firstName, "
    		+ "upLastName      = :lastName, "
    		+ "upAddress1      = :address1, "
    		+ "upAddress2      = :address2, "
    		+ "upCity          = :city, "
    		+ "upState         = :state, "
    		+ "upZip           = :zip, "
    		+ "upZip4          = :zip4, "
    		+ "upPhone         = :phone, "
    		+ "upFax           = :fax, "
    		+ "upEmail         = :email, "
    		+ "upssn           = :ssn, "
    		+ "updob           = :birthdate, "
    		+ "upActiveYn      = :activeYN, "
    		+ "upPwdexpd       = :passwordExpirationDate, "
    		+ "upDisabledYn    = :disabledYN, "
    		+ "upDisabledDate  = :disabledDate, "
    		+ "upLastLoginDate = :lastLoginDate, "
    		+ "upPwdAttempts   = :pwdAttempts "
    		+ "WHERE upUserid  = :userId");
	   	int rows = qry
	   		.setParameter(1, token)
	   		.setParameter(2, homeMarket)
	   		.setParameter(3, firstName)
	   		.setParameter(4, lastName)
	   		.setParameter(5, address1)
	   		.setParameter(6, address2)
	   		.setParameter(7, city)
	   		.setParameter(8, state)
	   		.setParameter(9, zip)
	   		.setParameter(10, zip4)
	   		.setParameter(11, phone)
	   		.setParameter(12, fax)
	   		.setParameter(13, email)
	   		.setParameter(14, ssn)
	   		.setParameter(15, birthdate, TemporalType.DATE)
	   		.setParameter(16, activeYN)
	   		.setParameter(17, passwordExpirationDate, TemporalType.DATE)
	   		.setParameter(18, disabledYN)
	   		.setParameter(19, disabledDate, TemporalType.TIMESTAMP)
	   		.setParameter(20, lastLoginDate, TemporalType.TIMESTAMP)
	   		.setParameter(21, pwdAttempts)
	   		.executeUpdate();
		return rows;
	}
	 
	/*
	 * findUserProfile(userName);
	 * if userProfile == null
	 *   feedback = com.yardi.rentSurvey.YardiConstants.YRD0001;
	 * if disabled
	 *   feedback = com.yardi.rentSurvey.YardiConstants.YRD0003;
	 * if inactive
	 *   feedback = com.yardi.rentSurvey.YardiConstants.YRD0004;
	 * authenticate
	 * if invalid password
	 *   sigonAttempts++
	 *   if (signonAttempts == maxSignonAttempts)
	 *     disable profile
	 * else
	 *   if user is not changing password and the password expired
	 *     return false
	 *   if user is not changing password and the password has not expired
	 *     userProfileBean.loginSuccess(userName)
	 *          update user profile upPwdAttempts=0, upDisabledDate=null, upLastLoginDate
	 */
	public boolean authenticate(String userName, String password, boolean userIsChangingPassword) {
		System.out.println("com.yardi.ejb.UserProfileBean authenticate() 0013");
		feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
		java.sql.Timestamp today = new java.sql.Timestamp(new java.util.Date().getTime());
		User_Profile userProfile = find(userName); 

		//if (getPwdPolicy()== null) { //get the password policy
		if (pwdPolicy==null) { //get the password policy
			//debug
			System.out.println("com.yardi.ejb.UserProfileBean authenticate() 0010 pwdPolicy == null");
			//debug
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
			return false;
		}

		if (userProfile == null) {
			//debug
			System.out.println("com.yardi.ejb.UserProfileBean authenticate() 0006 userProfile == null");
			//debug
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0001;
			return false;
		}

		//debug
		System.out.println("com.yardi.ejb.UserProfileBean authenticate() 0007 userProfile =" + userProfile + "\n");
		//debug

		if (userProfile.getUpDisabledDate() != null) {
			//debug
			System.out.println("com.yardi.ejb.UserProfileBean authenticate() 0008 userProfile.getUpDisabledDate() != null\n");
			//debug
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0003;
			return false;
		}

		if (userProfile.getUpActiveYn().equals("N")) {
			//debug
			System.out.println("com.yardi.ejb.UserProfileBean authenticate() 0009 userProfile.getUpActiveYn().equals(N)");
			//debug
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0004;
			return false;
		}

		short maxSignonAttempts = pwdPolicy.getPpMaxSignonAttempts();
		long passwordExpiration = userProfile.getUpPwdexpd().getTime();
		short signonAttempts = userProfile.getUpPwdAttempts();
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication(); 
		boolean pwdValid = passwordAuthentication.authenticate(password.toCharArray(), userProfile.getUptoken());

		if (pwdValid == false) {
			System.out.println("com.yardi.ejb.UserProfileBean authenticate() 0017");
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000F;
			signonAttempts++;
			int rows = setUpPwdAttempts(userName, signonAttempts);
			//debug
			System.out.println("com.yardi.ejb.UserProfileBean authenticate() 0012"
					+ "\n "
					+ "  feedback =" + feedback  
					+ "\n "
					+ "  signonAttempts = " + signonAttempts
					+ "\n "
					+ "  rows =" + rows
					);
			//debug

			if (rows != 1) {
				int z = rows;
			}

			if (signonAttempts == maxSignonAttempts) {
				rows = disable(userName);
				//debug
				System.out.println("com.yardi.ejb.UserProfileBean authenticate() 000C"
						+ "\n "
						+ "  signonAttempts == maxSignonAttempts"  
						+ "\n "
						+ "  rows =" + rows
						);
				//debug

				if (rows != 1) {
					int z = rows;
				}

				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000C;
				//debug
				System.out.println("com.yardi.ejb.UserProfileBean authenticate() 000D"
						+ "feedback =" + feedback + "\n");
				//debug
			}
		} else {

			if (userIsChangingPassword == false && passwordExpiration <= today.getTime()) {
				/*
				 * Password expired. Use chgPwd() in this class to change it
				 * 
				 * If the password is being changed because it expired then the expired password error should be ignored (it is
				 * expected) as long as all of the other criteria (they have provided valid credentials, they are active and 
				 * the password is not disabled) have been met. For this reason, the expired password test happens last. 
				 */
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0002;
				//debug
				System.out.println("com.yardi.ejb.UserProfileBean authenticate() 000E"
						+ "\n "
						+ "  userIsChangingPassword =" + userIsChangingPassword   
						+ "\n "
						+ "  passwordExpiration =" + passwordExpiration
						+ "\n "
						+ "  today =" + today
						+ "\n "
						+ "  feedback =" + feedback
						);
				//debug
				return false;
			}

			if (userIsChangingPassword == false) {
				/*
				 * They have successfully logged in at this point only if they are not changing the password so only set 
				 * last login date when they are not changing the password 
				 */
				int rows = loginSuccess(userName);
				//debug
				System.out.println("com.yardi.ejb.UserProfileBean authenticate() 000F"
						+ "\n "
						+ "  userIsChangingPassword == false"
						+ "\n "
						+ "  rows =" + rows
						);   
				//debug

				if (rows != 1) {
					int z = rows;
				}
			}
		}
		return pwdValid;
	}
	
	public String getFeedback() {
		return feedback;
	}

	private Pwd_Policy getPwdPolicy() {
		
		if (pwdPolicy==null) {
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean getPwdPolicy() 000A"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy
			);
		//debug
		return pwdPolicy;
	}

	private void setPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean setPwdPolicy() 0014"
				+ "\n"
				+ "   passwordPolicyBean="
				+ passwordPolicyBean
				);
		//debug
		pwdPolicy = passwordPolicyBean.getPwdPolicy();
		
		if (pwdPolicy == null) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
			System.out.println("com.yardi.ejb.UserProfileBean setPwdPolicy() pwdPolicy==null 0011");
			return;
		}
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean setPwdPolicy() 000B"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy
			+ "\n"
			+ "   feedback="
			+ feedback);
		//debug
	}

	@Remove
	public void removeBean() {
		System.out.println("com.yardi.ejb.UserProfileBean removeBean() 0018 ");
	}
	
	public String stringify() {
		return "UserProfileBean="
				+ this;
	}
}
