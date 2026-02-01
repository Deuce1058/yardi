package com.yardi.ejb;

import java.util.Vector;

import com.yardi.ejb.crypto.Jargon2Bean;
import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.shared.userServices.PasswordStatistics;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;

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
	 * Injected reference to com.yardi.ejb.crypto.Jargon2Bean.
	 */
	@EJB Jargon2Bean jargon2Bean;
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
     * @param password current password in plain text
     * @param newPassword new password in plain text
     * @param userName user ID
     * @param userTokens all of the stored tokens for the user
     * @return boolean indicating whether new password conforms to password policy
     */
	public boolean enforce(String password, final String newPassword, final String userName, final Vector<Unique_Tokens> userTokens) {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0000 "
				+ "\n "
				+ "   password="
				+ password
				+ "\n "
				+ "   newPassword="
				+ newPassword
				+ "\n "
				+ "   userName="
				+ userName
				+ "\n "
				+ "   userTokens="
				+ userTokens.toString()
				);
		PasswordStatistics pwdStatistics; 
		feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
		
		if (pwdPolicy==null) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() pwdPolicy==null 0001 ");
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
			return false;
		}
		
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0002 "
				+ "\n "
				+ "  pwdPolicy.getPpUpperRqd()="
				+ pwdPolicy.getPpUpperRqd() 
				+ "\n "
				+ "  pwdPolicy.getPpLowerRqd()="
				+ pwdPolicy.getPpLowerRqd()
				+ "\n "
				+ "  pwdPolicy.getPpNumberRqd()="
				+ pwdPolicy.getPpNumberRqd()
				+ "\n "
				+ "  pwdPolicy.getPpSpecialRqd()="
				+ pwdPolicy.getPpSpecialRqd()
				+ "\n "
				+ "  password policy="
				+ pwdPolicy.toString()
				+ "\n "
				+ "  newPassword="
				+ newPassword
				+ "\n "
				+ "  newPassword len="
				+ newPassword.length() 
				);   
		
		/*
		 * Scan the new password and compile statistics needed to enforce certain rules like upper case character required.
		 */
		pwdStatistics = new PasswordStatistics(newPassword.toCharArray());
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0003 "
				+ "\n   "
				+ pwdStatistics.toString1());
		
		//at least one lower case character required and number of lower > 0
		if (isLowerRequired(pwdStatistics.getPwdNbrLower())) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0042 ");
	    	return false;
		}

		//at least one upper case character required and number of upper > 0
		if (isUpperRequired(pwdStatistics.getPwdNbrUpper())) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0048 ");
	    	return false;
		}
	    
	    //at least one digit is required and number of digits > 0
	    if (isNumberRequired(pwdStatistics.getPwdNbrNbr())) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0045 ");
	    	return false;
	    } 
	    
	    //at least one special character is required and number of special characters > 0
	    if (isSpecialRequired(pwdStatistics.getPwdNbrSpecial())) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0047 ");
	    	return false;
	    }
	    
	    //new password length is compared to minimum password length
	    if (!isMinLength(pwdStatistics.getPwdLength())) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0043 ");
	    	return false;
	    }

	    //max password length is in force and new password length is > max password length
	    if (isMoreThanMaxLength(pwdStatistics.getPwdLength())) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0044 ");
	    	return false;
	    }
	    
	    //max repeated characters is in force and number of repeated characters is > max repeated characters
	    if (hasTooManyRepeatedChars(pwdStatistics.getPwdNbrRepeatedChar())) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0041 ");
	    	return false;
	    }
	    
        //at least one number is required and number of digits < minimum number of digits
	    if (hasTooFewDigits(pwdStatistics.getPwdNbrRepeatedChar())) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 003D ");
	    	return false;
	    }

	    //at least one upper case character is required and number of upper < minimum number of upper
	    if (hasTooFewUpper(pwdStatistics.getPwdNbrUpper())) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0040 ");
	    	return false;
	    }
	    
        //at least one lower case character is required and number of lower < minimum number of lower
	    if (hasTooFewLower(pwdStatistics.getPwdNbrLower())) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 003E ");
	    	return false;
	    }
	    
	    //at least one special character is required and number of special < minimum number of special
	    if (hasTooFewSpecial(pwdStatistics.getPwdNbrSpecial())) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 003F ");
	    	return false;
	    }
	    
	    //If unique tokens being enforced and they have stored tokens
	    if (isPasswordReused(newPassword, userTokens)) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0046 ");
	    	return false;	    	
	    }

		//password cant contain user ID is in force and new password contains user ID
		if (pwdPolicy.isPpCantContainId() && passwordConatinsUserName(userName, newPassword)) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0013 ");
			return false;
		}

		//new password cant contain the current password is in force and new password contains current password
		if (pwdPolicy.isPpCantContainPwd() && passwordContainsCurrent(password, newPassword)) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0014 ");
			return false;
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
    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.getPwdPolicy() 001A ");
		
		if (pwdPolicy==null) {
	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.getPwdPolicy() 001B ");
			setPwdPolicy();
		}
		
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.getPwdPolicy() 001C "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			);

		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.getPwdPolicy() 001D ");
		return pwdPolicy;
	}

	/**
	 * Determine whether the new password has too few digits.<p>
	 * 
	 * If the minimum number of digits rule is enforced the new password must contain at least the number of digits required by password policy.
	 * @param pwdNbrRepeatedChar number of digits
	 * @return true if minimum number of digits rule is enforced and the number of digits in the new password is fewer than the minimum number of digits 
	 */
	private boolean hasTooFewDigits(int pwdNbrRepeatedChar) {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.hasTooFewDigits() 000F ");
		
		//at least one number is required and number of digits < minimum number of digits
		if (pwdPolicy.getPpNbrDigits() !=null && pwdNbrRepeatedChar < pwdPolicy.getPpNbrDigits()) {
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0017.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrDigits() + s[1];
			return true;
		}
		
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.hasTooFewDigits() 0032 ");
		return false;
	}

	/** 
	 * Determine whether the new password has too few lower case characters.<p>
	 * 
	 * If the minimum number of lower case characters rule is enforced the new password must contain at least the number of lower case characters required by password policy.  
	 * @param pwdNbrLower number of lower case
	 * @return true if the minimum number of lower case rule is enforced and the new password contains fewer than the minimum number of lower case characters 
	 */
	private boolean hasTooFewLower(int pwdNbrLower) {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.hasTooFewLower() 0011 ");
		
		//at least one lower case character is required and number of lower < minimum number of lower 
		if (pwdPolicy.getPpNbrLower() !=null && pwdNbrLower < pwdPolicy.getPpNbrLower()) {
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0019.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrLower() + s[1];
			return true;
		}
		
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.hasTooFewLower() 0035 ");
		return false;
	}

	/**
	 * Determine whether the new password contains too few special characters.<p>
	 * 
	 * If the minimum number of special characters rule is enforced the new password must contain at least the number of special characters required by password policy.
	 * @param pwdNbrSpecial number of special characters 
	 * @return true if the minimum number of special rule is enforced and the new password contains fewer than the minimum number of special characters 
	 */
	private boolean hasTooFewSpecial(int pwdNbrSpecial) {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.hasTooFewSpecial() 0012 ");
		
		//at least one special character is required and number of special < minimum number of special
		if (pwdPolicy.getPpNbrSpecial() !=null && pwdNbrSpecial < pwdPolicy.getPpNbrSpecial()) {
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD001A.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrSpecial() + s[1];
			return true;
		}
		
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.hasTooFewSpecial() 0036 ");
		return false;
	}

	/**
	 * Determine whether new password contains too few upper case characters.<p>
	 * 
	 * If the minimum number of upper case characters rule is enforced the new password must contain at least the number of upper case characters required by password policy.
	 * @param pwdNbrUpper number of upper case characters 
	 * @return true if the minimum number of upper case characters rule is enforced and number of upper case characters in the new password is fewer than the minimum 
	 * number of upper case characters
	 */
	private boolean hasTooFewUpper(int pwdNbrUpper) {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.hasTooFewUpper() 0010 ");
		
		//at least one upper case character is required and number of upper < minimum number of upper 
		if (pwdPolicy.getPpNbrUpper() !=null && pwdNbrUpper < pwdPolicy.getPpNbrUpper()) {
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0018.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrUpper() + s[1];
			return true;
		}
		
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.hasTooFewUpper() 0034 ");
		return false;
	}
	
	/**
	 * Determine whether the new password contains too many repeated characters.<p>
	 * 
	 * If max number of repeated characters rule is enforced the number of repeated characters in the new password must be less than the number required by password policy.  
	 * @param pwdNbrRepeatedChar number of repeated characters
	 * @return true if max number of repeats is enforced and the number of repeated characters in the new password exceeds the maximum 
	 */
	private boolean hasTooManyRepeatedChars(int pwdNbrRepeatedChar) {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.hasTooManyRepeatedChars() 000E ");
		
		//max repeated characters is in force and number of repeated characters is > max repeated characters
		if (pwdPolicy.getPpMaxRepeatChar() !=null && pwdNbrRepeatedChar > pwdPolicy.getPpMaxRepeatChar()) {
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0016.split("%n");
			feedback = s[0] + pwdPolicy.getPpMaxRepeatChar() + s[1];
			return true;
		}			
		
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.hasTooManyRepeatedChars() 0031 ");
		return false;
	}

	/**
	 * Determine whether new password contains too few lower case characters.<p>
	 * 
	 * If the lower case required rule is enforced the new password must contain at least the number of lower case characters required by password policy. 
	 * @param nbrLower the number of lower case characters 
	 * @return true if lower case required is enforced and new password does not contain at least 1 lower case
	 */
	private boolean isLowerRequired(int nbrLower) {
        System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isLowerRequired() 0004 "
        		+ "\n"
        		+ "    nbrLower="
        		+ nbrLower
        		);

        //not enforced
        if (!pwdPolicy.getPpLowerRqd()) {
	        System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isLowerRequired() 003A ");
	        return false;
        }
        
		//at least one lower case character required and number of lower <= 0
        if (nbrLower<=0) {
		    feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0007;

		    if (pwdPolicy.getPpNbrLower() !=null) {
		        String[] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0019.split("%n");
		        feedback = s[0] + pwdPolicy.getPpNbrLower() + s[1];
		    }
		    
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isLowerRequired() 0019 "
					+ "\n"
					+ "    feedback="
					+ feedback
					);
	    	return true;
        }
        
        
	    System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isLowerRequired() 002A ");
	    return false;
	}

	/**
	 * Determine whether the new password is at least the minimum length.<p>
	 * 
	 * If the minimum length rule is enforced then then the new password must contain at least the minimum number of characters required by password policy.  
	 * @param pwdLength length of the new password 
	 * @return true new password is at least the minimum length
	 */
	private boolean isMinLength(int pwdLength) {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isMinLength() 000C ");
		
		//new password length is compared to minimum password length
		if (pwdLength < pwdPolicy.getPpPwdMinLen()) {
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0005.split("%n");
			feedback = s[0] + pwdPolicy.getPpPwdMinLen() + s[1];
			return false;
		}
		
    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isMinLength() 0028 ");
		return true;
	}

	/**
	 * Determine whether the new password exceeds the maximum length.<p>
	 * 
	 * If the maximum password length rule is enforced the new password must contain no more than the maximum number of characters required by password policy. 
	 * @param pwdLength length of the new password 
	 * @return true if maximum password length rule is enforced and new password exceeds the max password length 
	 */
	private boolean isMoreThanMaxLength(int pwdLength) {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isMoreThanMaxLength() 000D ");
		
		//max password length is enforced and new password length is > max password length
		if (pwdPolicy.getPpMaxPwdLen() !=null && pwdLength > pwdPolicy.getPpMaxPwdLen()) {
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0015.split("%n");
			feedback = s[0] + pwdPolicy.getPpMaxPwdLen() + s[1];
			return true;
		}
		
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isMoreThanMaxLength() 0030 ");
		return false;
	}

	/**
	 * Determine whether the new password has at least one digit.<p>
	 * 
	 * If the number required rule is enforced the new password must contain at least one digit.
	 * @param pwdNbrNbr number of digits
	 * @return true if the number required rule is enforced and new password contains at least 1 digit
	 */
	private boolean isNumberRequired(int pwdNbrNbr) {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isNumberRequired() 0008 ");
				
		//not enforced
		if (!pwdPolicy.getPpNumberRqd()) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isNumberRequired() 003B ");
			return false;
		}
		
		//does not contain at least one digit
		if (pwdNbrNbr<=0) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0008;

			if (pwdPolicy.getPpNbrDigits() !=null) {
				    String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0017.split("%n");
				    feedback = s[0] + pwdPolicy.getPpNbrDigits() + s[1];
			}

	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isNumberRequired() 0024 ");
			return true;
		}
				
    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isNumberRequired() 0025 ");
		return false;
	}

	/**
	 * Determine whether the new password was used previously.<p>
	 * 
	 * If unique tokens is being enforced the new password must not match a previously used password.
	 * @param newPassword new password
	 * @param userTokens password history
	 * @return true if unique tokens is enforced and the new password matches a previously used password
	 */
	private boolean isPasswordReused(String newPassword, Vector<Unique_Tokens> userTokens) {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isPasswordReused() 0037 ");
		short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
		
		if (maxUniqueTokens > 0 && !(userTokens==null)) { //If unique tokens being enforced and they have stored tokens
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isPasswordReused() 0015 ");
			for (Unique_Tokens u : userTokens) {
				System.out.println(
					  "\n"
					+ "   userTokens="
					+ u
					);
			}
			
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isPasswordReused() 0016 "
				+ "\n"
				+ "   userTokens.size()="
				+ userTokens.size()
				);
			
			/* 
			 * userTokens has exactly the number of tokens that need to checked in it because com.yardi.ejb.UserServicesBean.chgPwd() has found all the tokens and then removed extra 
			 * stored tokens. If, for example, password policy is enforcing 10 unique tokens but the user has 15 tokens, then tokens 11 - 15 are removed by 
			 * com.yardi.ejb.UserServicesBean.chgPwd() before this method is called   
			 */
			for(Unique_Tokens uniqueToken : userTokens) {
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isPasswordReused() 0017 "
					+ "\n"
					+ "   uniqueToken="
					+ uniqueToken  
					);
						
				if (jargon2Bean.verify(newPassword, uniqueToken.getUp1Token())) {
					feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000A;
					System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isPasswordReused() 0018 "
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
					return true;
				}
			}
		}
		
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isPasswordReused() 0038 ");
		return false;
	}

	/**
	 * Determine whether the new password contains at least one special character.<p>
	 * 
	 * If the special characters required rule is enforced then the new password must contain at least one special character.
	 * @param pwdNbrSpecial number of special case characters 
	 * @return true if special characters required rule is enforced and new password contains at least 1 special character
	 */
	private boolean isSpecialRequired(int pwdNbrSpecial) {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isSpecialRequired() 000A ");

		//not enforced
		if (!pwdPolicy.getPpSpecialRqd()) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isSpecialRequired() 003C ");
			return false;
		}
		
		//at least one special character is required and number of special characters > 0
		if (pwdNbrSpecial<=0) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0009;
			
			if (pwdPolicy.getPpNbrSpecial() !=null) {
				String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD001A.split("%n");
				feedback = s[0] + pwdPolicy.getPpNbrSpecial() + s[1];
			}
			
	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isSpecialRequired() 0026 ");
			return true;
		}
		
    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isSpecialRequired() 0027 ");
		return false;
	}
	
	/**
	 * Determine whether the new password contains at least one upper case character.<p>
	 * 
	 * If the upper case required rule is enforced the new password must contain at least one upper case character.   
	 * @param pwdNbrUpper the number of upper case characters 
	 * @return true if upper case required is enforced and new password does not contain at least 1 upper case  
	 */
	private boolean isUpperRequired(int pwdNbrUpper) {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isUpperRequired() 0006 ");
		
		//not enforced
		if (!pwdPolicy.getPpUpperRqd()) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isUpperRequired() 0007 ");
			return false;
		}
		
		//must have at least 1 upper
		if (pwdNbrUpper<=0) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0006;
			
			if (pwdPolicy.getPpNbrUpper() !=null) {
			    String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0018.split("%n");
			    feedback = s[0] + pwdPolicy.getPpNbrUpper() + s[1];
			}
			
	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isUpperRequired() 0022 ");
			return true;
		}
		
    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.isUpperRequired() 0023 ");
		return false;
	}

	/**
     * Determine whether the new password contains the user name.<p>
     * 
     * If the new password contains user name rule is enforced the new password may not contain the user name. The caller is responsible for checking whether 
     * rule is enforced.
     * @param userName user ID
     * @param password new password in plain text
     * @return true if new password contains user name rule is enforced and the new password contains the user ID
     */
    private boolean passwordConatinsUserName(final String userName, final String newPassword) {
    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 001E "
    			+ "\n    "
    			+ "userName="
    			+ userName
    			+ "\n    "
    			+ "newPassword="
    			+ newPassword
    			);
    	
    	if (userName==null || newPassword==null) {
	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 0020 ");
    		return false; //new password does not contain user ID
    	} 
    	
    	if (newPassword.contains(userName)) {
	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 001F ");
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0011;
			return true;
    	}
    	
    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 0021 ");
		return false; //new password does not contain user ID		
	}
    
    /**
     * Determine whether the new password contains the current password.<p>
     * 
     * If the if new password contains current password rule is enforced the new password must not contain the current password. The caller is responsible for checking whether 
     * rule is enforced.
     * @param password current password
     * @param newPassword new password
     * @return true if new password contains current password rule is enforced and new password contains the current password
     */
	private boolean passwordContainsCurrent(final String password, final String newPassword) {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordContainsCurrent() 0029 "
				+ "\n    "
				+ "password="
				+ password
				+ "\n    "
				+ "newPassword="
				+ newPassword
				);

		if (password==null || newPassword==null) {
	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordContainsCurrent() 0033 ");
    		return false; //new password does not contain user ID
    	} 
		
		if (newPassword.contains(password)) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0010;
			return true;
		}
		
    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordContainsCurrent() 0039 ");
		return false; //does not contain current password
	}

	/**
	 * Initialize the bean by obtaining a reference to password policy.
	 */
	@PostConstruct
	private void postConstructCallback() {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.postConstructCallback() 002B ");
    	getPwdPolicy();
    	feedback  = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
	}

	/**
	 * Stateful session bean remove method.<p>
	 * Clients call this method so that com.yardi.ejb.PwdCompositionRulesBean can release resources it has before being removed.
	 */
	@Remove
	public void removeBean() {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.removeBean() 002C ");
	}

	/**
	 * Obtain a reference to password policy from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy().<p>
	 * 
	 * <strong>The following feedback is provided:</strong><br>
	 * <span style="font-family:consolas;">YRD000B Password policy is missing</span>
	 */
	private void setPwdPolicy() {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.setPwdPolicy() 002D ");
		pwdPolicy = passwordPolicyBean.getPwdPolicy();
		
		if (pwdPolicy == null) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.setPwdPolicy() pwdPolicy==null 002E ");
			return;
		}
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.setPwdPolicy() 002F "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			);
	}
}
