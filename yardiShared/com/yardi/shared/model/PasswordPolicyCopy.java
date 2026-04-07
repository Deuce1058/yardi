package com.yardi.shared.model;

/**
 * Immutable copy of entity {@link com.yardi.ejb.model.Pwd_Policy Pwd_Policy}<p>
 * Table: DB2ADMIN.PWD_POLICY
 *  
 * @param ppDays Column: <code>PP_DAYS Password</code> life in days.<p>
 * 
 * @param ppNbrUnique Column: <code>PP_NBR_UNIQUE<code> Enforce unique tokens. Positive defines the number of unique tokens to store per user. Zero if not enforcing unique tokens.<p>
 * 
 * @param ppMaxSignonAttempts Column: <code>PP_MAX_SIGNON_ATTEMPTS</code> Allowed number of invalid password attempts since the most recent successful login. When the number of unsuccessful login 
 * attempts reaches this number, the user profile entity is disabled and the user cant login until an admin resets the password. Greater than zero.<p>
 * 
 * @param ppPwdMinLen Column: <code>PP_PWD_MIN_LEN Minimum</code> password length. Greater than zero.<p>
 * 
 * @param ppUpperRqd Boolean representation of field <code>pp_upper_rqd</code> provided for convenience. Field <code>ppUpperRqd</code> is tested to determine whether at least one upper 
 * case character is required in the new password.<p>
 * 
 * @param pp_upper_rqd Column: <code>PP_UPPER_RQD</code> Determines whether upper case characters are required in the new password. The y/n value of this field has a Boolean representation, 
 * <code>ppUpperRqd</code>, that is provided for convenience.<p>
 * 
 * @param ppLowerRqd Boolean representation of field <code>pp_lower_rqd</code> provided for convenience. Field <code>ppLowerRqd</code> is tested to determine whether 
 * at least one lower case character is required in the new password. Not stored in <code>PWD_POLICY</code> database table.<p>
 * 
 * @param pp_lower_rqd Column: <code>PP_LOWER_RQD</code> Determines whether lower case characters are required in the new password. The y/n value of this field has a Boolean representation, 
 * <code>ppLowerRqd</code>, that is provided for convenience.<p>
 * 
 * @param ppNumberRqd Boolean representation of field <code>pp_number_rqd</code> provided for convenience. Field <code>ppNumberRqd</code> is tested to determine whether at least
 * one number is required in the new password. Not stored in <code>PWD_POLICY</code> database table.<p>
 * 
 * @param pp_number_rqd Column: <code>PP_NUMBER_RQD</code> Indicates that at least one number is required in the new password. The y/n value of this field has a Boolean representation, 
 * <code>ppNumberRqd</code>, that is provided for convenience.<p>
 * 
 * @param ppSpecialRqd Boolean representation of field <code>pp_special_rqd</code> provided for convenience. Field <code>ppSpecialRqd</code> is tested to determine whether 
 * at least one special character is required in the new password. Not stored in <code>PWD_POLICY</code> database table.<p>
 * 
 * @param pp_special_rqd Column: <code>PP_SPECIAL_RQD</code> Determines whether special characters are required in the new password. The y/n value of this field has a Boolean representation,
 * <code>ppSpecialRqd</code>, that is provided for convenience.<p>
 * 
 * @param ppMaxPwdLen Column: <code>PP_MAX_PWD_LEN</code> Maximum length of new password. Set to <i>null</i> if this rule is not being enforced.<p>
 * 
 * @param ppMaxRepeatChar Column: <code>PP_MAX_REPEAT_CHAR</code> The maximum number of repeated characters allowed in the new password. Set to <i>null</i> if this rule is not being enforced.<p>
 * 
 * @param ppNbrDigits Column: <code>PP_NBR_DIGITS</code> If digits are required in the new password, then new passwords must contain at least the number of digits defined by field 
 * <code>ppNbrDigits</code>. Set to <i>null</i> if this rule is not being enforced.<p>
 * 
 * @param ppNbrUpper Column: <code>PP_NBR_UPPER</code> If upper case characters are required in the new password then new passwords must contain at least the number of upper case characters 
 * defined by field <code>ppNbrUpper</code>. Set to <i>null</i> if this rule is not being enforced.<p>
 * 
 * @param ppNbrLower Column: <code>PP_NBR_LOWER</code> If lower case characters are required in the new password then new passwords must contain at least the number of lower case characters 
 * defined by field <code>ppNbrLower</code>. Set to <i>null</i> if this rule is not being enforced.<p>
 * 
 * @param ppNbrSpecial Column: <code>PP_NBR_SPECIAL</code> If special characters are required in the new password then the new password must contain at least the number of special characters 
 * defined by field <code>ppNbrSpecial</code>. Set to <i>null</i> if this rule is not being enforced.<p>
 * 
 * @param pp_cant_contain_id Column: <code>PP_CANT_CONTAIN_ID</code> Determines whether the new password may contain the user ID in any case. The y/n value of this field has a Boolean 
 * representation, <code>ppCantContainId</code>, that is provided for convenience.<p>
 * 
 * @param ppCantContainId Boolean representation of field <code>pp_cant_contain_id</code> provided for convenience. Field <code>ppCantContainId</code> is tested to determine whether
 * the new password may contain the user ID in any case. Not stored in <code>PWD_POLICY</code> database table.<p>
 * 
 * @param pp_cant_contain_pwd Column: <code>PP_CANT_CONTAIN_PWD<code> Determines whether the new password may contain the current password. The y/n value of this field has a Boolean 
 * representation, <code>ppCantContainPwd</code>, that is provided for convenience.<p>
 * 
 * @param ppCantContainPwd Boolean representation of field <code>pp_cant_contain_pwd</code> provided for convenience. Field <code>ppCantContainPwd</code> is tested to determine whether 
 * the new password may contain the current password. Not stored in <code>PWD_POLICY<code> database table.<p>
 * 
 * @param ppTempPwdTtl Column: <code>PP_TEMP_PWD_TTL</code> Temporary password time to live in minutes.<p>
 * 
 * @param ppRrn	Column: <code>PP_RRN</code> Primary key. Relative record number. Sequence is generated by the database.<p>
 */
