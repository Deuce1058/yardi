package com.yardi.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class PasswordPolicySessionBean
 */
@Stateless
public class PasswordPolicySessionBean implements PasswordPolicySessionBeanRemote {
	@PersistenceContext(name="yardiEJB")
	private EntityManager emgr;

    /**
     * Default constructor. 
     */
    public PasswordPolicySessionBean() {
    }

	@Override
	public PpPwdPolicy pwdPolicyRowExists(Long rrn) {
		PpPwdPolicy pwdPolicy = null;
		pwdPolicy = emgr.find(PpPwdPolicy.class, rrn);
		return pwdPolicy;
	}

}
