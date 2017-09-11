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
	public PpPwdPolicy findPwdPolicy(Long rrn) {
		PpPwdPolicy pwdPolicy = null;
		pwdPolicy = emgr.find(PpPwdPolicy.class, rrn);

		//works but EclipseLink implementation of JPA wont support named parameters for native query. This is because the JPA spec 
		//does not require named parameters for native query. Hibernate implementation of JPA supports named parameters
		/*
		pwdPolicy = (PpPwdPolicy) emgr.createNativeQuery("SELECT * FROM DB2ADMIN.PP_PWD_POLICY WHERE PP_RRN = ?", PpPwdPolicy.class)
				.setParameter(1, rrn).getSingleResult();
		*/
		return pwdPolicy;
	}
}
