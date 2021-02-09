package com.yardi.ejb.test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;

import com.yardi.ejb.LoginUserGroups;
import com.yardi.ejb.LoginUserProfile;
import com.yardi.ejb.PasswordPolicy;
import com.yardi.ejb.UniqueTokens;
import com.yardi.ejb.Unique_Tokens;
import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.shared.test.LoginStateRequest;
import com.yardi.shared.userServices.LoginInitialPage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Stateful
public class LoginStateBean implements LoginState {
	private LoginStateRequest loginStateRequest;
	private Pwd_Policy pwdPolicy = null;
	private String feedback;
	@EJB LoginUserProfile userProfileBean; 
	@EJB UniqueTokens uniqueTokensBean;
	@EJB PasswordPolicy passwordPolicyBean;
	@EJB LoginUserGroups userGroupsBean;
	
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

	public void mapEntities() {
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0004 ");
		//debug
		String [] s = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("=");
		loginStateRequest.setMsgid(s[0]);
		loginStateRequest.setMsgd(s[1]);
		
		if (findUserID()) {
			mapPwdPolicy();
			mapUserProfile();
			mapUniqueTokens();
			mapSessionsTable();
			mapUserGroups();
		}
	}

	private void mapPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean mapPwdPolicy() 0005 ");
		//debug
		loginStateRequest.setPpDays(new Short(pwdPolicy.getPpDays()).toString());
		loginStateRequest.setPpNbrUnique(new Short(pwdPolicy.getPpNbrUnique()).toString());
		loginStateRequest.setPpMaxSignonAttempts(new Short(pwdPolicy.getPpMaxSignonAttempts()).toString());
		loginStateRequest.setPpPwdMinLen(new Short(pwdPolicy.getPpPwdMinLen()).toString());
		loginStateRequest.setPpMaxPwdLen("null");
		loginStateRequest.setPpMaxRepeatChar("null");
		loginStateRequest.setPpNbrDigits("null");
		loginStateRequest.setPpNbrUpper("null");
		loginStateRequest.setPpNbrLower("null");
		loginStateRequest.setPpNbrSpecial("null");
		loginStateRequest.setPp_upper_rqd("FALSE");
		loginStateRequest.setPp_lower_rqd("FALSE");
		loginStateRequest.setPp_number_rqd("FALSE");
		loginStateRequest.setPp_special_rqd("FALSE");
		loginStateRequest.setPp_cant_contain_id("FALSE");
		loginStateRequest.setPp_cant_contain_pwd("FALSE");
		
		if (!(pwdPolicy.getPpMaxPwdLen() == null)) {
			loginStateRequest.setPpMaxPwdLen(new Short(pwdPolicy.getPpMaxPwdLen()).toString());
		}
		
		if (!(pwdPolicy.getPpMaxRepeatChar() == null)) {
			loginStateRequest.setPpMaxRepeatChar(new Short(pwdPolicy.getPpMaxRepeatChar()).toString());
		}
		
		if (!(pwdPolicy.getPpNbrDigits() == null)) {
			loginStateRequest.setPpNbrDigits(new Short(pwdPolicy.getPpNbrDigits()).toString());
		}
		
		if (!(pwdPolicy.getPpNbrUpper() == null)) {
			loginStateRequest.setPpNbrUpper(new Short(pwdPolicy.getPpNbrUpper()).toString());
		}
	
		if (!(pwdPolicy.getPpNbrLower() == null)) {
			loginStateRequest.setPpNbrLower(new Short(pwdPolicy.getPpNbrLower()).toString());
		}
		
		if (!(pwdPolicy.getPpNbrSpecial() == null)) {
			loginStateRequest.setPpNbrSpecial(new Short(pwdPolicy.getPpNbrSpecial()).toString());
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
		String [] ts = new String [2]; 
		loginStateRequest.setStSesssionId("");
		loginStateRequest.setStSessionToken("");
		loginStateRequest.setStLastRequest("");
		loginStateRequest.setStLastActiveDate("");
		loginStateRequest.setStLastActiveTime("");
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
			loginStateRequest.setStSesssionId(userGroupsBean.getLoginSessionTable().getStSessionId());
			loginStateRequest.setStSessionToken(userGroupsBean.getLoginSessionTable().getStSessionToken());
			loginStateRequest.setStLastRequest(userGroupsBean.getLoginSessionTable().getStLastRequest());
			ts = stringifyDate(userGroupsBean.getLoginSessionTable().getStLastActive());
			loginStateRequest.setStLastActiveDate(ts[0]);
			loginStateRequest.setStLastActiveTime(ts[1]);
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
		String [] ts = new String [2]; 
		loginStateRequest.setUptoken(userGroupsBean.getLoginUserProfile().getUptoken());
		loginStateRequest.setUpPwdexpd(stringifyDate(userGroupsBean.getLoginUserProfile().getUpPwdexpd()));
		loginStateRequest.setUpPwdAttempts(new Short(userGroupsBean.getLoginUserProfile().getUpPwdAttempts()).toString());
		loginStateRequest.setUpDisabledDate("");
		loginStateRequest.setUpDisabledTime("");
		
		if (  !(userGroupsBean.getLoginUserProfile().getUpDisabledDate() == null) ) {
			ts = stringifyDate(userGroupsBean.getLoginUserProfile().getUpDisabledDate());  
			loginStateRequest.setUpDisabledDate(ts[0]);
			loginStateRequest.setUpDisabledTime(ts[1]);
		}
		
		ts = stringifyDate(userGroupsBean.getLoginUserProfile().getUpLastLoginDate());  
		loginStateRequest.setUpLastLoginDate(ts[0]);
		loginStateRequest.setUpLastLoginTime(ts[1]);
		loginStateRequest.setUpActiveYn(userGroupsBean.getLoginUserProfile().getUpActiveYn());
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
	
	private String [] stringifyDate(java.sql.Timestamp ts) {
		String r []       = new String [2];
		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts.getTime()), ZoneId.systemDefault());
		String mm         = new Integer(ldt.getMonthValue()).toString();
		String d          = new Integer(ldt.getDayOfMonth()).toString();
		String h          = new Integer(ldt.getHour()).toString();
		String min        = new Integer(ldt.getMinute()).toString();
		String sec        = new Integer(ldt.getSecond()).toString();
		
		if (ldt.getMonthValue() < 10 ) {
			mm = "0" + new Integer(ldt.getMonthValue()).toString();
		}
		
		if (ldt.getDayOfMonth() < 10) {
			d = "0" + new Integer(ldt.getDayOfMonth()).toString();
		} 
		
		r[0] =   new Integer(ldt.getYear()).toString() 
			   + " "
			   + mm
			   + d;
		r[1] = h + ":" + min + ":" + sec;
		return r;
	}
	
	private String stringifyDate(java.util.Date date) {
		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
		String m = new Integer(ldt.getMonthValue()).toString();
		String d = new Integer(ldt.getDayOfMonth()).toString();
		
		if (ldt.getMonthValue() < 10 ) {
			m = "0" + new Integer(ldt.getMonthValue()).toString();
		}
		
		if (ldt.getDayOfMonth() < 10) {
			d = "0" + new Integer(ldt.getDayOfMonth()).toString();
		} 
		
		return new String(new Integer(ldt.getYear()).toString() + " " + m + d);
	}
}
