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

import com.yardi.ejb.model.Sessions_Table;
import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.shared.userServices.LoginInitialPage;
import com.yardi.shared.userServices.LoginRequest;
import com.yardi.shared.userServices.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Services for authentication and user profile management<p>
 * 1. Authentication
 * 2. Change password
 * 3. Password policy
 */
@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class UserServicesBean implements UserServices {
	/**
	 * Reference to the password policy obtained from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy()
	 */
	private Pwd_Policy pwdPolicy = null;
	/**
	 * Status of the most recent method call that provides feedback<p> 
	 * Clients can read this field to determine the status of the most recent method call that provides feedback.
	 */
	private String feedback = "";
	/**
	 * Today's date and time
	 */
	private java.sql.Timestamp today = new java.sql.Timestamp(new java.util.Date().getTime());
	/**
	 * POJO representation of the web request to login
	 */
	private LoginRequest loginRequest;
	/**
	 * POJO representation of the response to the web request to login
	 */
	private LoginResponse loginResponse;
	/**
	 *  Vector containing the description and URL of each group the user belongs to
	 */
	private Vector<LoginInitialPage> initialPageList;
	/**
	 * The URL of the page for the group the user belongs to.<p>
	 * If the user belongs to multiple groups the field initialPage is set to views/selectGroup.html.
	 */
	private String initialPage = "";
	/**
	 * The value returned by HttpServletRequest.getSession().getId()
	 */
	private String sessionID = "";
	/**
	 * Injected reference to com.yardi.ejb.UserProfileBean
	 */
	@EJB UserProfile userProfileBean; 
	/**
	 * Injected reference to com.yardi.ejb.UniqueTokensBean
	 */
	@EJB UniqueTokens uniqueTokensBean;
	/**
	 * Injected reference to com.yardi.ejb.PasswordPolicyBean
	 */
	@EJB PasswordPolicy passwordPolicyBean;
	/**
	 * Injected reference to com.yardi.ejb.UserGroupsBean
	 */
	@EJB UserGroups userGroupsBean;
	/**
	 * Injected reference to com.yardi.ejb.SessionsTableBean
	 */
	@EJB SessionsTable sessionsBean;
	/**
	 * Injected reference to com.yardi.ejb.PwdCompositionRulesBean
	 */
	@EJB PwdCompositionRules pwdCompRulesBean;
	@Resource UserTransaction tx;
	
	public UserServicesBean() {
		System.out.println("com.yardi.ejb.UserServicesBean UserServicesBean() 0006");
	}
	/**
	 * Authenticate the user.<p> 
	 * 
	 * If the user is in the process of changing their password then the transaction begins with method chgPwd(). Method chgPwd() makes the user authenticate again 
	 * by calling this method. This is why the transaction can not begin with authenticate() if the user is in the process of changing their password.<p>
	 * 
	 * Authentication is delegated to com.yadi.ejb.UserProfileBean.authenticate().<br><br>
	 * <div style="display:flex; flex-direction: row">
	 *   <div>
	 *     <strong><u>If the user successfully authenticates:</u></strong>
	 *     <ul>
	 *       <li>
	 *         If the persistence context has no Sessions_Table entity matching the session ID and the SESSIONS_TABLE database table has no row matching session 
	 *         ID then persist the Sessions_Table entity.
	 *       </li>
	 *       <li>
	 *         If the persistence context has a Sessions_Table entity matching the session ID then update the Sessions_Table entity.
	 *       </li>
	 *       <li>
	 *         Construct com.yardi.shared.userServices.LoginResponse.
	 *       </li>
	 *       <li>
	 *         If the user is not changing the password then commit the transaction. Otherwise, method chgPwd() will determine when to commit the transaction.
	 *       </li>
	 *     </ul>
	 *   </div>
	 *   <div>
	 *     <strong><u>If authentication fails:</u></strong>
	 *     <ul>
	 *       <li>
	 *         If the feedback is <span style="font-family:consolas;">YRD000C, YRD000F or YRD0002</span> and the user is not in the process of changing their password then commit the transaction. 
	 *         Otherwise, method chgPwd() determines when to commit the transaction.
	 *       </li>
	 *       <li>
	 *         If the feedback is <span style="font-family:consolas;">YRD000F</span> then set the feedback to <span style="font-family:consolas;">YRD0001</span>.
	 *       </li>
	 *       <li>
	 *         If the user fails to authenticate for a reason other than <span style="font-family:consolas;">YRD000C, YRD000F or YRD0002</span> and the user 
	 *         is not in the process of changing their password then rollback the transaction. Otherwise, method chgPwd() determines what happens to the transaction.
	 *       </li>
	 *     </ul>
	 *   </div>
	 * </div>
	 * <p>
	 * 
	 * <strong>The following feedback is provided:</strong><br>
	 * <pre>
	 * YRD0000 normal completion
	 * YRD0001 Invalid user name or password
	 * YRD0002 the password has expired
	 * YRD0003 the User_Profile entity is disabled (user cant login). Password must be reset to login
	 * YRD0004 the User_Profile entity is inactive. Administrator must clear the inactive flag to login
	 * YRD000B password policy is missing
	 * YRD000C maximum signon attempts exceeded. The User_Profile entity is disabled
	 * YRD000E User belongs to multiple groups
	 * YRD000F invalid password
	 * </pre>
	 * 
	 * @return boolean indicating whether authentication was successful or unsuccessful
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
			System.out.println("com.yardi.ejb.UserServicesBean authenticate() 0028  ");
			//debug
			
			/*
			 * If they are not changing the password then the transaction begins in authenticate()
			 * If they are changing the password then the transaction begins in chgPwd()
			 * The transaction begin is conditioned because chgPwd() will call authenticate() while in a transaction
			 */
			if (loginRequest.getChangePwd()==false) {
				//debug
				System.out.println("com.yardi.ejb.UserServicesBean authenticate() 000C  ");
				//debug
				tx.begin();
			}
			
			
			txStatus();
			boolean authenticated = false;
			userGroupsBean.find(loginRequest.getUserName());

			if (userGroupsBean.getLoginUserProfile() == null) {
				//debug
				System.out.println("com.yardi.ejb.UserServicesBean authenticate() 0009 ");
				//debug
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0001;
			} else {
				//debug
				System.out.println("com.yardi.ejb.UserServicesBean authenticate() 000A ");
				//debug
				initialPage = userGroupsBean.getInitialPage(loginRequest.getUserName());
				userProfileBean.setUserProfile(userGroupsBean.getLoginUserProfile());
				authenticated = userProfileBean.authenticate(loginRequest.getUserName(),
						loginRequest.getPassword(), loginRequest.getChangePwd());
				feedback = userProfileBean.getFeedback();
			}
			
			if (authenticated) {
				//debug
				System.out.println("com.yardi.ejb.UserServicesBean authenticate() 0029 "
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
					System.out.println("com.yardi.ejb.UserServicesBean authenticate() 000B");
					//debug
					tx.commit();
				} 
				
			} else {
				//debug
				System.out.println("com.yardi.ejb.UserServicesBean authenticate() 002A");
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
					System.out.println("com.yardi.ejb.UserServicesBean authenticate() 002B");
					//debug
					if (feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000F)) {
						feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0001;
					}
					
					if (loginRequest.getChangePwd()==false) {
						//debug
						System.out.println("com.yardi.ejb.UserServicesBean authenticate() 000D ");
						//debug
						tx.commit();
					} 
				} else {
					//debug
					System.out.println("com.yardi.ejb.UserServicesBean authenticate() 002C");
					//debug
					if (loginRequest.getChangePwd()==false) {
						//debug
						System.out.println("com.yardi.ejb.UserServicesBean authenticate() 000F ");
						//debug
						rollback(tx);
					} 
				}
			}

			//debug
			System.out.println("com.yardi.ejb.UserServicesBean authenticate() 002D");
			//debug.Login
			return authenticated;
		} catch (Exception e) {
			System.out.println("com.yardi.ejb.UserServicesBean authenticate() 0035"
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
	 * Change the user's current token stored in USER_PROFILE database table.<p>
	 * 
	 * Delegate to com.yardi.ejb.UserProfileBean.changeUserToken() to set a new hashed password.<p>
	 * 
	 * Delegate to com.yardi.ejb.UserProfileBean.loginSuccess() to give the user credit for successfully authenticating although they have not yet
	 * completed the change password process. 
	 * 
	 * @param userName identifies the user profile to change
	 * @param newPassword char array containing the new password in plain text
	 */
	private void changeUserToken(final String userName, final char [] newPassword) {
		//debug
		System.out.println("com.yardi.ejb.UserServicesBean changeUserToken() 0004  ");
		//debug
		txStatus();
		userProfileBean.changeUserToken(newPassword); //store new token in user profile
		userProfileBean.loginSuccess();
		feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
	}

	/**
	 * Support for changing password either on demand or when the current password has expired.<p>
	 * The transaction begins and ends here, not in method authenticate().<p>
	 * Before the user is able to change their password, they must first authenticate using their current credentials.<p>
	 * Database table PWD_POLICY has parameters that control password policy.<br><br>
	 * 
	 * <div style="display:flex; flex-direction: row">
	 *   <div>
	 *     <strong><u>Authentication was unsuccessful</u></strong>
	 *     <ul>
	 *       <li>
	 *         If the reason was <span style="font-family:consolas;">YRD000C</span> or <span style="font-family:consolas;">YRD0001</span> commit the transaction
	 *       </li>
	 *       <li>
	 *         If the reason was not <span style="font-family:consolas;">YRD000C</span> and not <span style="font-family:consolas;">YRD0001</span> 
	 *         rollback the transaction 
	 *       </li>
	 *       <li>
	 *         Return false indicating that the change password request was not successful
	 *       </li>
	 *     </ul>
	 *   </div>
	 *   <div style="padding-left: 5px">
	 *     <strong><u>Authentication was successful</u></strong>
	 *     <ul>
	 *       <li>
	 *         Get the password policy from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy().
	 *       </li>
	 *       <li>
	 *         If the user has more stored tokens in history than the maximum number of tokens to store per user, as defined in password policy, then 
	 *         remove the extra tokens. This can happen if the maximum number of tokens to store per user in password policy is changed.
	 *       </li>
	 *       <li>
	 *         If the new password does not comply with password policy then rollback the transaction and return false to indicate the change password 
	 *         request was unsuccessful
	 *       </li>
	 *       <li>
	 *         Remove the oldest stored token in history for this user so that when the current token is saved in history, the number of tokens being stored 
	 *         for this user does not exceed the maximum number of tokens to store per user as defined in password policy. 
	 *       </li>
	 *       <li>
	 *         If unique tokens is being enforced in password policy then save the current token in history.
	 *       </li>
	 *       <li>
	 *         Change the token in the User_Profile entity to the new token.
	 *       </li>       
	 *       <li>
	 *         If the persistence context has no Sessions_Table entity matching the session ID and the SESSIONS_TABLE database table has no row matching session 
	 *         ID then persist the Sessions_Table entity.
	 *       </li>
	 *       <li>
	 *         If the persistence context has a Sessions_Table entity matching the session ID then update the Sessions_Table entity.
	 *       </li>
	 *       <li>
	 *         Commit the transaction.
	 *       </li>
	 *       <li>
	 *         Return true to indicate the change password request was successful.
	 *       </li>       
	 *     </ul>
	 *   </div>
	 * </div>
	 * <p>
	 * <strong>The following feedback is provided:</strong><br>
	 * <pre>
	 * YRD0000 normal completion
	 * YRD0001 Invalid user name or password
	 * YRD0002 the password has expired
	 * YRD0003 the User_Profile entity is disabled (user cant login). Password must be reset to login
	 * YRD0004 the User_Profile entity is inactive. Administrator must clear the inactive flag to login
	 * YRD0005 Password must be at least %n characters long
	 * YRD0006 Password must contain at least 1 upper case
	 * YRD0007 Password must contain at least 1 lower case
	 * YRD0008 Password must contain at least 1 number
	 * YRD0009 Password must contain at least 1 special character
     * YRD000A Password matches a password that was previously used
	 * YRD000B Password policy is missing
	 * YRD000C maximum signon attempts exceeded. The User_Profile entity is disabled
	 * YRD000E User belongs to multiple groups
	 * YRD000F invalid password
	 * YRD0010 New password must not contain current password
	 * YRD0011 New password must not contain user name in any case
	 * YRD0015 Password must not be longer than %n characters
	 * YRD0016 Password contains more than %n repeated characters
	 * YRD0017 Password must contain at least %n numbers
	 * YRD0018 Password must contain at least %n upper case characters
	 * YRD0019 Password must contain at least %n lower case characters
	 * YRD001A Password must contain at least %n special characters
	 * </pre>
	 * 
	 * @return boolean indicating whether the change password process was successful 
	 */
	public boolean chgPwd() {
		//debug
		System.out.println("com.yardi.ejb.UserServicesBean chgPwd() 0032");
		//debug
		try {
			tx.begin();
			txStatus();
			String userName     = loginRequest.getUserName();
			char [] oldPassword = loginRequest.getPassword().toCharArray();
			char [] newPassword = loginRequest.getNewPassword().toCharArray();
			//debug
			System.out.println("com.yardi.ejb.UserServicesBean chgPwd() 001A"
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
				System.out.println("com.yardi.ejb.UserServicesBean chgPwd() 000E ");
				//debug
				if (feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000C) ||
						feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD0001)) {
					//debug
					System.out.println("com.yardi.ejb.UserServicesBean chgPwd() 0012 ");
					//debug
					tx.commit();
				} else {
					//debug
					System.out.println("com.yardi.ejb.UserServicesBean chgPwd() 0013 ");
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
			System.out.println("com.yardi.ejb.UserServicesBean chgpwd() 0030");
			//debug
			txStatus();
			getPwdPolicy(); 
			short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
			short pwdLifeInDays   = pwdPolicy.getPpDays();
			//debug
			System.out.println("com.yardi.ejb.UserServicesBean chgpwd() 0033"
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
				System.out.println("com.yardi.ejb.UserServicesBean chgpwd() 0026"
						+ "\n"
						+ "   Vector<Unique_Tokens>="
						+ userTokens
						);
			}
			//debug				
			if (pwdCompRulesBean.enforce(new String(newPassword), userName, userGroupsBean.getLoginUserProfile().getUptoken(), userTokens) == false ) {
				//apply password policy to the new password
				//debug
				System.out.println("com.yardi.ejb.UserServicesBean chgPwd() 0010 "
						+ "\n "
						+ "  pwdCompRulesBean.enforce()== false"
						);   
				//debug
				feedback = pwdCompRulesBean.getFeedback();
				rollback(tx);
				return false;
			}

			//debug
			if (!(userTokens==null)) {
				System.out.println("com.yardi.ejb.UserServicesBean chgpwd() 0027"
						+ "\n"
						+ "   Vector<Unique_Tokens>="
						+ userTokens
						);
			}
			//debug				
			uniqueTokensBean.removeOldestToken(userTokens);
			//debug
			System.out.println("com.yardi.ejb.UserServicesBean chgpwd() 0014");
			//debug

			//if unique tokens is being enforced in password policy save the current token in history
			if (maxUniqueTokens > 0) {
				//debug
				System.out.println("com.yardi.ejb.UserServicesBean chgpwd() 0020  "
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
			System.out.println("com.yardi.ejb.UserServicesBean chgpwd() 0025");
			//debug
			changeUserToken(userName, newPassword);
			loginSuccess();
			tx.commit();
			txStatus();
			return true;
		} catch (Exception e) {
			System.out.println("com.yardi.ejb.UserServicesBean chgpwd() 0034"
					+ "\n"
					+ "   exception="
					+ e
					);
			e.printStackTrace();
			rollback(tx);
		}
		//debug
		System.out.println("com.yardi.ejb.UserServicesBean chgpwd() 0031");
		//debug
		return true;
	}
	
	/**
	 * Returns the status of the most recent method call that provides feedback.<p>
	 * Clients call <i>getFeedback()</i> to determine the status of the most recent method call that provides feedback.
	 * @return feedback from the most recent method call that provides feedback.
	 */
	public String getFeedback() {
		return feedback;
	}

    /**
     * Returns the URL of the page for the group that the user belongs to.<p>
     * 
     * If the user belongs to multiple groups the field <i>initialPage</i> is set to <i>views/selectGroup.html</i>. The initial page is selected 
     * by the user from a list of initial page names and descriptions representing each group they belong to.<br><br> 
     * @return the user's initial page.
     */
	public String getInitialPage() {
		return initialPage;
	}

	/**
	 * Returns the POJO representation of the web request to login.<p> The login request contains the user's credentials, message ID, message description, 
	 * string indicating whether user is changing their password, boolean indicating whether user is changing their password and the session ID which is 
	 * equivalent to HttpServletRequest.getSession().getId().
	 * @return POJO representation of the web request to login.
	 */
	public LoginRequest getLoginRequest() {
		return loginRequest;
	}

	/**
	 * Returns the POJO representation of the response to the web request to login.<p>
	 * The login response contains the user's credentials, message ID and message description. Field <i>loginResponse</i> is converted to JSON when
	 * responding to the web request.
	 * @return POJO representation of the response to the web request to login.
	 */
	public LoginResponse getLoginResponse() {
		return loginResponse;
	}

	/**
	 * Returns the password policy obtained from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy().
	 * @return reference to Pwd_Policy entity
	 */
	private Pwd_Policy getPwdPolicy() {
		
		if (pwdPolicy == null) {
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb.UserServicesBean getPwdPolicy 0017"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy);
		//debug
		return pwdPolicy;
	}

	/**
	 * Returns the session ID which is equivalent to HttpServletRequest.getSession().getId().
	 * @return the value of field <i>sessionID</i>
	 */
	public String getSessionID() {
		return sessionID;
	}
	
	/**
	 * When the user successfully authenticates, this method will either persist or update the Sessions_Table entity.<p>
	 *  
	 * If there is a Sessions_Table entity that matches the session ID in the persistence context then the Sessions_Table entity is updated.
	 * If the persistence context does not have a Sessions_Table entity that matches the session ID and the SESSIONS_TABLE database table has 
	 * no row matching the session ID then the Sessions_Table entity is persisted.<p>
	 * 
	 * The actual update or persist is delegated to com.yardi.ejb.SessionsTableBean.<br><br>
	 * 
	 * @throws JsonProcessingException problems encountered when processing (parsing, generating) JSON content
	 */
	private void loginSuccess() throws JsonProcessingException {
		//debug
		System.out.println("com.yardi.ejb.UserServicesBean loginSuccess() 0005  ");
		//debug
        txStatus();
        Sessions_Table sessionsTable = sessionsBean.find(sessionID); 

		if (sessionsTable == null) {
			sessionsBean.persist(
					loginRequest.getUserName(), 
					sessionID, 
					initialPage, 
					new java.sql.Timestamp(new java.util.Date().getTime()));
			//debug
			System.out.println("com.yardi.ejb.UserServicesBean loginSuccess() 001D"
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
			System.out.println("com.yardi.ejb.UserServicesBean loginSuccess() 001E  "
				+ "\n"
				+ "  Sessions_Table="
				+ sessionsTable);
			//debug
		}

		System.out.println("com.yardi.ejb.UserServicesBean loginSuccess() 0016  "
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

	/**
	 * Stateful session bean remove method.<p>
	 * Clients call this method so that com.yardi.ejb.UserServicesBean can release resources it has before being removed.
	 */
	@Override
	@Remove
	public void remove() {
		System.out.println("com.yardi.ejb.UserServicesBean remove() 0000");
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
		System.out.println("com.yardi.ejb.UserServicesBean rollback() 0011");
		//debug
		try {
			tx.rollback();
		} catch (Exception e) {
			System.out.println("com.yardi.ejb.UserServicesBean rollback() 0001"
					+ "\n"
					+ "   exception="
					+ e
					);	
			e.printStackTrace();
		}
		txStatus();
	}

	/** 
	 * Inject the request from the web to login.
	 * @param loginRequest POJO representation of the web request to login
	 */
	public void setLoginRequest(LoginRequest loginRequest) {
		this.loginRequest = loginRequest;
		//debug
		System.out.println("com.yardi.ejb.UserServicesBean setLoginRequest() 001B " 
				+ "\n   "
				+ toString());
		//debug
	}

	/**
	 * Construct the POJO response to the request to login.<p>
	 * 
	 * This response consists of the user name, a JSON array of the description and URL of each group the user belongs to, message ID and the URL 
	 * of the initial for the group that the user belongs to.
	 * 
	 * @throws JsonProcessingException problems encountered when processing (parsing, generating) JSON content
	 */
	private void setLoginResponse() throws JsonProcessingException {
		//msg is needed for the response to yardiLogin.html/changePwd.html but depends on userGroups.size() 
		initialPageList = userGroupsBean.getInitialPageList();
		String msg[] = userGroupsBean.getFeedback().split("=");
		feedback = userGroupsBean.getFeedback();
		//debug
		System.out.println("com.yardi.ejb.UserServicesBean setLoginResponse() 0008  "
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
		System.out.println("com.yardi.ejb.UserServicesBean setLoginResponse() 0007  "
			+ "\n"
			+ "   loginResponse="
			+ loginResponse.toString()
			);
	}
	
	/**
	 * Obtain a reference to password policy from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy().<p>
	 * 
	 * <strong>The following feedback is provided:</strong><br>
	 * <span style="font-family:consolas;">YRD000B Password policy is missing</span>
	 */
	private void setPwdPolicy() {
		pwdPolicy = passwordPolicyBean.getPwdPolicy();
		
		if (pwdPolicy == null) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
		}
		//debug
		System.out.println("com.yardi.ejb.UserServicesBean setPwdPolicy 001F"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy
			+ "\n"
			+ "   feedback="
			+ feedback);
		//debug
	}
	
	/** 
	 * Set field <i>sessionID</i>.<p>
	 * 
	 * @param sessionID the value to set. Equivalent to HttpServletRequest.getSession().getId().
	 */
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	@Override
	public String toString() {
		return "UserServicesBean [feedback=" + feedback + ", today=" + today + ", pwdPolicy=" + pwdPolicy + ", User_Profile="
				+ userGroupsBean.getLoginUserProfile() + ", loginRequest=" + loginRequest + ", loginResponse=" + loginResponse
				+ ", initialPageList=" + initialPageList + ", initialPage=" + initialPage + ", userProfileBean="
				+ userProfileBean + ", uniqueTokensBean=" + uniqueTokensBean + ", passwordPolicyBean="
				+ passwordPolicyBean + ", userGroupsBean=" + userGroupsBean + ", sessionsBean=" + sessionsBean + ", tx="
				+ tx + "]";
	}

	/**
	 * Log the transaction status
	 */
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
			System.out.println("com.yardi.ejb.UserServicesBean txStatus() SystemException 0003 ");
			e.printStackTrace();
		}
		
		System.out.println("com.yardi.ejb.UserServicesBean txStatus() 0002 "
  				+ "\n"
  				+ "   tx status="
  				+ status
  				);
	}
}
