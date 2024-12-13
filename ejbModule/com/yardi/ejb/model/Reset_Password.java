package com.yardi.ejb.model;

import java.io.Serializable;
import java.lang.String;
import java.sql.Timestamp;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entity for user profile details. These details are displayed on the password reset page used by the help desk.
 */
@Entity
@Table(name="USER_PROFILE", schema="DB2ADMIN")
public class Reset_Password implements Serializable {
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column: UP_USERID</span><p>
	 * ID column
	 */
	@Id
	@Column(name="UP_USERID") 	
	private String upUserid;
	/**
	 * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column: UP_TEMP_PWD</span><p>
	 * The user's hashed temporary password.
	 */ 
	@Column(name="UP_TEMP_PWD") 
	private String upTempPwd;
	/**
	 * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column: UP_ACTIVE_YN</span><p>
	 * User profile active flag. Y for active. N for inactive. User cant login unless this column is Y. Only an admin can set this column to Y.
	 */
	@Column(name="UP_ACTIVE_YN")
	private String upActiveYn;
	/**
	 * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column: UP_DISABLED_DATE</span><p>
	 * Date and time when the user profile became disabled due to too many invalid password attempts since the most recent successful login.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UP_DISABLED_DATE")
	private Timestamp upDisabledDate;
	/**
	 * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column: UP_PWDEXPD</span><p>
	 * Password expiration date. The date and time when the password must be changed. 
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UP_PWDEXPD") 
	private Timestamp upPwdexpd;
	/**
	 * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column: UP_LAST_LOGIN_DATE</span><p>
	 * Date and time of most recent successful login.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UP_LAST_LOGIN_DATE") 
	private Timestamp upLastLoginDate;
	/**
	 * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column: UP_PWD_ATTEMPTS</span><p>
	 * Number of invalid password attempts since the most recent successful login.
	 */
	@Column(name="UP_PWD_ATTEMPTS") 
	private short upPwdAttempts;

	/**
	 * Default constructor
	 */
	public Reset_Password() {
		System.out.println("com.yardi.ejb.model.Reset_Password.Reset_Password() 0000");
	}
	
	/**
	 * Return account active flag
	 * @return account active flag
	 */
	public String getUpActiveYn() {
		System.out.println("com.yardi.ejb.model.Reset_Password.getUpActiveYn() 0001");
		return this.upActiveYn;
	}

	/**
	 * Date and time when the user profile became disabled due to too many invalid password attempts since the most recent successful login
	 * @return date and time when the user profile became disabled
	 */
	public Timestamp getUpDisabledDate() {
		System.out.println("com.yardi.ejb.model.Reset_Password.getUpDisabledDate() 0002");
		return this.upDisabledDate;
	}   
	
	/**
	 * Return date and time of most recent successful login
	 * @return date and time of most recent successful login
	 */
	public Timestamp getUpLastLoginDate() {
		System.out.println("com.yardi.ejb.model.Reset_Password.getUpLastLoginDate() 0003");
		return this.upLastLoginDate;
	}

	/**
	 * Return number of invalid password attempts since the most recent successful login
	 * @return number of invalid password attempts
	 */
	public short getUpPwdAttempts() {
		System.out.println("com.yardi.ejb.model.Reset_Password.getUpPwdAttempts() 0004");
		return this.upPwdAttempts;
	}   
	
	/**
	 * Password expiration date. The date and time when the password must be changed.	 
	 * @return date and time when the password must be changed
	 */
	public Date getUpPwdexpd() {
		System.out.println("com.yardi.ejb.model.Reset_Password.getUpPwdexpd() 0005");
		return this.upPwdexpd;
	}

	/**
	 * The user's hashed temporary password
	 * @return user's hashed temporary password
	 */
	public String getUpTempPwd() {
		System.out.println("com.yardi.ejb.model.Reset_Password.getUpTempPwd() 0006");
		return this.upTempPwd;
	}   
	
	/**
	 * Return ID column
	 * @return ID column
	 */
	public String getUpUserid() {
		System.out.println("com.yardi.ejb.model.Reset_Password.getUpUserid() 0007");
		return this.upUserid;
	}

	/**
	 * Set user profile active flag to the given String.<p> 
	 * Y for active. N for inactive. User cant login unless this column is Y. Only an admin can set this column to Y.
	 * @param upActiveYn user profile active flag
	 */
	public void setUpActiveYn(String upActiveYn) {
		System.out.println("com.yardi.ejb.model.Reset_Password.setUpActiveYn() 0008");
		this.upActiveYn = upActiveYn;
	}   
	
	/**
	 * Set date and time when the user profile became disabled due to too many invalid password attempts since the most recent successful login to the given Timestamp
	 * @param upDisabledDate date and time when the user profile became disabled
	 */
	public void setUpDisabledDate(Timestamp upDisabledDate) {
		System.out.println("com.yardi.ejb.model.Reset_Password.setUpDisabledDate() 0009");
		this.upDisabledDate = upDisabledDate;
	}

	/**
	 * Set date and time of most recent successful login to the given Timestamp 
	 * @param upLastLoginDate date and time of most recent successful login
	 */
	public void setUpLastLoginDate(Timestamp upLastLoginDate) {
		System.out.println("com.yardi.ejb.model.Reset_Password.setUpLastLoginDate() 000A");
		this.upLastLoginDate = upLastLoginDate;
	}
	
	/**
	 * Set number of invalid password attempts since the most recent successful login to the given short
	 * @param upPwdAttempts number of invalid password attempts
	 */
	public void setUpPwdAttempts(short upPwdAttempts) {
		System.out.println("com.yardi.ejb.model.Reset_Password.setUpPwdAttempts() 000B");
		this.upPwdAttempts = upPwdAttempts;
	}

	/**
	 * Set password expiration date to the given Timestamp. The date and time when the password must be changed	
	 * @param upPwdexpd date and time when password must be changed
	 */
	public void setUpPwdexpd(java.sql.Timestamp upPwdexpd) {
		System.out.println("com.yardi.ejb.model.Reset_Password.setUpPwdexpd() 000C");
		this.upPwdexpd = upPwdexpd;
	}   
	
	/**
	 * Set the user's hashed temporary password to the given String
	 * @param upTempPwd the user's hashed temporary password
	 */
	public void setUpTempPwd(String upTempPwd) {
		System.out.println("com.yardi.ejb.model.Reset_Password.setUpTempPwd() 000D");
		this.upTempPwd = upTempPwd;
	}

	/**
	 * Set ID column to the given String
	 * @param upUserid user ID
	 */
	public void setUpUserid(String upUserid) {
		System.out.println("com.yardi.ejb.model.Reset_Password.setUpUserid() 000E");
		this.upUserid = upUserid;
	}   
}
