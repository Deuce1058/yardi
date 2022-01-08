package com.yardi.ejb.model;
/*
 * 2020 1104
 * remove attribute for Login_Sessions_Table. This is attribute is now defined in Login_User_Groups
 * remove getUgSessionTable() 
 * added bidirectional OneToMany association with Login_User_Groups
 */

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Persistence class containing USER_PROFILE data elements required to login
 * 
 * @author Jim
 *
 */
@Entity
@Table(name="USER_PROFILE", schema="DB2ADMIN")
public class Login_User_Profile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="UP_USERID")
	private String upUserid;

	@Column(name="UPTOKEN")
	private String uptoken;

	@Temporal(TemporalType.DATE)
	@Column(name="UP_PWDEXPD")
	private java.util.Date upPwdexpd;

	@Column(name="UP_PWD_ATTEMPTS")
	private short upPwdAttempts;

	@Column(name="UP_DISABLED_DATE")
	private java.sql.Timestamp upDisabledDate;

	@Column(name="UP_LAST_LOGIN_DATE")
	private java.sql.Timestamp upLastLoginDate;

	@Column(name="UP_ACTIVE_YN")
	private String upActiveYn;
	
	@OneToMany(mappedBy = "ugUserProfile")
	private List<Login_User_Groups> upLoginUserGroups;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUpActiveYn() {
		return upActiveYn;
	}

	public java.sql.Timestamp getUpDisabledDate() {
		return upDisabledDate;
	}

	public java.sql.Timestamp getUpLastLoginDate() {
		return upLastLoginDate;
	}

	public short getUpPwdAttempts() {
		return upPwdAttempts;
	}

	public java.util.Date getUpPwdexpd() {
		return upPwdexpd;
	}

	public String getUptoken() {
		return uptoken;
	}

	public String getUpUserid() {
		return upUserid;
	}

	public void setUpDisabledDate(java.sql.Timestamp upDisabledDate) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Login_User_Profile.setUpDisabledDate() 0000");
		/*debug*/
		this.upDisabledDate = upDisabledDate;
	}

	public void setUpLastLoginDate(java.sql.Timestamp upLastLoginDate) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Login_User_Profile.setUpLastLoginDate() 0000");
		/*debug*/
		this.upLastLoginDate = upLastLoginDate;
	}

	public void setUpPwdAttempts(short upPwdAttempts) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Login_User_Profile.setUpPwdAttempts() 0000");
		/*debug*/
		this.upPwdAttempts = upPwdAttempts;
	}

	public void setUpPwdexpd(java.util.Date upPwdexpd) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Login_User_Profile.setUpPwdexpd() 0000");
		/*debug*/
		this.upPwdexpd = upPwdexpd;
	}

	public void setUptoken(String uptoken) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Login_User_Profile.setUptoken() 0000");
		/*debug*/
		this.uptoken = uptoken;
	}
}
