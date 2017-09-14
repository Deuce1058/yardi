package com.yardi.userServices;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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

	public UserServices(LoginRequest loginRequest) {
		this.loginRequest = loginRequest;
		passwordPolicyBean = loginRequest.getPasswordPolicyBean();
		uniqueTokensBean = loginRequest.getUniqueTokensBean();
		userProfileBean = loginRequest.getUserProfileBean();
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
		
		if (authenticate() == false) {
			return false;
		}
		
		if (passwordPolicy(newPassword.toString()) == false ) {
			//apply password policy to the new password
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
		ArrayList<UniqueToken> userTokens = uniqueTokensBean.findTokens(userName);
		int nbrOfPwds = userTokens.size();
		UniqueToken uniqueToken; //single element from userTokens which is an ArrayList of UniqueToken.class 
		
		if (nbrOfPwds > maxUniqueTokens) {
			//there are more previous tokens stored than the current max. remove extra rows
			
			for(int i=0;nbrOfPwds > 0 && i<nbrOfPwds; i++) {
				uniqueToken = userTokens.get(i);
				
				if (i >= maxUniqueTokens) {
					//extra rows
					long rrn = uniqueToken.getUp1Rrn();
					uniqueToken = uniqueTokensBean.find(rrn);
					
					if (uniqueToken != null) {
						//uniqueTokensBean.beginTransaction();
						uniqueTokensBean.remove(uniqueToken); //delete the extra row
						//uniqueTokensBean.commitTransaction();
						userTokens.remove(i);
					}
				} else {
					//passwordAuthentication set in authenticate()
					if(passwordAuthentication.authenticate(newPassword, uniqueToken.getUp1Token())) {
						feedback = com.yardi.rentSurvey.YardiConstants.YRD000A;
						return false;
					}
				}
			}
		}
		
		if (nbrOfPwds == maxUniqueTokens) {
			//remove oldest stored token because a new token will be inserted
			uniqueToken = userTokens.get(userTokens.size() - 1);
			long rrn = uniqueToken.getUp1Rrn();
			uniqueToken = uniqueTokensBean.find(rrn);
			
			if (uniqueToken != null) {
				//uniqueTokensBean.beginTransaction();
				uniqueTokensBean.remove(uniqueToken); //delete 
				//uniqueTokensBean.commitTransaction();
				userTokens.remove(userTokens.size() - 1);
			}
		}
		
		String userToken = passwordAuthentication.hash(newPassword); //hash of new password
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.HOUR, 0);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.HOUR_OF_DAY, 0);
		long time = gc.getTimeInMillis();

		if (maxUniqueTokens > 0) {
			//uniqueTokensBean.beginTransaction();		
			uniqueToken = new UniqueToken(userName, userToken, new java.util.Date(gc.getTimeInMillis()));
			uniqueTokensBean.persist(uniqueToken); //insert
			//uniqueTokensBean.commitTransaction();
		}
		
		gc.add(Calendar.DAY_OF_MONTH, pwdLifeInDays); //new password expiration date
		//userProfileBean.beginTransaction();
		userProfile.setUptoken(userToken); //store new token in user profile
		userProfile.setUpPwdexpd(new java.util.Date(gc.getTimeInMillis())); //set password expires date
		//userProfileBean.commitTransaction();
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
		
		if (userProfile == null) {
			return false;
		}
		
		if (userProfile.getUpDisabledDate() != null) {
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0003;
			return false;
		}
		
		if (userProfile.getUpActiveYn().equals("N")) {
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0004;
			return false;
		}

		long passwordExpiration = userProfile.getUpPwdexpd().getTime();
		signonAttempts = userProfile.getUpPwdAttempts();
		Boolean pwdValid = passwordAuthentication.authenticate(password, userProfile.getUptoken());
		//userProfileBean.beginTransaction();
		
		if (pwdValid == false) {
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0001;
			signonAttempts++;
			//userProfile was set by findUserProfile(userName) see above
			userProfile.setUpPwdAttempts(signonAttempts);
			
			if (signonAttempts == maxSignonAttempts) {
				userProfile.setUpDisabledDate(new java.util.Date());
				userProfile.setUpPwdAttempts(maxSignonAttempts);
				feedback = com.yardi.rentSurvey.YardiConstants.YRD000C;
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
				return false;
			}
			
			if (userIsChangingPassword == false) {
				/*
				 * They have successfully logged in at this point only if they are not changing the password so only set 
				 * last login date when they are not changing the password 
				 */
				userProfile.setUpDisabledDate(null);
				userProfile.setUpPwdAttempts((short) 0);
				userProfile.setUpLastLoginDate(new java.util.Date());
			}
		}
		
		//userProfileBean.commitTransaction();
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
			return false;
		}
		
		upperRqd   = Boolean.valueOf(pwdPolicy.getPpUpperRqd());
		lowerRqd   = Boolean.valueOf(pwdPolicy.getPpLowerRqd());
		numberRqd  = Boolean.valueOf(pwdPolicy.getPpNumberRqd());
		specialRqd = Boolean.valueOf(pwdPolicy.getPpSpecialRqd());
		
		if (lowerRqd && com.yardi.rentSurvey.YardiConstants.PATTERN_LOWER.matcher(password).matches()) {
			hasLower = true;
		} else {
			
			if (lowerRqd) {
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0007;
				return false;
			}
		}
		
		if (upperRqd && com.yardi.rentSurvey.YardiConstants.PATTERN_UPPER.matcher(password).matches()) {
			hasUpper = true;
		} else {
			
			if (upperRqd) {
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0006;
				return false;
			}
		}
		
		if (numberRqd && com.yardi.rentSurvey.YardiConstants.PATTERN_NUMBER.matcher(password).matches()) {
			hasNumber = true;
		} else {
			
			if (numberRqd) {
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0008;
				return false;
			}
		}
		
		if (specialRqd && com.yardi.rentSurvey.YardiConstants.PATTERN_SPECIAL1.matcher(password).matches()) {
			hasSpecial = true;
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
				+ "]";
	}
}
