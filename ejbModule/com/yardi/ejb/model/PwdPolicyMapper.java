package com.yardi.ejb.model;

import com.yardi.shared.model.PasswordPolicyCopy;

public class PwdPolicyMapper {

	public static PasswordPolicyCopy toCopy(Pwd_Policy p) {
		if (p == null) return null;
		
		return new PasswordPolicyCopy (
				p.getPpDays(),
				p.getPpNbrUnique(),
				p.getPpMaxSignonAttempts(),
				p.getPpPwdMinLen(),
				p.getPpUpperRqd(),
				p.getPp_upper_rqd(),
				p.getPpLowerRqd(),
				p.getPp_lower_rqd(),
				p.getPpNumberRqd(),
				p.getPp_number_rqd(),
				p.getPpSpecialRqd(),
				p.getPp_special_rqd(),
				p.getPpMaxPwdLen(),
				p.getPpMaxRepeatChar(),
				p.getPpNbrDigits(),
				p.getPpNbrUpper(),				
				p.getPpNbrLower(),
				p.getPpNbrSpecial(),				
				p.getPp_cant_contain_id(),
				p.isPpCantContainId(),				
				p.getPp_cant_contain_pwd(),
				p.isPpCantContainPwd(),
				p.getPpTempPwdTtl(),				
				p.getPpRrn()
		);		
	}

}
