package com.yardi.shared.test;

public class LoginStateRequest {
	private String msgid;
	private String msgd;
	private String userName;
	private String ppDays;
	private String ppNbrUnique;
	private String ppMaxSignonAttempts;
	private String ppPwdMinLen;
	private String pp_upper_rqd;
	private String pp_lower_rqd;
	private String pp_number_rqd;
	private String pp_special_rqd;
	private String ppMaxPwdLen;
	private String ppMaxRepeatChar;
	private String ppNbrDigits;
	private String ppNbrUpper;
	private String ppNbrLower;
	private String ppNbrSpecial;
	private String pp_cant_contain_id;
	private String pp_cant_contain_pwd;
	private String uptoken;
	private String upPwdexpd;
	private String upPwdAttempts;
	private String upDisabledDate;
	private String upDisabledTime;
	private String upLastLoginDate;
	private String upLastLoginTime;
	private String upActiveYn;
	private String uniqueTokensString;
	private String stSesssionId;
	private String stSessionToken;
	private String stLastRequest;
	private String stLastActiveDate;
	private String stLastActiveTime;
	private String userGroups;
	
	public LoginStateRequest() {
	}

	public String getMsgd() {
		return msgd;
	}

	public String getMsgid() {
		return msgid;
	}

	public String getPp_cant_contain_id() {
		return pp_cant_contain_id;
	}

	public String getPp_cant_contain_pwd() {
		return pp_cant_contain_pwd;
	}

	public String getPp_lower_rqd() {
		return pp_lower_rqd;
	}

	public String getPp_number_rqd() {
		return pp_number_rqd;
	}

	public String getPp_special_rqd() {
		return pp_special_rqd;
	}

	public String getPp_upper_rqd() {
		return pp_upper_rqd;
	}

	public String getPpDays() {
		return ppDays;
	}

	public String getPpMaxPwdLen() {
		return ppMaxPwdLen;
	}

	public String getPpMaxRepeatChar() {
		return ppMaxRepeatChar;
	}

	public String getPpMaxSignonAttempts() {
		return ppMaxSignonAttempts;
	}

	public String getPpNbrDigits() {
		return ppNbrDigits;
	}

	public String getPpNbrLower() {
		return ppNbrLower;
	}

	public String getPpNbrSpecial() {
		return ppNbrSpecial;
	}

	public String getPpNbrUnique() {
		return ppNbrUnique;
	}

	public String getPpNbrUpper() {
		return ppNbrUpper;
	}

	public String getPpPwdMinLen() {
		return ppPwdMinLen;
	}

	public String getStLastActiveDate() {
		return stLastActiveDate;
	}

	public String getStLastActiveTime() {
		return stLastActiveTime;
	}

	public String getStLastRequest() {
		return stLastRequest;
	}

	public String getStSessionToken() {
		return stSessionToken;
	}

	public String getStSesssionId() {
		return stSesssionId;
	}

	public String getUniqueTokensString() {
		return uniqueTokensString;
	}

	public String getUpActiveYn() {
		return upActiveYn;
	}

	public String getUpDisabledDate() {
		return upDisabledDate;
	}

	public String getUpDisabledTime() {
		return upDisabledTime;
	}

	public String getUpLastLoginDate() {
		return upLastLoginDate;
	}

	public String getUpLastLoginTime() {
		return upLastLoginTime;
	}

	public String getUpPwdAttempts() {
		return upPwdAttempts;
	}

	public String getUpPwdexpd() {
		return upPwdexpd;
	}

	public String getUptoken() {
		return uptoken;
	}

	public String getUserGroups() {
		return userGroups;
	}

	public String getUserName() {
		return userName;
	}

