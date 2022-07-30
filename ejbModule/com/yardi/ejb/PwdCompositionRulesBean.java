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
		
		upperRqd   = pwdPolicy.getPpUpperRqd();
		lowerRqd   = pwdPolicy.getPpLowerRqd();
		numberRqd  = pwdPolicy.getPpNumberRqd();
		specialRqd = pwdPolicy.getPpSpecialRqd();
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
		
		pwdStatistics = new PasswordStatistics(password.toCharArray());
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0003 "
				+ "\n   "
				+ pwdStatistics.toString1());
		//debug


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

		if (pwdStatistics.getPwdLength() < pwdPolicy.getPpPwdMinLen()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000C ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0005.split("%n");
			feedback = s[0] + pwdPolicy.getPpPwdMinLen() + s[1];
			return false;
		}

		if (!(pwdPolicy.getPpMaxPwdLen()==null) && pwdStatistics.getPwdLength() > pwdPolicy.getPpMaxPwdLen()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000D ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0015.split("%n");
			feedback = s[0] + pwdPolicy.getPpMaxPwdLen() + s[1];
			return false;
		}

		if (!(pwdPolicy.getPpMaxRepeatChar()==null) && pwdStatistics.getPwdNbrRepeatedChar() > pwdPolicy.getPpMaxRepeatChar()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000E ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0016.split("%n");
			feedback = s[0] + pwdPolicy.getPpMaxRepeatChar() + s[1];
			return false;
		}			

		if (!(pwdPolicy.getPpNbrDigits()==null) && pwdStatistics.getPwdNbrNbr() < pwdPolicy.getPpNbrDigits()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 000F ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0017.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrDigits() + s[1];
			return false;
		}

		if (!(pwdPolicy.getPpNbrUpper()==null) && pwdStatistics.getPwdNbrUpper() < pwdPolicy.getPpNbrUpper()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0010 ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0018.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrUpper() + s[1];
			return false;
		}

		if (!(pwdPolicy.getPpNbrLower()==null) && pwdStatistics.getPwdNbrLower() < pwdPolicy.getPpNbrLower()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0011 ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0019.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrLower() + s[1];
			return false;
		}

		if (!(pwdPolicy.getPpNbrSpecial()==null) && pwdStatistics.getPwdNbrSpecial() < pwdPolicy.getPpNbrSpecial()) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0012 ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD001A.split("%n");
			feedback = s[0] + pwdPolicy.getPpNbrSpecial() + s[1];
			return false;
		}

		if (pwdPolicy.isPpCantContainId() && passwordConatinsUserName(userName, password)) {
			//debug
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.enforce() 0013 ");
			//debug
			return false;
		}

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
			Unique_Tokens uniqueToken; //single element from userTokens which is an ArrayList of UniqueToken.class 
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

	public String getFeedback() {
		return feedback;
	}
	
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
				return false;
			}
		}
		
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
			return false;
		}		
		
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
		return false;
	}
    
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
			
			System.out.println("com.yardi.ejb.PwdCompositionRulesBean.passwordContainsCurrent() 002A "
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
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.postConstructCallback() 002B ");
		//debug
    	getPwdPolicy();
    	feedback  = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
	}

	@Remove
	public void removeBean() {
		//debug
		System.out.println("com.yardi.ejb.PwdCompositionRulesBean.removeBean() 002C ");
		//debug
	}

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
