package com.yardi.ejb;

import javax.ejb.Remote;
import javax.persistence.EntityManager;

@Remote
public interface PasswordPolicySessionBeanRemote {
	PpPwdPolicy findPwdPolicy(Long rrn);
}
