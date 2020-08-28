package com.yardi.QSECOFR;

public class EditPwdPolicyRequest {
	private String action;
	private String msgId;
	private String msgDescription;
	private String pwdLifeInDays;
	private String nbrUnique;
	private String maxSignonAttempts;
	private String pwdMinLen;
	private String upperRqd;
	private String lowerRqd;
	private String nbrRqd;
	private String specialRqd;
	private String maxPwdLen;
	private String maxRepeatChar;
	private String nbrDigits;
	private String nbrUpper;
	private String nbrLower;
	private String nbrSpecial;
	private String cantContainId;
	private String cantContainPwd;

	public EditPwdPolicyRequest() {
	}

	public EditPwdPolicyRequest(String action, String msgId, String msgDescription, String pwdLifeInDays,
			String nbrUnique, String maxSignonAttempts, String pwdMinLen, String upperRqd, String lowerRqd,
			String nbrRqd, String specialRqd, String maxPwdLen, String maxRepeatChar, String nbrDigits, String nbrUpper,
			String nbrLower, String nbrSpecial, String cantContainId, String cantContainPwd) {
		this.action = action;
		this.msgId = msgId;
		this.msgDescription = msgDescription;
		this.pwdLifeInDays = pwdLifeInDays;
		this.nbrUnique = nbrUnique;
		this.maxSignonAttempts = maxSignonAttempts;
		this.pwdMinLen = pwdMinLen;
		this.upperRqd = upperRqd;
		this.lowerRqd = lowerRqd;
		this.nbrRqd = nbrRqd;
		this.specialRqd = specialRqd;
		this.maxPwdLen = maxPwdLen;
		this.maxRepeatChar = maxRepeatChar;
		this.nbrDigits = nbrDigits;
		this.nbrUpper = nbrUpper;
		this.nbrLower = nbrLower;
		this.nbrSpecial = nbrSpecial;
		this.cantContainId = cantContainId;
		this.cantContainPwd = cantContainPwd;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgDescription() {
		return msgDescription;
	}

	public void setMsgDescription(String msgDescription) {
		this.msgDescription = msgDescription;
	}

	public String getPwdLifeInDays() {
		return pwdLifeInDays;
	}

	public void setPwdLifeInDays(String pwdLifeInDays) {
		this.pwdLifeInDays = pwdLifeInDays;
	}

	public String getNbrUnique() {
		return nbrUnique;
	}

	public void setNbrUnique(String nbrUnique) {
		this.nbrUnique = nbrUnique;
	}

	public String getMaxSignonAttempts() {
		return maxSignonAttempts;
	}

	public void setMaxSignonAttempts(String maxSignonAttempts) {
		this.maxSignonAttempts = maxSignonAttempts;
	}

	public String getPwdMinLen() {
		return pwdMinLen;
	}

	public void setPwdMinLen(String pwdMinLen) {
		this.pwdMinLen = pwdMinLen;
	}

	public String getUpperRqd() {
		return upperRqd;
	}

	public void setUpperRqd(String upperRqd) {
		this.upperRqd = upperRqd;
	}

	public String getLowerRqd() {
		return lowerRqd;
	}

	public void setLowerRqd(String lowerRqd) {
		this.lowerRqd = lowerRqd;
	}

	public String getNbrRqd() {
		return nbrRqd;
	}

	public void setNbrRqd(String nbrRqd) {
		this.nbrRqd = nbrRqd;
	}

	public String getSpecialRqd() {
		return specialRqd;
	}

	public void setSpecialRqd(String specialRqd) {
		this.specialRqd = specialRqd;
	}

	public String getMaxPwdLen() {
		return maxPwdLen;
	}

	public void setMaxPwdLen(String maxPwdLen) {
		this.maxPwdLen = maxPwdLen;
	}

	public String getMaxRepeatChar() {
		return maxRepeatChar;
	}

	public void setMaxRepeatChar(String maxRepeatChar) {
		this.maxRepeatChar = maxRepeatChar;
	}

	public String getNbrDigits() {
		return nbrDigits;
	}

	public void setNbrDigits(String nbrDigits) {
		this.nbrDigits = nbrDigits;
	}

	public String getNbrUpper() {
		return nbrUpper;
	}

	public void setNbrUpper(String nbrUpper) {
		this.nbrUpper = nbrUpper;
	}

	public String getNbrLower() {
		return nbrLower;
	}

	public void setNbrLower(String nbrLower) {
		this.nbrLower = nbrLower;
	}

	public String getNbrSpecial() {
		return nbrSpecial;
	}

	public void setNbrSpecial(String nbrSpecial) {
		this.nbrSpecial = nbrSpecial;
	}

	public String getCantContainId() {
		return cantContainId;
	}

	public void setCantContainId(String cantContainId) {
		this.cantContainId = cantContainId;
	}

	public String getCantContainPwd() {
		return cantContainPwd;
	}

	public void setCantContainPwd(String cantContainPwd) {
		this.cantContainPwd = cantContainPwd;
	}

	@Override
	public String toString() {
		return "EditPwdPolicyRequest [action=" + action + ", msgId=" + msgId + ", msgDescription=" + msgDescription
				+ ", pwdLifeInDays=" + pwdLifeInDays + ", nbrUnique=" + nbrUnique + ", maxSignonAttempts="
				+ maxSignonAttempts + ", pwdMinLen=" + pwdMinLen + ", upperRqd=" + upperRqd + ", lowerRqd=" + lowerRqd
				+ ", nbrRqd=" + nbrRqd + ", specialRqd=" + specialRqd + ", maxPwdLen=" + maxPwdLen + ", maxRepeatChar="
				+ maxRepeatChar + ", nbrDigits=" + nbrDigits + ", nbrUpper=" + nbrUpper + ", nbrLower=" + nbrLower
				+ ", nbrSpecial=" + nbrSpecial + ", cantContainId=" + cantContainId + ", cantContainPwd="
				+ cantContainPwd + "]";
	}
}
