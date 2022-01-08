package com.yardi.ejb.test;

import jakarta.ejb.Local;

import com.yardi.shared.test.LoginStateRequest;

@Local
public interface LoginState {
	String getFeedback();
	LoginStateRequest getLoginStateRequest();
	void mapEntities();
	void removeBean();
	void setLoginStateRequest(LoginStateRequest loginStsteRequest);
}
