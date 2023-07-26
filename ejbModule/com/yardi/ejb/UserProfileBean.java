package com.yardi.ejb;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.metamodel.EntityType;

import com.yardi.ejb.model.User_Profile;
import com.yardi.ejb.model.Full_User_Profile;
import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.shared.userServices.PasswordAuthentication;

/**
 * Session Bean implementation of methods for working with User_Pofile entity 
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
	/**
	 * Clients can read this field to obtain the status after calling a method that provides feedback. 
	 */
	private String feedback = "";
	/**
	 * The password policy obtained from com.yardi.ejb.PasswordPolicyBean.
	 */
	private Pwd_Policy pwdPolicy;
	/**
	 * The User_Profile entity injected by clients
	 */
	private User_Profile userProfile;
	/**
	 * EJB com.yardi.ejb.PasswordPolicyBean
	 */
	@EJB PasswordPolicy passwordPolicyBean;

    public UserProfileBean() {
    	System.out.println("com.yardi.ejb.UserProfileBean UserProfileBean() 0015 ");
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
	 *          
	 */
    /**
     * Authenticate the user.
     * <p>
     * <strong>Initial conditions:</strong>
     * <ul>
     *   <li>There is a reference to Password_Policy entity</li>
     *   <li>There is a reference to User_Profile entity</li>
     *   <li>The User_Profile entity is not disabled</li>
     *   <li>The User_Proile entity is active</li>
     * </ul><br>
	 *
     * If these conditions are satisfied then com.yardi.shared.userServices.PasswordAuthentication.athenticate() checks that the hash of the 
     * plain text password matches the hashed password stored in User_Profile entity.<br><br><br>
     * 
     * 
     * <strong>If the password is invalid:</strong>
     * <ul>
     *   <li>Number of password attempts is incremented</li> 
     *   <li>Set <i>upPwdAttempts</i> in User_Profile entity</li>
     *   <li>If the number of password attempts equals <i>ppMaxSignonAttempts</i> defined in Password_Policy entity then disable the User_Profile entity 
     *       by setting <i>upDisabledDate</i></li>
     * </ul>
     * <br>
     * <strong>If the password is valid:</strong>
     * <div style="display:flex; flex-direction: row">
     *   <div>
     *     <pre>    <u>If the password needs to be changed:</u></pre>
     *     <ul>
     *       <li>set <i>upPwdAttempts</i> in User_Profile entity to zero to give the user credit for successfully authenticating</li>
     *       <li>set feedback to <span style="font-family:consolas;">YRD0002</span></li>
     *       <li>return false to indicate authentication has not yet succeeded</li>
     *     </ul>
     *   </div>
     *   <div>
     *     <pre>    <u>If the user is not in the process of changing their password:</u></pre>
     *     <ul>
     *       <li>call the <i>loginSuccess()</i> method</li>
     *       <li>return the boolean value from com.yardi.shared.userServices.PasswordAuthentication.authenticate()</li>
     *     </ul>    
     *   </div>
     * </div>
     * <br><br>  
     * <strong>Feedback provided by this method:</strong> 
     * <pre>
     * YRD0000 normal completion
     * YRD0001 the injected User_Profile entity is null (indicates an invalid user name)
     * YRD0002 the password has expired
     * YRD0003 the User_Profile entity is disabled (user cant login). Password must be reset to login
     * YRD0004 the User_Profile entity is inactive. Administrator must clear the inactive flag to login
     * YRD000B password policy is missing
     * YRD000C maximum signon attempts exceeded. The User_Profile entity is disabled
     * YRD000F invalid password
     * </pre>
     * 
     * 
     * @param userName  user's name
     * @param password  password in plain text
     * @param userIsChangingPassword boolean indicating whether user is in the process of changing their password
     * @return boolean indicating whether authentication was successful 
     */
	public boolean authenticate(String userName, String password, boolean userIsChangingPassword) {
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean authenticate() 0013 ");
		//debug
		isJoined();
		isManaged(userProfile);
		feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
		java.sql.Timestamp today = new java.sql.Timestamp(new java.util.Date().getTime());

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
			System.out.println("com.yardi.ejb.UserProfileBean authenticate() 0017 ");
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000F;
			signonAttempts++;
			setUpPwdAttempts(signonAttempts);
			//debug
			System.out.println("com.yardi.ejb.UserProfileBean authenticate() 0012"
					+ "\n "
					+ "  feedback =" + feedback  
					+ "\n "
					+ "  signonAttempts = " + signonAttempts
					);
			//debug

			if (signonAttempts == maxSignonAttempts) {
				disable();
				//debug
				System.out.println("com.yardi.ejb.UserProfileBean authenticate() 000C"
						+ "\n "
						+ "  signonAttempts == maxSignonAttempts"  
						);
				//debug

				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000C;
				//debug
				System.out.println("com.yardi.ejb.UserProfileBean authenticate() 000D"
						+ "feedback =" + feedback + "\n");
				//debug
			}
		} else {

			if (userIsChangingPassword == false && passwordExpiration <= today.getTime()) {
				/*
				 * there is pwdPolicy
				 * there is userProfile
				 * user profile is active
				 * user profile is not disabled
				 * password is valid 
				 * Password expired. Use chgUserToken() in this class to change it
				 * They made it this far so give them credit and reset password attempts. 
				 * Still return false so no session table row is created.
				 * com.yardi.ejb.UserServicesBean.authenticate() makes an exception for YRD0002 and will commit instead of rollback
				 */
				//debug
				System.out.println("com.yardi.ejb.UserProfileBean authenticate() 0021 ");
				//debug
				setUpPwdAttempts((short) 0); //give them credit for successfully authenticating
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0002;
				//debug
				System.out.println("com.yardi.ejb.UserProfileBean authenticate() 000E "
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
				//debug
				System.out.println("com.yardi.ejb.UserProfileBean authenticate() 000F"
						+ "\n "
						+ "   userIsChangingPassword="
						+ userIsChangingPassword
						+ "\n "
						+ "   feedback="
						+ feedback
						);   
				//debug
				loginSuccess();
			}
		}
		return pwdValid;
	}

	/**
	 * Change the hashed password stored in the User_Profile entity.<p>
	 * 
	 * <strong>Steps to change the user's token:</strong>
	 * <ul>
	 *   <li>Hash the new password</li>
	 *   <li>Calculate the new password expiration date based on today + the password life in days as defined by <i>ppDays</i> in the Pwd_Policy entity</li>
	 *   <li>Set the hashed new password in the User_Profile entity</li>
	 *   <li>Set the password expiration date in the User_Profile entity</li>
	 * </ul>  
	 * 
	 * @param newPassword the new plain text password 
	 */
	public void changeUserToken(final char [] newPassword) {
    	//debug
    	System.out.println("com.yardi.ejb.UserProfileBean changeUserToken() 0002 ");
    	//debug
		isJoined();
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		String userToken="";
		try {
			userToken = passwordAuthentication.hash(newPassword); //hash new password
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.add(Calendar.DAY_OF_MONTH, Short.valueOf(pwdPolicy.getPpDays()).intValue()); //new password expiration date
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean changeUserToken() 0004 "
				+ "\n "
				+ "  userName=" + userProfile.getUpUserid()
				+ "\n "
				+ "  userToken="
				+ userToken
				+ "\n "
				+ "  gc="
				+ gc.toString()
				+ "\n "
				+ "  check USER PROFILE"
				);
		//debug
		userProfile.setUptoken(userToken);
		userProfile.setUpPwdexpd(new java.util.Date(gc.getTimeInMillis()));
		isJoined();
		User_Profile managedUserProfile = em.merge(userProfile);
		managedUserProfile.setUptoken(userProfile.getUptoken());
		managedUserProfile.setUpPwdexpd(userProfile.getUpPwdexpd());
		isJoined();
    	isManaged(managedUserProfile);
    }
	
	/**
	 * Disable the User_Profile entity. <p>
	 *  
	 * The disabled date Timestamp is set to the system time.<br><br>
	 * 
	 * The number of password attempts is set to the max defined in Pwd_Policy entity.<br><br>
	 * 
	 * The state in the <i>userProfile</i> field is merged into the persistence context. 
	 */
	private void disable() {
    	//debug
    	System.out.println("com.yardi.ejb.UserProfileBean disable() 001A ");
    	//debug
		isJoined();
    	userProfile.setUpPwdAttempts(pwdPolicy.getPpMaxSignonAttempts());
    	userProfile.setUpDisabledDate(new java.sql.Timestamp(new java.util.Date().getTime()));
    	User_Profile managedUserProfile = em.merge(userProfile);
    	managedUserProfile.setUpPwdAttempts(userProfile.getUpPwdAttempts());
    	managedUserProfile.setUpDisabledDate(userProfile.getUpDisabledDate());
		isJoined();
    	isManaged(managedUserProfile);
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean disable() 001B "
				+ "\n "
				+ "  userName=" 
				+ userProfile.getUpUserid()
				+ "\n "
				+ "  disabledDate=" 
				+ userProfile.getUpDisabledDate()
				+ "\n "
				+ "  pwdAttempts=" 
				+ userProfile.getUpPwdAttempts()
				);
		//debug
    }

    /**
     * Return the User_Profile entity specified by <i>userName</i>.<p> 
     * 
     * Returns null if the User_Profile entity is not in the persistence context and USER_PROFILE database table has no row matching userName.<p>
     * 
	 * Clients use this method to obtain a User_Profile entity. This method is not called during login.<br><br> 
     * 
     * Note that the returned entity contains only the columns from USER_PROFILE that are needed for login.
     * 
     * @param userName the entity to find.
     * @return User_Profile entity that matches <i>userName</i>.
     */
    public User_Profile find(String userName) {
    	return em.find(User_Profile.class, userName);
    }
    
    /**
	 * Return the Full_User_Profile entity specified by <i>userID</i>.<p>
	 * 
	 * Returns null if the Full_User_Profile entity is not in the persistence context and the USER_PROFILE database table has no row matching userID.<br><br>
	 * 
	 * Clients use this method to obtain a Full_User_Profile entity. This method is not called during login.<br><Br> 
	 * 
	 * Note that Full_User_Profile entity contains all columns from USER_PROFILE database table.
	 * 
	 * @param userID the Full_User_Profile to find
	 * @return Full_User_Profile that matches <i>userID</i>
	 */
	public Full_User_Profile findFullUserProfile(String userID) {
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean findFullUserProfile() 0022 ");
		//debug
		isJoined();
    	Full_User_Profile userProfile = em.find(Full_User_Profile.class, userID);
   		isManaged(userProfile);
    	
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean findFullUserProfile() 0023 "
				+ "\n "
				+ "  userName=" + userID
				+ "\n "
				+ "  userProfile=" + userProfile
				+ "\n "
				+ "  em="
				+ em
				);
		//debug
    	return userProfile;
	}
    
	/**
	 * Return the status of the most recent method call that provides feedback.<p>
	 * Clients call <i>getFeedback()</i> to determine the status of the most recent method call that provides feedback.
	 * @return feedback from the most recent method call that provides feedback.
	 */
    public String getFeedback() {
		return feedback;
	}
    
    /**
     * Returns the password policy obtained from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy()
     * 
     * @return Pwd_Policy entity 
     */
    private Pwd_Policy getPwdPolicy() {
    	//debug
    	System.out.println("com.yardi.ejb.UserProfileBean getPwdPolicy() 001C ");
    	//debug
    	isJoined();
		
		if (pwdPolicy==null) {
	    	//debug
	    	System.out.println("com.yardi.ejb.UserProfileBean getPwdPolicy() 0020 ");
	    	//debug
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean getPwdPolicy() 000A "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			);

		System.out.println("com.yardi.ejb.UserProfileBean getPwdPolicy() 001E ");
		//debug
    	isManaged(pwdPolicy);
		return pwdPolicy;
	}
    
    /**
     * Return the class's reference to the User_Profile entity stored in the <i>userProfile</i> field
     * @return reference to the User_Profile entity
     */
    public User_Profile getUserProfile() {
		return userProfile;
	}
    
	/**
	 * Test whether the instance is an entity.
	 * 
	 * @param clazz the instance to test. 
	 * @return boolean indicating whether the given instance is an entity.
	 */
	private boolean isEntity(Class<?> clazz) {
		System.out.println("com.yardi.ejb.UserProfileBean.isEntity() 0029 ");
	    boolean foundEntity = false;
	    Set<EntityType<?>> entities = em.getMetamodel().getEntities();
	    
	    for(EntityType<?> entityType :entities) {
	        Class<?> entityClass = entityType.getJavaType();
	        
	        if(entityClass.equals(clazz)) {
	            foundEntity = true;
	        }
	    }
	    
		System.out.println("com.yardi.ejb.UserGroupsBean isEntity() 002A " + foundEntity);
	    return foundEntity;
	}
			
	/**
	 * Test whether the EntityManager is joined to a transaction.
	 * 
	 * @return boolean indicating whether the EntityManager is joined to the current transaction.
	 */
    private boolean isJoined() {
  		System.out.println("com.yardi.ejb.UserProfileBean isJoined() 0019 "
  				+ "\n"
  				+ "   isJoined="
  				+ em.isJoinedToTransaction()
  				);
		return em.isJoinedToTransaction();
	}
    
    /**
	 * Test whether the persistence context contains the given Full_User_Profile.<p>
	 * 
	 * If <i>userProfile</i> is null return false.<br><br>
	 * 
	 * If <i>userProfile</i> is not an entity return false
	 * 
	 * @param userProfile the Full_User_Profile entity to test
	 * @return boolean indicating whether the persistence context contains specified Full_User_Profile 
	 */
	private boolean isManaged(Full_User_Profile userProfile) {
	  	System.out.println("com.yardi.ejb.UserProfileBean.isManaged() 002B ");
	  		
  		if (userProfile==null) {
  	  		System.out.println(
  	  				  "com.yardi.ejb.UserProfileBean.isManaged() 002C "
  	  				+ "\n"
	  				+ "   em.contains(Full_User_Profile)=false"
	  				);
	  		return false;
  		} 
  		
  		if (isEntity(userProfile.getClass())==false) {
  	  		System.out.println(
	  				  "com.yardi.ejb.UserProfileBean.isManaged() 002D "
	  				+ "\n"
	  				+ "   em.contains(Full_User_Profile)=false"
	  				);
	  		return false;
  		}

		System.out.println("com.yardi.ejb.UserProfileBean.isManaged() 0027 "
				+ "\n "
				+ "   em.contains(Full_User_Profile)="
				+ em.contains(userProfile)
				);
    	return em.contains(userProfile);
	}

    /**
	 * Test whether the persistence context contains the given Pwd_Policy.<p>
	 * 
	 * If <i>pwdPolicy</i> is null return false.<br><br>
	 * 
	 * If <i>pwdPolicy</i> is not an entity return false
	 * 
	 * 
	 * @param pwdPolicy the Pwd_Policy entity to test
	 * @return boolean indicating whether the persistence context contains the given Pwd_Policy 
	 */
	private boolean isManaged(Pwd_Policy pwdPolicy) {
	  	System.out.println("com.yardi.ejb.UserProfileBean.isManaged() 002E ");

  		if (pwdPolicy==null) {
  	  		System.out.println(
  	  				  "com.yardi.ejb.UserProfileBean.isManaged() 002F "
  	  				+ "\n"
	  				+ "   em.contains(Pwd_Policy)=false"
	  				);
	  		return false;
  		} 
  		
  		if (isEntity(pwdPolicy.getClass())==false) {
  	  		System.out.println(
	  				  "com.yardi.ejb.UserProfileBean.isManaged() 0030 "
	  				+ "\n"
	  				+ "   em.contains(Pwd_Policy)=false"
	  				);
	  		return false;
  		}

		System.out.println("com.yardi.ejb.UserProfileBean.isManaged() 001D "
				+ "\n "
				+ "   em.contains(Pwd_Policy)="
				+ em.contains(pwdPolicy)
				);
    	return em.contains(pwdPolicy);
	}
	
	/**
	 * Test whether the persistence context contains the given User_Profile.<p>
	 * 
	 * If <i>userProfile</i> is null return false.<br><br>
	 * 
	 * If <i>userProfile</i> is not an entity return false.
	 * 
	 * @param userProfile the User_Profile entity to test.
	 * @return boolean indicating whether the persistence context contains the given User_Profile.
	 */
	private boolean isManaged(User_Profile userProfile) {
		System.out.println("com.yardi.ejb.UserProfileBean.isManaged() 0031 ");

  		if (userProfile==null) {
  	  		System.out.println(
  	  				  "com.yardi.ejb.UserProfileBean.isManaged() 0032 "
  	  				+ "\n"
	  				+ "   em.contains(User_Profile)=false"
	  				);
	  		return false;
  		} 
  		
  		if (isEntity(userProfile.getClass())==false) {
  	  		System.out.println(
	  				  "com.yardi.ejb.UserProfileBean.isManaged() 0033 "
	  				+ "\n"
	  				+ "   em.contains(User_Profile)=false"
	  				);
	  		return false;
  		}

		System.out.println("com.yardi.ejb.UserProfileBean.isManaged() 0018 "
				+ "\n "
				+ "   em.contains(User_Profile)="
				+ em.contains(userProfile)
				);
    	return em.contains(userProfile);
	}
	
	/**
     * Update the user profile to reflect successful login.<p> 
     * 
     * Password attempts is set to zero, disabled date is set to null, last login date is today.<br><br>
     * 
     * Merges the state in the <i>userProfile</i> field into the persistence context.
     */
    public void loginSuccess() {
    	//debug
    	System.out.println("com.yardi.ejb.UserProfileBean loginSuccess() 0000 ");
    	//debug
		isJoined();
    	isManaged(userProfile);
    	userProfile.setUpPwdAttempts((short) 0);
    	userProfile.setUpDisabledDate(null);
    	userProfile.setUpLastLoginDate(new java.sql.Timestamp(new java.util.Date().getTime()));
    	User_Profile managedUserProfile = em.merge(userProfile);
    	managedUserProfile.setUpPwdAttempts(  userProfile.getUpPwdAttempts());
    	managedUserProfile.setUpDisabledDate( userProfile.getUpDisabledDate());
    	managedUserProfile.setUpLastLoginDate(userProfile.getUpLastLoginDate());
    	//debug
		System.out.println("com.yardi.ejb.UserProfileBean loginSuccess() 0003 "
				+ "\n "
				+ "   userName=" 
				+ userProfile.getUpUserid()
				+ "\n "
				+ "   upPwdAttempts="
				+ userProfile.getUpPwdAttempts()
				+ "\n "
				+ "   upDisabledDate="
				+ userProfile.getUpDisabledDate()
				+ "\n "
				+ "   loginDate=" 
				+ userProfile.getUpLastLoginDate()
				+ "\n "
				+ "   isManaged(managedUserProfile)="
				+ isManaged(managedUserProfile)
				);
		//debug
		isJoined();
    	isManaged(managedUserProfile);
    }
	
    /**
     * Merge the given Full_User_Profile state into the persistence context.
     * 
     * @param userProfile entity containing the state to be merged
     * @return the managed Full_User_Profile that the state was merged to
     */
    public Full_User_Profile merge(Full_User_Profile userProfile) {
    	/*debug*/
    	System.out.println("com.yardi.ejb.UserProfileBean merge() 0026 ");
    	/*debug*/
    	isJoined();
    	isManaged(userProfile);
    	Full_User_Profile mergedUserProfile = em.merge(userProfile);
    	isManaged(mergedUserProfile);
    	return mergedUserProfile;
    }
    
	/**
     * Persist a Full_User_Profile 
     * 
     * @param userProfile the Full_User_Profile to persist.
     */
	public void persist(Full_User_Profile userProfile) {
		/*debug*/
    	System.out.println("com.yardi.ejb.UserProfileBean.persist() 0028 ");
		/*debug*/
		isJoined();
		em.persist(userProfile);
		isManaged(userProfile);
	}
	
	@PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.UserProfileBean postConstructCallback() 0016 ");
    	getPwdPolicy();
    }
	
	/**
	 * Remove the given Full_User_Profile entity.<p>
	 *  
	 * @param userProfile the entity to remove.
	 */
	public void remove(com.yardi.ejb.model.Full_User_Profile userProfile) {
		/*debug*/
    	System.out.println("com.yardi.ejb.UserProfileBean.remove() 0024 ");
		/*debug*/
		isJoined();
		
		if (userProfile!=null) {
			/*debug*/
	    	System.out.println("com.yardi.ejb.UserProfileBean.remove() 0025 ");
			/*debug*/
			em.remove(userProfile);			
		}
		
		isManaged(userProfile);
	}

	/**
	 *  Stateful session bean remove method. Called by clients to release resources used by com.yardi.ejb.UserProfileBean.
	 */
	@Remove
	public void removeBean() {
		System.out.println("com.yardi.ejb.UserProfileBean removeBean() 0007 ");
	}
	
	/**
	 * Obtain a reference to the password policy from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy() and store it in the <i>pwdPolicy</i> field.<p>
	 * 
	 * Provides feedback: <pre>YRD000B password policy is missing</pre>
	 * 
	 */
	private void setPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean setPwdPolicy() 0014 ");
		//debug
		isJoined();
		pwdPolicy = passwordPolicyBean.getPwdPolicy();
		
		if (pwdPolicy == null) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
			System.out.println("com.yardi.ejb.UserProfileBean setPwdPolicy() pwdPolicy==null 0011 ");
			return;
		}
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean setPwdPolicy() 000B "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			+ "\n"
			+ "   feedback="
			+ feedback);
		//debug
		isManaged(pwdPolicy);
	}
	
	/**
	 * Update the number of invalid password attempts since the last successful login in the User_Profile entity.<p>
	 * 
	 * The state stored in the <i>userProfile</i> field is merged into the persistence context.
	 * @param pwdAttempts the value to set
	 */
	public void setUpPwdAttempts(short pwdAttempts) {
    	//debug
    	System.out.println("com.yardi.ejb.UserProfileBean setUpPwdAttempts() 0005 ");
    	//debug
		isJoined();
    	userProfile.setUpPwdAttempts(pwdAttempts);
    	User_Profile managedUserProfile = em.merge(userProfile);
    	managedUserProfile.setUpPwdAttempts(pwdAttempts);
		isJoined();
    	isManaged(managedUserProfile);
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean setUpPwdAttempts() 0001 "
				+ "\n "
				+ "  userName=" + userProfile.getUpUserid()
				+ "\n "
				+ "  pwdAttempts=" + userProfile.getUpPwdAttempts()
				);
		//debug
    }

	/**
	 * Inject the given User_Profile entity.<p>
	 * 
	 * During login clients inject the User_Profile entity because a reference has been obtained prior to this point and the class should use that reference
	 * 
	 * @param userProfile the User_Profile entity to inject. 
	 */
	public void setUserProfile(User_Profile userProfile) {
		//debug
		System.out.println("com.yardi.ejb.UserProfileBean setUserProfile() 001F ");
		//debug
		isJoined();
		this.userProfile = userProfile;
		isManaged(this.userProfile);
	}
}