	public void setMsgd(String msgd) {
		this.msgd = msgd;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public void setPp_cant_contain_id(String pp_cant_contain_id) {
		this.pp_cant_contain_id = pp_cant_contain_id;
	}

	public void setPp_cant_contain_pwd(String pp_cant_contain_pwd) {
		this.pp_cant_contain_pwd = pp_cant_contain_pwd;
	}

	public void setPp_lower_rqd(String pp_lower_rqd) {
		this.pp_lower_rqd = pp_lower_rqd;
	}

	public void setPp_number_rqd(String pp_number_rqd) {
		this.pp_number_rqd = pp_number_rqd;
	}

	public void setPp_special_rqd(String pp_special_rqd) {
		this.pp_special_rqd = pp_special_rqd;
	}

	public void setPp_upper_rqd(String pp_upper_rqd) {
		this.pp_upper_rqd = pp_upper_rqd;
	}

	public void setPpDays(String ppDays) {
		this.ppDays = ppDays;
	}

	public void setPpMaxPwdLen(String ppMaxPwdLen) {
		this.ppMaxPwdLen = ppMaxPwdLen;
	}

	public void setPpMaxRepeatChar(String ppMaxRepeatChar) {
		this.ppMaxRepeatChar = ppMaxRepeatChar;
	}

	public void setPpMaxSignonAttempts(String ppMaxSignonAttempts) {
		this.ppMaxSignonAttempts = ppMaxSignonAttempts;
	}

	public void setPpNbrDigits(String ppNbrDigits) {
		this.ppNbrDigits = ppNbrDigits;
	}

	public void setPpNbrLower(String ppNbrLower) {
		this.ppNbrLower = ppNbrLower;
	}

	public void setPpNbrSpecial(String ppNbrSpecial) {
		this.ppNbrSpecial = ppNbrSpecial;
	}

	public void setPpNbrUnique(String ppNbrUnique) {
		this.ppNbrUnique = ppNbrUnique;
	}

	public void setPpNbrUpper(String ppNbrUpper) {
		this.ppNbrUpper = ppNbrUpper;
	}

	public void setPpPwdMinLen(String ppPwdMinLen) {
		this.ppPwdMinLen = ppPwdMinLen;
	}

	public void setStLastActiveDate(String stLastActiveDate) {
		this.stLastActiveDate = stLastActiveDate;
	}

	public void setStLastActiveTime(String stLastActiveTime) {
		this.stLastActiveTime = stLastActiveTime;
	}

	public void setStLastRequest(String stLastRequest) {
		this.stLastRequest = stLastRequest;
	}

	public void setStSessionToken(String stSessionToken) {
		this.stSessionToken = stSessionToken;
	}

	public void setStSesssionId(String stSesssionId) {
		this.stSesssionId = stSesssionId;
	}

	public void setUniqueTokensString(String uniqueTokensString) {
		this.uniqueTokensString = uniqueTokensString;
	}

	public void setUpActiveYn(String upActiveYn) {
		this.upActiveYn = upActiveYn;
	}

	public void setUpDisabledDate(String upDisabledDate) {
		this.upDisabledDate = upDisabledDate;
	}

	public void setUpDisabledTime(String upDisabledTime) {
		this.upDisabledTime = upDisabledTime;
	}

	public void setUpLastLoginDate(String upLastLoginDate) {
		this.upLastLoginDate = upLastLoginDate;
	}

	public void setUpLastLoginTime(String upLastLoginTime) {
		this.upLastLoginTime = upLastLoginTime;
	}

	public void setUpPwdAttempts(String upPwdAttempts) {
		this.upPwdAttempts = upPwdAttempts;
	}

	public void setUpPwdexpd(String upPwdexpd) {
		this.upPwdexpd = upPwdexpd;
	}

	public void setUptoken(String uptoken) {
		this.uptoken = uptoken;
	}

	public void setUserGroups(String userGroups) {
		this.userGroups = userGroups;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "LoginStateRequest [msgid=" + msgid + ", msgd=" + msgd + ", userName=" + userName + ", ppDays=" + ppDays
				+ "\n    "
				+ ", ppNbrUnique=" + ppNbrUnique + ", ppMaxSignonAttempts=" + ppMaxSignonAttempts + ", ppPwdMinLen="
				+ ppPwdMinLen + ", pp_upper_rqd=" + pp_upper_rqd 
				+ "\n    "
				+ ", pp_lower_rqd=" + pp_lower_rqd + ", pp_number_rqd="
				+ pp_number_rqd + ", pp_special_rqd=" + pp_special_rqd + ", ppMaxPwdLen=" + ppMaxPwdLen
				+ "\n    "
				+ ", ppMaxRepeatChar=" + ppMaxRepeatChar + ", ppNbrDigits=" + ppNbrDigits + ", ppNbrUpper=" + ppNbrUpper
				+ ", ppNbrLower=" + ppNbrLower 
				+ "\n    "
				+ ", ppNbrSpecial=" + ppNbrSpecial + ", pp_cant_contain_id="
				+ pp_cant_contain_id + ", pp_cant_contain_pwd=" + pp_cant_contain_pwd + ", uptoken=" + uptoken
				+ "\n    "
				+ ", upPwdexpd=" + upPwdexpd + ", upPwdAttempts=" + upPwdAttempts + ", upDisabledDate=" + upDisabledDate
				+ ", upDisabledTime=" + upDisabledTime 
				+ "\n    "
				+ ", upLastLoginDate=" + upLastLoginDate + ", upLastLoginTime="
				+ upLastLoginTime + ", upActiveYn=" + upActiveYn + ", uniqueTokensString=" + uniqueTokensString
				+ "\n    "
				+ ", stSesssionId=" + stSesssionId + ", stSessionToken=" + stSessionToken + ", stLastRequest="
				+ stLastRequest + ", stLastActiveDate=" + stLastActiveDate 
				+ "\n    "
				+ ", stLastActiveTime=" + stLastActiveTime
				+ ", userGroups=" + userGroups + "]";
	}
}
