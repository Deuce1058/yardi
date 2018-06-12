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
	//boolean passwordPolicy(String password, Vector<Unique_Tokens> userToken);
	void setLoginRequest(LoginRequest loginRequest);
	LoginRequest getLoginRequest();
	Vector<InitialPage> getInitialPageList();
	String getInitialPage();
	void loginSuccess();
	LoginResponse getLoginResponse();
}
