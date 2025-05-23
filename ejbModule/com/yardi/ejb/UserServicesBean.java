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
	/**
	 * User transaction
	 */
	@Resource UserTransaction tx;
	
	/**
	 * Default constructor
	 */
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
	 *         Insert into or update the SESSIONS_TABLE database table
	 *       </li>
	 *       <li>
	 *         Construct {@link com.yardi.shared.userServices.LoginResponse com.yardi.shared.userServices.LoginResponse}.
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
	 *         If the feedback is <span style="font-family:consolas;">YRD0002, YRD000C, YRD000F or YRD001B</span> and the user is not in the process of changing their password then commit the transaction. 
	 *         Otherwise, method chgPwd() determines when to commit the transaction.
	 *       </li>
	 *       <li>
	 *         If the feedback is <span style="font-family:consolas;">YRD000F</span> then set the feedback to <span style="font-family:consolas;">YRD0001</span>.
	 *       </li>
	 *       <li>
	 *         If the user fails to authenticate for a reason other than <span style="font-family:consolas;">YRD0002, YRD000C, YRD00F or YRD001B</span> and the user 
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
	 * YRD000D No such user name
	 * YRD000E User belongs to multiple groups
	 * YRD000F invalid password
	 * YRD001B Authenticated with temporary password
	 * YRD001C Temporary password expired. Contact help desk 
	 * </pre>
	 * 
	 * @return true if authentication was successful
	 */
	public boolean authenticate() {
		try {
			System.out.println("com.yardi.ejb.UserServicesBean.authenticate() 0028  ");
			
			/*
			 * If they are not changing the password then the transaction begins in authenticate()
			 * If they are changing the password then the transaction begins in chgPwd()
			 * The transaction begin is conditioned because chgPwd() will call authenticate() while in a transaction
			 */
			if (!loginRequest.getChangePwd()) {
				System.out.println("com.yardi.ejb.UserServicesBean.authenticate() 000C ");
				tx.begin();
			}
					
			txStatus();
			
			if (!isUserNameValid()) {
				handleAuthFailure();
				return false;
			}
			
			if (isAuthenticated()) {
				System.out.println("com.yardi.ejb.UserServicesBean.authenticate() 0029 "
						+ "\n"
						+ "    feedback="
						+ feedback
						);
				loginSuccess();
				setLoginResponse();
				
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
				if (!loginRequest.getChangePwd()) {
					System.out.println("com.yardi.ejb.UserServicesBean.authenticate() 000B");
					tx.commit();
				} 
				
				return true;
			} else {
				handleAuthFailure();
				return false;
			}
		} catch (Exception e) {
			System.out.println("com.yardi.ejb.UserServicesBean.authenticate() 002D "
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
	 * Delegate to {@link com.yardi.ejb.UserProfileBean#changeUserToken(char []) com.yardi.ejb.UserProfileBean.changeUserToken(char [])} to set a new hashed password.<p>
	 * 
	 * Delegate to {@link com.yardi.ejb.UserProfileBean#loginSuccess() com.yardi.ejb.UserProfileBean.loginSuccess()} to give the user credit for 
	 * successfully authenticating although they have not yet completed the change password process. 
	 * 
	 * @param userName identifies the user profile to change
	 * @param newPassword char array containing the new password in plain text
	 */
	private void changeUserToken(final String userName, final char [] newPassword) {
		System.out.println("com.yardi.ejb.UserServicesBean.changeUserToken() 0004  ");
		txStatus();
		userProfileBean.changeUserToken(newPassword); //store new token in user profile
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
	 *         If the reason was 
	 *         <span style="font-family:consolas;">YRD0001</span> or 
	 *         <span style="font-family:consolas;">YRD000C</span> or 
	 *         commit the transaction
	 *       </li>
	 *       <li>
	 *         For any other reason rollback the transaction 
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
	 *         Get the password policy from {@link com.yardi.ejb.PasswordPolicyBean#getPwdPolicy() com.yardi.ejb.PasswordPolicyBean.getPwdPolicy()   } .
	 *       </li>
	 *       <li>
	 *         Find the user's token history using {@link com.yardi.ejb.UniqueTokens#findTokens(String) com.yardi.ejb.UniqueTokens.findTokens(String)}.
	 *       </li>
	 *       <li>
	 *         Password policy defines the maximum number of tokens to retain in the token history. If a user's token history exceeds this limit, 
	 *         the oldest tokens must be removed to enforce password policy correctly.
	 *         There may be extra tokens in history because password policy was changed. 
	 *       </li>
	 *       <li>
	 *         If the new password does not comply with password policy then rollback the transaction and return false to indicate the change password 
	 *         request was unsuccessful
	 *       </li>
	 *       <li>
	 *         Since saving the current token may exceed the maximum allowed by password policy, remove the oldest token beforehand to maintain compliance.
	 *       </li>
	 *       <li>
	 *         If unique tokens is being enforced in password policy then save the current token in history.
	 *       </li>
	 *       <li>
	 *         Change the token in the User_Profile entity to the new token.
	 *       </li>       
	 *       <li>
	 *         Update the user profile to reflect successful login.
	 *       </li>       
	 *       <li>
	 *         If the database table SESSIONS_TABLE has no row matching the session ID then insert a row for the current session.
	 *       </li>
	 *       <li>
	 *         If the database table SESSIONS_TABLE has a row matching the session ID then update the row in SESSIONS_TABLE.
	 *       </li>
	 *       <li>
	 *         Construct a response to the web request.
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
	 * YRD001B Authenticated with temporary password
	 * YRD001C Temporary password expired. Contact help desk 
	 * </pre>
	 * 
	 * @return true if change password process was successful 
	 */
	public boolean chgPwd() {
		System.out.println("com.yardi.ejb.UserServicesBean.chgPwd() 0032"
				+ "\n"
				+ "    userName ="
				+ loginRequest.getUserName()
				+ "\n"
				+ "    oldPassword ="
				+ loginRequest.getPassword()
				+ "\n"
				+ "    newPassword ="
				+ loginRequest.getNewPassword()
		);
		
		try {
			tx.begin();
			txStatus();

			if (!authenticate()) {
				System.out.println("com.yardi.ejb.UserServicesBean.chgPwd() 000E ");				
				handlePwdChangeAuthFailure();
				return false;
			}

			System.out.println("com.yardi.ejb.UserServicesBean.chgpwd() 0030");
			txStatus();
			getPwdPolicy(); 
			Vector<Unique_Tokens> userTokens = uniqueTokensBean.findTokens(loginRequest.getUserName());
			userTokens = uniqueTokensBean.removeExtraTokens(userTokens);
			
			if (!isPwdValidated(userTokens)) {
				System.out.println("com.yardi.ejb.UserServicesBean.chgpwd() 0026 ");
				return false;
			}
			
			uniqueTokensBean.removeOldestToken(userTokens);
			System.out.println("com.yardi.ejb.UserServicesBean.chgpwd() 0014 "
					+ "\n"
					+ "    userTokens="
					+ userTokens
					);
			persistUserToken();
			changeUserToken(loginRequest.getUserName(), loginRequest.getNewPassword().toCharArray());
			userProfileBean.loginSuccess();
			loginSuccess();
			setLoginResponse();
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
		System.out.println("com.yardi.ejb.UserServicesBean chgpwd() 0031");
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
		System.out.println("com.yardi.ejb.UserServicesBean getPwdPolicy 0021 ");
		
		if (pwdPolicy == null) {
			setPwdPolicy();
		}
		
		System.out.println("com.yardi.ejb.UserServicesBean getPwdPolicy 0017 "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy);
		return pwdPolicy;
	}
	
    /**
	 * Handle authorization failure.<p>
	 * <strong>Commit if user is not changing the password and feedback is:</strong><pre>
	 * YRD0002=Password expired
	 * YRD000C=Maximum signon attempts exceeded. The user profile has been disabled
	 * YRD000F=Invalid password
	 * YRD001B=Authenticated with temporary password
	 * </pre>
	 * <strong>Rollback if feedback was not in the above list and user is not changing the password</strong>  
	 */
	private void handleAuthFailure() {
		System.out.println("com.yardi.ejb.UserServicesBean.handleAuthFailure() 002A");

		if (feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000C) ||
			feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000F) ||
			feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD001B) ||
			feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD0002)	) {
			/*
			 * Need to distinguish between invalid password and invalid user name. If its invalid password 
			 * set feedback to YRD0001 because html does not distinguish between invalid password and invalid 
			 * user name. Also html specifically checks for YRD0001.
			 */
			System.out.println("com.yardi.ejb.UserServicesBean.handleAuthFailure() 002B");
			
			if (feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000F)) {
				System.out.println("com.yardi.ejb.UserServicesBean.handleAuthFailure() 0035");
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0001;
			}
				
			try {
					
				if (!loginRequest.getChangePwd()) {
					System.out.println("com.yardi.ejb.UserServicesBean.handleAuthFailure() 000D ");
					tx.commit();
				} 				
			} catch (Exception e) {
				System.out.println("com.yardi.ejb.UserServicesBean.handleAuthFailure() 0015 "
						+ "\n"
						+ "   exception="
						+ e
						);
				e.printStackTrace();
				rollback(tx);
			}			
		} else {
			System.out.println("com.yardi.ejb.UserServicesBean.handleAuthFailure() 002C");
			
			if (!loginRequest.getChangePwd()) {
				System.out.println("com.yardi.ejb.UserServicesBean.handleAuthFailure() 000F ");
				rollback(tx);
			} 
		}
	}

	/**
	 * Handle authentication fail during change password.
	 * The reason for failure determines whether the transaction is committed or rolled back.<p>
	 * If the reason is one of these:
	 * <ul>
	 *   <li><span style="font-family:consolas;">YRD000C=Maximum signon attempts exceeded.</span> The user profile has been disabled</li>
	 *   <li><span style="font-family:consolas;">YRD0001=Invalid user name or password</span></li> 
	 * </ul><p> 
	 * commit the transaction. Otherwise rollback the transaction.  
	 */
	private void handlePwdChangeAuthFailure() {
		System.out.println("com.yardi.ejb.UserServicesBean.handlePwdChangeAuthFailure() 0027 ");
		
		try {
			if (feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000C) ||
					feedback.equals(com.yardi.shared.rentSurvey.YardiConstants.YRD0001)) {
					System.out.println("com.yardi.ejb.UserServicesBean.handlePwdChangeAuthFailure() 0012 ");
					tx.commit();
				} else {
					System.out.println("com.yardi.ejb.UserServicesBean.handlePwdChangeAuthFailure() 0013 ");
					rollback(tx);
				}			
		} catch(Exception e) {
			System.out.println("com.yardi.ejb.UserServicesBean.handlePwdChangeAuthFailure() 0018 " 
					+ "\n    "
					+ e
			);
			rollback(tx);
		}		
	}

	/**
	 * Delegate to 
	 * {@link com.yardi.ejb.UserProfileBean#authenticate(String, String, boolean) com.yardi.ejb.UserProfileBean.authenticate(String, String, boolean)}
	 * for password authentication.
	 * <pre><strong>Feedback provided:</strong>
	 * YRD0001=Invalid user name or password
	 * YRD0003=This account is disabled
	 * YRD0004=This account is not active
	 * YRD000B=Password policy is missing
	 * YRD000C=Maximum signon attempts exceeded. The user profile has been disabled
	 * YRD000F=Invalid password
	 * YRD001C=Temporary password expired. Contact help desk for a new password
	 * </pre>
	 * @return true if the hash of the password the user provided is the same as the hashed password stored on the user profile 
	 */
	private boolean isAuthenticated() {
		System.out.println("com.yardi.ejb.UserServicesBean.isAuthenticated() 000A ");
		boolean authenticated = userProfileBean.authenticate(loginRequest.getUserName(),
				loginRequest.getPassword(), loginRequest.getChangePwd());
		feedback = userProfileBean.getFeedback();
		return authenticated;
	}

	/**
	 * Determine whether the new password conforms to password policy. 
	 * The transaction is rolled back if the new password does not conform to password policy.
	 * <pre>
	 * <strong>The following feedback is provided:</strong>
	 * YRD0000 Process completed normally
	 * YRD0005 Password must be at least %n characters long
	 * YRD0006 Password must contain at least 1 upper case
	 * YRD0007 Password must contain at least 1 lower case
	 * YRD0008 Password must contain at least 1 number
	 * YRD0009 Password must contain at least 1 special character
	 * YRD000A Password matches a password that was previously used
	 * YRD000B Password policy is missing
	 * YRD0010 New password must not contain current password
	 * YRD0011 New password must not contain user name in any case
	 * YRD0015 Password must not be longer than %n characters
	 * YRD0016 Password contains more than %n repeated characters
	 * YRD0017 Password must contain at least %n numbers
	 * YRD0018 Password must contain at least %n upper case characters
	 * YRD0019 Password must contain at least %n lower case characters
	 * YRD001A Password must contain at least %n special characters
	 * </pre>
	 * @param userTokens user's saved tokens from database table UNIQUE_TOKENS 
	 * @return true if the new password conforms to password policy
	 */
	private boolean isPwdValidated(Vector<Unique_Tokens> userTokens) {
		System.out.println("com.yardi.ejb.UserServicesBean.isPwdValidated() 0033"
				+ "\n    "
				+ "maxUniqueTokens="
				+ pwdPolicy.getPpNbrUnique() 
				+ "\n    "
				+ "tokens="
				+ userTokens
				);

		if (!pwdCompRulesBean.enforce(
				loginRequest.getNewPassword(), 
				loginRequest.getUserName(), 
				userProfileBean.getUserProfile().getUptoken(), 
				userTokens)) {
			System.out.println("com.yardi.ejb.UserServicesBean.isPwdValidated() 0010 ");
			feedback = pwdCompRulesBean.getFeedback();
			rollback(tx);
			return false;
		}

		return true;
	}
	
	/**
	 * Validate user name. The user name is valid if there is a corresponding row in the USER_PROFILE table.<p>
	 * If the user name is valid:
	 * <ul>
	 * <li>Inject the user profile into {@link com.yardi.ejb.UserGroupsBean com.yardi.ejb.UserGroupsBean}</li>
	 * <li>Set the initial page from {@link com.yardi.ejb.UserGroupsBean#getInitialPage(String) 
	 *   com.yardi.ejb.UserGroupsBean.getInitialPage(String)}</li>
	 * </ul>
	 * 
	 * <pre><strong>Feedback provided:</strong>
	 * YRD0001=Invalid user name or password</pre>
	 * @return false if there is no row in the USER_PROFILE table corresponding to the user name  
	 */
	private boolean isUserNameValid() {
		System.out.println("com.yardi.ejb.UserServicesBean.isUserNameValid() 0019 ");
		userGroupsBean.find(loginRequest.getUserName());
		
		if (userGroupsBean.getLoginUserProfile() == null) {
			System.out.println("com.yardi.ejb.UserServicesBean.isUserNameValid() 0009 ");
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0001;
			return false;
		}
		
		userProfileBean.setUserProfile(userGroupsBean.getLoginUserProfile());
		initialPage = userGroupsBean.getInitialPage(loginRequest.getUserName());
		
		if (userGroupsBean.getFeedback().equals(com.yardi.shared.rentSurvey.YardiConstants.YRD000E)) {
			System.out.println("com.yardi.ejb.UserServicesBean.isUserNameValid() 001C "
					+ "\n"
					+ "    userGroupsBean.getInitialPageList()="
					+ userGroupsBean.getInitialPageList()
					);
		}
		
		return true;
	}
	
	/**
	 * When the user successfully authenticates, this method will either insert into or update the database table SESSIONS_TABLE.<p>
	 *  
	 * If there is a row corresponding to sessionID in database table SESSIONS_TABLE then SESSIONS_TABLE is updated.
	 * If database table SESSIONS_TABLE has no row corresponding to sessionID insert a new row corresponding to sessionID.<p>
	 * 
	 * The actual update or persist is delegated to {@link com.yardi.ejb.SessionsTableBean com.yardi.ejb.SessionsTableBean}.<br><br>
	 * 
	 * @throws JsonProcessingException problems encountered when processing (parsing, generating) JSON content
	 */
	private void loginSuccess() throws JsonProcessingException {
		System.out.println("com.yardi.ejb.UserServicesBean.loginSuccess() 0005 ");
        txStatus();
        Sessions_Table sessionsTable = sessionsBean.find(loginRequest.getSessionID()); 

		if (sessionsTable == null) {
			sessionsBean.persist(
					loginRequest.getUserName(), 
					loginRequest.getSessionID(), 
					initialPage, 
					new java.sql.Timestamp(new java.util.Date().getTime()));
			System.out.println("com.yardi.ejb.UserServicesBean.loginSuccess() 001D"
				+ "\n"
				+ "   Sessions_Table="
				+ sessionsTable
				+ "\n"
				+ "   feedback="
				+ feedback
				);
		} else {
			sessionsBean.update(
					sessionsTable,
					loginRequest.getSessionID(), 
					initialPage, 
					new java.sql.Timestamp(new java.util.Date().getTime()));
			System.out.println("com.yardi.ejb.UserServicesBean.loginSuccess() 001E  "
				+ "\n"
				+ "  Sessions_Table="
				+ sessionsTable);
		}

		System.out.println("com.yardi.ejb.UserServicesBean.loginSuccess() 0016  "
				+ "\n"
				+ "    feedback="
				+ feedback
				+ "\n"
				+ "    initialPageList="
				+ initialPageList
				);
		txStatus();
	}
	
	/**
	 * If unique tokens is being enforced in password policy then persist the current user token in database table UNIQUE_TOKENS
	 */
	private void persistUserToken() {
		System.out.println("com.yardi.ejb.UserServicesBean.persistUserToken() 0025 ");
		
		if (pwdPolicy.getPpNbrUnique() > 0) {
			System.out.println("com.yardi.ejb.UserServicesBean.persistUserToken() 0020  "
					+ "\n"
					+ "   username="
					+ loginRequest.getUserName()
					+ "\n"
					+ "   userGroupsBean.getLoginUserProfile().getUptoken()="
					+ userGroupsBean.getLoginUserProfile().getUptoken()
					);
			txStatus();
			uniqueTokensBean.persist(loginRequest.getUserName(), userGroupsBean.getLoginUserProfile().getUptoken(), new java.util.Date()); //insert
		}
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
		System.out.println("com.yardi.ejb.UserServicesBean rollback() 0011");

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
		System.out.println("com.yardi.ejb.UserServicesBean setLoginRequest() 001B " 
				+ "\n   "
				+ toString());
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
		System.out.println("com.yardi.ejb.UserServicesBean.setLoginResponse() 0008  "
				+ "\n"
				+ "    msg[]="
				+ Arrays.toString(msg)
				+ "\n"
				+ "    feedback="
				+ feedback
				);
		ObjectMapper mapper = new ObjectMapper();
		loginResponse = new LoginResponse(
				loginRequest.getUserName(),
				"", //Password
				mapper.writeValueAsString(initialPageList), //new password
				msg[0],
				initialPage
			);
		System.out.println("com.yardi.ejb.UserServicesBean.setLoginResponse() 0007  "
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

		System.out.println("com.yardi.ejb.UserServicesBean setPwdPolicy 001F"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy
			+ "\n"
			+ "   feedback="
			+ feedback);
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
