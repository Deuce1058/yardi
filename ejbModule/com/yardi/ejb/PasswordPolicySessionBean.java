package com.yardi.ejb;

import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.yardi.userServices.PasswordAuthentication;

/**
 * Session Bean implementation class PasswordPolicySessionBean
 */
@Singleton
@Startup
public class PasswordPolicySessionBean implements PasswordPolicySessionBeanRemote {
	/*
	 * In the case of a RESOURCE_LOCAL, EntityManager.getTransaction().begin() and EntityManager.getTransaction().comit() 
	 * are allowed. Here, the container is managing begin and commit and an attempt to begin will throw Cannot use an 
	 * EntityTransaction while using JTA 
	 */
	@PersistenceContext(unitName="yardi")
	private EntityManager emgr;
	private String feedback = com.yardi.rentSurvey.YardiConstants.YRD0000;
	private PpPwdPolicy pwdPolicy;

    public PasswordPolicySessionBean() {
    	System.out.println("com.yadri.ejb PasswordPolicySessionBean PasswordPolicySessionBean()");
    }
    
    @PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb PasswordPolicySessionBean postConstructCallback()");
    	getPwdPolicy();
    }

	public PpPwdPolicy find(Long rrn) {
		PpPwdPolicy pwdPolicy = null;
		//pwdPolicy = emgr.find(PpPwdPolicy.class, rrn);
		TypedQuery<PpPwdPolicy> qry = emgr.createQuery(
			  "SELECT p from PpPwdPolicy p "
			+ "WHERE p.ppRrn = :rrn ",
			PpPwdPolicy.class);
		pwdPolicy = qry
			.setParameter("rrn", rrn)
			.getSingleResult();
		System.out.println("com.yardi.ejb PasswordPolicySessionBean find() 0000 "
				+ "\n "
				+ "  rrn=" + rrn
				+ "\n "
				+ "  pwdPolicy=" + pwdPolicy
				);
		//debug
		return pwdPolicy;
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
	public boolean enforce(final String password, final Vector<UniqueTokens> userTokens) {
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
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		//getPwdPolicy();
		
		if (pwdPolicy==null) {
			//debug 
			System.out.println("com.yardi.ejb PasswordPolicySessionBean enforce() pwdPolicy==null 0001");
			//debug
			feedback = com.yardi.rentSurvey.YardiConstants.YRD000B;
			return false;
		}
		
		upperRqd   = Boolean.valueOf(pwdPolicy.getPpUpperRqd());
		lowerRqd   = Boolean.valueOf(pwdPolicy.getPpLowerRqd());
		numberRqd  = Boolean.valueOf(pwdPolicy.getPpNumberRqd());
		specialRqd = Boolean.valueOf(pwdPolicy.getPpSpecialRqd());
		//debug
		System.out.println("com.yardi.ejb PasswordPolicySessionBean enforce() 0002"
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
			System.out.println("com.yardi.ejb PasswordPolicySessionBean enforce() 0003"
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
			System.out.println("com.yardi.ejb com.yardi.ejb PasswordPolicySessionBean enforce() 0004"
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
			System.out.println("com.yardi.ejb PasswordPolicySessionBean enforce() 0005"
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
			System.out.println("com.yardi.ejb PasswordPolicySessionBean enforce() 0006"
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
			System.out.println("com.yardi.ejb PasswordPolicySessionBean enforce() 0007");
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
			System.out.println("com.yardi.ejb PasswordPolicySessionBean enforce() 0008"
				+ "\n"
				+ "   nbrOfStoredTokens="
				+ nbrOfStoredTokens 
				);
			//debug
			for(int i=0; i<nbrOfStoredTokens; i++) {
				uniqueToken = userTokens.get(i);
				//debug
				System.out.println("com.yardi.ejb PasswordPolicySessionBean enforce() 0009"
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
					feedback = com.yardi.rentSurvey.YardiConstants.YRD000A;
					//debug
					System.out.println("com.yardi.ejb PasswordPolicySessionBean enforce() 000A"
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
				System.out.println("com.yardi.ejb PasswordPolicySessionBean enforce() 000B"
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

	public PpPwdPolicy getPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb PasswordPolicySessionBean getPwdPolicy() 000C");
		//debug
		
		if (pwdPolicy == null) {
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb PasswordPolicySessionBean getPwdPolicy() 000F"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy);
		//debug
		return pwdPolicy;
	}

	private void setPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb PasswordPolicySessionBean setPwdPolicy() 0010");
		//debug
		feedback = com.yardi.rentSurvey.YardiConstants.YRD0000;
		pwdPolicy = find(1L);
		
		if (pwdPolicy == null) {
			System.out.println("com.yardi.ejb PasswordPolicySessionBean setPwdPolicy() 000E");
			feedback = com.yardi.rentSurvey.YardiConstants.YRD000B;
			return;
		}
		//debug
		System.out.println("com.yardi.ejb PasswordPolicySessionBean setPwdPolicy() 000D"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy
			+ "\n"
			+ "   feedback="
			+ feedback);
		//debug
	}

	public String stringify() {
		return "PasswordPolicySessionBean [emgr=" + emgr + "]"
				+ "\n  "
				+ this;
	}
}
