package com.yardi.ejb;

import java.util.Vector;

import javax.ejb.Remote;

@Remote
public interface PasswordPolicySessionBeanRemote {
	PpPwdPolicy find(Long rrn);
	boolean enforce(final String password, final Vector<UniqueTokens> userTokens);
	String getFeedback();
	PpPwdPolicy getPwdPolicy();
	String stringify();
}
