package com.yardi.ejb;

import java.util.Vector;

import javax.ejb.Remote;

import com.yardi.userServices.InitialPage;
import com.yardi.userServices.LoginRequest;
import com.yardi.userServices.LoginResponse;

/**
 * Specifies methods implemented by UserServices.
 * 
 * @author Jim
 *
 */
@Remote
public interface UserServicesRemote {
	boolean authenticate();
	boolean chgPwd();
	String getFeedback();
	//boolean passwordPolicy(String password, Vector<UniqueTokens> userToken);
	void setLoginRequest(LoginRequest loginRequest);
	LoginRequest getLoginRequest();
	Vector<InitialPage> getInitialPageList();
	String getInitialPage();
	void loginSuccess();
	LoginResponse getLoginResponse();
}
