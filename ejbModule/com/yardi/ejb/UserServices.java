package com.yardi.ejb;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.yardi.ejb.UniqueTokens;
import com.yardi.ejb.UniqueTokensSesssionBeanRemote;
import com.yardi.ejb.UserProfileSessionBeanRemote;
import com.yardi.ejb.UserServicesRemote;
import com.yardi.userServices.InitialPage;
import com.yardi.userServices.LoginRequest;
import com.yardi.userServices.LoginResponse;
import com.yardi.userServices.PasswordAuthentication;
import com.yardi.userServices.UserGroupsGraph;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class UserServices implements UserServicesRemote {
	private PasswordAuthentication passwordAuthentication = null;
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
		Vector<UserGroupsGraph> userGroups = userGroupsBean.find(loginRequest.getUserName());
		//need initialPage for the sessions table and in the response to yardiLogin.html/changePwd.html so set it here
		initialPage = userGroups.get(0).getGmInitialPage(); //GM_INITIAL_PAGE from GROUPS_MASTER
		//debug
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
		//debug

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
		
		feedback = msg[0];
		//tokenize the session ID
		passwordAuthentication = new PasswordAuthentication();
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

		initialPageList = new Vector<InitialPage>();

		for (UserGroupsGraph g : userGroups) {
			//getGmDescription returns a string containing the short description for the button and a label for the button
			//getGmInitialPage() returns the url value for url= attribute
			initialPageList.add(new InitialPage(g.getGmDescription(),
				g.getGmInitialPage()));
		}
		
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
	public boolean chgPwd() {
		String userName = loginRequest.getUserName();
		char [] oldPassword = loginRequest.getPassword().toCharArray();
		char [] newPassword = loginRequest.getNewPassword().toCharArray();
		short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
		short pwdLifeInDays = pwdPolicy.getPpDays();
		UniqueTokens uniqueToken = null; //single element from userTokens which is an ArrayList of UniqueToken.class 
		Vector<UniqueTokens> userTokens = uniqueTokensBean.findTokens(userName);
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
			+ "\n "
			+ "  maxUniqueTokens="
			+ maxUniqueTokens 
			+ "\n "
			+ "  pwdLifeInDays="
			+ pwdLifeInDays 
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
		
		userTokens = removeExtraTokens(userTokens, uniqueToken, maxUniqueTokens, userName);
		//debug
		if (!(userTokens==null)) {
			System.out.println("com.yardi.ejb UserServices chgpwd() 0026"
					+ "\n"
					+ "   Vector<UniqueTokens>="
					+ userTokens
					);
		}
		//debug				
		if (passwordPolicy(new String(newPassword), userTokens) == false ) {
			//apply password policy to the new password
			//debug
			System.out.println("com.yardi.ejb UserServices chgPwd() 0010 "
					+ "\n "
					+ "  passwordPolicy() == false"
					);   
			//debug
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
		removeOldestToken(userTokens, maxUniqueTokens, uniqueToken);

		if (maxUniqueTokens > 0) {
			uniqueTokensBean.persist(userName, userProfile.getUptoken(), new java.util.Date()); //insert
		}
		
		changeUserToken(pwdLifeInDays, userName, newPassword);
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

		if (getPwdPolicy() == null) { //get the password policy
			return false;
		}
		
		String userName = loginRequest.getUserName();
		char [] password = loginRequest.getPassword().toCharArray();
		boolean userIsChangingPassword = loginRequest.getChangePwd();
		short signonAttempts = 0;
		short maxSignonAttempts = pwdPolicy.getPpMaxSignonAttempts();
		passwordAuthentication = getPasswordAuthentication(); //get an instance of PasswordAuthentication
		feedback = com.yardi.rentSurvey.YardiConstants.YRD0000;
		//if there is a row in UserProfile for userName userProfile will have a reference to UserProfile.class
		findUserProfile(userName); 
		//debug
		System.out.println("com.yardi.ejb UserServices authenticate() 0000"
				+ "\n " 
				+ "  userName =" + userName 
				+ "\n " 
				+ "  password = " + new String(password)
				+ "\n "
				+ "  userIsChangingPassword =" + userIsChangingPassword
				+ "\n "
				+ "  signonAttempts =" + signonAttempts 
				+ "\n "
				+ "  maxSignonAttempts = " + maxSignonAttempts
				+ "\n "
				+ "  feedback =" + feedback
				);
		//debug 
		
		if (userProfile == null) {
			//debug
			System.out.println("com.yardi.ejb UserServices authenticate() 0001 userProfile == null \n");
			//debug
			return false;
		}

		//debug
		System.out.println("com.yardi.ejb UserServices authenticate() 0002 userProfile =" + userProfile + "\n");
		//debug
		
		if (userProfile.getUpDisabledDate() != null) {
			//debug
			System.out.println("com.yardi.ejb UserServices authenticate() 0003 userProfile.getUpDisabledDate() != null\n");
			//debug
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0003;
			return false;
		}
		
		if (userProfile.getUpActiveYn().equals("N")) {
			//debug
			System.out.println("com.yardi.ejb UserServices authenticate() 0004 userProfile.getUpActiveYn().equals(N)\n");
			//debug
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0004;
			return false;
		}

		long passwordExpiration = userProfile.getUpPwdexpd().getTime();
		signonAttempts = userProfile.getUpPwdAttempts();
		Boolean pwdValid = passwordAuthentication.authenticate(password, userProfile.getUptoken());
		//debug
		System.out.println("com.yardi.ejb UserServices authenticate() 0005 "
				+ "\n "
				+ "  passwordExpiration =" + passwordExpiration 
				+ "\n "
				+ "  signonAttempts = " + signonAttempts
				+ "\n "
				+ "  pwdValid =" + pwdValid
				+ "\n "
				+ "  password =" + new String(password)
				+ "\n "
				+ "  userProfile.getUptoken() =" + userProfile.getUptoken()
				);
		//debug
		
		if (pwdValid == false) {
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0001;
			signonAttempts++;
			//userProfile was set by findUserProfile(userName) see above
			int rows = userProfileBean.setUpPwdAttempts(userName, signonAttempts);
			//debug
			System.out.println("com.yardi.ejb UserServices authenticate() 0006 "
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
				rows = userProfileBean.disable(userName, new java.sql.Timestamp(new java.util.Date().getTime()), 
					maxSignonAttempts);
				//debug
				System.out.println("com.yardi.ejb UserServices authenticate() 0007 "
						+ "\n "
						+ "  signonAttempts == maxSignonAttempts"  
						+ "\n "
						+ "  rows =" + rows
						);
				//debug

				if (rows != 1) {
					int z = rows;
				}
							
				feedback = com.yardi.rentSurvey.YardiConstants.YRD000C;
				//debug
				System.out.println("com.yardi.ejb UserServices authenticate() 0008 "
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
				System.out.println("com.yardi.ejb UserServices authenticate() 0009 "
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
				int rows = userProfileBean.loginSuccess(userName);
				//debug
				System.out.println("com.yardi.ejb UserServices authenticate() 000A "
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

	/**
     * <p>Retrieve password policy from the PpPwdPolicy table and make the policy available to other methods</p>
     * <p>Policy consists of:<br>
     *   1 Complexity</p>
     * <p>  1a Upper case required</p>
     * <p>  1b Lower case required,</p>
     * <p>  1c Special characters required</p>
     * <p>  1d Number required</p>
     * <p>2 Password length</p>
     * <p>3 Number of days before password must be changed</p>
     * <p>4 Number of unique passwords required specifies how many unique tokens are stored. Applied when changing the password. 
     *   This policy causes the new password token to be checked against the saved tokens for a match. This prevents user from 
     *   reusing passwords,</p>
     * <p>5 Maximum login attempts before password is disabled</p>
     * <br>
     * <p>Specific rules applied in passwordPolicy are complexity and length,</p>
     * <br>
	 * <p>Instance variable feedback indicates the status of the authenticate process</p> 
	 * 
	 * @param password
	 * @return Boolean
	 */
	public boolean passwordPolicy(final String password, final Vector<UniqueTokens> userTokens ) {
		/* implement these new rules:
		 *   Minimum length
		 *   Max length
		 *   No more than N repeated char
		 *   https://stackoverflow.com/questions/2622776/regex-to-match-four-repeated-letters-in-a-string-using-a-java-pattern
		 *   No fewer than N digits
		 *   No fewer than N upper/lower
		 *   No fewer than N special
		 *   Can not contain the user name (in any case)
		 */
		boolean hasUpper = false;
		boolean hasLower = false;
		boolean hasNumber = false;
		boolean hasSpecial = false;
		boolean upperRqd = false;
		boolean lowerRqd = false;
		boolean numberRqd = false;
		boolean specialRqd = false;
		feedback = com.yardi.rentSurvey.YardiConstants.YRD0000;
		
		if (getPwdPolicy() == null) {
			//debug 
			System.out.println("com.yardi.ejb UserServices passwordPolicy() 0019 getPwdPolicy() == null");
			//debug
			feedback = com.yardi.rentSurvey.YardiConstants.YRD000B;
			return false;
		}
		
		upperRqd   = Boolean.valueOf(pwdPolicy.getPpUpperRqd());
		lowerRqd   = Boolean.valueOf(pwdPolicy.getPpLowerRqd());
		numberRqd  = Boolean.valueOf(pwdPolicy.getPpNumberRqd());
		specialRqd = Boolean.valueOf(pwdPolicy.getPpSpecialRqd());
		//debug
		System.out.println("com.yardi.ejb UserServices passwordPolicy() 000B "
				+ "\n "
				+ "  upperRqd="
				+ upperRqd 
				+ "\n "
				+ "  lowerRqd="
				+ lowerRqd 
				+ "\n "
				+ "  numberRqd="
				+ numberRqd 
				+ "\n "
				+ "  specialRqd="
				+ specialRqd 
				+ "\n "
				+ "  password policy="
				+ pwdPolicy.toString()
				+ "\n "
				+ "  password="
				+ password
				+ "\n "
				+ "  password len="
				+ password.length() 
				);   
		//debug
		
		if (lowerRqd && com.yardi.rentSurvey.YardiConstants.PATTERN_LOWER.matcher(password).matches()) {
			hasLower = true;
			//debug
			System.out.println("com.yardi.ejb UserServices passwordPolicy() 000C "
					+ "\n "
					+ "  hasLower="
					+ hasLower 
					);   
			//debug
		} else {
			
			if (lowerRqd) {
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0007;
				return false;
			}
		}
		
		if (upperRqd && com.yardi.rentSurvey.YardiConstants.PATTERN_UPPER.matcher(password).matches()) {
			hasUpper = true;
			//debug
			System.out.println("com.yardi.ejb UserServices passwordPolicy() 000D "
					+ "\n "
					+ "  hasUpper="
					+ hasUpper 
					);   
			//debug
		} else {
			
			if (upperRqd) {
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0006;
				return false;
			}
		}
		
		if (numberRqd && com.yardi.rentSurvey.YardiConstants.PATTERN_NUMBER.matcher(password).matches()) {
			hasNumber = true;
			//debug
			System.out.println("com.yardi.ejb UserServices passwordPolicy() 000E "
					+ "\n "
					+ "  hasNumber="
					+ hasNumber 
					);   
			//debug
		} else {
			
			if (numberRqd) {
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0008;
				return false;
			}
		}
		
		if (specialRqd && com.yardi.rentSurvey.YardiConstants.PATTERN_SPECIAL1.matcher(password).matches()) {
			hasSpecial = true;
			//debug
			System.out.println("com.yardi.ejb UserServices passwordPolicy() 000F "
					+ "\n "
					+ "  hasSpecial="
					+ hasSpecial 
					);   
			//debug
		} else {
			
			if (specialRqd) {
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0009;
				return false;
			}
		}
		
		if (password.length() < pwdPolicy.getPpPwdMinLen()) {
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0005;
			return false;
		}
		
		short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
		
		if (maxUniqueTokens > 0 && !(userTokens==null)) { //If unique tokens being enforced and they have stored tokens
			//debug
			System.out.println("com.yardi.ejb UserServices passwordPolicy() 0021");
			for (UniqueTokens u : userTokens) {
				System.out.println(
					  "\n"
					+ "   userTokens="
					+ u
					);
			}
			//debug
			int nbrOfStoredTokens = userTokens.size();
			UniqueTokens uniqueToken; //single element from userTokens which is an ArrayList of UniqueToken.class 
			//debug
			System.out.println("com.yardi.ejb UserServices passwordPolicy() 0022"
				+ "\n"
				+ "   nbrOfStoredTokens="
				+ nbrOfStoredTokens 
				);
			//debug
			for(int i=0; i<nbrOfStoredTokens; i++) {
				uniqueToken = userTokens.get(i);
				//debug
				System.out.println("com.yardi.ejb UserServices passwordPolicy() 0023"
					+ "\n"
					+ "   uniqueToken="
					+ uniqueToken  
					);
				//debug
						
				if (passwordAuthentication.authenticate(//does the new password match any of the stored tokens?
					loginRequest.getNewPassword().toCharArray(), uniqueToken.getUp1Token())) {
					/* 
					 * Only check up to the maximum number of stored tokens established in password policy. If more tokens 
					 * are stored than the current maximum then ignore the extra tokens
					 */
					feedback = com.yardi.rentSurvey.YardiConstants.YRD000A;
					//debug
					System.out.println("com.yardi.ejb UserServices passwordPolicy() 0015 "
							+ "\n "
							+ "  newPassword="
							+ loginRequest.getNewPassword()
							+ "\n "
							+ "  uniqueToken.getUp1Token()="
							+ uniqueToken.getUp1Token()
							+ "\n "
							+ "  feedback="
							+ feedback
							);
					//debug  
					return false;
				}
				//debug
				System.out.println("com.yardi.ejb UserServices passwordPolicy() 0024"
					+ "\n"
					+ "   i="
					+ i  
					);
				//debug
			}
		}
		return true;
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
		pwdPolicy = passwordPolicyBean.find(1L);
		
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

	private PasswordAuthentication getPasswordAuthentication() {
		
		if (passwordAuthentication == null) {
			setPasswordAuthentication();
		}
		
		return passwordAuthentication;
	}

	private void setPasswordAuthentication() {
		passwordAuthentication = new PasswordAuthentication();
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
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove extra tokens in token history when the number of stored tokens exceeds the maximum defined in password 
	 * policy. This can happen if the maximum number of tokens to store is changed. 
	 * 
	 * @param userTokens
	 * @param uniqueToken
	 * @param maxUniqueTokens
	 * @param userName
	 */
	private Vector<UniqueTokens> removeExtraTokens(Vector<UniqueTokens> userTokens, UniqueTokens uniqueToken,
			final short maxUniqueTokens, final String userName) {
		//debug
		if (!(userTokens==null)) {
			System.out.println("com.yardi.ejb UserServices removeExtraTokens() 0028"
					+ "\n"
					+ "   Vector<UniqueTokens>="
					+ userTokens
					);
		}
		//debug
		
		if (maxUniqueTokens > 0) { // is unique passwords being enforced? 
			userTokens = uniqueTokensBean.findTokens(userName);
			int nbrOfStoredTokens = 0;
			
			if (!(userTokens == null)) { // do they have any stored tokens?
				//debug
				System.out.println("com.yardi.ejb UserServices removeExtraTokens() 0020");
				for (UniqueTokens u : userTokens) {
					System.out.println(
						  "\n"
						+ "   userTokens="
						+ u
						);
				}
				//debug
				nbrOfStoredTokens = userTokens.size();
				for(int i=maxUniqueTokens, tokenToRemove=maxUniqueTokens; i<nbrOfStoredTokens; i++) {
					//debug
					System.out.println("com.yardi.ejb UserServices removeExtraTokens() 0014 "
							+ "\n "
							+ "   uniqueToken="
							+ userTokens.get(tokenToRemove).toString()
							+ "\n "
							+ "   rrn="
							+ userTokens.get(tokenToRemove).getUp1Rrn()
							+ "\n "
							+ "   i="
							+ i
							+ "\n "
							+ "   maxUniqueTokens="
							+ maxUniqueTokens
							+ "\n "
							+ "   nbrOfStoredTokens="
							+ nbrOfStoredTokens
							+ "\n "
							+ "   tokenToRemove="
							+ tokenToRemove
							);
					//debug
					/*
					 * More tokens are being stored than the current max. These are extra rows
					 * First delete all of the extra tokens so that the check for unique tokens can just check all the 
					 * remaining tokens. 
					 * Next, remove the oldest stored token
					 * Finally, insert the new token
					 */
					uniqueToken = userTokens.get(tokenToRemove);
					long rrn = uniqueToken.getUp1Rrn();
					uniqueTokensBean.remove(rrn); // Delete the extra row. A new row will be inserted 
					userTokens.remove(tokenToRemove);
					// The vector elements after the one that was removed move up and occupy the position that was removed
				}
				//debug
				System.out.println("com.yardi.ejb UserServices removeExtraTokens() 0029"
						+ "\n"
						+ "   Vector<UniqueTokens>="
						+ userTokens
						);
				//debug
			}
		}
		return userTokens;
	}
	
	/**
	 * Remove the oldest stored token in token history so that the number of stored tokens in token history will not
	 * exceed the maximum defined in password policy when the new token is stored  
	 * 
	 * @param userTokens
	 * @param maxUniqueTokens
	 * @param uniqueToken
	 */
	private void removeOldestToken(Vector<UniqueTokens> userTokens, final short maxUniqueTokens, 
			UniqueTokens uniqueToken) {
		//debug
		if (!(userTokens==null)) {
			System.out.println("com.yardi.ejb UserServices removeOldestToken() 0011"
					+ "\n"
					+ "   maxUniqueTokens="
					+ maxUniqueTokens
					+ "\n"
					+ "   userTokens.size()="
					+ userTokens.size()
					);
			for (UniqueTokens u : userTokens) {
				System.out.println(
						  "\n"
						+ "   userTokens="
						+ u
						);
			} 
		}
		//debug
		
		if (!(userTokens==null) && maxUniqueTokens > 0 && userTokens.size() >= maxUniqueTokens) { 
			/*
			 *  if there are stored tokens and
			 *  unique tokens is being enforced and
			 *  the number of stored tokens is greater or equal to the maximum number of unique tokens to store
			 *  then remove oldest token 
			 */
			int tokenToRemove = maxUniqueTokens - 1;
			uniqueToken = userTokens.get(tokenToRemove);
			long rrn = uniqueToken.getUp1Rrn();
			uniqueTokensBean.remove(rrn); // Delete the extra row. A new row will be inserted 
			userTokens.remove(tokenToRemove);
			//debug
			System.out.println("com.yardi.ejb UserServices removeOldestToken() 0025"
					+ "\n"
					+ "   tokenToRemove="
					+ tokenToRemove
					+ "\n"
					+ "   rrn="
					+ rrn);
			for (UniqueTokens u : userTokens) {
				System.out.println(
					  "\n"
					+ "   uniqueToken="
					+ u
					);
			}
			//debug
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
		String userToken = passwordAuthentication.hash(newPassword); //hash of new password
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		long time = gc.getTimeInMillis();
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
	
	@Override
	public String toString() {
		return "UserServices [passwordAuthentication=" + passwordAuthentication + ", feedback=" + feedback + ", today="
				+ today + ", pwdPolicy=" + pwdPolicy + ", userProfile=" + userProfile + ", loginRequest=" + loginRequest
				+ ", loginResponse=" + loginResponse + ", initialPageList=" + initialPageList + ", initialPage="
				+ initialPage + ", userProfileBean=" + userProfileBean + ", uniqueTokensBean=" + uniqueTokensBean
				+ ", passwordPolicyBean=" + passwordPolicyBean + ", userGroupsBean=" + userGroupsBean
				+ ", sessionsBean=" + sessionsBean + "]";
	}
}
