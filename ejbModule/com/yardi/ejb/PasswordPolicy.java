package com.yardi.ejb;

import java.util.Vector;

import javax.ejb.Remote;

import com.yardi.ejb.model.Pwd_Policy;

@Remote
public interface PasswordPolicy {
	Pwd_Policy find(Long rrn);
	boolean enforce(final String password, final String userName, final String userToken, final Vector<Unique_Tokens> userTokens);
	String getFeedback();
	Pwd_Policy getPwdPolicy();
	String stringify();
}
