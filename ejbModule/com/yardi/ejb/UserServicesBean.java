package com.yardi.ejb;

import java.security.NoSuchAlgorithmException;
import java.util.Vector;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.transaction.UserTransaction;

//import com.yardi.ejb.UserServices;
import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.shared.userServices.InitialPage;
import com.yardi.shared.userServices.LoginRequest;
import com.yardi.shared.userServices.LoginResponse;
import com.yardi.shared.userServices.PasswordAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;


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
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class UserServicesBean implements UserServices {
	private String feedback = "";
	private java.sql.Timestamp today = new java.sql.Timestamp(new java.util.Date().getTime());
	private Pwd_Policy pwdPolicy = null;
	private User_Profile userProfile = null;
	private LoginRequest loginRequest;
	private LoginResponse loginResponse;
	private Vector<InitialPage> initialPageList;
	private String initialPage = "";
	private String sessionID = "";
	@EJB UserProfile userProfileBean; //bean is thread safe unless marked reentrant in the deployment descriptor
	@EJB UniqueTokens uniqueTokensBean;
	@EJB PasswordPolicy passwordPolicyBean;
	@EJB UserGroups userGroupsBean;
	@EJB SessionsTable sessionsBean;
	@EJB PwdCompositionRules pwdCompRulesBean;
	@Resource UserTransaction tx;

	public UserServicesBean() {
	}

	private void loginSuccess() {
		/*
		 * Successful login.
		 * 1 lookup initial page with join Groups_Master and User_Groups
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
		String msg [] = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("=");
		//debug
		
		initialPage = userGroupsBean.getInitialPage(loginRequest.getUserName());
		msg = userGroupsBean.getFeedback().split("=");
		feedback = msg[0];
		initialPageList = userGroupsBean.getInitialPageList();
		//tokenize the session ID
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		String sessionToken="";
		try {
			sessionToken = passwordAuthentication.hash(loginRequest.getSessionID().toCharArray());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		//fetch the session table row for the session
		Sessions_Table sessionsTable = null;
		sessionsTable = sessionsBean.findSession(loginRequest.getSessionID()); 

		if (sessionsTable == null) {
			sessionsBean.persist(
					loginRequest.getUserName(), 
					loginRequest.getSessionID(), 
					sessionToken,
					initialPage, 
					new java.util.Date());
			//debug
			System.out.println("com.yardi.ejb UserServicesBean loginSuccess 001D"
				+ "\n"
				+ "  Sessions_Table="
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
			System.out.println("com.yardi.ejb UserServicesBean loginSuccess 001E"
				+ "\n"
				+ "  Sessions_Table="
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
		System.out.println("com.yardi.ejb UserServicesBean loginSuccess 0016");
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
		System.out.println("com.yardi.ejb UserServicesBean chgPwd() 0032");
		//debug
		String userName;
		userName            = loginRequest.getUserName();
		char [] oldPassword = loginRequest.getPassword().toCharArray();
		char [] newPassword = loginRequest.getNewPassword().toCharArray();
		//debug
		System.out.println("com.yardi.ejb UserServicesBean chgPwd() 001A"
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
			System.out.println("com.yardi.ejb UserServicesBean chgpwd() 0030");
			//debug
			tx.begin();
			getPwdPolicy();
			short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
			short pwdLifeInDays   = pwdPolicy.getPpDays();
			//debug
			System.out.println("com.yardi.ejb UserServicesBean chgpwd() 0033"
					+ "\n "
					+ "  maxUniqueTokens="
					+ maxUniqueTokens 
					+ "\n "
					+ "  pwdLifeInDays="
					+ pwdLifeInDays 
					);
			//debug
			Vector<Unique_Tokens> userTokens = uniqueTokensBean.findTokens(userName);
			userTokens = uniqueTokensBean.removeExtraTokens(userTokens);
			//debug
			if (!(userTokens==null)) {
				System.out.println("com.yardi.ejb UserServicesBean chgpwd() 0026"
						+ "\n"
						+ "   Vector<Unique_Tokens>="
						+ userTokens
						);
			}
			//debug				
			if (pwdCompRulesBean.enforce(new String(newPassword), userName, userProfile.getUptoken(), userTokens) == false ) {
				//apply password policy to the new password
				//debug
				System.out.println("com.yardi.ejb UserServicesBean chgPwd() 0010 "
						+ "\n "
						+ "  pwdCompRulesBean.enforce() == false"
						);   
				//debug
				feedback = passwordPolicyBean.getFeedback();
				rollback(tx);
				return false;
			}

			//debug
			if (!(userTokens==null)) {
				System.out.println("com.yardi.ejb UserServicesBean chgpwd() 0027"
						+ "\n"
						+ "   Vector<Unique_Tokens>="
						+ userTokens
						);
			}
			//debug				
			uniqueTokensBean.removeOldestToken(userTokens);
			//debug
			System.out.println("com.yardi.ejb UserServicesBean chgpwd() 0014");
			//debug

			if (maxUniqueTokens > 0) {
				userProfile = findUserProfile(userName);
				//debug
				System.out.println("com.yardi.ejb UserServicesBean chgpwd() 0020"
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
			System.out.println("com.yardi.ejb UserServicesBean chgpwd() 0025");
			//debug
			changeUserToken(userName, newPassword);
			loginSuccess();
			tx.commit();
			return true;
		} catch (Exception e) {
			System.out.println("com.yardi.ejb UserServicesBean chgpwd() 0034"
					+ "\n"
					+ "   exception="
					+ e
					);
			e.printStackTrace();
			rollback(tx);
		}
		//debug
		System.out.println("com.yardi.ejb UserServicesBean chgpwd() 0031");
		//debug
		return true;
	}

	/**
	 * Support for authentication. 
	 * 1. Get the password policy
	 * 2. Validate the user name against the User_Profile table
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
				UserProfileBean.authenticate all feedback
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
			System.out.println("com.yardi.ejb UserServicesBean authenticate() 0028");
			//debug
			tx.begin();
			boolean authenticated = userProfileBean.authenticate(loginRequest.getUserName(),
					loginRequest.getPassword(), loginRequest.getChangePwd());
			feedback = userProfileBean.getFeedback();

			if (authenticated) {
				//debug
				System.out.println("com.yardi.ejb UserServicesBean authenticate() 0029");
				//debug
				loginSuccess();
				tx.commit();
			} else {
				//debug
				System.out.println("com.yardi.ejb UserServicesBean authenticate() 002A");
				//debug

				if (feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000C) ||
						feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000F)) {
					/*
					 * Need to distinguish between invalid password and invalid user name. If its invalid password 
					 * set feedback to YRD0001 because html does not distinguish between invalid password and invalid 
					 * user name. Also html specifically checks for YRD0001.
					 */
					//debug
					System.out.println("com.yardi.ejb UserServicesBean authenticate() 002B");
					//debug
					if (feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000F)) {
						feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0001;
					}
					tx.commit();
				} else {
					//debug
					System.out.println("com.yardi.ejb UserServicesBean authenticate() 002C");
					//debug
					rollback(tx);
				}
			}

			//debug
			System.out.println("com.yardi.ejb UserServicesBean authenticate() 002D");
			//debug
			return authenticated;
		} catch (Exception e) {
			System.out.println("com.yardi.ejb UserServicesBean authenticate() 0035"
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

	private Pwd_Policy getPwdPolicy() {
		
		if (pwdPolicy == null) {
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb UserServicesBean getPwdPolicy 0017"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy);
		//debug
		return pwdPolicy;
	}

	private void setPwdPolicy() {
		pwdPolicy = passwordPolicyBean.getPwdPolicy();
		
		if (pwdPolicy == null) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
		}
		//debug
		System.out.println("com.yardi.ejb UserServicesBean setPwdPolicy 001F"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy
			+ "\n"
			+ "   feedback="
			+ feedback);
		//debug
	}

	private User_Profile findUserProfile(String userName) {
		userProfile = null;
		userProfile = userProfileBean.find(userName);
		
		if (userProfile == null) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0001;
		}
		
		return userProfile;
	}

	public void setLoginRequest(LoginRequest loginRequest) {
		this.loginRequest = loginRequest;
		//debug
		System.out.println("com.yardi.ejb UserServicesBean setLoginRequest() 001B " + toString());
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
			System.out.println("com.yardi.ejb UserServicesBean setLoginResponse() 0000"
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
	private void changeUserToken(final String userName, final char [] newPassword) {
		userProfileBean.changeUserToken(userName, newPassword); //store new token in user profile
		userProfileBean.loginSuccess(userName);
		feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
	}
	
	/**
	 * Attempt to roll back the transaction
	 * @param tx - The transaction to roll back
	 */
	private void rollback(UserTransaction tx) {
		//debug
		System.out.println("com.yardi.ejb UserServicesBean rollback() 0011");
		//debug
		try {
			tx.rollback();
		} catch (Exception e) {
			System.out.println("com.yardi.ejb UserServicesBean rollback() 0001"
					+ "\n"
					+ "   exception="
					+ e
					);	
			e.printStackTrace();
		}
	}
	
	@Override
	@Remove
	public void remove() {
		System.out.println("com.yardi.ejb.UserServicesBean remove() 0002 ");
		userGroupsBean.removeBean();
		userProfileBean.removeBean();	
		pwdCompRulesBean.removeBean();
	}
	
	@Override
	public String toString() {
		return "UserServicesBean [feedback=" + feedback + ", today=" + today + ", pwdPolicy=" + pwdPolicy + ", userProfile="
				+ userProfile + ", loginRequest=" + loginRequest + ", loginResponse=" + loginResponse
				+ ", initialPageList=" + initialPageList + ", initialPage=" + initialPage + ", userProfileBean="
				+ userProfileBean + ", uniqueTokensBean=" + uniqueTokensBean + ", passwordPolicyBean="
				+ passwordPolicyBean + ", userGroupsBean=" + userGroupsBean + ", sessionsBean=" + sessionsBean + ", tx="
				+ tx + ", sessionID=" + sessionID + "]";
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}
