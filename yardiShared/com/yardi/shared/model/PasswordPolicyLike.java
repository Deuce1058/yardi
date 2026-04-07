package com.yardi.shared.model;

/**
 * Abstraction for password policy data used by business logic that operates on both persistent entities and detached/copy representations.<p>
 *
 * Implementations of this interface provide a unified view of password policy attributes and allow service-layer code such as 
 * {@link com.yardi.ejb.EditPasswordPolicyBean#newEditPwdPolicyRequest(PasswordPolicyLike) EditPasswordPolicyBean.newEditPwdPolicyRequest(PasswordPolicyLike)} to map password policy request/response objects such as 
 * {@link com.yardi.shared.QSECOFR.EditPwdPolicyRequest EditPwdPolicyRequest} without depending on a specific implementation type.
 */
 public interface PasswordPolicyLike {
	/**
	 * Return password cant contain user ID indicator
	 * @return value of password cant contain user ID
	 */
	String getPp_cant_contain_id();

	/**
	 * Return password cant contain current password indicator
	 * @return value of password cant contain current password
	 */
	String getPp_cant_contain_pwd();
	
	/**
	 * Return password must contain at least one lower case character indicator
	 * @return value of password must contain lower case
	 */
	String getPp_lower_rqd();
	
	/**
	 * Return password must contain at least one number indicator
	 * @return password must contain at least one number
	 */
	String getPp_number_rqd();
	
	/**
	 * Return password must contain at least one special character indicator<p>
	 * @return value of password must contain special character
	 */
	String getPp_special_rqd();
	
	/**
	 * Return password must contain at least one upper case character indicator<p>
	 * @return value of password must have upper case character
	 */
	String getPp_upper_rqd(); 
	
	/**
	 * Return password life in days
	 * @return value of password life in days
	 */
	short getPpDays(); 
	
	/**
	 * Return the rule for password must have at least one lower case character
	 * @return value of password must have lower case character
	 */
	Boolean getPpLowerRqd();
	
	/**
	 * Return maximum password length
	 * @return value of maximum password length
	 */
	Short getPpMaxPwdLen();
	
	/**
	 * Return maximum number of repeated characters allowed in the password 
	 * @return value of maximum number of repeated characters allowed
	 */
	Short getPpMaxRepeatChar();
	
	/**
	 * Return number of invalid login attempts allowed since the most recent successful login
	 * @return number of invalid login attempts allowed
	 */
	short getPpMaxSignonAttempts();
	
	/**
	 * Return number of digits required in the password  
	 * @return number of digits required
	 */
	Short getPpNbrDigits();
	
	/**
	 * Return number of lower case characters required in the password
	 * @return number of lower case characters required 
	 */
	Short getPpNbrLower();
	
	/**
	 * Return number of special characters required in the password
	 * @return number of special characters required
	 */
	Short getPpNbrSpecial();
	
	/**
	 * Return number of unique tokens to store per user
	 * @return number of unique tokens to store
	 */
	short getPpNbrUnique();
	
	/**
	 * Return number of upper case characters required in the password 
	 * @return number of upper case characters required
	 */
	Short getPpNbrUpper();
	
	/**
	 * Return the rule for password must have at least one number 
	 * @return rule for password must have at least one number 
	 */
	Boolean getPpNumberRqd();
	
	/**
	 * Return minimum password length 
	 * @return minimum password length value
	 */
	short getPpPwdMinLen();
	
	/**
	 * Return the relative record number
	 * @return relative record number value
	 */
	long getPpRrn();
	
	/**
	 * Return the rule for at least one special character is required in the password  
	 * @return Boolean indicating whether special character required
	 */
	Boolean getPpSpecialRqd();
	
	/**
	 * Return temporary password time to live in minutes
	 * @return temporary password time to live in minutes
	 */
	short getPpTempPwdTtl();
	
	/**
	 * Return the rule for at least one upper case character is required in the password 
	 * @return Boolean indicating whether upper case is required
	 */
	Boolean getPpUpperRqd();
	
	/**
	 * Return the rule for password cant contain user ID
	 * @return password cant contain user ID
	 */
	boolean isPpCantContainId();
	
	/**
	 * Return the rule for new password cant contain current password.
	 * @return new password cant contain current password
	 */
	boolean isPpCantContainPwd();
	
	String toString();
}
