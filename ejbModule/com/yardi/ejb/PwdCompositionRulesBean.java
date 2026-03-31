package com.yardi.ejb;

import java.util.Vector;

import com.yardi.ejb.crypto.Jargon2Bean;
import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.ejb.userServices.PasswordRule;
import com.yardi.ejb.userServices.PasswordValidationContext;
import com.yardi.ejb.userServices.PasswordValidationException;
import com.yardi.shared.model.PasswordPolicyCopy;
import com.yardi.shared.userServices.PasswordStatistics;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

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
@Stateless 
public class PwdCompositionRulesBean implements PwdCompositionRules {
	/**
	 * Reference to Pwd_Policy entity obtained from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy().
	 */
	private PasswordPolicyCopy pwdPolicy;
	/**
	 * Injected reference to com.yardi.ejb.PasswordPolicyBean.
	 */
	@EJB PasswordPolicy passwordPolicyBean;
	/**
	 * Injected reference to com.yardi.ejb.crypto.Jargon2Bean.
	 */
	@EJB Jargon2Bean jargon2Bean;

    public PwdCompositionRulesBean() {
    }

    /**
     * Enforce the password policy on the new password<p>
     * @param password current password in plain text
     * @param newPassword new password in plain text
     * @param userName user ID
     * @param userTokens all of the stored tokens for the user
     * @throws PasswordValidationException new password does not conform to password policy
     * @return boolean indicating whether new password conforms to password policy
     */
    @Override
	public void enforce(String password, final String newPassword, final String userName, final Vector<Unique_Tokens> userTokens) 
			throws PasswordValidationException {
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
		
		if (pwdPolicy==null) {
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() pwdPolicy==null 0001 ");
		}
		
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0002 "
				+ "\n "
				+ "  pwdPolicy.toString()="
				+ pwdPolicy.toString() 
				);   
		
		/*
		 * Scan the new password and compile statistics needed to enforce certain rules like upper case character required.
		 */
		pwdStatistics = new PasswordStatistics(newPassword.toCharArray());
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0003 "
				+ "\n   "
				+ pwdStatistics.toString());
		PasswordValidationContext ctx = new PasswordValidationContext(password, newPassword, userName, userTokens, pwdStatistics, pwdPolicy, jargon2Bean);
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0004 "
				+ "\n   "
				+ "PasswordValidationContext="
				+ ctx.toString());
		
	    for (PasswordRule rule : PasswordRule.values()) {
	    	rule.validate(ctx);
	    }
	}

	/**
	 * Returns the password policy obtained from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy().
	 * @return reference to Pwd_Policy entity
	 */
    private PasswordPolicyCopy getPwdPolicy() {
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
	 * Initialize the bean by obtaining a reference to password policy.
	 */
	@PostConstruct
	private void postConstructCallback() {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.postConstructCallback() 002B ");
    	getPwdPolicy();
	}

	/**
	 * Obtain a reference to password policy from com.yardi.ejb.PasswordPolicyBean.getPwdPolicy().<p>
	 * 
	 * <strong>The following feedback is provided:</strong><br>
	 * <span style="font-family:consolas;">YRD000B Password policy is missing</span>
	 */
	private void setPwdPolicy() {
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.setPwdPolicy() 002D ");
		pwdPolicy = passwordPolicyBean.getPasswordPolicyCopy();
		
		if (pwdPolicy == null) {
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
