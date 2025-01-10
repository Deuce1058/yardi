package com.yardi.ejb.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Set temporary password and password life in minutes.<p>
 * In case the user profile is disabled, clear disabled date and password attempts. Doing this makes it appear as if they are authenticating normally except with a 
 * temporary password.<p>
 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><br>
 * <span style="font-family:consolas;">Schema:         DB2ADMIN</span>
 */
@Entity
@Table(name="USER_PROFILE", schema="DB2ADMIN")
public class Update_Temp_Password implements Serializable {
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * User ID<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_USERID</span>
	 */
	@Id
	@Column(name="UP_USERID")
	private String upUserid;
	/**
	 * Temporary password<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_TEMP_PWD</span>
	 */
	@Column(name="UP_TEMP_PWD")
	private String upTempPwd;
	/**
	 * Password expiration date and time<p> 
	 * If a temporary password was assigned by the help desk, the password expires in minutes<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_PWDEXPD</span>
	 */
	@Column(name="UP_PWDEXPD")
	private Timestamp upPwdexpd;
	/**
	 * Date and time when the user profile became disabled due to too many failed login attempts since the most recent successful login<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_DISABLED_DATE</span>
	 */
	@Column(name="UP_DISABLED_DATE")
	private Timestamp upDisabledDate;
	/**
	 * Number of failed login attempts since the most recent successful login<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_PWD_ATTEMPTS</span>
	 */
	@Column(name="UP_PWD_ATTEMPTS")
	private short upPwdAttempts;

	/**
	 * Default constructor
	 */
	public Update_Temp_Password() {
		System.out.println("com.yardi.ejb.model.Update_Temp_Password.Update_Temp_Password() 0000");
	}   

	/**
	 * Constructor for setting temporary password, password expiration date and time.<p> 
	 * When the helpdesk assigns a temporary password, disabled date and time and password attempts are cleared to make it appear that the user is authenticating 
	 * normally except with a temporary password.    
	 * @param upUserid user ID
	 * @param upTempPwd temporary password
	 * @param ldt date and time when password expires
	 * @param upDisabledDate date and time when user profile was disabled
	 * @param upPwdAttempts password attempts
	 */
	public Update_Temp_Password(String upUserid, String upTempPwd, LocalDateTime ldt, Timestamp upDisabledDate,
			short upPwdAttempts) {
		System.out.println("com.yardi.ejb.model.Update_Temp_Password.Update_Temp_Password() 0001");
		this.upUserid       = upUserid;
		this.upTempPwd      = upTempPwd;
		this.upPwdexpd      = Timestamp.valueOf(ldt);
		this.upDisabledDate = upDisabledDate;
		this.upPwdAttempts  = upPwdAttempts;
	}

	/**
	 * Return date and time when the user profile was disabled due to too many failed login attempts since the most recent successful login<p> 
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_DISABLED_DATE</span>
	 * @return date and time when the user profile was disabled
	 */
	public Timestamp getUpDisabledDate() {
		return this.upDisabledDate;
	}

	/**
	 * Return number of failed login attempts since the most recent successful login.<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_PWD_ATTEMPTS</span>
	 * @return number of failed login attempts since the most recent successful login
	 */
	public short getUpPwdAttempts() {
		return this.upPwdAttempts;
	}

	/**
	 * Return date and time when the password expires<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_PWDEXPD</span>
	 * @return date and time when the password expires
	 */
	public Timestamp getUpPwdexpd() {
		return this.upPwdexpd;
	}

	/**
	 * Temporary password<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_TEMP_PWD</span>
	 * @return temporary password
	 */
	public String getUpTempPwd() {
		return this.upTempPwd;
	}   
	
	/**
	 * User ID<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_USERID</span>
	 * @return User ID
	 */
	public String getUpUserid() {
		return this.upUserid;
	}

	/**
	 * Set date and time when the user profile became disabled to the given Timestamp<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_DISABLED_DATE</span>
	 * @param upDisabledDate date and time when the user profile became disabled due to too many failed login attempts since the most recent successful login
	 */
	public void setUpDisabledDate(Timestamp upDisabledDate) {
		System.out.println("com.yardi.ejb.model.Update_Temp_Password.setUpDisabledDate() 0002");
		this.upDisabledDate = upDisabledDate;
	}   
	
	/**
	 * Set number of failed login attempts since the most recent successful login to the given short<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_PWD_ATTEMPTS</span>
	 * @param upPwdAttempts number of failed login attempts since the most recent successful login
	 */
	public void setUpPwdAttempts(short upPwdAttempts) {
		System.out.println("com.yardi.ejb.model.Update_Temp_Password.setUpPwdAttempts() 0003");
		this.upPwdAttempts = upPwdAttempts;
	}

	/**
	 * Set password expiration date and time to the given LocalDateTime<p> 
	 * If a temporary password was assigned by the help desk, the password expires in minutes<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_PWDEXPD</span>
	 * @param ldt password expiration date and time
	 */
	public void setUpPwdexpd(LocalDateTime ldt) {
		System.out.println("com.yardi.ejb.model.Update_Temp_Password.setUpPwdexpd() 0004");
		this.upPwdexpd = Timestamp.valueOf(ldt);
	}   
	
	/**
	 * Set password expiration date and time to the given Timestamp<p> 
	 * If a temporary password was assigned by the help desk, the password expires in minutes<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_PWDEXPD</span>
	 * @param upPwdexpd password expiration date and time
	 */
	public void setUpPwdexpd(Timestamp upPwdexpd) {
		System.out.println("com.yardi.ejb.model.Update_Temp_Password.setUpPwdexpd() 0005");
		this.upPwdexpd = upPwdexpd;
	}   
	
	/**
	 * Set temporary password to the given String<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_TEMP_PWD</span>
	 * @param upTempPwd temporary password
	 */
	public void setUpTempPwd(String upTempPwd) {
		System.out.println("com.yardi.ejb.model.Update_Temp_Password.setUpTempPwd() 0006");
		this.upTempPwd = upTempPwd;
	}

	/**
	 * Set user ID to the given String<p>
	 * <span style="font-family:consolas;">Database table: USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column:         UP_USERID</span>
	 * @param upUserid user ID
	 */
	public void setUpUserid(String upUserid) {
		System.out.println("com.yardi.ejb.model.Update_Temp_Password.setUpUserid() 0007");
		this.upUserid = upUserid;
	}  
}
