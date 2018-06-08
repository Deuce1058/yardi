package com.yardi.ejb;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;

import com.yardi.ejb.UniqueTokens;
import com.yardi.ejb.UniqueTokensSesssionBeanRemote;
import com.yardi.ejb.UserProfileSessionBeanRemote;
import com.yardi.ejb.UserServicesRemote;
import com.yardi.userServices.InitialPage;
import com.yardi.userServices.LoginRequest;
import com.yardi.userServices.LoginResponse;
import com.yardi.userServices.PasswordAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.PasswordPolicySessionBeanRemote;
import com.yardi.ejb.PpPwdPolicy;
import com.yardi.ejb.UserProfile;


/**
 * Services for authentication and user profile management:
 * 1. Authentication
 * 2. Change password
 * 3. Password policy
 * 
 * https://stackoverflow.com/questions/2860943/how-can-i-hash-a-password-in-java
 * Hash passwords for storage, and test passwords against password tokens.
 * 
 * Instances of this class can be used concurrently by multiple threads.
 *
 * @author Jim
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class UserServices implements UserServicesRemote {
	//private PasswordAuthentication passwordAuthentication = null;
	//private String userName = "";
	private String feedback = "";
	private java.sql.Timestamp today = new java.sql.Timestamp(new java.util.Date().getTime());
	private PpPwdPolicy pwdPolicy = null;
	private UserProfile userProfile = null;
	private LoginRequest loginRequest;
	private LoginResponse loginResponse;
	private Vector<InitialPage> initialPageList;
	private String initialPage = "";
	@EJB UserProfileSessionBeanRemote userProfileBean; //bean is thread safe unless marked reentrant in the deployment descriptor
	@EJB UniqueTokensSesssionBeanRemote uniqueTokensBean;
	@EJB PasswordPolicySessionBeanRemote passwordPolicyBean;
	@EJB UserGroupsSessionBeanRemote userGroupsBean;
	@EJB SessionsTableSessionBeanRemote sessionsBean;
	@Resource UserTransaction tx;

	public UserServices() {
	}

	public void loginSuccess() {
		/*
		 * Successful login.
		 * 1 lookup initial page with join GroupsMaster and UserGroups
		 *   1A if user is in multiple groups set ST_LAST_REQUEST to the html select group page. User picks the initial page
		 *   1B if user is in only one group set ST_LAST_REQUEST to GM_INITIAL_PAGE
		 * 2 Set user ID as session attribute  
		 * 3 Write/update session table
		 *   3A tokenize session ID. This serves as a password for the session to login. It is not enough for the session 
		 *      to be in the session table, the session must also login in order for the session to be considered authentic.
		 *   3B CreateTokenService is used to create a token from the session ID
		 * 4 Respond to yardiLogin.html/changePwd.html  
		 */

		//msg is needed for the response to yardiLogin.html/changePwd.html but depends on userGroups.size() 
		String msg [] = com.yardi.rentSurvey.YardiConstants.YRD0000.split("=");
		//what groups is the user in and what is the initial page for the group?
		//Vector<UserGroupsGraph> userGroups = userGroupsBean.find(loginRequest.getUserName());
		//need initialPage for the sessions table and in the response to yardiLogin.html/changePwd.html so set it here
		//initialPage = userGroups.get(0).getGmInitialPage(); //GM_INITIAL_PAGE from GROUPS_MASTER
		//debug
		/*
		System.out.println("com.yardi.ejb UserServices loginSuccess 0012" 
				+ "\n"
				+ "   initialPage="
				+ initialPage
				);
		System.out.println("com.yardi.ejb UserServices loginSuccess 0013");
		for (UserGroupsGraph u : userGroups) {
			System.out.println(
				  "\n"
				+ "   UserGroupsGraph=" 
				+ u.toString()
				);
		}
		*/
		//debug
		
		/*
		if (userGroups.size()>1) {
			// user is in multiple groups. Set ST_LAST_REQUEST to the html select group page. User picks the initial page
			initialPage = com.yardi.rentSurvey.YardiConstants.USER_SELECT_GROUP_PAGE;
			msg = com.yardi.rentSurvey.YardiConstants.YRD000E.split("=");
			//debug
			System.out.println("com.yardi.ejb UserServices loginSuccess 001C" 
					+ "\n"
					+ "   initialPage="
					+ initialPage
					);
			//debug
		}
		*/
		
		initialPage = userGroupsBean.getInitialPage(loginRequest.getUserName());
		msg = userGroupsBean.getFeedback().split("=");
		feedback = msg[0];
		initialPageList = userGroupsBean.getInitialPageList();
		//tokenize the session ID
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		String sessionToken = passwordAuthentication.hash(loginRequest.getSessionID().toCharArray());
		//fetch the session table row for the session
		SessionsTable sessionsTable = null;
		sessionsTable = sessionsBean.findSession(loginRequest.getSessionID()); 

		if (sessionsTable == null) {
			sessionsBean.persist(
					loginRequest.getUserName(), 
					loginRequest.getSessionID(), 
					sessionToken,
					initialPage, 
					new java.util.Date());
			//debug
			System.out.println("com.yardi.ejb UserServices loginSuccess 001D"
				+ "\n"
				+ "  SessionsTable="
				+ sessionsTable);
			//debug
		} else {
			sessionsBean.update(
					loginRequest.getUserName(), 
					loginRequest.getSessionID(), 
					sessionToken,
					initialPage, 
					new java.util.Date());
			//debug
			System.out.println("com.yardi.ejb UserServices loginSuccess 001E"
				+ "\n"
				+ "  SessionsTable="
				+ sessionsTable);
			//debug
		}

		/*
		initialPageList = new Vector<InitialPage>();

		for (UserGroupsGraph g : userGroups) {
			//getGmDescription returns a string containing the short description for the button and a label for the button
			//getGmInitialPage() returns the url value for url= attribute
			initialPageList.add(new InitialPage(g.getGmDescription(),
				g.getGmInitialPage()));
		}
		*/
		//debug
		System.out.println("com.yardi.ejb UserServices loginSuccess 0016");
		for (InitialPage i : initialPageList) {
			System.out.println(
				  "\n"
				+ "   initialPageList="
				+ i
			);
		}
		//debug
		setLoginResponse();
	}
