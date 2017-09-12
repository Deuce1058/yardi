package com.yardi.ejb;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the USER_PROFILE database table.
 * 
 */
@Entity
@Table(name="USER_PROFILE")
@NamedQuery(name="UserProfile.findAll", query="SELECT u FROM UserProfile u")
public class UserProfile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="UP_USERID")
	private String upUserid;

	@Column(name="UPTOKEN")
	private String uptoken;

	@Column(name="UP_HOME_MARKET")
	private short upHomeMarket;

	@Column(name="UP_FIRST_NAME")
	private String upFirstName;

	@Column(name="UP_LAST_NAME")
	private String upLastName;

	@Column(name="UP_ADDRESS1")
	private String upAddress1;

	@Column(name="UP_ADDRESS2")
	private String upAddress2;

	@Column(name="UP_CITY")
	private String upCity;

	@Column(name="UP_STATE")
	private String upState;

	@Column(name="UP_ZIP")
	private String upZip;

	@Column(name="UP_ZIP4")
	private String upZip4;

	@Column(name="UP_PHONE")
	private String upPhone;

	@Column(name="UP_FAX")
	private String upFax;

	@Column(name="UP_EMAIL")
	private String upEmail;

	@Column(name="UPSSN")
	private String upssn;

	// TemporalType.DATE maps a java.util.Date to a java.sql.Date
	@Temporal(TemporalType.DATE) 
	@Column(name="UPDOB")
	private java.util.Date updob;
 
	@Column(name="UP_ACTIVE_YN")
	private String upActiveYn;

	// TemporalType.DATE maps a java.util.Date to a java.sql.Date
	@Temporal(TemporalType.DATE)
	@Column(name="UP_PWDEXPD")
	private java.util.Date upPwdexpd;

	@Column(name="UP_DISABLED_YN")
	private String upDisabledYn;

	// TemporalType.TIMESTAMP maps a java.util.Date to a java.sql.Timestamp 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UP_DISABLED_DATE")
	private java.util.Date upDisabledDate;

	// TemporalType.TIMESTAMP maps a java.util.Date to a java.sql.Timestamp 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UP_LAST_LOGIN_DATE")
	private java.util.Date upLastLoginDate;

	@Column(name="UP_PWD_ATTEMPTS")
	private short upPwdAttempts;

	@GeneratedValue
	@Column(name="UPRRN")
	private long uprrn;

	public UserProfile() {
	}

	public String getUpUserid() {
		return this.upUserid;
	}

	public void setUpUserid(String upUserid) {
		this.upUserid = upUserid;
	}

	public String getUpActiveYn() {
		return this.upActiveYn;
	}

	public void setUpActiveYn(String upActiveYn) {
		this.upActiveYn = upActiveYn;
	}

	public String getUpAddress1() {
		return this.upAddress1;
	}

	public void setUpAddress1(String upAddress1) {
		this.upAddress1 = upAddress1;
	}

	public String getUpAddress2() {
		return this.upAddress2;
	}

	public void setUpAddress2(String upAddress2) {
		this.upAddress2 = upAddress2;
	}

	public String getUpCity() {
		return this.upCity;
	}

	public void setUpCity(String upCity) {
		this.upCity = upCity;
	}

	public java.util.Date getUpDisabledDate() {
		return this.upDisabledDate;
	}

	public void setUpDisabledDate(java.util.Date upDisabledDate) {
		this.upDisabledDate = upDisabledDate;
	}

	public String getUpDisabledYn() {
		return this.upDisabledYn;
	}

	public void setUpDisabledYn(String upDisabledYn) {
		this.upDisabledYn = upDisabledYn;
	}

	public String getUpEmail() {
		return this.upEmail;
	}

	public void setUpEmail(String upEmail) {
		this.upEmail = upEmail;
	}

	public String getUpFax() {
		return this.upFax;
	}

	public void setUpFax(String upFax) {
		this.upFax = upFax;
	}

	public String getUpFirstName() {
		return this.upFirstName;
	}

	public void setUpFirstName(String upFirstName) {
		this.upFirstName = upFirstName;
	}

	public short getUpHomeMarket() {
		return this.upHomeMarket;
	}

	public void setUpHomeMarket(short upHomeMarket) {
		this.upHomeMarket = upHomeMarket;
	}

	public java.util.Date getUpLastLoginDate() {
		return this.upLastLoginDate;
	}

	public void setUpLastLoginDate(java.util.Date upLastLoginDate) {
		this.upLastLoginDate = upLastLoginDate;
	}

	public String getUpLastName() {
		return this.upLastName;
	}

	public void setUpLastName(String upLastName) {
		this.upLastName = upLastName;
	}

	public String getUpPhone() {
		return this.upPhone;
	}

	public void setUpPhone(String upPhone) {
		this.upPhone = upPhone;
	}

	public short getUpPwdAttempts() {
		return this.upPwdAttempts;
	}

	public void setUpPwdAttempts(short upPwdAttempts) {
		this.upPwdAttempts = upPwdAttempts;
	}

	public java.util.Date getUpPwdexpd() {
		return this.upPwdexpd;
	}

	public void setUpPwdexpd(java.util.Date upPwdexpd) {
		this.upPwdexpd = upPwdexpd;
	}

	public String getUpState() {
		return this.upState;
	}

	public void setUpState(String upState) {
		this.upState = upState;
	}

	public String getUpZip() {
		return this.upZip;
	}

	public void setUpZip(String upZip) {
		this.upZip = upZip;
	}

	public String getUpZip4() {
		return this.upZip4;
	}

	public void setUpZip4(String upZip4) {
		this.upZip4 = upZip4;
	}

	public java.util.Date getUpdob() {
		return this.updob;
	}

	public void setUpdob(java.util.Date updob) {
		this.updob = updob;
	}

	public long getUprrn() {
		return this.uprrn;
	}

	public void setUprrn(long uprrn) {
		this.uprrn = uprrn;
	}

	public String getUpssn() {
		return this.upssn;
	}

	public void setUpssn(String upssn) {
		this.upssn = upssn;
	}

	public String getUptoken() {
		return this.uptoken;
	}

	public void setUptoken(String uptoken) {
		this.uptoken = uptoken;
	}

	@Override
	public String toString() {
		return "UserProfile [upUserid=" + upUserid + ", uptoken=" + uptoken
				+ ", upHomeMarket=" + upHomeMarket + ", upFirstName="
				+ upFirstName + ", upLastName=" + upLastName + ", upAddress1="
				+ upAddress1 + ", upAddress2=" + upAddress2 + ", upCity="
				+ upCity + ", upState=" + upState + ", upZip=" + upZip
				+ ", upZip4=" + upZip4 + ", upPhone=" + upPhone + ", upFax="
				+ upFax + ", upEmail=" + upEmail + ", upssn=" + upssn
				+ ", updob=" + updob + ", upActiveYn=" + upActiveYn
				+ ", upPwdexpd=" + upPwdexpd + ", upDisabledYn=" + upDisabledYn
				+ ", upDisabledDate=" + upDisabledDate + ", upLastLoginDate="
				+ upLastLoginDate + ", upPwdAttempts=" + upPwdAttempts
				+ ", uprrn=" + uprrn + "]";
	}
}