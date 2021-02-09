package com.yardi.ejb;

import javax.ejb.Remote;

import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.shared.QSECOFR.EditPwdPolicyRequest;

@Remote
public interface EditPasswordPolicy {
	EditPwdPolicyRequest getPwd_Policy();
	Pwd_Policy newPwdPolicy(EditPwdPolicyRequest editPwdPolicyRequest);
	void persist(EditPwdPolicyRequest editPwdPolicyRequest);
	void updateAll(EditPwdPolicyRequest editPwdPolicyRequest);
}
