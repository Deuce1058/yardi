package com.yardi.ejb;

import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.yardi.userServices.PasswordAuthentication;
import com.yardi.userServices.PasswordStatistics;

/**
 * Session Bean implementation class PasswordPolicyBean
 */
@Singleton
@Startup
public class PasswordPolicyBean implements PasswordPolicy {
	/*
	 * In the case of a RESOURCE_LOCAL, EntityManager.getTransaction().begin() and EntityManager.getTransaction().comit() 
	 * are allowed. Here, the container is managing begin and commit and an attempt to begin will throw Cannot use an 
	 * EntityTransaction while using JTA 
	 */
	@PersistenceContext(unitName="yardi")
	private EntityManager emgr;
	private String feedback = com.yardi.rentSurvey.YardiConstants.YRD0000;
	private Pwd_Policy pwdPolicy;
	
    public PasswordPolicyBean() {
    	System.out.println("com.yadri.ejb PasswordPolicyBean PasswordPolicyBean()");
    }
    
    @PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb PasswordPolicyBean postConstructCallback()");
    	getPwdPolicy();
    }

	public Pwd_Policy find(Long rrn) {
		Pwd_Policy pwdPolicy = null;
		TypedQuery<Pwd_Policy> qry = emgr.createQuery(
			  "SELECT p from Pwd_Policy p "
			+ "WHERE p.ppRrn = :rrn ",
			Pwd_Policy.class);
		pwdPolicy = qry
			.setParameter("rrn", rrn)
			.getSingleResult();
		if (!(pwdPolicy==null)) {
			pwdPolicy.setPpUpperRqd();
			pwdPolicy.setPpLowerRqd();
			pwdPolicy.setPpNumberRqd();
			pwdPolicy.setPpSpecialRqd();
		}
		//debug
		System.out.println("com.yardi.ejb PasswordPolicyBean find() 0000 "
				+ "\n "
				+ "  rrn=" + rrn
				+ "\n "
				+ "  pwdPolicy=" + pwdPolicy
				);
		//debug
		return pwdPolicy;
	}

	/**
     * <p>Retrieve password policy from the Pwd_Policy table and make the policy available to other methods</p>
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
	public boolean enforce(final String password, final Vector<Unique_Tokens> userTokens) {
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
		PasswordStatistics pwdStatistics; 
		feedback = com.yardi.rentSurvey.YardiConstants.YRD0000;
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		//getPwdPolicy();
		
		if (pwdPolicy==null) {
			//debug 
			System.out.println("com.yardi.ejb PasswordPolicyBean enforce() pwdPolicy==null 0001");
			//debug
			feedback = com.yardi.rentSurvey.YardiConstants.YRD000B;
			return false;
		}
		
		upperRqd   = pwdPolicy.getPpUpperRqd();
		lowerRqd   = pwdPolicy.getPpLowerRqd();
		numberRqd  = pwdPolicy.getPpNumberRqd();
		specialRqd = pwdPolicy.getPpSpecialRqd();
		//debug
		System.out.println("com.yardi.ejb PasswordPolicyBean enforce() 0002"
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
		
		synchronized(this) {
			pwdStatistics = new PasswordStatistics(password.toCharArray());
			//debug
			System.out.println(pwdStatistics.toString1());
			//debug


			if (lowerRqd && pwdStatistics.getPwdNbrLower() > 0) {
				hasLower = true;
				//debug
				System.out.println("com.yardi.ejb PasswordPolicyBean enforce() 0003"
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

			if (upperRqd && pwdStatistics.getPwdNbrUpper() > 0) {
				hasUpper = true;
				//debug
				System.out.println("com.yardi.ejb com.yardi.ejb PasswordPolicyBean enforce() 0004"
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

			if (numberRqd && pwdStatistics.getPwdNbrNbr() > 0) {
				hasNumber = true;
				//debug
				System.out.println("com.yardi.ejb PasswordPolicyBean enforce() 0005"
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

			if (specialRqd && pwdStatistics.getPwdNbrSpecial() > 0) {
				hasSpecial = true;
				//debug
				System.out.println("com.yardi.ejb PasswordPolicyBean enforce() 0006"
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

			if (pwdStatistics.getPwdLength() < pwdPolicy.getPpPwdMinLen()) {
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0005;
				return false;
			}
		}
		
		short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
		
		if (maxUniqueTokens > 0 && !(userTokens==null)) { //If unique tokens being enforced and they have stored tokens
			//debug
			System.out.println("com.yardi.ejb PasswordPolicyBean enforce() 0007");
			for (Unique_Tokens u : userTokens) {
				System.out.println(
					  "\n"
					+ "   userTokens="
					+ u
					);
			}
			//debug
			int nbrOfStoredTokens = userTokens.size();
			Unique_Tokens uniqueToken; //single element from userTokens which is an ArrayList of UniqueToken.class 
			//debug
			System.out.println("com.yardi.ejb PasswordPolicyBean enforce() 0008"
				+ "\n"
				+ "   nbrOfStoredTokens="
				+ nbrOfStoredTokens 
				);
			//debug
			for(int i=0; i<nbrOfStoredTokens; i++) {
				uniqueToken = userTokens.get(i);
				//debug
				System.out.println("com.yardi.ejb PasswordPolicyBean enforce() 0009"
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
					System.out.println("com.yardi.ejb PasswordPolicyBean enforce() 000A"
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
				System.out.println("com.yardi.ejb PasswordPolicyBean enforce() 000B"
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

	public Pwd_Policy getPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb PasswordPolicyBean getPwdPolicy() 000C");
		//debug
		
		if (pwdPolicy == null) {
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb PasswordPolicyBean getPwdPolicy() 000F"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy);
		//debug
		return pwdPolicy;
	}

	private void setPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb PasswordPolicyBean setPwdPolicy() 0010");
		//debug
		feedback = com.yardi.rentSurvey.YardiConstants.YRD0000;
		pwdPolicy = find(1L);
		
		if (pwdPolicy == null) {
			System.out.println("com.yardi.ejb PasswordPolicyBean setPwdPolicy() 000E");
			feedback = com.yardi.rentSurvey.YardiConstants.YRD000B;
			return;
		}
		//debug
		System.out.println("com.yardi.ejb PasswordPolicyBean setPwdPolicy() 000D"
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy
			+ "\n"
			+ "   feedback="
			+ feedback);
		//debug
	}
	
	private boolean passwordContainsCurrent(final String userName, final String password, final String userToken) {
		int passwordLength = password.length();
		int lengthToExtract;
		int startPosition;
		int endPosition;
		String substr = "";
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();

		for(lengthToExtract=1, startPosition=0, endPosition=startPosition+lengthToExtract; 
				lengthToExtract<passwordLength;
				endPosition++) {
			substr = password.substring(startPosition, endPosition);
			startPosition++;
			
			if (startPosition+lengthToExtract > passwordLength) {
				startPosition = 0;
				endPosition = startPosition+lengthToExtract;
				lengthToExtract++;
			}
			
			/* substr is handed off to authenticate to see if it matches the current or previous password*/
			if (passwordAuthentication.authenticate(substr.toCharArray(), userToken)) {
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0010;
				return true;
			}
			
			System.out.println("com.yardi.ejb PasswordPolicyBean passwordContainsCurrent() 0011"
					+ "\n "
					+ "   substr="
					+ substr
					);
		}
		
		return false;
	}
	
	private boolean passwordConatinsUserName(final String userName, final String password) {
		int passwordLength = password.length();
		int lengthToExtract;
		int startPosition;
		int endPosition;
		String substr = "";
		
		for(lengthToExtract=1, startPosition=0, endPosition=startPosition+lengthToExtract; 
				lengthToExtract<passwordLength;
				endPosition++) {
			substr = password.substring(startPosition, endPosition);
			startPosition++;
			
			if (startPosition+lengthToExtract > passwordLength) {
				startPosition = 0;
				endPosition = startPosition+lengthToExtract;
				lengthToExtract++;
			}
			
			if (substr.equalsIgnoreCase(password)) {
				feedback = com.yardi.rentSurvey.YardiConstants.YRD0011;
				return true;
			}
			
			System.out.println("com.yardi.ejb PasswordPolicyBean passwordContainsCurrent() 0013"
					+ "\n "
					+ "   substr="
					+ substr
					);
		}
		
		return false;
	}
	
	public String stringify() {
		return "PasswordPolicyBean [emgr=" + emgr + "]"
				+ "\n  "
				+ this;
	}
}
