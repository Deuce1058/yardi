package com.yardi.ejb;

import java.util.Vector;

import javax.ejb.Remote;

@Remote
public interface PasswordPolicy {
	Pp_Pwd_Policy find(Long rrn);
	boolean enforce(final String password, final Vector<Unique_Tokens> userTokens);
	String getFeedback();
	Pp_Pwd_Policy getPwdPolicy();
	String stringify();
}
