package com.yardi.ejb;

import com.yardi.shared.QSECOFR.EditPwdPolicyRequest;

import jakarta.ejb.Local;

/**
 * Template for methods for editing password policy
 */
@Local
public interface EditPasswordPolicy {
    /**
	 * A factory that produces an {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest EditPwdPolicyRequest} using {@link com.yardi.shared.model.PasswordPolicyCopy PasswordPolicyCopy} 
	 * as the source
	 * @return {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest EditPwdPolicyRequest} container which holds all of the password policy elements that can be edited
	 */
	EditPwdPolicyRequest fromPolicyCopy();
    /**
     * A factory that produces a {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest EditPwdPolicyRequest} using {@link com.yardi.ejb.model.Pwd_Policy Pwd_Policy} as the source
     * @return {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest EditPwdPolicyRequest} container which holds all of the password policy elements that can be modified by the user.
     */
	EditPwdPolicyRequest fromPwd_Policy();
    /**
	 * Persist a new {@link com.yardi.ejb.model.Pwd_Policy com.yardi.ejb.model.Pwd_Policy} entity.
	 * 
	 * @param editPwdPolicyRequest a container which holds all the password policy elements that can be modified
	 * by the user. 
	 */ 
	void persist(EditPwdPolicyRequest editPwdPolicyRequest);
	
	/**
     * Update all of the password policy elements that can be modified by the user.
	 * 
	 * @param editPwdPolicyRequest an <code>EditPwdPolicyRequest</code> container which holds all the password policy elements that can be modified
	 * by the user. 
     */
	void updateAll(EditPwdPolicyRequest editPwdPolicyRequest);
}
