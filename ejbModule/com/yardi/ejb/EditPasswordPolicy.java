package com.yardi.ejb;

import jakarta.ejb.Local;

import com.yardi.ejb.model.Pwd_Policy;
import com.yardi.shared.QSECOFR.EditPwdPolicyRequest;

/**
 * Template for methods for editing password policy
 */
@Local
public interface EditPasswordPolicy  {
    /**
     * Return a {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest com.yardi.shared.QSECOFR.EditPwdPolicyRequest} container.<p>
     * 
     * Clients use this method to obtain a container which holds all of the password policy elements that can be modified by the user. 
     * 
     * @return a container which holds all of the password policy elements that can be modified by the user.
     */
	EditPwdPolicyRequest getPwd_Policy();
    /**
	 * Construct a new {@link com.yardi.ejb.model.Pwd_Policy com.yardi.ejb.model.Pwd_Policy} entity by mapping 
	 * {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest com.yardi.shared.QSECOFR.EditPwdPolicyRequest} container to <code>Pwd_Policy</code> entity.
	 * 
	 * @param editPwdPolicyRequest a container that holds all the password policy elements which can be modified by the user. 	  
	 * @return password policy entity
	 */	
	Pwd_Policy newPwdPolicy(EditPwdPolicyRequest editPwdPolicyRequest);
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
