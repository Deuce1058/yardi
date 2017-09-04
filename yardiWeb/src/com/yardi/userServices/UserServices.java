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
import com.yardi.ejb.UniqueTokensSesssionBean;
import com.yardi.ejb.UniqueTokensSesssionBeanRemote;
import com.yardi.ejb.UserProfileSessionBeanRemote;
import com.yardi.ejb.PasswordPolicySessionBeanRemote;
import com.yardi.ejb.PpPwdPolicy;
import com.yardi.ejb.UserProfile;


/**
 * Services for authentication and user profile management
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
	private String userName = "";
	private String feedback = "";
	private java.sql.Timestamp today = new java.sql.Timestamp(new java.util.Date().getTime());
	private PpPwdPolicy pwdPolicy = null;
	private UserProfile userProfile = null;
	
	@EJB UserProfileSessionBeanRemote userProfileBean; //bean is thread safe unless marked reentrant in the deployment descriptor
	@EJB PasswordPolicySessionBeanRemote passwordPolicyBean;
	@EJB UniqueTokensSesssionBeanRemote uniqueTokensBean;

	public UserServices(String userName) {
		this.userName = userName;
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
	public boolean chgPwd(String userName, char [] oldPassword, char [] newPassword) {

		if (authenticate(userName, oldPassword) == false) {
			return false;
		}
		
		if ( passwordPolicy(newPassword.toString()) == false ) {
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
		List<UniqueToken> userTokens = new ArrayList<UniqueToken>(50);
		EntityManager emgr = uniqueTokensBean.getEntityManager();
		Query qry = emgr.createNativeQuery(
			"SELECT * FROM DB2ADMIN.UNIQUE_TOKENS WHERE UP1_USER_NAME = :userName ORDER BY UP1_DATE_ADDED, UP1_RRN", 
			UniqueToken.class);
		userTokens = (ArrayList<UniqueToken>)qry.setParameter("userName", userName).getResultList();
		int nbrOfPwds = userTokens.size();
		UniqueToken uniqueToken; //single element from userTokens which is an ArrayList of UniqueToken.class 
		
		if (userTokens.size() > pwdPolicy.getPpNbrUnique()) {
			//there are more previous tokens stored than the current max. remove extra rows
			
			for(int i=0;userTokens.size() > 0 && i<userTokens.size(); i++) {
				uniqueToken = userTokens.get(i);
				
				if (i >= pwdPolicy.getPpNbrUnique()) {
					//extra rows
					long rrn = uniqueToken.getUp1Rrn();
					uniqueToken = null;
					uniqueToken = uniqueTokensBean.exists(rrn);
					
					if (uniqueToken != null) {
						uniqueTokensBean.beginTransaction();
						uniqueTokensBean.remove(uniqueToken); //delete the extra row
						uniqueTokensBean.commitTransaction();
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
		
		if (userTokens.size() == pwdPolicy.getPpNbrUnique() ) {
			//remove oldest stored token because a new token will be inserted
			uniqueToken = userTokens.get(userTokens.size() - 1);
			long rrn = uniqueToken.getUp1Rrn();
			uniqueToken = null;
			uniqueToken = uniqueTokensBean.exists(rrn);
			
			if (uniqueToken != null) {
				uniqueTokensBean.beginTransaction();
				uniqueTokensBean.remove(uniqueToken); //delete 
				uniqueTokensBean.commitTransaction();
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

		if (pwdPolicy.getPpNbrUnique() > 0) {
			uniqueTokensBean.beginTransaction();		
			uniqueToken = new UniqueToken(userName, userToken, new java.util.Date(gc.getTimeInMillis()));
			uniqueTokensBean.persist(uniqueToken); //insert
			uniqueTokensBean.commitTransaction();
		}
		
		gc.add(Calendar.DAY_OF_MONTH, pwdPolicy.getPpDays()); //new password expires date
		userProfileBean.beginTransaction();
		userProfile.setUptoken(userToken); //store new token in user profile
		userProfile.setUpPwdexpd(new java.util.Date(gc.getTimeInMillis())); //set password expires date
		userProfileBean.commitTransaction();
		return true;
	}
	
	public boolean authenticate(String userName, char [] password) {
		this.userName = userName;
		passwordAuthentication = getPasswordAuthentication(); //get an instance of PasswordAuthentication
		
		if (getPwdPolicy() == null) { //get the password policy
			return false;
		}
		
		feedback = com.yardi.rentSurvey.YardiConstants.YRD0000;
		//if there is a row in UserProfile for userName userProfile will have a reference to UserProfile.class
		findUserProfile(userName); 
		short signonAttempts = 0;
		
		if (userProfile == null) {
			return false;
		} else {
			signonAttempts = userProfile.getUpPwdAttempts();
			
			if (userProfile.getUpDisabledDate() != null) {
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0003;
				return false;
			}
			
			if (userProfile.getUpActiveYn().equals("N")) {
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0004;
				return false;
			}
			
			if (userProfile.getUpPwdexpd().getTime() <= today.getTime()) {
				//caller needs to call password expired method implemented as another servlet
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0002;
				return false;
			}
		}

		Boolean pwdValid = passwordAuthentication.authenticate(password, userProfile.getUptoken());
		userProfileBean.beginTransaction();
		
		if (pwdValid == false) {
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0001;
			signonAttempts++;
			//userProfile was set by findUserProfile(userName) see above
			userProfile.setUpPwdAttempts(signonAttempts);
			
			if (signonAttempts == pwdPolicy.getPpMaxSignonAttempts()) {
				userProfile.setUpDisabledDate(new java.util.Date());
				userProfile.setUpPwdAttempts(pwdPolicy.getPpMaxSignonAttempts());
				feedback = com.yardi.rentSurvey.YardiConstants.YRD000C;
			}
		} else {
			userProfile.setUpDisabledDate(null);
			userProfile.setUpPwdAttempts((short) 0);
			userProfile.setUpLastLoginDate(new java.util.Date());
		}
		
		userProfileBean.commitTransaction();
		return pwdValid;
	}

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private PpPwdPolicy getPwdPolicy() {
		
		if (pwdPolicy == null) {
			setPwdPolicy();
		}
		
		return pwdPolicy;
	}

	private void setPwdPolicy() {
		pwdPolicy = null;
		pwdPolicy = passwordPolicyBean.pwdPolicyRowExists(1L);
		
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
		UserProfile userProfile = null;
		userProfile = userProfileBean.userNameExists(userName);
		
		if (userProfile == null) {
			feedback = com.yardi.rentSurvey.YardiConstants.YRD0001;
		}
		
		return userProfile;
	}
}
