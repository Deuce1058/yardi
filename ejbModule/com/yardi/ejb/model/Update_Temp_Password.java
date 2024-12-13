package com.yardi.ejb.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entity for setting temporary password<p>
 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
 * <span style="font-family:consolas;">Schema:         DB2ADMIN</span>
 */
@Entity
@Table(name="USER_PROFILE", schema="DB2ADMIN")
public class Update_Temp_Password implements Serializable {
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;
	   
	@Id
	@Column(name="UP_USERID")
	private String upUserid;
	@Column(name="UP_TEMP_PWD")
	private String upTempPwd;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UP_PWDEXPD")
	private Timestamp upPwdexpd;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UP_DISABLED_DATE")
	private Timestamp upDisabledDate;
	@Column(name="UP_PWD_ATTEMPTS")
	private short upPwdAttempts;

	public Update_Temp_Password() {
	}   

	/**
	 * @param upUserid
	 * @param upTempPwd
	 * @param ldt
	 * @param upDisabledDate
	 * @param upPwdAttempts
	 */
	public Update_Temp_Password(String upUserid, String upTempPwd, LocalDateTime ldt, Timestamp upDisabledDate,
			short upPwdAttempts) {
		this.upUserid       = upUserid;
		this.upTempPwd      = upTempPwd;
		this.upPwdexpd      = Timestamp.valueOf(ldt);
		this.upDisabledDate = upDisabledDate;
		this.upPwdAttempts  = upPwdAttempts;
	}

	public Timestamp getUpDisabledDate() {
		return this.upDisabledDate;
	}

	public short getUpPwdAttempts() {
		return this.upPwdAttempts;
	}
	
	public Timestamp getUpPwdexpd() {
		return this.upPwdexpd;
	}

	public String getUpTempPwd() {
		return this.upTempPwd;
	}   
	
	public String getUpUserid() {
		return this.upUserid;
	}

	public void setUpDisabledDate(Timestamp upDisabledDate) {
		this.upDisabledDate = upDisabledDate;
	}   
	
	public void setUpPwdAttempts(short upPwdAttempts) {
		this.upPwdAttempts = upPwdAttempts;
	}

	public void setUpPwdexpd(Timestamp upPwdexpd) {
		this.upPwdexpd = upPwdexpd;
	}   
	
	public void setUpTempPwd(String upTempPwd) {
		this.upTempPwd = upTempPwd;
	}

	public void setUpUserid(String upUserid) {
		this.upUserid = upUserid;
	}  
}
