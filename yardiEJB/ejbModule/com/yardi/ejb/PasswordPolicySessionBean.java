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
	/*
	 * In the case of a RESOURCE_LOCAL, EntityManager.getTransaction().begin() and EntityManager.getTransaction().comit() 
	 * are allowed. Here, the container is managing begin and commit and an attempt to begin will throw Cannot use an 
	 * EntityTransaction while using JTA 
	 */
	@PersistenceContext(name="yardiEJB")
	private EntityManager emgr;

    /**
     * Default constructor. 
     */
    public PasswordPolicySessionBean() {
    }

	@Override
	public PpPwdPolicy find(Long rrn) {
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
