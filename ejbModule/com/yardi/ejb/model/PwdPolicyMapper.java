package com.yardi.ejb.model;

import com.yardi.shared.model.PasswordPolicyCopy;

/**
 * Maps {@link com.yardi.ejb.model.Pwd_Policy Pwd_Policy} entities to immutable {@link com.yardi.shared.model.PasswordPolicyCopy PasswordPolicyCopy} instances.<p>
 * 
 * This mapper creates a defensive, read-only representation of the password policy, suitable for use outside the persistence layer.
 */
public class PwdPolicyMapper {

    private PwdPolicyMapper() {
        // Prevent instantiation
    }
	
	/**
     * Convert the given {@link com.yardi.ejb.model.Pwd_Policy Pwd_Policy} entity into an immutable {@link com.yardi.shared.model.PasswordPolicyCopy PasswordPolicyCopy}.
     * @param p an instance of Pwd_Policy. May be <i>null</i>
	 * @return immutable copy {@link com.yardi.shared.model.PasswordPolicyCopy PasswordPolicyCopy} or <i>null</i> if the input is <i>null</i>
	 */
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