public record PasswordPolicyCopy (
		short ppDays,
		short ppNbrUnique,
		short ppMaxSignonAttempts,
		short ppPwdMinLen,
		Boolean ppUpperRqd,
		String pp_upper_rqd, 
		Boolean ppLowerRqd,
		String pp_lower_rqd,
		Boolean ppNumberRqd,
		String pp_number_rqd, 
		Boolean ppSpecialRqd,
		String pp_special_rqd,
		Short ppMaxPwdLen,
		Short ppMaxRepeatChar,
		Short ppNbrDigits,
		Short ppNbrUpper,
		Short ppNbrLower,
		Short ppNbrSpecial,
		String pp_cant_contain_id,
		boolean ppCantContainId,
		String pp_cant_contain_pwd, 
		boolean ppCantContainPwd,
		short ppTempPwdTtl,
		Long ppRrn
) implements PasswordPolicyLike {
	/**
	 * Return password cant contain user ID indicator
	 * @return value of password cant contain user ID
	 */
	@Override
	public String getPp_cant_contain_id() { return pp_cant_contain_id; }

	/**
	 * Return password cant contain current password indicator
	 * @return value of password cant contain current password
	 */
	@Override
	public String getPp_cant_contain_pwd() { return pp_cant_contain_pwd; }
	
	/**
	 * Return password must contain at least one lower case character indicator
	 * @return value of password must contain lower case
	 */
	@Override
	public String getPp_lower_rqd() { return pp_lower_rqd; }
	
	/**
	 * Return password must contain at least one number indicator
	 * @return password must contain at least one number
	 */
	@Override
	public String getPp_number_rqd() { return pp_number_rqd; }
	
	/**
	 * Return password must contain at least one special character indicator<p>
	 * @return value of password must contain special character
	 */
	@Override
	public String getPp_special_rqd() { return pp_special_rqd; }
	
	/**
	 * Return password must contain at least one upper case character indicator<p>
	 * @return value of password must have upper case character
	 */
	@Override
	public String getPp_upper_rqd() { return pp_upper_rqd; }
	
	/**
	 * Return password life in days
	 * @return value of password life in days
	 */
	@Override
	public short getPpDays() { return ppDays; }
	
	/**
	 * Return the rule for password must have at least one lower case character
	 * @return value of password must have lower case character
	 */
	@Override
	public Boolean getPpLowerRqd() { return ppLowerRqd; }
	
	/**
	 * Return maximum password length
	 * @return value of maximum password length
	 */
	@Override
	public Short getPpMaxPwdLen() { return ppMaxPwdLen; }
	
	/**
	 * Return maximum number of repeated characters allowed in the password 
	 * @return value of maximum number of repeated characters allowed
	 */
	@Override
	public Short getPpMaxRepeatChar() { return ppMaxRepeatChar; }
	
	/**
	 * Return number of invalid login attempts allowed since the most recent successful login
	 * @return number of invalid login attempts allowed
	 */
	@Override
	public short getPpMaxSignonAttempts() { return ppMaxSignonAttempts; }
	
	/**
	 * Return number of digits required in the password  
	 * @return number of digits required
	 */
	@Override
	public Short getPpNbrDigits() { return ppNbrDigits; }
	
	/**
	 * Return number of lower case characters required in the password
	 * @return number of lower case characters required 
	 */
	@Override
	public Short getPpNbrLower() { return ppNbrLower; }
	
	/**
	 * Return number of special characters required in the password
	 * @return number of special characters required
	 */
	@Override
	public Short getPpNbrSpecial() { return ppNbrSpecial; }
	
	/**
	 * Return number of unique tokens to store per user
	 * @return number of unique tokens to store
	 */
	@Override
	public short getPpNbrUnique() { return ppNbrUnique; }
	
	/**
	 * Return number of upper case characters required in the password 
	 * @return number of upper case characters required
	 */
	@Override
	public Short getPpNbrUpper() { return ppNbrUpper; }
	
	/**
	 * Return the rule for password must have at least one number 
	 * @return rule for password must have at least one number 
	 */
	@Override
	public Boolean getPpNumberRqd() { return ppNumberRqd; }
	
	/**
	 * Return minimum password length 
	 * @return minimum password length value
	 */
	@Override
	public short getPpPwdMinLen() { return ppPwdMinLen; }
	
	/**
	 * Return the relative record number
	 * @return relative record number value
	 */
	@Override
	public long getPpRrn() { return ppRrn; }
	
	/**
	 * Return the rule for at least one special character is required in the password  
	 * @return Boolean indicating whether special character required
	 */
	@Override
	public Boolean getPpSpecialRqd() { return ppSpecialRqd; }
	
	/**
	 * Return temporary password time to live in minutes
	 * @return temporary password time to live in minutes
	 */
	@Override
	public short getPpTempPwdTtl() { return ppTempPwdTtl; }
	
	/**
	 * Return the rule for at least one upper case character is required in the password 
	 * @return Boolean indicating whether upper case is required
	 */
	@Override
	public Boolean getPpUpperRqd() { return ppUpperRqd; }
	
	/**
	 * Return the rule for password cant contain user ID
	 * @return password cant contain user ID
	 */
	@Override
	public boolean isPpCantContainId() { return ppCantContainId; }
	
	/**
	 * Return the rule for new password cant contain current password.
	 * @return new password cant contain current password
	 */
	@Override
	public boolean isPpCantContainPwd() { return ppCantContainPwd; }
}
