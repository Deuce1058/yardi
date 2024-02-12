package com.yardi.ejb;

import java.util.Vector;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;

import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.shared.userServices.PasswordAuthentication;
import com.yardi.shared.userServices.PasswordStatistics;

/**
 * Implementation of password policy rules for password composition.<p>
 * 
 * When the user changes their password either on demand or because the password expired, the <i>enforce()</i> method of this class tests the new
 * password to make sure that it conforms to the password policy rules on complexity and reuse. Note that com.yardi.ejb.PasswordPolicyBean is a 
 * singleton. Enforcing the password policy in PasswordPolicyBean may not be thread safe. Instead, the password policy rules for complexity and 
 * reuse are enforced here where the state is easier to manage.<p>
 * 
 * Helper classes:<br>
 * com.yardi.shared.userServices.PasswordAuthentication hashes the new password.<br>
 * com.yardi.shared.userServices.PasswordStatistics scans the new password and compiles statistics such as number of repeated characters.
 */
@Stateful
public class PwdCompositionRulesBean implements PwdCompositionRules {
	/**
	 * Reference to Pwd_Policy entity obtained from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy().
	 */
	private Pwd_Policy pwdPolicy;
	/**
	 * Injected reference to com.yardi.ejb.PasswordPolicyBean.
	 */
	@EJB PasswordPolicy passwordPolicyBean;
	/**
	 * Status of the most recent method call that provides feedback<p> 
	 * Clients can read this field to determine the status of the most recent method call that provides feedback.
	 */
	private String feedback = "";

    public PwdCompositionRulesBean() {
    }

    /**
     * Enforce the password policy on the new password<p>
     * <strong>The following feedback is provided:</strong><br>
     * <span style="font-family:consolas;">  YRD0000 Process completed normally</span><br>
     * <span style="font-family:consolas;">  YRD0005 Password must be at least %n characters long</span><br>
     * <span style="font-family:consolas;">  YRD0006 Password must contain at least 1 upper case</span><br>
     * <span style="font-family:consolas;">  YRD0007 Password must contain at least 1 lower case</span><br>
     * <span style="font-family:consolas;">  YRD0008 Password must contain at least 1 number</span><br>
     * <span style="font-family:consolas;">  YRD0009 Password must contain at least 1 special character</span><br>
     * <span style="font-family:consolas;">  YRD000A Password matches a password that was previously used</span><br>
     * <span style="font-family:consolas;">  YRD000B Password policy is missing</span><br>
     * <span style="font-family:consolas;">  YRD0010 New password must not contain current password</span><br>
     * <span style="font-family:consolas;">  YRD0011 New password must not contain user name in any case</span><br>
     * <span style="font-family:consolas;">  YRD0015 Password must not be longer than %n characters</span><br>
     * <span style="font-family:consolas;">  YRD0016 Password contains more than %n repeated characters</span><br>
     * <span style="font-family:consolas;">  YRD0017 Password must contain at least %n numbers</span><br>
     * <span style="font-family:consolas;">  YRD0018 Password must contain at least %n upper case characters</span><br>
     * <span style="font-family:consolas;">  YRD0019 Password must contain at least %n lower case characters</span><br>
     * <span style="font-family:consolas;">  YRD001A Password must contain at least %n special characters</span>
     * @param password new password in plain text
     * @param userName user ID
     * @param userToken current hashed password 
     * @param userTokens all of the stored tokens for the user
     * @return boolean indicating whether new password conforms to password policy
     */
	public boolean enforce(final String password, final String userName, final String userToken, final Vector<Unique_Tokens> userTokens) {
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0000 "
				+ "\n "
				+ "   password="
				+ password
				+ "\n "
				+ "   userName="
				+ userName
				+ "\n "
				+ "   userToken="
				+ "\n "
				+ "   userTokens="
				+ userTokens.toString()
				);
		//debug
		boolean hasUpper = false;
		boolean hasLower = false;
		boolean hasNumber = false;
		boolean hasSpecial = false;
		boolean upperRqd = false;
		boolean lowerRqd = false;
		boolean numberRqd = false;
		boolean specialRqd = false;
		PasswordStatistics pwdStatistics; 
		feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		//getPwdPolicy();
		
		if (pwdPolicy==null) {
			//debug 
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() pwdPolicy==null 0001 ");
			//debug
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
			return false;
		}
		
		upperRqd   = pwdPolicy.getPpUpperRqd();    //at least one upper case character required
		lowerRqd   = pwdPolicy.getPpLowerRqd();    //at least one lower case character required
		numberRqd  = pwdPolicy.getPpNumberRqd();   //at least one number required
		specialRqd = pwdPolicy.getPpSpecialRqd();  //at least one special character required
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0002 "
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
		
