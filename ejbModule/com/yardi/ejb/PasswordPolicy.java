package com.yardi.ejb;

import java.util.Vector;

import javax.ejb.Remote;

import com.yardi.ejb.model.Pwd_Policy;

@Remote
public interface PasswordPolicy {
	Pwd_Policy find(Long rrn);
	String getFeedback();
	Pwd_Policy getPwdPolicy();
	String stringify();
}