/**
 * Support for changing password either on demand or when current password has expired.
 * Table PP_PWD_POLICY has parameters that control password policy
 * 
 * Steps:
 * 1 Authenticate with current credentials
 * 2 Based on the password policy (table PP_PWD_POLICY):
 *   2a Remove any extra rows in UNIQUE_TOKENS table if more tokens are being stored than are required
 *   2b Determine whether the new password matches a previously used password
 *   2c Store the tokenized new password in UNIQUE_TOKENS table
 *   2d Store the tokenized new password in the USER_PROFILE table
 *   2e Set the password expiration date and store it in the USER_PROFILE table
 * 
 * Instance variable feedback indicates the status of the change password process 
 * 
 * @param userName
 * @param oldPassword
 * @param newPassword
 * @return Boolean
 */
	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean chgPwd() {
		//debug
		System.out.println("com.yardi.ejb UserServices chgPwd() 0032");
		//debug
		String userName;
		userName            = loginRequest.getUserName();
		char [] oldPassword = loginRequest.getPassword().toCharArray();
		char [] newPassword = loginRequest.getNewPassword().toCharArray();
		//debug
		System.out.println("com.yardi.ejb UserServices chgPwd() 001A"
				+ "\n "
				+ "  userName ="
				+ userName
				+ "\n "
				+ "  oldPassword ="
				+ new String(oldPassword)
				+ "\n "
				+ "  newPassword ="
				+ new String(newPassword)
				);
		//debug

		if (authenticate() == false) {
			return false;
		}

			/*
			 * max  stored  del elements
           0    5       0 - size
                1       0 - size
                0       nothing then insert

           1    5       0 - size then insert
                1       0 - size then insert
                0       nothing then insert

           2    5       1 - size then insert
                1       1 - size then insert
                0       nothing then insert

           3    5       2 - size then insert
                1       2 - size then insert
                0       nothing then insert
			 */

		try {
			//debug
			System.out.println("com.yardi.ejb UserServices chgpwd() 0030");
			//debug
			tx.begin();
			getPwdPolicy();
			short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
			short pwdLifeInDays   = pwdPolicy.getPpDays();
			//debug
			System.out.println("com.yardi.ejb UserServices chgpwd() 0033"
					+ "\n "
					+ "  maxUniqueTokens="
					+ maxUniqueTokens 
					+ "\n "
					+ "  pwdLifeInDays="
					+ pwdLifeInDays 
					);
			//debug
			Vector<UniqueTokens> userTokens = uniqueTokensBean.findTokens(userName);
			userTokens = uniqueTokensBean.removeExtraTokens(userTokens);
			//debug
			if (!(userTokens==null)) {
				System.out.println("com.yardi.ejb UserServices chgpwd() 0026"
						+ "\n"
						+ "   Vector<UniqueTokens>="
						+ userTokens
						);
			}
			//debug				
			if (passwordPolicyBean.enforce(new String(newPassword), userTokens) == false ) {
				//apply password policy to the new password
				//debug
				System.out.println("com.yardi.ejb UserServices chgPwd() 0010 "
						+ "\n "
						+ "  passwordPolicy().enforce() == false"
						);   
				//debug
				feedback = passwordPolicyBean.getFeedback();
				rollback(tx);
				return false;
			}

			//debug
			if (!(userTokens==null)) {
				System.out.println("com.yardi.ejb UserServices chgpwd() 0027"
						+ "\n"
						+ "   Vector<UniqueTokens>="
						+ userTokens
						);
			}
			//debug				
			uniqueTokensBean.removeOldestToken(userTokens);
			//debug
			System.out.println("com.yardi.ejb UserServices chgpwd() 0014");
			//debug

			if (maxUniqueTokens > 0) {
				userProfile = findUserProfile(userName);
				//debug
				System.out.println("com.yardi.ejb UserServices chgpwd() 0020"
						+ "\n"
						+ "   username="
						+ userName
						+ "\n"
						+ "   userProfile.getUptoken()="
						+ userProfile.getUptoken()
						);
				//debug
				uniqueTokensBean.persist(userName, userProfile.getUptoken(), new java.util.Date()); //insert
			}

			//debug
			System.out.println("com.yardi.ejb UserServices chgpwd() 0025");
			//debug
			changeUserToken(pwdLifeInDays, userName, newPassword);
			tx.commit();
			return true;
		} catch (Exception e) {
			System.out.println("com.yardi.ejb UserServices chgpwd() 0034"
					+ "\n"
					+ "   exception="
					+ e
					);
			e.printStackTrace();
			rollback(tx);
		}
		//debug
		System.out.println("com.yardi.ejb UserServices chgpwd() 0031");
		//debug
		return true;
	}

	/**
	 * Support for authentication. 
	 * 1. Get the password policy
	 * 2. Validate the user name against the UserProfile table
	 * 3. The account must be active
	 * 4. The account is not disabled because too many invalid passwords have been attempted. Maximum login attempts is 
	 *    determined by password policy
	 * 
	 * Instance variable feedback indicates the status of the authenticate process 
	 * 
	 * @param userName
	 * @param password
	 * @return Boolean
	 */
	public boolean authenticate() {
		try {
			/*
				UserProfileSessionBean.authenticate all feedback
				YRD000B f rollback
				YRD0001 f rollback
				YRD0003 f rollback
				YRD0004 f rollback
				YRD000F f commit
				YRD000C f commit
				YRD0002 f rollback
				YRD0000 t commit
			 */
			//debug
			System.out.println("com.yardi.ejb UserServices authenticate() 0028");
			//debug
			tx.begin();
			boolean authenticated = userProfileBean.authenticate(loginRequest.getUserName(),
					loginRequest.getPassword(), loginRequest.getChangePwd());
			feedback = userProfileBean.getFeedback();

			if (authenticated) {
				//debug
				System.out.println("com.yardi.ejb UserServices authenticate() 0029");
				//debug
				tx.commit();
			} else {
				//debug
				System.out.println("com.yardi.ejb UserServices authenticate() 002A");
				//debug

				if (feedback.equals(com.yardi.rentSurvey.YardiConstants.YRD000C) ||
						feedback.equals(com.yardi.rentSurvey.YardiConstants.YRD000F)) {
					/*
					 * Need to distinguish between invalid password and invalid user name. If its invalid password 
					 * set feedback to YRD0001 because html does not distinguish between invalid password and invalid 
					 * user name. Also html specifically checks for YRD0001.
					 */
					//debug
					System.out.println("com.yardi.ejb UserServices authenticate() 002B");
					//debug
					if (feedback.equals(com.yardi.rentSurvey.YardiConstants.YRD000F)) {
						feedback = com.yardi.rentSurvey.YardiConstants.YRD0001;
					}
					tx.commit();
				} else {
					//debug
					System.out.println("com.yardi.ejb UserServices authenticate() 002C");
					//debug
					rollback(tx);
				}
			}

			//debug
			System.out.println("com.yardi.ejb UserServices authenticate() 002D");
			//debug
			return authenticated;
		} catch (Exception e) {
			System.out.println("com.yardi.ejb UserServices authenticate() 0035"
					+ "\n"
					+ "   exception="
					+ e
					);
			e.printStackTrace();
			rollback(tx);
			return false;
		} 
	}

	public String getFeedback() {
		return feedback;
	}

	private PpPwdPolicy getPwdPolicy() {
		
		if (pwdPolicy == null) {
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb UserServices getPwdPolicy 0017"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy);
		//debug
		return pwdPolicy;
	}

	private void setPwdPolicy() {
		pwdPolicy = passwordPolicyBean.getPwdPolicy();
		
		if (pwdPolicy == null) {
			feedback = com.yardi.rentSurvey.YardiConstants.YRD000B;
		}
		//debug
		System.out.println("com.yardi.ejb UserServices setPwdPolicy 001F"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy
			+ "\n"
			+ "   feedback="
			+ feedback);
		//debug
	}

	private UserProfile findUserProfile(String userName) {
		userProfile = null;
		userProfile = userProfileBean.find(userName);
		
		if (userProfile == null) {
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0001;
		}
		
		return userProfile;
	}

	public void setLoginRequest(LoginRequest loginRequest) {
		this.loginRequest = loginRequest;
		//debug
		System.out.println("com.yardi.ejb UserServices setLoginRequest() 001B " + toString());
		//debug
	}

	public LoginRequest getLoginRequest() {
		return loginRequest;
	}
	
	public Vector<InitialPage> getInitialPageList() {
		return initialPageList;
	}

	public String getInitialPage() {
		return initialPage;
	}

	public LoginResponse getLoginResponse() {
		return loginResponse;
	}

	private void setLoginResponse() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			this.loginResponse = new LoginResponse(
				loginRequest.getUserName(),
				"", //Password
				mapper.writeValueAsString(initialPageList), //new password
				feedback,
				initialPage
			);
		} catch (Exception e) {
			System.out.println("com.yardi.ejb UserServices setLoginResponse() 0000"
					+ "\n"
					+ "   exception="
					+ e
					);
			e.printStackTrace();
		}
	}

	/**
	 * Change the user's current token stored in USER_PROFILE. Hash the new password, calculate the new password 
	 * expiration date based on password policy, delegate to the userProfileBean to update the user profile, and 
	 * note that there was a successful login (update the sessions table)
	 * 
	 * @param pwdLifeInDays
	 * @param userName
	 * @param newPassword
	 */
	private void changeUserToken(final short pwdLifeInDays, final String userName, final char [] newPassword) {
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		String userToken = passwordAuthentication.hash(newPassword); //hash of new password
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		//debug
		System.out.println("com.yardi.ejb UserServices changeUserToken() 0018 "
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
		gc.add(Calendar.DAY_OF_MONTH, pwdLifeInDays); //new password expiration date
		userProfileBean.changeUserToken(userName, userToken, new java.util.Date(gc.getTimeInMillis())); //store new token in user profile
		userProfileBean.loginSuccess(userName);
		feedback = com.yardi.rentSurvey.YardiConstants.YRD0000;
	}
	
	/**
	 * Attempt to roll back the transaction
	 * @param tx - The transaction to roll back
	 */
	private void rollback(UserTransaction tx) {
		//debug
		System.out.println("com.yardi.ejb UserServices rollback() 0011");
		//debug
		try {
			tx.rollback();
		} catch (Exception e) {
			System.out.println("com.yardi.ejb UserServices rollback() 0001"
					+ "\n"
					+ "   exception="
					+ e
					);	
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "UserServices [feedback=" + feedback + ", today=" + today + ", pwdPolicy=" + pwdPolicy + ", userProfile="
				+ userProfile + ", loginRequest=" + loginRequest + ", loginResponse=" + loginResponse
				+ ", initialPageList=" + initialPageList + ", initialPage=" + initialPage + ", userProfileBean="
				+ userProfileBean + ", uniqueTokensBean=" + uniqueTokensBean + ", passwordPolicyBean="
				+ passwordPolicyBean + ", userGroupsBean=" + userGroupsBean + ", sessionsBean=" + sessionsBean + ", tx="
				+ tx + "]";
	}
}
