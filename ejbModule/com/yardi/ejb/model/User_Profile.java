package com.yardi.ejb.model;
/*
 * 2020 1104
 * remove attribute for Sessions_Table. This is attribute is now defined in User_Groups
 * remove getUgSessionTable() 
 * added bidirectional OneToMany association with User_Groups
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
 * Entity implementation class for Entity: User_Profile.<br><br>
 * 
 * <pre>Database table: USER_PROFILE
 *Schema: DB2ADMIN</pre>
 *
 * Only columns needed for login are included.
 */
@Entity
@Table(name="USER_PROFILE", schema="DB2ADMIN")
public class User_Profile implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Column: UP_USERID User ID primary key 
	 */
	@Id
	@Column(name="UP_USERID")
	private String upUserid;

	/**
	 * Column: UPTOKEN Hashed password 
	 */
	@Column(name="UPTOKEN")
	private String uptoken;

	/**
	 * Column: UP_PWDEXPD<p>
	 * Password expiration date. Password must be changed on or after this date. 
	 */
	@Temporal(TemporalType.DATE)
	@Column(name="UP_PWDEXPD")
	private java.util.Date upPwdexpd;

	/**
	 * Column: UP_PWD_ATTEMPTS<p>
	 * The number of invalid password attempts since the most recent successful login 
	 */
	@Column(name="UP_PWD_ATTEMPTS")
	private short upPwdAttempts;

	/**
	 * Column: UP_DISABLED_DATE<p>
	 * Date and time when the User_Profile entity was disabled due to too many invalid login attempts since the most recent successful login  
	 */
	@Column(name="UP_DISABLED_DATE")
	private java.sql.Timestamp upDisabledDate;

	/**
	 * Column: UP_LAST_LOGIN_DATE<p>
	 * Date and time of the most recent successful login 
	 */
	@Column(name="UP_LAST_LOGIN_DATE")
	private java.sql.Timestamp upLastLoginDate;

	/**
	 * Column: UP_ACTIVE_YN<p> 
	 * Indicates whether the User_Profile entity is active. An admin must clear this flag before the user is able to login. 
	 */
	@Column(name="UP_ACTIVE_YN")
	private String upActiveYn;
	
	/**
	 * Reference to User_Groups entity.
	 * <pre>
	 * Database tables: USER_GROUPS
	 * Schema:          DB2ADMIN
	 * Mapped by:       ugUserProfile
	 * </pre> 
	 * 
	 * User_Profile has a bidirectional, one to many relationship with User_Groups entity. Field <i>upLoginUserGroups</i> defines the inverse side.
	 */
	@OneToMany(mappedBy = "ugUserProfile")
	private List<User_Groups> upLoginUserGroups;

	/**
	 * Return serial version
	 * @return serial version
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Return user profile active Y/N indicator
	 * @return user profile active Y/N indicator
	 */
	public String getUpActiveYn() {
		return upActiveYn;
	}

	/**
	 * Return the date and time when the User_Profile entity was disabled due to too many invalid login attempts since the most recent successful login.
	 * @return date and time when the User_Profile entity was disabled
	 */
	public java.sql.Timestamp getUpDisabledDate() {
		return upDisabledDate;
	}

	/**
	 * Return the date and time of the most recent successful login
	 * @return date and time of the most recent successful login
	 */
	public java.sql.Timestamp getUpLastLoginDate() {
		return upLastLoginDate;
	}

	/**
	 * Return the number of invalid password attempts since the most recent successful login 
	 * @return number of invalid password attempts since the most recent successful login
	 */
	public short getUpPwdAttempts() {
		return upPwdAttempts;
	}

	/**
	 * Return the password expiration date.<p>
	 * Password must be changed on or after this date. 
	 * @return password expiration date
	 */
	public java.util.Date getUpPwdexpd() {
		return upPwdexpd;
	}

	/**
	 * Return the user's hashed password
	 * @return hashed password
	 */
	public String getUptoken() {
		return uptoken;
	}

	/**
	 * Return the user ID
	 * @return user ID
	 */
	public String getUpUserid() {
		return upUserid;
	}

	/**
	 * Set the disabled date to the given Timestamp.<p>
	 * The date and time when the user profile became disabled due to too many invalid login attempts since the most recent successful login is set.
	 * @param upDisabledDate the date and time to set
	 */
	public void setUpDisabledDate(java.sql.Timestamp upDisabledDate) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.User_Profile.setUpDisabledDate() 0000");
		/*debug*/
		this.upDisabledDate = upDisabledDate;
	}

	/**
	 * The date and time of the most recent successful login is set to the given Timestamp.
	 * @param upLastLoginDate date and time of the most recent successful login
	 */
	public void setUpLastLoginDate(java.sql.Timestamp upLastLoginDate) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.User_Profile.setUpLastLoginDate() 0000");
		/*debug*/
		this.upLastLoginDate = upLastLoginDate;
	}

	/**
	 * The number of invalid login attempts since the most recent successful login are set to the given short.
	 * @param upPwdAttempts number of invalid login attempts
	 */
	public void setUpPwdAttempts(short upPwdAttempts) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.User_Profile.setUpPwdAttempts() 0000");
		/*debug*/
		this.upPwdAttempts = upPwdAttempts;
	}

	/**
	 * The password expiration date is set to the given Date.
	 * @param upPwdexpd password expiration date
	 */
	public void setUpPwdexpd(java.util.Date upPwdexpd) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.User_Profile.setUpPwdexpd() 0000");
		/*debug*/
		this.upPwdexpd = upPwdexpd;
	}

	/**
	 * The hashed password is set to the given String.
	 * @param uptoken hashed password
	 */
	public void setUptoken(String uptoken) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.User_Profile.setUptoken() 0000");
		/*debug*/
		this.uptoken = uptoken;
	}
}
