package com.yardi.ejb;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import com.yardi.ejb.model.Login_User_Profile;
import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.userServices.PasswordAuthentication;

/**
 * Session Bean implementation class UserProfileBean
 */
@Stateful
public class LoginUserProfileBean implements LoginUserProfile {
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
	private Login_User_Profile userProfile;
	@EJB PasswordPolicy passwordPolicyBean;

    public LoginUserProfileBean() {
    	System.out.println("com.yardi.ejb.LoginUserProfileBean LoginUserProfileBean() 0015");
    }

	@PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.LoginUserProfileBean postConstructCallback() 0016");
    	getPwdPolicy();
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
		//debug
		System.out.println("com.yardi.ejb.LoginUserProfileBean authenticate() 0013");
		//debug
		isJoined();
		isManaged(userProfile);
		feedback = com.yardi.rentSurvey.YardiConstants.YRD0000;
		java.sql.Timestamp today = new java.sql.Timestamp(new java.util.Date().getTime());

		if (pwdPolicy==null) { //get the password policy
			//debug
			System.out.println("com.yardi.ejb.LoginUserProfileBean authenticate() 0010 pwdPolicy == null");
			//debug
			feedback = com.yardi.rentSurvey.YardiConstants.YRD000B;
			return false;
		}

		if (userProfile == null) {
			//debug
			System.out.println("com.yardi.ejb.LoginUserProfileBean authenticate() 0006 userProfile == null");
			//debug
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0001;
			return false;
		}

		if (userProfile.getUpDisabledDate() != null) {
			//debug
			System.out.println("com.yardi.ejb.LoginUserProfileBean authenticate() 0008 userProfile.getUpDisabledDate() != null\n");
			//debug
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0003;
			return false;
		}

		if (userProfile.getUpActiveYn().equals("N")) {
			//debug
			System.out.println("com.yardi.ejb.LoginUserProfileBean authenticate() 0009 userProfile.getUpActiveYn().equals(N)");
			//debug
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0004;
			return false;
		}

		short maxSignonAttempts = pwdPolicy.getPpMaxSignonAttempts();
		long passwordExpiration = userProfile.getUpPwdexpd().getTime();
		short signonAttempts = userProfile.getUpPwdAttempts();
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication(); 
		boolean pwdValid = passwordAuthentication.authenticate(password.toCharArray(), userProfile.getUptoken());

