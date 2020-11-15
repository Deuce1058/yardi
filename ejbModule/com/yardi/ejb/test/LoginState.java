package com.yardi.ejb.test;

import javax.ejb.Remote;

import com.yardi.test.LoginStateRequest;

@Remote
public interface LoginState {
	String getFeedback();
	LoginStateRequest getLoginStateRequest();
	void mapEntities();
	void removeBean();
	void setLoginStateRequest(LoginStateRequest loginStsteRequest);
}
