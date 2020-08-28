package com.yardi.ejb;

import javax.ejb.Remote;

import com.yardi.QSECOFR.EditPwdPolicyRequest;
import com.yardi.ejb.model.Pwd_Policy;

@Remote
public interface EditPasswordPolicy {
	EditPwdPolicyRequest getPwd_Policy();
	Pwd_Policy newPwdPolicy(EditPwdPolicyRequest editPwdPolicyRequest);
	void persist(EditPwdPolicyRequest editPwdPolicyRequest);
	void updateAll(EditPwdPolicyRequest editPwdPolicyRequest);
}