		if (pwdValid == false) {
			System.out.println("com.yardi.ejb.LoginUserProfileBean authenticate() 0017");
			feedback = com.yardi.rentSurvey.YardiConstants.YRD000F;
			signonAttempts++;
			setUpPwdAttempts(signonAttempts);
			//debug
			System.out.println("com.yardi.ejb.LoginUserProfileBean authenticate() 0012"
					+ "\n "
					+ "  feedback =" + feedback  
					+ "\n "
					+ "  signonAttempts = " + signonAttempts
					);
			//debug

			if (signonAttempts == maxSignonAttempts) {
				disable();
				//debug
				System.out.println("com.yardi.ejb.LoginUserProfileBean authenticate() 000C"
						+ "\n "
						+ "  signonAttempts == maxSignonAttempts"  
						);
				//debug

				feedback = com.yardi.rentSurvey.YardiConstants.YRD000C;
				//debug
				System.out.println("com.yardi.ejb.LoginUserProfileBean authenticate() 000D"
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
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0002;
				//debug
				System.out.println("com.yardi.ejb.LoginUserProfileBean authenticate() 000E"
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
				System.out.println("com.yardi.ejb.LoginUserProfileBean authenticate() 000F"
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

    public void changeUserToken(final char [] newPassword) {
    	//debug
    	System.out.println("com.yardi.ejb.LoginUserProfileBean changeUserToken() 0002 ");
    	//debug
		isJoined();
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		String userToken = passwordAuthentication.hash(newPassword); //hash of new password
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		gc.add(Calendar.DAY_OF_MONTH, new Short(pwdPolicy.getPpDays()).intValue()); //new password expiration date
		//debug
		System.out.println("com.yardi.ejb.LoginUserProfileBean changeUserToken() 0004 "
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
		Login_User_Profile managedUserProfile = em.merge(userProfile);
		managedUserProfile.setUptoken(userProfile.getUptoken());
		managedUserProfile.setUpPwdexpd(userProfile.getUpPwdexpd());
		isJoined();
    	isManaged(managedUserProfile);
    }
    
    private void disable() {
    	//debug
    	System.out.println("com.yardi.ejb.LoginUserProfileBean disable() 001A ");
    	//debug
		isJoined();
    	userProfile.setUpPwdAttempts(pwdPolicy.getPpMaxSignonAttempts());
    	userProfile.setUpDisabledDate(new java.sql.Timestamp(new java.util.Date().getTime()));
    	Login_User_Profile managedUserProfile = em.merge(userProfile);
    	managedUserProfile.setUpPwdAttempts(userProfile.getUpPwdAttempts());
    	managedUserProfile.setUpDisabledDate(userProfile.getUpDisabledDate());
		isJoined();
    	isManaged(managedUserProfile);
		//debug
		System.out.println("com.yardi.ejb.LoginUserProfileBean disable() 001B "
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
    
    public String getFeedback() {
		return feedback;
	}
    
    private Pwd_Policy getPwdPolicy() {
    	//debug
    	System.out.println("com.yardi.ejb.LoginUserProfileBean getPwdPolicy() 001C ");
    	//debug
    	isJoined();
		
		if (pwdPolicy==null) {
	    	//debug
	    	System.out.println("com.yardi.ejb.LoginUserProfileBean getPwdPolicy() 0020 ");
	    	//debug
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb.LoginUserProfileBean getPwdPolicy() 000A "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			);

		System.out.println("com.yardi.ejb.LoginUserProfileBean getPwdPolicy() 001E ");
		//debug
    	isManaged(pwdPolicy);
		return pwdPolicy;
	}
    
    public Login_User_Profile getUserProfile() {
		return userProfile;
	}

	private boolean isJoined() {
  		System.out.println("com.yardi.ejb.LoginUserProfileBean isJoined() 0019 "
  				+ "\n"
  				+ "   isJoined="
  				+ em.isJoinedToTransaction()
  				);
		return em.isJoinedToTransaction();
	}
	
	private boolean isManaged(Login_User_Profile userProfile) {
		System.out.println("com.yardi.ejb.LoginUserProfileBean isManaged() 0018 "
				+ "\n "
				+ "   em.contains(userProfile)="
				+ em.contains(userProfile)
				);
    	return em.contains(userProfile);
	}
	
	private boolean isManaged(Pwd_Policy pwdPolicy) {
		System.out.println("com.yardi.ejb.LoginUserProfileBean isManaged() 001D "
				+ "\n "
				+ "   em.contains(pwdPolicy)="
				+ em.contains(pwdPolicy)
				);
    	return em.contains(pwdPolicy);
	}
	
	/**
     * Update the user profile to reflect successful login. Password attempts is set to zero, disabled date is set to null,
     * last login date is today. 
     * 
     * @param userName
     */
    public void loginSuccess() {
    	//debug
    	System.out.println("com.yardi.ejb.LoginUserProfileBean loginSuccess() 0000 ");
    	//debug
		isJoined();
    	isManaged(userProfile);
    	userProfile.setUpPwdAttempts((short) 0);
    	userProfile.setUpDisabledDate(null);
    	userProfile.setUpLastLoginDate(new java.sql.Timestamp(new java.util.Date().getTime()));
    	Login_User_Profile managedUserProfile = em.merge(userProfile);
    	managedUserProfile.setUpPwdAttempts(  userProfile.getUpPwdAttempts());
    	managedUserProfile.setUpDisabledDate( userProfile.getUpDisabledDate());
    	managedUserProfile.setUpLastLoginDate(userProfile.getUpLastLoginDate());
    	//debug
		System.out.println("com.yardi.ejb.LoginUserProfileBean loginSuccess() 0003 "
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

	@Remove
	public void removeBean() {
		System.out.println("com.yardi.ejb.LoginUserProfileBean removeBean() 0007 ");
	}
	
	private void setPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb.LoginUserProfileBean setPwdPolicy() 0014 ");
		//debug
		isJoined();
		pwdPolicy = passwordPolicyBean.getPwdPolicy();
		
		if (pwdPolicy == null) {
			feedback = com.yardi.rentSurvey.YardiConstants.YRD000B;
			System.out.println("com.yardi.ejb.LoginUserProfileBean setPwdPolicy() pwdPolicy==null 0011 ");
			return;
		}
		//debug
		System.out.println("com.yardi.ejb.LoginUserProfileBean setPwdPolicy() 000B "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			+ "\n"
			+ "   feedback="
			+ feedback);
		//debug
		isManaged(pwdPolicy);
	}
	
	public void setUpPwdAttempts(short pwdAttempts) {
    	//debug
    	System.out.println("com.yardi.ejb.LoginUserProfileBean setUpPwdAttempts() 0005 ");
    	//debug
		isJoined();
    	userProfile.setUpPwdAttempts(pwdAttempts);
    	Login_User_Profile managedUserProfile = em.merge(userProfile);
    	managedUserProfile.setUpPwdAttempts(pwdAttempts);
		isJoined();
    	isManaged(managedUserProfile);
		//debug
		System.out.println("com.yardi.ejb.LoginUserProfileBean setUpPwdAttempts() 0001 "
				+ "\n "
				+ "  userName=" + userProfile.getUpUserid()
				+ "\n "
				+ "  pwdAttempts=" + userProfile.getUpPwdAttempts()
				);
		//debug
    }

	public void setUserProfile(Login_User_Profile userProfile) {
		//debug
		System.out.println("com.yardi.ejb.LoginUserProfileBean setUserProfile() 001F ");
		//debug
		isJoined();
		this.userProfile = userProfile;
		isManaged(this.userProfile);
	}
}
