package com.yardi.ejb;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the USER_PROFILE database table.
 * 
 */
@Entity
@Table(name="USER_PROFILE", schema="DB2ADMIN")
@NamedQuery(name="User_Profile.findAll", query="SELECT u FROM User_Profile u")
public class User_Profile implements Serializable {
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
	@Column(name="UP_DISABLED_DATE")
	private java.sql.Timestamp upDisabledDate;

	// TemporalType.TIMESTAMP maps a java.util.Date to a java.sql.Timestamp 
	@Column(name="UP_LAST_LOGIN_DATE")
	private java.sql.Timestamp upLastLoginDate;

	@Column(name="UP_PWD_ATTEMPTS")
	private short upPwdAttempts;

	@GeneratedValue
	@Column(name="UPRRN")
	private long uprrn;

	public User_Profile() {
		/*debug*/
		System.out.println("com.yardi.ejb.User_Profile.User_Profile() 0000");
		/*debug*/
	}

	public String getUpUserid() {
		return this.upUserid;
	}

	public void setUpUserid(String upUserid) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpUserid() 0000");
		/*debug*/
		this.upUserid = upUserid;
	}

	public String getUpActiveYn() {
		return this.upActiveYn;
	}

	public void setUpActiveYn(String upActiveYn) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpActiveYn() 0000");
		/*debug*/
		this.upActiveYn = upActiveYn;
	}

	public String getUpAddress1() {
		return this.upAddress1;
	}

	public void setUpAddress1(String upAddress1) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpAddress1() 0000");
		/*debug*/
		this.upAddress1 = upAddress1;
	}

	public String getUpAddress2() {
		return this.upAddress2;
	}

	public void setUpAddress2(String upAddress2) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpAddress2() 0000");
		/*debug*/
		this.upAddress2 = upAddress2;
	}

	public String getUpCity() {
		return this.upCity;
	}

	public void setUpCity(String upCity) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpCity() 0000");
		/*debug*/
		this.upCity = upCity;
	}

	public java.sql.Timestamp getUpDisabledDate() {
		return this.upDisabledDate;
	}

	public void setUpDisabledDate(java.util.Date upDisabledDate) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpDisabledDate() 0000");
		/*debug*/
		this.upDisabledDate.setTime(upDisabledDate.getTime()); 
	}

	public String getUpDisabledYn() {
		return this.upDisabledYn;
	}

	public void setUpDisabledYn(String upDisabledYn) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpDisabledYn() 0000");
		/*debug*/
		this.upDisabledYn = upDisabledYn;
	}

	public String getUpEmail() {
		return this.upEmail;
	}

	public void setUpEmail(String upEmail) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpEmail() 0000");
		/*debug*/
		this.upEmail = upEmail;
	}

	public String getUpFax() {
		return this.upFax;
	}

	public void setUpFax(String upFax) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpFax() 0000");
		/*debug*/
		this.upFax = upFax;
	}

	public String getUpFirstName() {
		return this.upFirstName;
	}

	public void setUpFirstName(String upFirstName) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpFirstName() 0000");
		/*debug*/
		this.upFirstName = upFirstName;
	}

	public short getUpHomeMarket() {
		return this.upHomeMarket;
	}

	public void setUpHomeMarket(short upHomeMarket) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpHomeMarket() 0000");
		/*debug*/
		this.upHomeMarket = upHomeMarket;
	}

	public java.sql.Timestamp getUpLastLoginDate() {
		return this.upLastLoginDate;
	}

	public void setUpLastLoginDate(java.util.Date upLastLoginDate) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpLastLoginDate() 0000");
		/*debug*/
		this.upLastLoginDate.setTime(upLastLoginDate.getTime()); 
	}

	public String getUpLastName() {
		return this.upLastName;
	}

	public void setUpLastName(String upLastName) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpLastName() 0000");
		/*debug*/
		this.upLastName = upLastName;
	}

	public String getUpPhone() {
		return this.upPhone;
	}

	public void setUpPhone(String upPhone) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpPhone() 0000");
		/*debug*/
		this.upPhone = upPhone;
	}

	public short getUpPwdAttempts() {
		return this.upPwdAttempts;
	}

	public void setUpPwdAttempts(short upPwdAttempts) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpPwdAttempts() 0000");
		/*debug*/
		this.upPwdAttempts = upPwdAttempts;
	}

	public java.util.Date getUpPwdexpd() {
		return this.upPwdexpd;
	}

	public void setUpPwdexpd(java.util.Date upPwdexpd) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpPwdexpd() 0000");
		/*debug*/
		this.upPwdexpd = upPwdexpd;
	}

	public String getUpState() {
		return this.upState;
	}

	public void setUpState(String upState) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpState() 0000");
		/*debug*/
		this.upState = upState;
	}

	public String getUpZip() {
		return this.upZip;
	}

	public void setUpZip(String upZip) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpZip() 0000");
		/*debug*/
		this.upZip = upZip;
	}

	public String getUpZip4() {
		return this.upZip4;
	}

	public void setUpZip4(String upZip4) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpZip4() 0000");
		/*debug*/
		this.upZip4 = upZip4;
	}

	public java.util.Date getUpdob() {
		return this.updob;
	}

	public void setUpdob(java.util.Date updob) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpdob() 0000");
		/*debug*/
		this.updob = updob;
	}

	public long getUprrn() {
		return this.uprrn;
	}

	public void setUprrn(long uprrn) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUprrn() 0000");
		/*debug*/
		this.uprrn = uprrn;
	}

	public String getUpssn() {
		return this.upssn;
	}

	public void setUpssn(String upssn) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUpssn() 0000");
		/*debug*/
		this.upssn = upssn;
	}

	public String getUptoken() {
		return this.uptoken;
	}

	public void setUptoken(String uptoken) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUptoken() 0000");
		/*debug*/
		this.uptoken = uptoken;
	}

	@Override
	public String toString() {
		return "User_Profile [upUserid=" + upUserid + ", uptoken=" + uptoken
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