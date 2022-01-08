package com.yardi.ejb;

import java.util.Arrays;
import java.util.Vector;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

import com.yardi.ejb.model.Login_Sessions_Table;
import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.shared.userServices.LoginInitialPage;
import com.yardi.shared.userServices.LoginRequest;
import com.yardi.shared.userServices.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class LoginUserServicesBean implements LoginUserServices {
	private Pwd_Policy pwdPolicy = null;
	private String feedback = "";
	private java.sql.Timestamp today = new java.sql.Timestamp(new java.util.Date().getTime());
	private LoginRequest loginRequest;
	private LoginResponse loginResponse;
	private Vector<LoginInitialPage> initialPageList;
	private String initialPage = "";
	private String sessionID = "";
	@EJB LoginUserProfile userProfileBean; //bean is thread safe unless marked reentrant in the deployment descriptor
	@EJB UniqueTokens uniqueTokensBean;
	@EJB PasswordPolicy passwordPolicyBean;
	@EJB LoginUserGroups userGroupsBean;
	@EJB LoginSessionsTable sessionsBean;
	@EJB PwdCompositionRules pwdCompRulesBean;
	@Resource UserTransaction tx;
	
	public LoginUserServicesBean() {
		System.out.println("com.yardi.ejb.LoginUserServicesBean LoginUserServicesBean() 0006");
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
				
				NOTE: If they are changing the password then a transaction can not begin or end in authenticate() because the transaction begins/ends in chgPwd()
			 */
			//debug
			System.out.println("com.yardi.ejb.LoginUserServicesBean authenticate() 0028  ");
			//debug
			
			/*
			 * If they are not changing the password then the transaction begins in authenticate()
			 * If they are changing the password then the transaction begins in chgPwd()
			 * The transaction begin is conditioned because chgPwd() will call authenticate() while in a transaction
			 */
			if (loginRequest.getChangePwd()==false) {
				//debug
				System.out.println("com.yardi.ejb.LoginUserServicesBean authenticate() 000C  ");
				//debug
				tx.begin();
			}
			
			
			txStatus();
			boolean authenticated = false;
			userGroupsBean.find(loginRequest.getUserName());

			if (userGroupsBean.getLoginUserProfile() == null) {
				//debug
				System.out.println("com.yardi.ejb.LoginUserServicesBean authenticate() 0009 ");
				//debug
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0001;
			} else {
				//debug
				System.out.println("com.yardi.ejb.LoginUserServicesBean authenticate() 000A ");
				//debug
				initialPage = userGroupsBean.getInitialPage(loginRequest.getUserName());
				userProfileBean.setUserProfile(userGroupsBean.getLoginUserProfile());
				authenticated = userProfileBean.authenticate(loginRequest.getUserName(),
						loginRequest.getPassword(), loginRequest.getChangePwd());
				feedback = userProfileBean.getFeedback();
			}
			
			if (authenticated) {
				//debug
				System.out.println("com.yardi.ejb.LoginUserServicesBean authenticate() 0029 "
						+ "\n"
						+ "    feedback="
						+ feedback
						);
				//debug
				loginSuccess();
				
				/*
				 * If they are not changing the password at this point, the transaction is done. 
				 * If the password is being changed then at this point then the user has:
				 *     - successfully authenticated 
				 *     - been notified change password is required
				 *     - viewed change password page
				 *     - successfully authenticated again
				 *     - entered the new password
				 * If the password is being changed then at this point the sessions table row has been persisted
				 * Do not commit tx at this point if password is being changed because:
				 *     - tokens still need to be processed
				 *     - new password has to be checked to be sure it conforms to policy
				 *     - all of the above is part of the current tx
				 *     - chgPwd() will commit
				 */
				if (loginRequest.getChangePwd()==false) {
					//debug
					System.out.println("com.yardi.ejb.LoginUserServicesBean authenticate() 000B");
					//debug
					tx.commit();
				} 
				
			} else {
				//debug
				System.out.println("com.yardi.ejb.LoginUserServicesBean authenticate() 002A");
				//debug

				if (feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000C) ||
					feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000F) ||
					feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD0002)	) {
					/*
					 * Need to distinguish between invalid password and invalid user name. If its invalid password 
					 * set feedback to YRD0001 because html does not distinguish between invalid password and invalid 
					 * user name. Also html specifically checks for YRD0001.
					 */
					//debug
					System.out.println("com.yardi.ejb.LoginUserServicesBean authenticate() 002B");
					//debug
					if (feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000F)) {
						feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0001;
					}
					
					if (loginRequest.getChangePwd()==false) {
						//debug
						System.out.println("com.yardi.ejb.LoginUserServicesBean authenticate() 000D ");
						//debug
						tx.commit();
					} 
				} else {
					//debug
					System.out.println("com.yardi.ejb.LoginUserServicesBean authenticate() 002C");
					//debug
					if (loginRequest.getChangePwd()==false) {
						//debug
						System.out.println("com.yardi.ejb.LoginUserServicesBean authenticate() 000F ");
						//debug
						rollback(tx);
					} 
				}
			}

			//debug
			System.out.println("com.yardi.ejb.LoginUserServicesBean authenticate() 002D");
			//debug.Login
			return authenticated;
		} catch (Exception e) {
			System.out.println("com.yardi.ejb.LoginUserServicesBean authenticate() 0035"
					+ "\n"
					+ "   exception="
					+ e
					);
			e.printStackTrace();
			rollback(tx);
			return false;
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
		//debug
		System.out.println("com.yardi.ejb.LoginUserServicesBean changeUserToken() 0004  ");
		//debug
		txStatus();
		userProfileBean.changeUserToken(newPassword); //store new token in user profile
		userProfileBean.loginSuccess();
		feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
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
	public boolean chgPwd() {
		//debug
		System.out.println("com.yardi.ejb.LoginUserServicesBean chgPwd() 0032");
		//debug
		try {
			tx.begin();
			txStatus();
			String userName     = loginRequest.getUserName();
			char [] oldPassword = loginRequest.getPassword().toCharArray();
			char [] newPassword = loginRequest.getNewPassword().toCharArray();
			//debug
			System.out.println("com.yardi.ejb.LoginUserServicesBean chgPwd() 001A"
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
				//debug
				System.out.println("com.yardi.ejb.LoginUserServicesBean chgPwd() 000E ");
				//debug
				if (feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000C) ||
						feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD0001)) {
					//debug
					System.out.println("com.yardi.ejb.LoginUserServicesBean chgPwd() 0012 ");
					//debug
					tx.commit();
				} else {
					//debug
					System.out.println("com.yardi.ejb.LoginUserServicesBean chgPwd() 0013 ");
					//debug
					rollback(tx);
				}
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

			//debug
			System.out.println("com.yardi.ejb.LoginUserServicesBean chgpwd() 0030");
			//debug
			txStatus();
			getPwdPolicy();
			short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
			short pwdLifeInDays   = pwdPolicy.getPpDays();
			//debug
			System.out.println("com.yardi.ejb.LoginUserServicesBean chgpwd() 0033"
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
				System.out.println("com.yardi.ejb.LoginUserServicesBean chgpwd() 0026"
						+ "\n"
						+ "   Vector<Unique_Tokens>="
						+ userTokens
						);
			}
			//debug				
			if (pwdCompRulesBean.enforce(new String(newPassword), userName, userGroupsBean.getLoginUserProfile().getUptoken(), userTokens) == false ) {
				//apply password policy to the new password
				//debug
				System.out.println("com.yardi.ejb.LoginUserServicesBean chgPwd() 0010 "
						+ "\n "
						+ "  pwdCompRulesBean.enforce()== false"
						);   
				//debug
				feedback = passwordPolicyBean.getFeedback();
				rollback(tx);
				return false;
			}

			//debug
			if (!(userTokens==null)) {
				System.out.println("com.yardi.ejb.LoginUserServicesBean chgpwd() 0027"
						+ "\n"
						+ "   Vector<Unique_Tokens>="
						+ userTokens
						);
			}
			//debug				
			uniqueTokensBean.removeOldestToken(userTokens);
			//debug
			System.out.println("com.yardi.ejb.LoginUserServicesBean chgpwd() 0014");
			//debug

			if (maxUniqueTokens > 0) {
				//debug
				System.out.println("com.yardi.ejb.LoginUserServicesBean chgpwd() 0020  "
						+ "\n"
						+ "   username="
						+ userName
						+ "\n"
						+ "   userGroupsBean.getLoginUserProfile().getUptoken()="
						+ userGroupsBean.getLoginUserProfile().getUptoken()
						);
				//debug
				uniqueTokensBean.persist(userName, userGroupsBean.getLoginUserProfile().getUptoken(), new java.util.Date()); //insert
			}

			//debug
			System.out.println("com.yardi.ejb.LoginUserServicesBean chgpwd() 0025");
			//debug
			changeUserToken(userName, newPassword);
			loginSuccess();
			tx.commit();
			txStatus();
			return true;
		} catch (Exception e) {
			System.out.println("com.yardi.ejb.LoginUserServicesBean chgpwd() 0034"
					+ "\n"
					+ "   exception="
					+ e
					);
			e.printStackTrace();
			rollback(tx);
		}
		//debug
		System.out.println("com.yardi.ejb.LoginUserServicesBean chgpwd() 0031");
		//debug
		return true;
	}
	
	public String getFeedback() {
		return feedback;
	}

	public String getInitialPage() {
		return initialPage;
	}

	public LoginRequest getLoginRequest() {
		return loginRequest;
	}

	public LoginResponse getLoginResponse() {
		return loginResponse;
	}

	private Pwd_Policy getPwdPolicy() {
		
		if (pwdPolicy == null) {
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb.LoginUserServicesBean getPwdPolicy 0017"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy);
		//debug
		return pwdPolicy;
	}

	public String getSessionID() {
		return sessionID;
	}
	
	private void loginSuccess() throws JsonProcessingException {
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
		
		//debug
		System.out.println("com.yardi.ejb.LoginUserServicesBean loginSuccess() 0005  ");
		//debug
        txStatus();
		Login_Sessions_Table sessionsTable = null;
		sessionsTable = userGroupsBean.getLoginSessionTable(); 

		if (sessionsTable == null) {
			sessionsBean.persist(
					loginRequest.getUserName(), 
					sessionID, 
					initialPage, 
					new java.sql.Timestamp(new java.util.Date().getTime()));
			//debug
			System.out.println("com.yardi.ejb.LoginUserServicesBean loginSuccess() 001D"
				+ "\n"
				+ "   Sessions_Table="
				+ sessionsTable
				+ "\n"
				+ "   feedback="
				+ feedback
				);
			//debug
		} else {
			sessionsBean.update(
					sessionsTable,
					sessionID, 
					initialPage, 
					new java.sql.Timestamp(new java.util.Date().getTime()));
			//debug
			System.out.println("com.yardi.ejb.LoginUserServicesBean loginSuccess() 001E  "
				+ "\n"
				+ "  Sessions_Table="
				+ sessionsTable);
			//debug
		}

		System.out.println("com.yardi.ejb.LoginUserServicesBean loginSuccess() 0016  "
				+ "\n"
				+ "    feedback="
				+ feedback
				);
		if (feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000E)) {
			for (LoginInitialPage i : initialPageList) {
				System.out.println(
					  "\n"
					+ "   initialPageList="
					+ i
				);
			}
		}
		//debug
		txStatus();
		setLoginResponse();
	}

	@Override
	@Remove
	public void remove() {
		System.out.println("com.yardi.ejb.LoginUserServicesBean remove() 0000");
		userGroupsBean.removeBean();
		userProfileBean.removeBean();
		pwdCompRulesBean.removeBean();
	}

	/**
	 * Attempt to roll back the transaction
	 * @param tx - The transaction to roll back
	 */
	private void rollback(UserTransaction tx) {
		//debug
		System.out.println("com.yardi.ejb.LoginUserServicesBean rollback() 0011");
		//debug
		try {
			tx.rollback();
		} catch (Exception e) {
			System.out.println("com.yardi.ejb.LoginUserServicesBean rollback() 0001"
					+ "\n"
					+ "   exception="
					+ e
					);	
			e.printStackTrace();
		}
		txStatus();
	}

	public void setLoginRequest(LoginRequest loginRequest) {
		this.loginRequest = loginRequest;
		//debug
		System.out.println("com.yardi.ejb.LoginUserServicesBean setLoginRequest() 001B " 
				+ "\n   "
				+ toString());
		//debug
	}

	private void setLoginResponse() throws JsonProcessingException {
		//msg is needed for the response to yardiLogin.html/changePwd.html but depends on userGroups.size() 
		initialPageList = userGroupsBean.getInitialPageList();
		String msg[] = userGroupsBean.getFeedback().split("=");
		feedback = userGroupsBean.getFeedback();
		//debug
		System.out.println("com.yardi.ejb.LoginUserServicesBean setLoginResponse() 0008  "
				+ "\n"
				+ "    msg[]="
				+ Arrays.toString(msg)
				+ "\n"
				+ "    feedback="
				+ feedback
				);
		//debug
		ObjectMapper mapper = new ObjectMapper();
		loginResponse = new LoginResponse(
				loginRequest.getUserName(),
				"", //Password
				mapper.writeValueAsString(initialPageList), //new password
				msg[0],
				initialPage
			);
		System.out.println("com.yardi.ejb.LoginUserServicesBean setLoginResponse() 0007  "
			+ "\n"
			+ "   loginResponse="
			+ loginResponse.toString()
			);
	}
	
	private void setPwdPolicy() {
		pwdPolicy = passwordPolicyBean.getPwdPolicy();
		
		if (pwdPolicy == null) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
		}
		//debug
		System.out.println("com.yardi.ejb.LoginUserServicesBean setPwdPolicy 001F"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy
			+ "\n"
			+ "   feedback="
			+ feedback);
		//debug
	}
	
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	@Override
	public String toString() {
		return "LoginUserServicesBean [feedback=" + feedback + ", today=" + today + ", pwdPolicy=" + pwdPolicy + ", Login_User_Profile="
				+ userGroupsBean.getLoginUserProfile() + ", loginRequest=" + loginRequest + ", loginResponse=" + loginResponse
				+ ", initialPageList=" + initialPageList + ", initialPage=" + initialPage + ", userProfileBean="
				+ userProfileBean + ", uniqueTokensBean=" + uniqueTokensBean + ", passwordPolicyBean="
				+ passwordPolicyBean + ", userGroupsBean=" + userGroupsBean + ", sessionsBean=" + sessionsBean + ", tx="
				+ tx + "]";
	}

	private void txStatus() {
		String status = null;
		
		try {
			switch(tx.getStatus()) {
			case jakarta.transaction.Status.STATUS_ACTIVE:
				status = "active";
				break;
			case jakarta.transaction.Status.STATUS_COMMITTED:
				status = "committed";
				break;
			case jakarta.transaction.Status.STATUS_COMMITTING:
				status = "committing";
				break;
			case jakarta.transaction.Status.STATUS_MARKED_ROLLBACK:
				status = "marked rollback";
				break;
			case jakarta.transaction.Status.STATUS_NO_TRANSACTION:
				status = "no transaction";
				break;
			case jakarta.transaction.Status.STATUS_PREPARED:
				status = "prepared";
				break;
			case jakarta.transaction.Status.STATUS_PREPARING:
				status = "prepairing";
				break;
			case jakarta.transaction.Status.STATUS_ROLLEDBACK:
				status = "rolled back";
				break;
			case jakarta.transaction.Status.STATUS_ROLLING_BACK:
				status = "rolling back";
				break;
			case jakarta.transaction.Status.STATUS_UNKNOWN:
				status = "unknown";
				break;
			default:
				status = "undefined";
			}
		} catch (SystemException e) {
			System.out.println("com.yardi.ejb.LoginUserServicesBean txStatus() SystemException 0003 ");
			e.printStackTrace();
		}
		
		System.out.println("com.yardi.ejb.LoginUserServicesBean txStatus() 0002 "
  				+ "\n"
  				+ "   tx status="
  				+ status
  				);
	}
}
