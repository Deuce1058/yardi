package com.yardi.ejb;

import jakarta.ejb.Local;

import com.yardi.shared.userServices.LoginRequest;
import com.yardi.shared.userServices.LoginResponse;

/**
 * Specifies methods implemented by UserServicesBean.
 * 
 * @author Jim
 *
 */
@Local
public interface UserServices {
	boolean authenticate();
	boolean chgPwd();
	String getFeedback();
	String getInitialPage();
	//Vector<LoginInitialPage> getInitialPageList();
	LoginRequest getLoginRequest();
	LoginResponse getLoginResponse();
	String getSessionID();
	void remove();
	void setLoginRequest(LoginRequest loginRequest);
	void setSessionID(String sessionID);
}