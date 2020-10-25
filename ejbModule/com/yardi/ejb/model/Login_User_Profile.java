package com.yardi.ejb.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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

	@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name = "UP_USERID", referencedColumnName = "ST_USER_ID", nullable=false) 
	//Multiple writable mappings exist for UP_USERID because this is also mapped to Login_User_Profile by UP_USERID
	//The only way around the multiple writable mappings is to use updatable=false, insertable=false to join to Login_Sessions_Table
	@JoinColumn(name = "UP_USERID", referencedColumnName = "ST_USER_ID", nullable=false, updatable=false, insertable=false)
	private Login_Sessions_Table ugSessionTable;

	public Login_Sessions_Table getUgSessionTable() {
		/*debug*/
		System.out.println("com.yardi.ejb.Login_User_Profile.getUgSessionTable() 0000 ");
		/*debug*/
		return ugSessionTable;
	}

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
		this.upDisabledDate = upDisabledDate;
	}

	public void setUpLastLoginDate(java.sql.Timestamp upLastLoginDate) {
		this.upLastLoginDate = upLastLoginDate;
	}

	public void setUpPwdAttempts(short upPwdAttempts) {
		this.upPwdAttempts = upPwdAttempts;
	}

	public void setUpPwdexpd(java.util.Date upPwdexpd) {
		this.upPwdexpd = upPwdexpd;
	}

	public void setUptoken(String uptoken) {
		this.uptoken = uptoken;
	}
}
