package com.yardi.userServices;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.Query;

import com.yardi.ejb.UniqueToken;
import com.yardi.ejb.UniqueTokensSesssionBeanRemote;
import com.yardi.ejb.UserProfileSessionBeanRemote;
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
public class UserServices {
	private PasswordAuthentication passwordAuthentication = null;
	//private String userName = "";
	private String feedback = "";
	private java.sql.Timestamp today = new java.sql.Timestamp(new java.util.Date().getTime());
	private PpPwdPolicy pwdPolicy = null;
	private UserProfile userProfile = null;
	private LoginRequest loginRequest;
	UserProfileSessionBeanRemote userProfileBean; //bean is thread safe unless marked reentrant in the deployment descriptor
	PasswordPolicySessionBeanRemote passwordPolicyBean;
	UniqueTokensSesssionBeanRemote uniqueTokensBean;

	public UserServices() {
	}
	
	public UserServices(LoginRequest loginRequest) {
		this.loginRequest = loginRequest;
		passwordPolicyBean = loginRequest.getPasswordPolicyBean();
		uniqueTokensBean = loginRequest.getUniqueTokensBean();
		userProfileBean = loginRequest.getUserProfileBean();
		//debug
		System.out.println("com.yardi.userServices UserServices com.yardi.userServices() 001B " + toString());
		//debug
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
		//debug
		System.out.println("com.yardi.userServices UserServices chgPwd() 001A"
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
		
		if (passwordPolicy(new String(newPassword)) == false ) {
			//apply password policy to the new password
			//debug
			System.out.println("com.yardi.userServices UserServices chgPwd() 0010 "
					+ "\n "
					+ "  passwordPolicy() == false"
					);   
			//debug
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
		
		feedback = com.yardi.rentSurvey.YardiConstants.YRD0000;
		short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
		short pwdLifeInDays = pwdPolicy.getPpDays();
		Vector<UniqueToken> userTokens = uniqueTokensBean.findTokens(userName);
		int nbrOfStoredTokens = userTokens.size();
		UniqueToken uniqueToken; //single element from userTokens which is an ArrayList of UniqueToken.class 
		//debug
		System.out.println("com.yardi.userServices UserServices chgPwd() 0011 "
				+ "\n "
				+ "  feedback="
				+ feedback
				+ "\n "
				+ "  maxUniqueTokens="
				+ maxUniqueTokens 
				+ "\n "
				+ "  pwdLifeInDays="
				+ pwdLifeInDays 
				+ "\n "
				+ "  nbrOfStoredTokens="
				+ nbrOfStoredTokens 
				);  
		System.out.println("com.yardi.userServices UserServices chgPwd() 0012 "
				+ "\n "
				);
		for (UniqueToken t : userTokens) {

			if (!(t==null)) {
				System.out.println("\n   uniqueToken=" + t);
			}
		}
		//debug
		
		if (nbrOfStoredTokens > maxUniqueTokens) {
			//there are more previous tokens stored than the current max. remove extra rows
			//debug
			System.out.println("com.yardi.userServices UserServices chgPwd() 0013 "
					+ "\n "
					+ "  nbrOfStoredTokens="
					+ nbrOfStoredTokens
					+ "\n "
					+ "  maxUniqueTokens="
					+ maxUniqueTokens
					);
			//debug
	
			for(int i=0;nbrOfStoredTokens > 0 && i<nbrOfStoredTokens; i++) {
				uniqueToken = userTokens.get(i);
				
				if (i >= maxUniqueTokens && uniqueToken != null) {
					//extra rows
					long rrn = uniqueToken.getUp1Rrn();
					uniqueTokensBean.remove(rrn); //delete the extra row
					userTokens.remove(i);
					//debug
					System.out.println("com.yardi.userServices UserServices chgPwd() 0014 "
							+ "\n "
							);
					for (UniqueToken t : userTokens) {

						if (!(t==null)) {
							System.out.println("\n   uniqueToken="
									+ t
									);  
						}
					}
					//debug
				} else {
					//passwordAuthentication set in authenticate()
					if (maxUniqueTokens > 0 && 
						passwordAuthentication.authenticate(newPassword, uniqueToken.getUp1Token())) {
						/* 
						 * Only check up to the maximum number of stored tokens established in password policy. If more tokens 
						 * are stored than the current maximum then ignore the extra tokens
						 */
						feedback = com.yardi.rentSurvey.YardiConstants.YRD000A;
						//debug
						System.out.println("com.yardi.userServices UserServices chgPwd() 0015 "
								+ "\n "
								+ "  newPassword="
								+ newPassword
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
				}
			}
		}
		
		if (nbrOfStoredTokens == maxUniqueTokens && maxUniqueTokens > 0) {
			//remove oldest stored token because a new token will be inserted
			uniqueToken = userTokens.get(userTokens.size() - 1);
			//debug
			System.out.println("com.yardi.userServices UserServices chgPwd() 0016 "
					+ "\n "
					);
			for (UniqueToken t : userTokens) {

				if (!(t==null)) {
					System.out.println("\n   uniqueToken="
							+ t
							);  
				}
			}
			//debug
			
			if (uniqueToken != null) {
				long rrn = uniqueToken.getUp1Rrn();
				uniqueTokensBean.remove(rrn); //delete 
				userTokens.remove(userTokens.size() - 1);
				//debug
				System.out.println("com.yardi.userServices UserServices chgPwd() 0017 "
						+ "\n "
						);
				for (UniqueToken t : userTokens) {

					if (!(t==null)) {
						System.out.println("\n   uniqueToken="
								+ t
								);  
					}
				}
				//debug
			}
		}
		
		String userToken = passwordAuthentication.hash(newPassword); //hash of new password
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		long time = gc.getTimeInMillis();
		//debug
		System.out.println("com.yardi.userServices UserServices chgPwd() 0018 "
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

		if (maxUniqueTokens > 0) {
			uniqueTokensBean.persist(userName, userProfile.getUptoken(), new java.util.Date()); //insert
		}
		
		gc.add(Calendar.DAY_OF_MONTH, pwdLifeInDays); //new password expiration date
		userProfileBean.changeUserToken(userName, userToken, new java.util.Date(gc.getTimeInMillis())); //store new token in user profile
		userProfileBean.loginSuccess(userName);
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
		System.out.println("com.yardi.userServices UserServices authenticate() 0000"
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
			System.out.println("com.yardi.userServices UserServices authenticate() 0001 userProfile == null \n");
			//debug
			return false;
		}

		//debug
		System.out.println("com.yardi.userServices UserServices authenticate() 0002 userProfile =" + userProfile + "\n");
		//debug
		
		if (userProfile.getUpDisabledDate() != null) {
			//debug
			System.out.println("com.yardi.userServices UserServices authenticate() 0003 userProfile.getUpDisabledDate() != null\n");
			//debug
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0003;
			return false;
		}
		
		if (userProfile.getUpActiveYn().equals("N")) {
			//debug
			System.out.println("com.yardi.userServices UserServices authenticate() 0004 userProfile.getUpActiveYn().equals(N)\n");
			//debug
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0004;
			return false;
		}

		long passwordExpiration = userProfile.getUpPwdexpd().getTime();
		signonAttempts = userProfile.getUpPwdAttempts();
		Boolean pwdValid = passwordAuthentication.authenticate(password, userProfile.getUptoken());
		//debug
		System.out.println("com.yardi.userServices UserServices authenticate() 0005 "
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
			System.out.println("com.yardi.userServices UserServices authenticate() 0006 "
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
				System.out.println("com.yardi.userServices UserServices authenticate() 0007 "
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
				System.out.println("com.yardi.userServices UserServices authenticate() 0008 "
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
				System.out.println("com.yardi.userServices UserServices authenticate() 0009 "
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
				System.out.println("com.yardi.userServices UserServices authenticate() 000A "
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
     * Retrieve password policy from the PpPwdPolicy table and make the policy available to other methods
     * Policy consists of:
     * 1 Complexity
     *   1a Upper case required
     *   1b Lower case required
     *   1c Special characters required
     *   1d Number required
     * 2 Password length
     * 3 Number of days before password must be changed
     * 4 Number of unique passwords required specifies how many unique tokens are stored. Applied when changing the password. 
     *   This policy causes the new password token to be checked against the saved tokens for a match. This prevents user from 
     *   reusing passwords
     * 5 Maximum login attempts before password is disabled
     * 
     * Specific rules applied in passwordPolicy are complexity and length
     * 
	 * Instance variable feedback indicates the status of the authenticate process 
	 * 
	 * @param password
	 * @return Boolean
	 */
	public boolean passwordPolicy(String password ) {
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
			System.out.println("com.yardi.userServices UserServices passwordPolicy() 0019 getPwdPolicy() == null");
			//debug
			return false;
		}
		
		upperRqd   = Boolean.valueOf(pwdPolicy.getPpUpperRqd());
		lowerRqd   = Boolean.valueOf(pwdPolicy.getPpLowerRqd());
		numberRqd  = Boolean.valueOf(pwdPolicy.getPpNumberRqd());
		specialRqd = Boolean.valueOf(pwdPolicy.getPpSpecialRqd());
		//debug
		System.out.println("com.yardi.userServices UserServices passwordPolicy() 000B "
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
			System.out.println("com.yardi.userServices UserServices passwordPolicy() 000C "
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
			System.out.println("com.yardi.userServices UserServices passwordPolicy() 000D "
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
			System.out.println("com.yardi.userServices UserServices passwordPolicy() 000E "
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
			System.out.println("com.yardi.userServices UserServices passwordPolicy() 000F "
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
		
		return true;
	}
	
	public String getFeedback() {
		return feedback;
	}

	private PpPwdPolicy getPwdPolicy() {
		
		if (pwdPolicy == null) {
			setPwdPolicy();
		}
		
		return pwdPolicy;
	}

	private void setPwdPolicy() {
		//passwordPolicyBean is null. Cant use @EJB in pojo. Has to be a servlet
		//LoginService needs to store @EJB references in LoginRequest
		//pwdPolicy = null;
		//EntityManager emgr = passwordPolicyBean.getEmgr();
		//pwdPolicy = (PpPwdPolicy) emgr.createNativeQuery("SELECT * FROM DB2ADMIN.PP_PWD_POLICY WHERE PP_RRN = :rrn", PpPwdPolicy.class)
		//		.setParameter("rrn", 1L).getSingleResult();

		pwdPolicy = passwordPolicyBean.find(1L);
		
		if (pwdPolicy == null) {
			feedback = com.yardi.rentSurvey.YardiConstants.YRD000B;
		}
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

	@Override
	public String toString() {
		return "UserServices [passwordAuthentication=" + passwordAuthentication
				+ ", feedback=" + feedback + ", today=" + today
				+ ", pwdPolicy=" + pwdPolicy + ", userProfile=" + userProfile
				+ ", loginRequest=" + loginRequest + ", userProfileBean="
				+ userProfileBean + ", passwordPolicyBean="
				+ passwordPolicyBean + ", uniqueTokensBean=" + uniqueTokensBean
				+ "]"
				+ "\n  "
				+ userProfileBean.stringify()
				+ "\n  "
				+ uniqueTokensBean.stringify()
				+ "\n  "
				+ passwordPolicyBean.stringify();
	}
}