		/*
		 * Scan the new password and compile statistics needed to enforce certain rules like upper case character required.
		 */
		pwdStatistics = new PasswordStatistics(password.toCharArray());
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0003 "
				+ "\n   "
				+ pwdStatistics.toString1());
		//debug


		//at least one lower case character required and number of lower > 0
		if (lowerRqd && pwdStatistics.getPwdNbrLower() > 0) {
			hasLower = true;
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0004 "
					+ "\n   "
					+ "  hasLower="
					+ hasLower 
					);   
			//debug
		} else {

			if (lowerRqd) {
				//debug
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0005 ");
				//debug
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0007;
				
				if (!(pwdPolicy.getPpNbrLower()==null)) {
				    String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0019.split("%n");
				    feedback = s[0] + pwdPolicy.getPpNbrLower() + s[1];
				}
				
				return false;
			}
		}

		//at least one upper case character required and number of upper > 0
		if (upperRqd && pwdStatistics.getPwdNbrUpper() > 0) {
			hasUpper = true;
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0006 "
					+ "\n "
					+ "  hasUpper="
					+ hasUpper 
					);   
			//debug
		} else {

			if (upperRqd) {
				//debug
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enfo0rce() 0007 ");
				//debug
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0006;
				
				if (!(pwdPolicy.getPpNbrUpper()==null)) {
				    String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0018.split("%n");
				    feedback = s[0] + pwdPolicy.getPpNbrUpper() + s[1];
				}
				
				return false;
			}
		}

		//at least one number is required and number of digits > 0
		if (numberRqd && pwdStatistics.getPwdNbrNbr() > 0) {
			hasNumber = true;
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0008 "
					+ "\n "
					+ "  hasNumber="
					+ hasNumber 
					);   
			//debug
		} else {

			if (numberRqd) {
				//debug
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0009 ");
				//debug
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0008;

				if (!(pwdPolicy.getPpNbrDigits()==null)) {
					    String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0017.split("%n");
					    feedback = s[0] + pwdPolicy.getPpNbrDigits() + s[1];
				}

				return false;
			}
		}

		//at least one special character is required and number of special characters > 0
		if (specialRqd && pwdStatistics.getPwdNbrSpecial() > 0) {
			hasSpecial = true;
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000A "
					+ "\n "
					+ "  hasSpecial="
					+ hasSpecial 
					);   
			//debug
		} else {

			if (specialRqd) {
				//debug
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000B ");
				//debug
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0009;
				
				if (!(pwdPolicy.getPpNbrSpecial()==null)) {
					String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD001A.split("%n");
					feedback = s[0] + pwdPolicy.getPpNbrSpecial() + s[1];
				}
				
				return false;
			}
		}

		//new password length is compared to minimum password length
		if (pwdStatistics.getPwdLength() < pwdPolicy.getPpPwdMinLen()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000C ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0005.split("%n");
			feedback = s[0] + pwdPolicy.getPpPwdMinLen() + s[1];
			return false;
		}

		//max password length is in force and new password length is > max password length
		if (!(pwdPolicy.getPpMaxPwdLen()==null) && pwdStatistics.getPwdLength() > pwdPolicy.getPpMaxPwdLen()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000D ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0015.split("%n");
			feedback = s[0] + pwdPolicy.getPpMaxPwdLen() + s[1];
			return false;
		}

		//max repeated characters is in force and number of repeated characters is > max repeated characters
		if (!(pwdPolicy.getPpMaxRepeatChar()==null) && pwdStatistics.getPwdNbrRepeatedChar() > pwdPolicy.getPpMaxRepeatChar()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000E ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0016.split("%n");
			feedback = s[0] + pwdPolicy.getPpMaxRepeatChar() + s[1];
			return false;
		}			

		//at least one number is required and number of digits < minimum number of digits
		if (!(pwdPolicy.getPpNbrDigits()==null) && pwdStatistics.getPwdNbrNbr() < pwdPolicy.getPpNbrDigits()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000F ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0017.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrDigits() + s[1];
			return false;
		}

		//at least one upper case character is required and number of upper < minimum number of upper 
		if (!(pwdPolicy.getPpNbrUpper()==null) && pwdStatistics.getPwdNbrUpper() < pwdPolicy.getPpNbrUpper()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0010 ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0018.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrUpper() + s[1];
			return false;
		}

		//at least one lower case character is required and number of lower < minimum number of lower 
		if (!(pwdPolicy.getPpNbrLower()==null) && pwdStatistics.getPwdNbrLower() < pwdPolicy.getPpNbrLower()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0011 ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0019.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrLower() + s[1];
			return false;
		}

		//at least one special character is required and number of special < minimum number of special
		if (!(pwdPolicy.getPpNbrSpecial()==null) && pwdStatistics.getPwdNbrSpecial() < pwdPolicy.getPpNbrSpecial()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0012 ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD001A.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrSpecial() + s[1];
			return false;
		}

		//password cant contain user ID is in force and new password contains user ID
		if (pwdPolicy.isPpCantContainId() && passwordConatinsUserName(userName, password)) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0013 ");
			//debug
			return false;
		}

		//new password cant contain the current password is in force and new password contains current password
		if (pwdPolicy.isPpCantContainPwd() && passwordContainsCurrent(userName, password, userToken)) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0014 ");
			//debug
			return false;
		}			

		short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
		
		if (maxUniqueTokens > 0 && !(userTokens==null)) { //If unique tokens being enforced and they have stored tokens
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0015 ");
			for (Unique_Tokens u : userTokens) {
				System.out.println(
					  "\n"
					+ "   userTokens="
					+ u
					);
			}
			//debug
			int nbrOfStoredTokens = userTokens.size();
			Unique_Tokens uniqueToken; //single element from userTokens which is a Vector of Unique_Token entities 
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0016 "
				+ "\n"
				+ "   nbrOfStoredTokens="
				+ nbrOfStoredTokens 
				);
			//debug
			for(int i=0; i<nbrOfStoredTokens; i++) {
				uniqueToken = userTokens.get(i);
				//debug
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0017 "
					+ "\n"
					+ "   uniqueToken="
					+ uniqueToken  
					);
				//debug
						
				if (passwordAuthentication.authenticate(//does the new password match any of the stored tokens?
					password.toCharArray(), uniqueToken.getUp1Token())) {
					/* 
					 * Only check up to the maximum number of stored tokens established in password policy. If more tokens 
					 * are stored than the current maximum then ignore the extra tokens
					 */
					feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000A;
					//debug
					System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0018 "
							+ "\n "
							+ "  newPassword="
							+ password
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
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0019 "
					+ "\n"
					+ "   i="
					+ i  
					);
				//debug
			}
		}
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
	 * Returns the password policy obtained from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy().
	 * @return reference to Pwd_Policy entity
	 */
    private Pwd_Policy getPwdPolicy() {
    	//debug
    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.getPwdPolicy() 001A ");
    	//debug
		
		if (pwdPolicy==null) {
	    	//debug
	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.getPwdPolicy() 001B ");
	    	//debug
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.getPwdPolicy() 001C "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			);

		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.getPwdPolicy() 001D ");
		//debug
		return pwdPolicy;
	}

	/**
     * Determine whether the new password contains the user name.<p>
     * There are three general cases to test:<br>
     * 1. The length of the new password is equal to the length of the user ID. Just compare the two strings.<br> 
     * 2. The length of the new password length is shorter than the user ID length. There is no way the new password can contain the user ID<br>
     * 3. The new password length is longer than the user ID length. Compare substrings of the new password to the user ID<br>
     * @param userName user ID
     * @param password new password in plain text
     * @return true if the new password contains the user ID, false if the new password does not contain the user ID
     */
    private boolean passwordConatinsUserName(final String userName, final String password) {
    	//debug
    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 001E "
    			+ "\n    "
    			+ "userName="
    			+ userName
    			+ "\n    "
    			+ "password="
    			+ password
    			);
    	//debug
		int passwordLength = password.length();
		int lengthToExtract;
		int startPosition;
		int endPosition;
		String substr = "";
		
		//trivial case #1 equal lengths. compare them directly
		if (userName.length()==password.length() && password.equalsIgnoreCase(userName)  ) {
	    	//debug
	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 001F "
	    			+ "\n    "
	    			+ "userName="
	    			+ userName
	    			+ "\n    "
	    			+ "password="
	    			+ password
	    			);
			//debug
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0011;
			return true;
		} else {

			if (userName.length()==password.length()) {
		    	//debug
		    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 0020 ");
				//debug
				return false; //new password does not contain user ID
			}
		}
		
		//trivial case #2 new password length is shorter than the user ID length. There is no way the new password can contain the user ID
		if (password.length() < userName.length()) {
			//if new password is shorter than the user name then the new password can never contain the user name
	    	//debug
	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 0021 "
	    			+ "\n    "
	    			+ "userName="
	    			+ userName
	    			+ "\n    "
	    			+ "password="
	    			+ password
	    			);
			//debug
			return false; //new password does not contain user ID
		}		
		
		//when the new password length is longer than the user ID length compare substrings of the new password to the user ID
		for(lengthToExtract=userName.length(), startPosition=0, endPosition=startPosition+lengthToExtract; 
				lengthToExtract<passwordLength;
				endPosition++) {
	    	//debug
	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 0022 "
	    			+ "\n    "
	    			+ "lengthToExtract="
	    			+ lengthToExtract
	    			+ "\n    "
	    			+ "startPosition="
	    			+ startPosition
	    			+ "\n    "
	    			+ "endPosition="
	    			+ endPosition
	    			+ "\n    "
	    			+ "passwordLength="
	    			+ passwordLength
	    			);
	    	//debug
	    	//extract a substring of the new password equal in length to the length of the user ID
			substr = password.substring(startPosition, endPosition);  
	    	//debug
	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 0023 "
	    			+ "\n    "
	    			+ "substr="
	    			+ substr
	    			);
	    	//debug

	    	if (substr.equalsIgnoreCase(userName)) {
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0011;
				//debug
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 0024 ");
				//debug
				return true;
			}
			
			startPosition++;
	    	//debug
	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 0025 "
	    			+ "\n    "
	    			+ "startPosition="
	    			+ startPosition
	    			);
	    	//debug
			
			if (startPosition+lengthToExtract > passwordLength) {
				/*
				 * The end of new password has been reached.
				 * Start over beginning at index 0 and ending at 1 + the current ending index. On the next iteration the substring will be one longer.
				 * Adjust the length to extract so that we stay inside the loop as long as lengthToExtract < passwordLength.
				 */
				startPosition = 0;
				endPosition = startPosition+lengthToExtract;
				lengthToExtract++;
		    	//debug
		    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 0026 "
		    			+ "\n    "
		    			+ "startPosition="
		    			+ startPosition
		    			+ "\n    "
		    			+ "endPosition="
		    			+ endPosition
		    			+ "\n    "
		    			+ "lengthToExtract="
		    			+ lengthToExtract
		    			);
		    	//debug
			}
			
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 0027 "
					+ "\n   "
					+ "   substr="
					+ substr
					);
		}
		
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 0028 ");
		//debug
		return false; //new password does not contain user ID
	}
    
    /**
     * Determine whether the new password contains the current password.<p>
     * 
     * Substrings of the new password are extracted and hashed. The new password contains the current password if the hash derived from the substring 
     * of the new password is equal to the hash of the current password.
     * @param userName user ID
     * @param password new password in plain text
     * @param userToken current hashed password
     * @return false if the new password does not contain the current password, true if the new password contains the current password 
     */
	private boolean passwordContainsCurrent(final String userName, final String password, final String userToken) {
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordContainsCurrent() 0029 ");
		//debug
		int passwordLength = password.length();
		int lengthToExtract;
		int startPosition;
		int endPosition;
		String substr = "";
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();

		/*
		 * Starting with a substring of one character from the new password, iterate through  
		 */
		for(lengthToExtract=1, startPosition=0, endPosition=startPosition+lengthToExtract; 
				lengthToExtract<passwordLength;
				endPosition++) {
			substr = password.substring(startPosition, endPosition);
			startPosition++;
			
			if (startPosition+lengthToExtract > passwordLength) {
				/*
				 * The end of the new password was reached.
				 * Start over beginning at index zero of the new password.
				 * On the next iteration the extracted substring is one character longer.
				 * Increment length to extract so that we eventually exit the loop.
				 */
				startPosition = 0;
				endPosition = startPosition+lengthToExtract;
				lengthToExtract++;
			}
			
			/* substr is handed off to authenticate to see if it matches the current password*/
			if (passwordAuthentication.authenticate(substr.toCharArray(), userToken)) {
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0010;
				return true;
			}
			
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordContainsCurrent() 002A "
					+ "\n   "
					+ "   substr="
					+ substr
					);
		}
		
		return false; //does not contain current password
	}

	/**
	 * Initialize the bean by obtaining a reference to password policy.
	 */
	@PostConstruct
	private void postConstructCallback() {
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.postConstructCallback() 002B ");
		//debug
    	getPwdPolicy();
    	feedback  = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
	}

	/**
	 * Stateful session bean remove method.<p>
	 * Clients call this method so that com.yardi.ejb.PwdCompositionRulesBean can release resources it has before being removed.
	 */
	@Remove
	public void removeBean() {
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.removeBean() 002C ");
		//debug
	}

	/**
	 * Obtain a reference to password policy from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy().<p>
	 * 
	 * <strong>The following feedback is provided:</strong><br>
	 * <span style="font-family:consolas;">YRD000B Password policy is missing</span>
	 */
	private void setPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.setPwdPolicy() 002D ");
		//debug
		pwdPolicy = passwordPolicyBean.getPwdPolicy();
		
		if (pwdPolicy == null) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.setPwdPolicy() pwdPolicy==null 002E ");
			return;
		}
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.setPwdPolicy() 002F "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			);
		//debug
	}
}
