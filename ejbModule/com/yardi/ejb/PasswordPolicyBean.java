package com.yardi.ejb;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import com.yardi.ejb.model.Pwd_Policy;

/**
 * Session Bean implementation of methods for working with password policy.<p>
 * 
 * This singleton runs at startup. Its mission is to give out references to Pwd_Policy entity to clients that need to know password policy.
 */
@Singleton
@Startup
public class PasswordPolicyBean implements PasswordPolicy {
	@PersistenceContext(unitName="yardi")
	private EntityManager emgr;
	/**
	 * Field <i>feedback</i> indicates the status of password policy
	 */
	private String feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
	/**
	 * Reference to the Pwd_Policy entity
	 */
	private Pwd_Policy pwdPolicy;
	
    public PasswordPolicyBean() {
    	System.out.println("com.yadri.ejb.PasswordPolicyBean PasswordPolicyBean()");
    }
    
    /**
     * Retrieve password policy from the PWD_POLICY database table.
     * 
     * <pre style="font-family:times;">
     * Policy consists of:
     *   1 Complexity
     *      1.1 Upper case required Y/N
     *          1.1.1 Number of upper case required
     *      1.2 Lower case required Y/N
     *          1.2.1 Number of lower case required
     *      1.3 Special characters required Y/N
     *          1.3.1 Number of special characters required
     *      1.4 Digits required Y/N
     *          1.4.1 Number of digits required
     *      1.5 Cant contain user name
     *      1.6 Cant contain current password
     *      1.7 Maximum number of repeated characters
     *   2 Password length
     *      2a minimum length
     *      2b maximum length
     *   3 Number of days before password must be changed
     *   4 Number of unique passwords required specifies how many unique tokens are stored per user. This rule is applied when changing 
     *      the password and causes the new hashed password to be checked against token history for a match. The user is prevented from 
     *      reusing passwords
     *   5 Maximum number of login attempts allowed since the most recent successful login. When the number of invalid logins reaches this number
     *      the User_Profile entity is disabled preventing any additional login attempts until the password is reset by an admin
     * </pre>
     * 
     * These rules are applied when logging in and when changing the password either on demand or because the password expired. 
	 * 
	 * @param rrn the relative record number to find in the PWD_POLICY database table
	 * @return a Pwd_Policy entity
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
			/*
			 *  Get the value of field pp_upper_rqd (column PP_UPPER_RQD in database table PWD_POLICY) in Pwd_Policy entity and
			 *  set field pp_upper_rqd (column PP_UPPER_RQD in database table PWD_POLICY) to this value. This will trigger the setter 
			 *  for boolean field ppUpperRqd. ppUpperRd is tested to determine whether upper case characters are required in the 
			 *  password. ppUpperRd is not stored in database table PWD_POLICY.
			 */
			pwdPolicy.setPp_upper_rqd(pwdPolicy.getPp_upper_rqd()); 
			/*
			 *  Get the value of field pp_lower_rqd (column PP_LOWER_RQD in database table PWD_POLICY) in Pwd_Policy entity and
			 *  set field pp_lower_rqd (column PP_LOWER_RQD in database table PWD_POLICY) to this value. This will trigger the setter 
			 *  for boolean field ppLowerRqd. ppLowerRqd is tested to determine whether lower case characters are required in the 
			 *  password. ppLowerRqd is not stored in database table PWD_POLICY.
			 */
			pwdPolicy.setPp_lower_rqd(pwdPolicy.getPp_lower_rqd());
			/*
			 *  Get the value of field pp_number_rqd (column PP_NUMBER_RQD in database table PWD_POLICY) in Pwd_Policy entity and
			 *  set field pp_number_rqd (column PP_NUMBER_RQD in database table PWD_POLICY) to this value. This will trigger the setter 
			 *  for boolean field ppNumberRqd. ppNumberRqd is tested to determine whether digits are required in the 
			 *  password. ppNumberRqd is not stored in database table PWD_POLICY.
			 */
			pwdPolicy.setPp_number_rqd(pwdPolicy.getPp_number_rqd());
			/*
			 *  Get the value of field pp_special_rqd (column PP_SPECIAL_RQD in database table PWD_POLICY) in Pwd_Policy entity and
			 *  set field pp_special_rqd (column PP_SPECIAL_RQD in database table PWD_POLICY) to this value. This will trigger the setter 
			 *  for boolean field ppSpecialRqd. ppSpecialRqd is tested to determine whether special characters are required in the 
			 *  password. ppSpecialRqd is not stored in database table PWD_POLICY.
			 */
			pwdPolicy.setPp_special_rqd(pwdPolicy.getPp_special_rqd());
			/*
			 *  Get the value of field pp_cant_contain_id (column PP_CANT_CONTAIN_ID in database table PWD_POLICY) in Pwd_Policy entity and
			 *  set field pp_cant_contain_id (column PP_CANT_CONTAIN_ID in database table PWD_POLICY) to this value. This will trigger the setter 
			 *  for boolean field ppCantContainId. ppCantContainId is tested to determine whether the password is allowed to contain the user ID. 
			 *  ppCantContainId is not stored in database table PWD_POLICY.
			 */
			pwdPolicy.setPp_cant_contain_id(pwdPolicy.getPp_cant_contain_id());
			/*
			 *  Get the value of field pp_cant_contain_pwd (column PP_CANT_CONTAIN_PWD in database table PWD_POLICY) in Pwd_Policy entity and
			 *  set field pp_cant_contain_pwd (column PP_CANT_CONTAIN_PWD in database table PWD_POLICY) to this value. This will trigger the setter 
			 *  for boolean field ppCantContainPwd. ppCantContainPwd is tested to determine whether the new password is allowed to contain the current  
			 *  password. ppCantContainPwd is not stored in database table PWD_POLICY.
			 */
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

	/**
	 * Return the status of the most recent method call that provides feedback.
	 * @return status of the most recent method call that provides feedback
	 */
	public String getFeedback() {
		return feedback;
	}
	
	/**
	 * Return a reference to the Pwd_Policy entity.
	 * @return reference to the Pwd_Policy entity
	 */
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
	
    /**
     * Initialize this class by calling the getter for field <i>pwdPolicy</i>.
     */
	@PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.PasswordPolicyBean postConstructCallback() ");
    	getPwdPolicy();
    }
	
	/**
	 * Sets a reference to Pwd_Policy by delegating to method find().<p><br>
	 * 
	 * <strong>The following feedback is provided:</strong><br>
   	 * <span style="font-family:consolas;">YRD0000 Process completed normally</span><br>
   	 * <span style="font-family:consolas;">YRD000B Password policy is missing</span>  
	 */
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
