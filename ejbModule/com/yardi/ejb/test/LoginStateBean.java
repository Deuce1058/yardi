package com.yardi.ejb.test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
@TransactionManagement(TransactionManagementType.BEAN)
public class LoginStateBean implements LoginState {
	private LoginStateRequest loginStateRequest;
	private Pwd_Policy pwdPolicy = null;
	private String feedback;
	@EJB LoginUserProfile userProfileBean; 
	@EJB UniqueTokens uniqueTokensBean;
	@EJB PasswordPolicy passwordPolicyBean;
	@EJB LoginUserGroups userGroupsBean;
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

	public void mapEntities() {
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0004 ");
		//debug
		try {
			tx.begin();
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

			tx.commit();
		} catch (NotSupportedException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0000 ");
			//debug
			e.printStackTrace();
		} catch (SystemException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0001 ");
			//debug
			e.printStackTrace();
		} catch (SecurityException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0002 ");
			//debug
			e.printStackTrace();
		} catch (IllegalStateException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0003 ");
			//debug
			e.printStackTrace();
		} catch (RollbackException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0005 ");
			//debug
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0006 ");
			//debug
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			//debug
			System.out.println("com.yardi.ejb.test.LoginStateBean mapEntities() 0007 ");
			//debug
			e.printStackTrace();
		}
	}
	
	private void mapPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb.test.LoginStateBean mapPwdPolicy() 0005 ");
		//debug
		loginStateRequest.setPpDays(Short.toString(pwdPolicy.getPpDays()));
		loginStateRequest.setPpNbrUnique(Short.toString(pwdPolicy.getPpNbrUnique()));
		loginStateRequest.setPpMaxSignonAttempts(Short.toString(pwdPolicy.getPpMaxSignonAttempts()));
		loginStateRequest.setPpPwdMinLen(Short.toString(pwdPolicy.getPpPwdMinLen()));
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
		loginStateRequest.setUpPwdAttempts(Short.toString(userGroupsBean.getLoginUserProfile().getUpPwdAttempts()));
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
		String mm         = Integer.toString(ldt.getMonthValue());
		String d          = Integer.toString(ldt.getDayOfMonth());
		String h          = Integer.toString(ldt.getHour());
		String min        = Integer.toString(ldt.getMinute());
		String sec        = Integer.toString(ldt.getSecond());
		
		if (ldt.getMonthValue() < 10 ) {
			mm = "0" + Integer.toString(ldt.getMonthValue());
		}
		
		if (ldt.getDayOfMonth() < 10) {
			d = "0" + Integer.toString(ldt.getDayOfMonth());
		} 
		
		r[0] =   Integer.toString(ldt.getYear()) 
			   + " "
			   + mm
			   + d;
		r[1] = h + ":" + min + ":" + sec;
		return r;
	}
	
	private String stringifyDate(java.util.Date date) {
		LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
		String m = Integer.toString(ldt.getMonthValue());
		String d = Integer.toString(ldt.getDayOfMonth());
		
		if (ldt.getMonthValue() < 10 ) {
			m = "0" + Integer.toString(ldt.getMonthValue());
		}
		
		if (ldt.getDayOfMonth() < 10) {
			d = "0" + Integer.toString(ldt.getDayOfMonth());
		} 
		
		return new String(Integer.toString(ldt.getYear()) + " " + m + d);
	}
}
