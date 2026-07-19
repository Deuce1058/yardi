package com.yardi.ejb.test;

import java.text.SimpleDateFormat;
import java.util.Vector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yardi.ejb.PasswordPolicy;
import com.yardi.ejb.UniqueTokens;
import com.yardi.ejb.Unique_Tokens;
import com.yardi.ejb.UserGroups;
import com.yardi.ejb.UserGroupsResult;
import com.yardi.ejb.UserProfile;
import com.yardi.ejb.UserServices;
import com.yardi.shared.model.PasswordPolicyCopy;
import com.yardi.shared.test.LoginStateRequest;
import com.yardi.shared.userServices.LoginInitialPage;

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

@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class LoginStateBean implements LoginState {
	private LoginStateRequest loginStateRequest;
	private PasswordPolicyCopy pwdPolicy = null;
	private String feedback;
	private UserGroupsResult userGroupsResult; 
	@EJB UserProfile userProfileBean; 
	@EJB UniqueTokens uniqueTokensBean;
	@EJB PasswordPolicy passwordPolicyBean;
	@EJB UserGroups userGroupsBean;
	@Resource UserTransaction tx;
	@EJB UserServices userServicesBean;
	
    public LoginStateBean() {
    	System.out.println("com.yardi.ejb.test.LoginStateBean LoginStateBean() ");
    }

	private boolean findUserID() {
    	System.out.println("com.yardi.ejb.test.LoginStateBean findUserID() 0000 ");
  		userGroupsResult = userGroupsBean.find(loginStateRequest.getUserName());
		
		if (userGroupsResult.getLoginUserProfile()==null) {
	    	System.out.println("com.yardi.ejb.test.LoginStateBean findUserID() 0001 ");
			String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD000D.split("=");
			loginStateRequest.setMsgid(s[0]);
			loginStateRequest.setMsgd(s[1]);
	    	return false;
		}
		
		userProfileBean.setUserProfile(userGroupsResult.getLoginUserProfile());
		return true;
	}

	@Override
	public String getFeedback() {
		return feedback;
	}

	@Override
	public LoginStateRequest getLoginStateRequest() {
		return loginStateRequest;
	}

	private PasswordPolicyCopy getPwdPolicy() {
		System.out.println("com.yardi.ejb.test.LoginStateBean getPwpolicy() 0002 ");
		
		if (pwdPolicy == null) {
			setPwdPolicy();
		}
		
		System.out.println("com.yardi.ejb.test.LoginStateBean getPwdPolicy 0003 "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy);
		return pwdPolicy;
	}

	private boolean isValidUserName() {
		return userProfileBean.doesUserExist(loginStateRequest.getUserName());
	}
	
	@Override
	public void mapEntities() {
		System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0004 ");
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
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0012 ");
			e.printStackTrace();
		} catch (SystemException e) {
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0013 ");
			e.printStackTrace();
		} catch (SecurityException e) {
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0014 ");
			e.printStackTrace();
		} catch (IllegalStateException e) {
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0015 ");
			e.printStackTrace();
		} catch (RollbackException e) {
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0016 ");
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0017 ");
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0018 ");
			e.printStackTrace();
		}
	}
	
	private void mapPwdPolicy() {
		System.out.println("com.yardi.ejb.test.LoginStateBean mapPwdPolicy() 0005 ");
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
		System.out.println("com.yardi.ejb.test.LoginStateBean mapSessionsTable() 0006 ");
		loginStateRequest.setStSesssionId("");
		loginStateRequest.setStSessionToken("");
		loginStateRequest.setStLastRequest("");
		loginStateRequest.setStLastActiveDate("");
		if (userGroupsBean==null) {
			System.out.println("com.yardi.ejb.test.LoginStateBean mapSessionsTable() 0010 ");
		}
		if (userGroupsResult.getLoginSessionTable()==null) {
			System.out.println("com.yardi.ejb.test.LoginStateBean mapSessionsTable() 0011 ");
		}
		
		if (userGroupsResult.getLoginSessionTable() != null) {
			System.out.println("com.yardi.ejb.test.LoginStateBean mapSessionsTable() 000F ");
			loginStateRequest.setStSesssionId    (userGroupsResult.getLoginSessionTable().getStSessionId());
			loginStateRequest.setStSessionToken  (userGroupsResult.getLoginSessionTable().getStSessionToken());
			loginStateRequest.setStLastRequest   (userGroupsResult.getLoginSessionTable().getStLastRequest());
			loginStateRequest.setStLastActiveDate(stringifyDate(userGroupsResult.getLoginSessionTable().getStLastActive()));
		}
	}

	private void mapUniqueTokens() {
		System.out.println("com.yardi.ejb.test.LoginStateBean mapUniqueTokens() 0007 ");
		Vector<Unique_Tokens> uniqueTokens = uniqueTokensBean.findTokens(loginStateRequest.getUserName());
		
		if (uniqueTokens == null) {
			System.out.println("com.yardi.ejb.test.LoginStateBean mapUniqueTokens() 0008 ");
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
		System.out.println("com.yardi.ejb.test.LoginStateBean mapUserGroups() 0009 ");
		Vector<LoginInitialPage> initialPageList = userGroupsResult.getInitialPageList();
        ObjectMapper mapper = new ObjectMapper();
		try {
			loginStateRequest.setUserGroups(mapper.writeValueAsString(initialPageList));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}			
	
	private void mapUserProfile() {
		System.out.println("com.yardi.ejb.test.LoginStateBean mapUserProfile() 000A ");
		loginStateRequest.setUptoken        (userGroupsResult.getLoginUserProfile().getUptoken());
		loginStateRequest.setUpTempPwd      (userGroupsResult.getLoginUserProfile().getUpTempPwd());
		loginStateRequest.setUpPwdexpd      (stringifyDate(userGroupsResult.getLoginUserProfile().getUpPwdexpd()));
		loginStateRequest.setUpPwdAttempts  (Short.toString(userGroupsResult.getLoginUserProfile().getUpPwdAttempts()));
		loginStateRequest.setUpDisabledDate ("");
		loginStateRequest.setUpLastLoginDate("");
		loginStateRequest.setUpActiveYn     (userGroupsResult.getLoginUserProfile().getUpActiveYn());
		
		if (!(userGroupsResult.getLoginUserProfile().getUpDisabledDate() == null)) {
			loginStateRequest.setUpDisabledDate(stringifyDate(userGroupsResult.getLoginUserProfile().getUpDisabledDate()));
		}
		
		if (!(userGroupsResult.getLoginUserProfile().getUpLastLoginDate() == null)) {
			loginStateRequest.setUpLastLoginDate(stringifyDate(userGroupsResult.getLoginUserProfile().getUpLastLoginDate()));
		}
	}
	
	@PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.test.LoginStateBean postConstructCallback() ");
    	getPwdPolicy();
    	userServicesBean.toString();
    }

	@Remove
	@Override
	public void removeBean() {
		System.out.println("com.yardi.ejb.test.LoginStateBean removeBean() 000B ");
		userProfileBean.removeBean();
	}

	@Override
	public void setLoginStateRequest(LoginStateRequest loginStsteRequest) {
		System.out.println("com.yardi.ejb.test.LoginStateBean setLoginStateRequest() 000E ");
		this.loginStateRequest = loginStsteRequest;
	}

	private void setPwdPolicy() {
		System.out.println("com.yardi.ejb.test.LoginStateBean setPwdPolicy() 000C ");
		pwdPolicy = passwordPolicyBean.getPasswordPolicyCopy();
		
		if (pwdPolicy == null) {
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
		}
		System.out.println("com.yardi.ejb.test.LoginStateBean setPwdPolicy() 000D "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy
			+ "\n"
			+ "   feedback="
			+ feedback);
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
