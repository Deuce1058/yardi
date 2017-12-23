package com.yardi.ejb;

import javax.ejb.Remote;

@Remote
public interface PasswordPolicySessionBeanRemote {
	PpPwdPolicy find(Long rrn);
	String stringify();
}
