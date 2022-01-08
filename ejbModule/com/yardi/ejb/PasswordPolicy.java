package com.yardi.ejb;

import jakarta.ejb.Local;

import com.yardi.ejb.model.Pwd_Policy;

@Local
public interface PasswordPolicy {
	Pwd_Policy find(Long rrn);
	String getFeedback();
	Pwd_Policy getPwdPolicy();
	String stringify();
}
