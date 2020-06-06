package com.yardi.ejb;

import java.util.Vector;

import javax.ejb.Remote;

import com.yardi.userServices.InitialPage;
import com.yardi.userServices.LoginRequest;
import com.yardi.userServices.LoginResponse;

/**
 * Specifies methods implemented by UserServicesBean.
 * 
 * @author Jim
 *
 */
@Remote
public interface UserServices {
	boolean authenticate();
	boolean chgPwd();
	String getFeedback();
	String getInitialPage();
	Vector<InitialPage> getInitialPageList();
	LoginRequest getLoginRequest();
	LoginResponse getLoginResponse();
	String getSessionID();
	void remove();
	void setLoginRequest(LoginRequest loginRequest);
	void setSessionID(String sessionID);
}