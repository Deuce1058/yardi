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
 * Implementation of password policy rules for password composition
 */
@Stateful
public class PwdCompositionRulesBean implements PwdCompositionRules {
	private Pwd_Policy pwdPolicy;
	@EJB PasswordPolicy passwordPolicyBean;
	private String feedback = "";

    public PwdCompositionRulesBean() {
    }

	public boolean enforce(final String password, final String userName, final String userToken, final Vector<Unique_Tokens> userTokens) {
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0009 "
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
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() pwdPolicy==null 000A ");
			//debug
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
			return false;
		}
		
		upperRqd   = pwdPolicy.getPpUpperRqd();
		lowerRqd   = pwdPolicy.getPpLowerRqd();
		numberRqd  = pwdPolicy.getPpNumberRqd();
		specialRqd = pwdPolicy.getPpSpecialRqd();
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000B "
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
		
		pwdStatistics = new PasswordStatistics(password.toCharArray());
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000C "
				+ "\n   "
				+ pwdStatistics.toString1());
		//debug


		if (lowerRqd && pwdStatistics.getPwdNbrLower() > 0) {
			hasLower = true;
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000D "
					+ "\n   "
					+ "  hasLower="
					+ hasLower 
					);   
			//debug
		} else {

			if (lowerRqd) {
				//debug
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0018 ");
				//debug
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0007;
				return false;
			}
		}

		if (upperRqd && pwdStatistics.getPwdNbrUpper() > 0) {
			hasUpper = true;
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000E "
					+ "\n "
					+ "  hasUpper="
					+ hasUpper 
					);   
			//debug
		} else {

			if (upperRqd) {
				//debug
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enfo0rce() 0019 ");
				//debug
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0006;
				return false;
			}
		}

		if (numberRqd && pwdStatistics.getPwdNbrNbr() > 0) {
			hasNumber = true;
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000F "
					+ "\n "
					+ "  hasNumber="
					+ hasNumber 
					);   
			//debug
		} else {

			if (numberRqd) {
				//debug
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 001A ");
				//debug
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0008;
				return false;
			}
		}

		if (specialRqd && pwdStatistics.getPwdNbrSpecial() > 0) {
			hasSpecial = true;
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0010 "
					+ "\n "
					+ "  hasSpecial="
					+ hasSpecial 
					);   
			//debug
		} else {

			if (specialRqd) {
				//debug
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 001B ");
				//debug
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0009;
				return false;
			}
		}

		if (pwdStatistics.getPwdLength() < pwdPolicy.getPpPwdMinLen()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 001C ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0005.split("%n");
			feedback = s[0] + pwdPolicy.getPpPwdMinLen() + s[1];
			return false;
		}

		if (!(pwdPolicy.getPpMaxPwdLen()==null) && pwdStatistics.getPwdLength() > pwdPolicy.getPpMaxPwdLen()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 001D ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0015.split("%n");
			feedback = s[0] + pwdPolicy.getPpMaxPwdLen() + s[1];
			return false;
		}

		if (!(pwdPolicy.getPpMaxRepeatChar()==null) && pwdStatistics.getPwdNbrRepeatedChar() > pwdPolicy.getPpMaxRepeatChar()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 001E ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0016.split("%n");
			feedback = s[0] + pwdPolicy.getPpMaxRepeatChar() + s[1];
			return false;
		}			

		if (!(pwdPolicy.getPpNbrDigits()==null) && pwdStatistics.getPwdNbrNbr() < pwdPolicy.getPpNbrDigits()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 001F ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0017.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrDigits() + s[1];
			return false;
		}

		if (!(pwdPolicy.getPpNbrUpper()==null) && pwdStatistics.getPwdNbrUpper() < pwdPolicy.getPpNbrUpper()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0020 ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0018.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrUpper() + s[1];
			return false;
		}

		if (!(pwdPolicy.getPpNbrLower()==null) && pwdStatistics.getPwdNbrLower() < pwdPolicy.getPpNbrLower()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0021 ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0019.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrLower() + s[1];
			return false;
		}

		if (!(pwdPolicy.getPpNbrSpecial()==null) && pwdStatistics.getPwdNbrSpecial() < pwdPolicy.getPpNbrSpecial()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0022 ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD001A.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrSpecial() + s[1];
			return false;
		}

		if (pwdPolicy.isPpCantContainId() && passwordConatinsUserName(userName, password)) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0023 ");
			//debug
			return false;
		}

		if (pwdPolicy.isPpCantContainPwd() && passwordContainsCurrent(userName, password, userToken)) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0024 ");
			//debug
			return false;
		}			

		short maxUniqueTokens = pwdPolicy.getPpNbrUnique();
		
		if (maxUniqueTokens > 0 && !(userTokens==null)) { //If unique tokens being enforced and they have stored tokens
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0011 ");
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
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0012 "
				+ "\n"
				+ "   nbrOfStoredTokens="
				+ nbrOfStoredTokens 
				);
			//debug
			for(int i=0; i<nbrOfStoredTokens; i++) {
				uniqueToken = userTokens.get(i);
				//debug
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0013 "
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
					System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0014 "
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
				System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0015 "
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
	
    private Pwd_Policy getPwdPolicy() {
    	//debug
    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.getPwdPolicy() 0001 ");
    	//debug
		
		if (pwdPolicy==null) {
	    	//debug
	    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.getPwdPolicy() 0002 ");
	    	//debug
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.getPwdPolicy() 0003 "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			);

		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.getPwdPolicy() 0004 ");
		//debug
		return pwdPolicy;
	}

    private boolean passwordConatinsUserName(final String userName, final String password) {
    	//debug
    	System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 0025 ");
    	//debug
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
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0011;
				return true;
			}
			
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordConatinsUserName() 0016 "
					+ "\n   "
					+ "   substr="
					+ substr
					);
		}
		
		return false;
	}
    
	private boolean passwordContainsCurrent(final String userName, final String password, final String userToken) {
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordContainsCurrent() 0026 ");
		//debug
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
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0010;
				return true;
			}
			
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordContainsCurrent() 0017 "
					+ "\n   "
					+ "   substr="
					+ substr
					);
		}
		
		return false;
	}

	@PostConstruct
	private void postConstructCallback() {
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.postConstructCallback() 0000 ");
		//debug
    	getPwdPolicy();
    	feedback  = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
	}

	@Remove
	public void removeBean() {
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.removeBean() 0008 ");
		//debug
	}

	private void setPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.setPwdPolicy() 0005 ");
		//debug
		pwdPolicy = passwordPolicyBean.getPwdPolicy();
		
		if (pwdPolicy == null) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.setPwdPolicy() pwdPolicy==null 0006 ");
			return;
		}
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.setPwdPolicy() 0007 "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			);
		//debug
	}
}
