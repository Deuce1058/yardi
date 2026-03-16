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
	public String getPp_cant_contain_id() { return pp_cant_contain_id; }

	public String getPp_cant_contain_pwd() { return pp_cant_contain_pwd; }
	
	public String getPp_lower_rqd() { return pp_lower_rqd; }
	
	public String getPp_number_rqd() { return pp_number_rqd; }
	
	public String getPp_special_rqd() { return pp_special_rqd; }
	
	public String getPp_upper_rqd() { return pp_upper_rqd; }
	
	public short getPpDays() { return ppDays; }
	
	public Boolean getPpLowerRqd() { return ppLowerRqd; }
	
	public Short getPpMaxPwdLen() { return ppMaxPwdLen; }
	
	public Short getPpMaxRepeatChar() { return ppMaxRepeatChar; }
	
	public short getPpMaxSignonAttempts() { return ppMaxSignonAttempts; }
	
	public Short getPpNbrDigits() { return ppNbrDigits; }
	
	public Short getPpNbrLower() { return ppNbrLower; }
	
	public Short getPpNbrSpecial() { return ppNbrSpecial; }
	
	public short getPpNbrUnique() { return ppNbrUnique; }
	
	public Short getPpNbrUpper() { return ppNbrUpper; }
	
	public Boolean getPpNumberRqd() { return ppNumberRqd; }
	
	public short getPpPwdMinLen() { return ppPwdMinLen; }
	
	public Long getPpRrn() { return ppRrn; }
	
	public Boolean getPpSpecialRqd() { return ppSpecialRqd; }
	
	public short getPpTempPwdTtl() { return ppTempPwdTtl; }
	
	public Boolean getPpUpperRqd() { return ppUpperRqd; }
	
	public boolean isPpCantContainId() { return ppCantContainId; }
	
	public boolean isPpCantContainPwd() { return ppCantContainPwd; }
}
