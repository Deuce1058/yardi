package com.yardi.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
	@PersistenceContext(unitName="pwdPolicy")
	private EntityManager emgr;

    /**
     * Default constructor. 
     */
    public PasswordPolicySessionBean() {
    }

	@Override
	public PpPwdPolicy find(Long rrn) {
		PpPwdPolicy pwdPolicy = null;
		//pwdPolicy = emgr.find(PpPwdPolicy.class, rrn);
		TypedQuery<PpPwdPolicy> qry = emgr.createQuery(
			  "SELECT p from PpPwdPolicy p "
			+ "WHERE p.ppRrn = :rrn ",
			PpPwdPolicy.class);
		pwdPolicy = qry
			.setParameter("rrn", rrn)
			.getSingleResult();

		//works but EclipseLink implementation of JPA wont support named parameters for native query. This is because the JPA spec 
		//does not require named parameters for native query. Hibernate implementation of JPA supports named parameters
		/*
		pwdPolicy = (PpPwdPolicy) emgr.createNativeQuery("SELECT * FROM DB2ADMIN.PP_PWD_POLICY WHERE PP_RRN = ?", PpPwdPolicy.class)
				.setParameter(1, rrn).getSingleResult();
		*/
		//debug
		System.out.println("com.yardi.ejb PasswordPolicySessionBean find() 0000 "
				+ "\n "
				+ "  rrn=" + rrn
				+ "\n "
				+ "  pwdPolicy=" + pwdPolicy
				);
		//debug
		return pwdPolicy;
	}

	public String stringify() {
		return "PasswordPolicySessionBean [emgr=" + emgr + "]"
				+ "\n  "
				+ this;
	}
}
