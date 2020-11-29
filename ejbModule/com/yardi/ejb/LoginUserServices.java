package com.yardi.ejb;

import javax.ejb.Remote;
import com.yardi.userServices.LoginRequest;
import com.yardi.userServices.LoginResponse;

/**
 * Specifies methods implemented by UserServicesBean.
 * 
 * @author Jim
 *
 */
@Remote
public interface LoginUserServices {
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