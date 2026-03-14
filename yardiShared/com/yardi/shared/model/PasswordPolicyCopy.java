package com.yardi.shared.model;

/**
 * Replacement for entity Pwd_Policy
 */

public record PasswordPolicyCopy(
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
) {

	public short ppDays() { return ppDays; }

	public short ppNbrUnique() { return ppNbrUnique; }

	public short ppMaxSignonAttempts() { return ppMaxSignonAttempts; }

	public short ppPwdMinLen() { return ppPwdMinLen; }

	public Boolean ppUpperRqd() { return ppUpperRqd; }

	public String pp_upper_rqd() { return pp_upper_rqd; }

	public Boolean ppLowerRqd() { return ppLowerRqd; }

	public String pp_lower_rqd() { return pp_lower_rqd; }

	public Boolean ppNumberRqd() { return ppNumberRqd; }

	public String pp_number_rqd() { return pp_number_rqd; }

	public Boolean ppSpecialRqd() { return ppSpecialRqd; }

	public String pp_special_rqd() { return pp_special_rqd; }

	public Short ppMaxPwdLen() { return ppMaxPwdLen; }

	public Short ppMaxRepeatChar() { return ppMaxRepeatChar; }

	public Short ppNbrDigits() { return ppNbrDigits; }

	public Short ppNbrUpper() { return ppNbrUpper; }

	public Short ppNbrLower() { return ppNbrLower; }

	public Short ppNbrSpecial() { return ppNbrSpecial; }

	public String pp_cant_contain_id() { return pp_cant_contain_id; }

	public boolean ppCantContainId() { return ppCantContainId; }

	public String pp_cant_contain_pwd() { return pp_cant_contain_pwd; }

	public boolean ppCantContainPwd() { return ppCantContainPwd; }

	public short ppTempPwdTtl() { return ppTempPwdTtl; }

	public Long ppRrn() { return ppRrn; }
}
