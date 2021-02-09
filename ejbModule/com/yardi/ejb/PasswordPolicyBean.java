package com.yardi.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.yardi.ejb.model.Pwd_Policy;

/**
 * Session Bean implementation class PasswordPolicyBean
 */
@Singleton
@Startup
public class PasswordPolicyBean implements PasswordPolicy {
	/*
	 * In the case of a RESOURCE_LOCAL, EntityManager.getTransaction().begin() and EntityManager.getTransaction().comit() 
	 * are allowed. Here, the container is managing begin and commit and an attempt to begin will throw Cannot use an 
	 * EntityTransaction while using JTA 
	 */
	@PersistenceContext(unitName="yardi")
	private EntityManager emgr;
	private String feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
	private Pwd_Policy pwdPolicy;
	
    public PasswordPolicyBean() {
    	System.out.println("com.yadri.ejb.PasswordPolicyBean PasswordPolicyBean()");
    }
    
    /**
     * <p>Retrieve password policy from the Pwd_Policy table and make the policy available to other methods</p>
     * <p>Policy consists of:<br>
     *   1 Complexity</p>
     * <p>  1a Upper case required</p>
     * <p>  1b Lower case required,</p>
     * <p>  1c Special characters required</p>
     * <p>  1d Number required</p>
     * <p>2 Password length</p>
     * <p>3 Number of days before password must be changed</p>
     * <p>4 Number of unique passwords required specifies how many unique tokens are stored. Applied when changing the password. 
     *   This policy causes the new password token to be checked against the saved tokens for a match. This prevents user from 
     *   reusing passwords,</p>
     * <p>5 Maximum login attempts before password is disabled</p>
     * <br>
     * <p>Specific rules applied in passwordPolicy are complexity and length,</p>
     * <br>
	 * <p>Instance variable feedback indicates the status of the authenticate process</p> 
	 * 
	 * @param password
	 * @return Boolean
	 */
	public Pwd_Policy find(Long rrn) {
		Pwd_Policy pwdPolicy = null;
		TypedQuery<Pwd_Policy> qry = emgr.createQuery(
			  "SELECT p from Pwd_Policy p "
			+ "WHERE p.ppRrn = :rrn ",
			Pwd_Policy.class);
		try {
			pwdPolicy = qry
				.setParameter("rrn", rrn)
				.getSingleResult();
		} catch (NoResultException e) {
			System.out.println("com.yardi.ejb.PasswordPolicyBean find() exception 0016 ");
			e.printStackTrace();
		}
		if (!(pwdPolicy==null)) {
			pwdPolicy.setPp_upper_rqd(pwdPolicy.getPp_upper_rqd());
			pwdPolicy.setPp_lower_rqd(pwdPolicy.getPp_lower_rqd());
			pwdPolicy.setPp_number_rqd(pwdPolicy.getPp_number_rqd());
			pwdPolicy.setPp_special_rqd(pwdPolicy.getPp_special_rqd());
			pwdPolicy.setPp_cant_contain_id(pwdPolicy.getPp_cant_contain_id());
			pwdPolicy.setPp_cant_contain_pwd(pwdPolicy.getPp_cant_contain_pwd());
		}
		//debug
		System.out.println("com.yardi.ejb.PasswordPolicyBean find() 0000 "
				+ "\n "
				+ "  rrn=" + rrn
				+ "\n "
				+ "  pwdPolicy=" + pwdPolicy
				);
		//debug
		return pwdPolicy;
	}

	public String getFeedback() {
		return feedback;
	}
	
    public Pwd_Policy getPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb.PasswordPolicyBean getPwdPolicy() 000C ");
		//debug
		
		if (pwdPolicy == null) {
			//debug
			System.out.println("com.yardi.ejb.PasswordPolicyBean getPwdPolicy() 0014 ");
			//debug
			setPwdPolicy();
		}
		
		//debug
		System.out.println("com.yardi.ejb.PasswordPolicyBean getPwdPolicy() 000F "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy);
		//debug
		return pwdPolicy;
	}
	
	@PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.PasswordPolicyBean postConstructCallback() ");
    	getPwdPolicy();
    }
	
	private void setPwdPolicy() {
		//debug
		System.out.println("com.yardi.ejb.PasswordPolicyBean setPwdPolicy() 0010 ");
		//debug
		feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
		pwdPolicy = find(1L);
		
		if (pwdPolicy == null) {
			System.out.println("com.yardi.ejb.PasswordPolicyBean setPwdPolicy() 000E ");
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000B;
			return;
		}
		//debug
		System.out.println("com.yardi.ejb.PasswordPolicyBean setPwdPolicy() 000D "
			+ "\n"
			+ "   pwdPolicy="
			+ pwdPolicy.toString()
			+ "\n"
			+ "   feedback="
			+ feedback);
		//debug
	}
	
	public String stringify() {
		return "PasswordPolicyBean [emgr=" + emgr + "]"
				+ "\n  "
				+ this;
	}
}
