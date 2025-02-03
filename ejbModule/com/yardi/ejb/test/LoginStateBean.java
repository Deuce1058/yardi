package com.yardi.ejb.test;

import java.text.SimpleDateFormat;
import java.util.Vector;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

import com.yardi.ejb.UserGroups;
import com.yardi.ejb.UserProfile;
import com.yardi.ejb.PasswordPolicy;
import com.yardi.ejb.UniqueTokens;
import com.yardi.ejb.Unique_Tokens;
import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.shared.test.LoginStateRequest;
import com.yardi.shared.userServices.LoginInitialPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class LoginStateBean implements LoginState {
	private LoginStateRequest loginStateRequest;
	private Pwd_Policy pwdPolicy = null;
	private String feedback;
	@EJB UserProfile userProfileBean; 
	@EJB UniqueTokens uniqueTokensBean;
	@EJB PasswordPolicy passwordPolicyBean;
	@EJB UserGroups userGroupsBean;
	@Resource UserTransaction tx;
	
    public LoginStateBean() {
		//debug
    	System.out.println("com.yardi.ejb.test.LoginStateBean LoginStateBean() ");
		//debug
    }

	private boolean findUserID() {
		//debug
    	System.out.println("com.yardi.ejb.test.LoginStateBean findUserID() 0000 ");
		//debug
  		userGroupsBean.find(loginStateRequest.getUserName());
		
		if (userGroupsBean.getLoginUserProfile()==null) {
			//debug
	    	System.out.println("com.yardi.ejb.test.LoginStateBean findUserID() 0001 ");
			//debug
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD000D.split("=");
			loginStateRequest.setMsgid(s[0]);
			loginStateRequest.setMsgd(s[1]);
	    	return false;
		}
		
		userProfileBean.setUserProfile(userGroupsBean.getLoginUserProfile());
		return true;
	}
	
	public String getFeedback() {
		return feedback;
	}

	public LoginStateRequest getLoginStateRequest() {
		return loginStateRequest;
	}

	private Pwd_Policy getPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean getPwpolicy() 0002 ");
		//debug
		
		if (pwdPolicy == null) {
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean getPwdPolicy 0003 "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy);
		//debug
		return pwdPolicy;
	}

	private boolean isValidUserName() {
		return userProfileBean.doesUserExist(loginStateRequest.getUserName());
	}
	
	public void mapEntities() {
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0004 ");
		//debug
		try {
			tx.begin();
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("=");
			loginStateRequest.setMsgid(s[0]);
			loginStateRequest.setMsgd(s[1]);
		
			if (isValidUserName()) {
				findUserID();
				mapPwdPolicy();
				mapUserProfile();
				mapUniqueTokens();
				mapSessionsTable();
				mapUserGroups();
			} else {
				s = com.yardi.shared.rentSurvey.YardiConstants.YRD000D.split("=");
				loginStateRequest.setMsgid(s[0]);
				loginStateRequest.setMsgd(s[1]);
			}

			tx.commit();
		} catch (NotSupportedException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0012 ");
			//debug
			e.printStackTrace();
		} catch (SystemException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0013 ");
			//debug
			e.printStackTrace();
		} catch (SecurityException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0014 ");
			//debug
			e.printStackTrace();
		} catch (IllegalStateException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0015 ");
			//debug
			e.printStackTrace();
		} catch (RollbackException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0016 ");
			//debug
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0017 ");
			//debug
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0018 ");
			//debug
			e.printStackTrace();
		}
	}
	
	private void mapPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean mapPwdPolicy() 0005 ");
		//debug
		loginStateRequest.setPpDays             (Short.toString(pwdPolicy.getPpDays()));
		loginStateRequest.setPpTempPwdTtl       (Short.toString(pwdPolicy.getPpTempPwdTtl()));
		loginStateRequest.setPpNbrUnique        (Short.toString(pwdPolicy.getPpNbrUnique()));
		loginStateRequest.setPpMaxSignonAttempts(Short.toString(pwdPolicy.getPpMaxSignonAttempts()));
		loginStateRequest.setPpPwdMinLen        (Short.toString(pwdPolicy.getPpPwdMinLen()));
		loginStateRequest.setPpMaxPwdLen        ("null");
		loginStateRequest.setPpMaxRepeatChar    ("null");
		loginStateRequest.setPpNbrDigits        ("null");
		loginStateRequest.setPpNbrUpper         ("null");
		loginStateRequest.setPpNbrLower         ("null");
		loginStateRequest.setPpNbrSpecial       ("null");
		loginStateRequest.setPp_upper_rqd       ("FALSE");
		loginStateRequest.setPp_lower_rqd       ("FALSE");
		loginStateRequest.setPp_number_rqd      ("FALSE");
		loginStateRequest.setPp_special_rqd     ("FALSE");
		loginStateRequest.setPp_cant_contain_id ("FALSE");
		loginStateRequest.setPp_cant_contain_pwd("FALSE");
		
		if (!(pwdPolicy.getPpMaxPwdLen() == null)) {
			loginStateRequest.setPpMaxPwdLen(Short.toString(pwdPolicy.getPpMaxPwdLen()));
		}
		
		if (!(pwdPolicy.getPpMaxRepeatChar() == null)) {
			loginStateRequest.setPpMaxRepeatChar(Short.toString(pwdPolicy.getPpMaxRepeatChar()));
		}
		
		if (!(pwdPolicy.getPpNbrDigits() == null)) {
			loginStateRequest.setPpNbrDigits(Short.toString(pwdPolicy.getPpNbrDigits()));
		}
		
		if (!(pwdPolicy.getPpNbrUpper() == null)) {
			loginStateRequest.setPpNbrUpper(Short.toString(pwdPolicy.getPpNbrUpper()));
		}
	
		if (!(pwdPolicy.getPpNbrLower() == null)) {
			loginStateRequest.setPpNbrLower(Short.toString(pwdPolicy.getPpNbrLower()));
		}
		
		if (!(pwdPolicy.getPpNbrSpecial() == null)) {
			loginStateRequest.setPpNbrSpecial(Short.toString(pwdPolicy.getPpNbrSpecial()));
		}
		
		if (pwdPolicy.getPpUpperRqd()) {
			loginStateRequest.setPp_upper_rqd("TRUE");
		} 
		
		if (pwdPolicy.getPpLowerRqd()) {
			loginStateRequest.setPp_lower_rqd("TRUE");
		} 
		
		if (pwdPolicy.getPpNumberRqd()) {
			loginStateRequest.setPp_number_rqd("TRUE");
		} 
		
		if (pwdPolicy.getPpSpecialRqd()) {
			loginStateRequest.setPp_special_rqd("TRUE");
		} 
		
		if (pwdPolicy.getPp_cant_contain_id().equalsIgnoreCase("y")) {
			loginStateRequest.setPp_cant_contain_id("TRUE");
		} 
		
		if (pwdPolicy.getPp_cant_contain_pwd().equalsIgnoreCase("y")) {
			loginStateRequest.setPp_cant_contain_pwd("TRUE");
		} 
	}

	private void mapSessionsTable() {
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean mapSessionsTable() 0006 ");
		//debug
		loginStateRequest.setStSesssionId("");
		loginStateRequest.setStSessionToken("");
		loginStateRequest.setStLastRequest("");
		loginStateRequest.setStLastActiveDate("");
		/*debug*/
		if (userGroupsBean==null) {
			System.out.println("com.yardi.ejb.test.LoginStateBean mapSessionsTable() 0010 ");
		}
		if (userGroupsBean.getLoginSessionTable()==null) {
			System.out.println("com.yardi.ejb.test.LoginStateBean mapSessionsTable() 0011 ");
		}
		/*debug*/
		
		if (userGroupsBean.getLoginSessionTable() != null) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapSessionsTable() 000F ");
			//debug
			loginStateRequest.setStSesssionId    (userGroupsBean.getLoginSessionTable().getStSessionId());
			loginStateRequest.setStSessionToken  (userGroupsBean.getLoginSessionTable().getStSessionToken());
			loginStateRequest.setStLastRequest   (userGroupsBean.getLoginSessionTable().getStLastRequest());
			loginStateRequest.setStLastActiveDate(stringifyDate(userGroupsBean.getLoginSessionTable().getStLastActive()));
		}
	}

	private void mapUniqueTokens() {
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean mapUniqueTokens() 0007 ");
		//debug
		Vector<Unique_Tokens> uniqueTokens = uniqueTokensBean.findTokens(loginStateRequest.getUserName());
		
		if (uniqueTokens == null) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapUniqueTokens() 0008 ");
			//debug
			uniqueTokens = new Vector<Unique_Tokens>(); 
		}
		
        ObjectMapper mapper = new ObjectMapper();
		try {
			loginStateRequest.setUniqueTokensString(mapper.writeValueAsString(uniqueTokens));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	private void mapUserGroups() {
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean mapUserGroups() 0009 ");
		//debug
		Vector<LoginInitialPage> initialPageList = userGroupsBean.getInitialPageList();
        ObjectMapper mapper = new ObjectMapper();
		try {
			loginStateRequest.setUserGroups(mapper.writeValueAsString(initialPageList));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}			
	
	private void mapUserProfile() {
		//debug 
		System.out.println("com.yardi.ejb.test.LoginStateBean mapUserProfile() 000A ");
		//debug
		loginStateRequest.setUptoken        (userGroupsBean.getLoginUserProfile().getUptoken());
		loginStateRequest.setUpTempPwd      (userGroupsBean.getLoginUserProfile().getUpTempPwd());
		loginStateRequest.setUpPwdexpd      (stringifyDate(userGroupsBean.getLoginUserProfile().getUpPwdexpd()));
		loginStateRequest.setUpPwdAttempts  (Short.toString(userGroupsBean.getLoginUserProfile().getUpPwdAttempts()));
		loginStateRequest.setUpDisabledDate ("");
		loginStateRequest.setUpLastLoginDate("");
		loginStateRequest.setUpActiveYn     (userGroupsBean.getLoginUserProfile().getUpActiveYn());
		
		if (!(userGroupsBean.getLoginUserProfile().getUpDisabledDate() == null)) {
			loginStateRequest.setUpDisabledDate(stringifyDate(userGroupsBean.getLoginUserProfile().getUpDisabledDate()));
		}
		
		if (!(userGroupsBean.getLoginUserProfile().getUpLastLoginDate() == null)) {
			loginStateRequest.setUpLastLoginDate(stringifyDate(userGroupsBean.getLoginUserProfile().getUpLastLoginDate()));
		}
	}
	
	@PostConstruct
    private void postConstructCallback() {
		//debug
    	System.out.println("com.yardi.ejb.test.LoginStateBean postConstructCallback() ");
		//debug
    	getPwdPolicy();
    }

	@Remove
	public void removeBean() {
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean removeBean() 000B ");
		//debug
		userProfileBean.removeBean();
		userGroupsBean.removeBean();
	}

	public void setLoginStateRequest(LoginStateRequest loginStsteRequest) {
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean setLoginStateRequest() 000E ");
		//debug
		this.loginStateRequest = loginStsteRequest;
	}

	private void setPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean setPwdPolicy() 000C ");
		//debug
		pwdPolicy = passwordPolicyBean.getPwdPolicy();
		
		if (pwdPolicy == null) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
		}
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean setPwdPolicy() 000D "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy
			+ "\n"
			+ "   feedback="
			+ feedback);
		//debug
	}
	
	private String stringifyDate(java.sql.Timestamp ts) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy MMdd HH:mm:ss");
		return f.format(ts);
	}
	
	private String stringifyDate(java.util.Date date) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy MMdd");
		return f.format(date);
	}
}
