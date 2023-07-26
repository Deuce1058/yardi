package com.yardi.ejb.model;

import java.io.Serializable;
import jakarta.persistence.*;

/**
 * Entity implementation class for entity: Full_User_Profile.<p>
 * Accesses all columns<br>
 * 
 * <pre>
 * Database table: USER_PROFILE
 * Schema: DB2ADMIN
 * </pre>
 *
 */
@Entity

@Table(name="USER_PROFILE", schema="DB2ADMIN")
@NamedQuery(name="User_Profile.findAll", query="SELECT u FROM User_Profile u")
public class Full_User_Profile implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Column: UP_USERID<p>
	 * ID column 
	 */
	@Id
	@Column(name="UP_USERID")
	private String upUserid;

	/**
	 * Column: UPTOKEN<p>
	 * The user's hashed password.
	 */
	@Column(name="UPTOKEN")
	private String uptoken;

	/**
	 * Column: UP_HOME_MARKET<p>
	 * Home market.
	 */
	@Column(name="UP_HOME_MARKET")
	private short upHomeMarket;

	/**
	 * Column: UP_FIRST_NAME<p>
	 * User's first name.
	 */
	@Column(name="UP_FIRST_NAME")
	private String upFirstName;

	/**
	 * Column: UP_LAST_NAME<p>
	 * User's last name.
	 */
	@Column(name="UP_LAST_NAME")
	private String upLastName;

	/**
	 * Column: <span style="font-family:times;">UP_ADDRESS1</span><p>
	 * <span style="font-family:times;">User address line 1</span>
	 */
	@Column(name="UP_ADDRESS1")
	private String upAddress1;

	/**
	 * Column: <span style="font-family:times;">UP_ADDRESS2</span><p>
	 * <span style="font-family:times;">User address line 2</span>
	 */
	@Column(name="UP_ADDRESS2")
	private String upAddress2;

	/**
	 * Column: UP_CITY<p>
	 * User's city
	 */
	@Column(name="UP_CITY")
	private String upCity;

	/**
	 * Column: UP_STATE<p>
	 * User's state
	 */
	@Column(name="UP_STATE")
	private String upState;

	/**
	 * Column: UP_ZIP<p>
	 * User's zip code
	 */
	@Column(name="UP_ZIP")
	private String upZip;

	/**
	 * Column: <span style="font-family:times;">UP_ZIP4</span><p>
	 * <span style="font-family:times;">Zip 4</span>
	 */
	@Column(name="UP_ZIP4")
	private String upZip4;

	/**
	 * Column: UP_PHONE<p>
	 * User's phone number
	 */
	@Column(name="UP_PHONE")
	private String upPhone;

	/**
	 * Column: UP_FAX<p>
	 * Fax number
	 */
	@Column(name="UP_FAX")
	private String upFax;

	/**
	 * Column: UP_EMAIL<p>
	 * Email address
	 */
	@Column(name="UP_EMAIL")
	private String upEmail;

	/**
	 * Column: UPSSN<p>
	 * User's SSN
	 */
	@Column(name="UPSSN")
	private String upssn;

	/**
	 * Column: UPDOB<p>
	 * User's birth date
	 */
	// TemporalType.DATE maps a java.util.Date to a java.sql.Date
	@Temporal(TemporalType.DATE) 
	@Column(name="UPDOB")
	private java.util.Date updob;
 
	/**
	 * Column: UP_ACTIVE_YN<p>
	 * User profile active flag. Y for active. N for inactive. User cant login unless this column is Y. Only an admin can set this column to Y. 
	 */
	@Column(name="UP_ACTIVE_YN")
	private String upActiveYn;

	/**
	 * Column: UP_PWDEXPD<p>
	 * Password expiration date. The date on which the password must be changed.
	 */
	// TemporalType.DATE maps a java.util.Date to a java.sql.Date
	@Temporal(TemporalType.DATE)
	@Column(name="UP_PWDEXPD")
	private java.util.Date upPwdexpd;

	/**
	 * NOT USED
	 */
	@Column(name="UP_DISABLED_YN")
	private String upDisabledYn;

	/**
	 * Column: UP_DISABLED_DATE<p>
	 * Date and time when the user profile became disabled due to too many invalid password attempts since the last successful login. 
	 */
	// TemporalType.TIMESTAMP maps a java.util.Date to a java.sql.Timestamp 
	@Column(name="UP_DISABLED_DATE")
	private java.sql.Timestamp upDisabledDate;

	/**
	 * Column: UP_LAST_LOGIN_DATE<p>
	 * Date and time of last successful login.
	 */
	// TemporalType.TIMESTAMP maps a java.util.Date to a java.sql.Timestamp 
	@Column(name="UP_LAST_LOGIN_DATE")
	private java.sql.Timestamp upLastLoginDate;

	/**
	 * Column: UP_PWD_ATTEMPTS<p>
	 * Number of invalid password attempts since the last successful login. 
	 */
	@Column(name="UP_PWD_ATTEMPTS")
	private short upPwdAttempts;

	/**
	 * Column: UPRRN<p>
	 * Sequence column.<br><br>
	 * <pre>
	 * YARDISEQ          Sequence table
	 * DB2ADMIN          Sequence table schema 
	 * SEQNAME           Sequence table primary key column
	 * userProfileSeq    Sequence table primary key value
	 * SEQVALUE          Column that stores the last value generated
	 * </pre>
	 */
	@TableGenerator(name="userProfileSeq",
			schema="DB2ADMIN",
			table="YARDISEQ",
			pkColumnName="SEQNAME",
			valueColumnName="SEQVALUE")
	@GeneratedValue(generator="userProfileSeq")
	@Column(name="UPRRN")
	private long uprrn;

	public Full_User_Profile() {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile() 0000");
		/*debug*/
	}

	/**
	 * Construct a Full_User_Profile using all fields.<p>
	 * @param upUserid user ID
	 * @param uptoken hashed password
	 * @param upHomeMarket home market
	 * @param upFirstName user's first name
	 * @param upLastName user's last name
	 * @param upAddress1 <span style="font-family:times;">user's address line 1</span>
	 * @param upAddress2 <span style="font-family:times;">user's address line 2</span>
	 * @param upCity user's city
	 * @param upState user's state
	 * @param upZip user's zip code
	 * @param upZip4 user's zip 4
	 * @param upPhone user's phone 
	 * @param upFax user's fax number
	 * @param upEmail email address
	 * @param upssn ssn
	 * @param updob user's birth date
	 * @param upActiveYn user profile active flag. Y is active. N is inactive. Must be Y to login.
	 * @param upPwdexpd password expiration date. Password must be changed on or after this date.
	 * @param upDisabledYn NOT USED
	 * @param upDisabledDate Timestamp of when the user profile was disabled due to too many invalid login attempts since the last successful login
	 * @param upLastLoginDate Timestamp of the most recent successful login
	 * @param upPwdAttempts number of invalid login attempts since the last successful login 
	 * @param uprrn sequence field
	 */
	public Full_User_Profile(
			String upUserid, 
			String uptoken,
			short upHomeMarket,
			String upFirstName,
			String upLastName,
			String upAddress1,
			String upAddress2,
			String upCity,
			String upState,
			String upZip,
			String upZip4,
			String upPhone,
			String upFax,
			String upEmail,
			String upssn,
			java.util.Date updob,
			String upActiveYn,
			java.util.Date upPwdexpd,
			String upDisabledYn,
			java.sql.Timestamp upDisabledDate,
			java.sql.Timestamp upLastLoginDate,
			short upPwdAttempts,
			long uprrn
			) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile() 0001 ");
		/*debug*/
		this.upUserid        = upUserid;
		this.uptoken         = uptoken;
		this.upHomeMarket    = upHomeMarket;
		this.upFirstName     = upFirstName;
		this.upLastName      = upLastName;
		this.upAddress1      = upAddress1;
		this.upAddress2      = upAddress2;
		this.upCity          = upCity;
		this.upState         = upState;
		this.upZip           = upZip;
		this.upZip4          = upZip4;
		this.upPhone         = upPhone;
		this.upFax           = upFax;
		this.upEmail         = upEmail;
		this.upssn           = upssn;
		this.updob           = updob;
		this.upActiveYn      = upActiveYn;
		this.upPwdexpd       = upPwdexpd;
		this.upDisabledYn    = upDisabledYn;
		this.upDisabledDate  = upDisabledDate; 
		this.upLastLoginDate = upLastLoginDate;
		this.upPwdAttempts   = upPwdAttempts;          this.uprrn = uprrn;
	}

	public String getUpActiveYn() {
		return this.upActiveYn;
	}

	public String getUpAddress1() {
		return this.upAddress1;
	}

	public String getUpAddress2() {
		return this.upAddress2;
	}

	public String getUpCity() {
		return this.upCity;
	}

	public java.sql.Timestamp getUpDisabledDate() {
		return this.upDisabledDate;
	}

	public String getUpDisabledYn() {
		return this.upDisabledYn;
	}

	public java.util.Date getUpdob() {
		return this.updob;
	}

	public String getUpEmail() {
		return this.upEmail;
	}

	public String getUpFax() {
		return this.upFax;
	}

	public String getUpFirstName() {
		return this.upFirstName;
	}

	public short getUpHomeMarket() {
		return this.upHomeMarket;
	}

	public java.sql.Timestamp getUpLastLoginDate() {
		return this.upLastLoginDate;
	}

	public String getUpLastName() {
		return this.upLastName;
	}

	public String getUpPhone() {
		return this.upPhone;
	}

	public short getUpPwdAttempts() {
		return this.upPwdAttempts;
	}

	public java.util.Date getUpPwdexpd() {
		return this.upPwdexpd;
	}

	public long getUprrn() {
		return this.uprrn;
	}

	public String getUpssn() {
		return this.upssn;
	}

	public String getUpState() {
		return this.upState;
	}

	public String getUptoken() {
		return this.uptoken;
	}

	public String getUpUserid() {
		return this.upUserid;
	}

	public String getUpZip() {
		return this.upZip;
	}

	public String getUpZip4() {
		return this.upZip4;
	}

	public void setUpActiveYn(String upActiveYn) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpActiveYn() 0002 ");
		/*debug*/
		this.upActiveYn = upActiveYn;
	}

	public void setUpAddress1(String upAddress1) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpAddress1() 0003 ");
		/*debug*/
		this.upAddress1 = upAddress1;
	}

	public void setUpAddress2(String upAddress2) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpAddress2() 0004 ");
		/*debug*/
		this.upAddress2 = upAddress2;
	}

	public void setUpCity(String upCity) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpCity() 0005 ");
		/*debug*/
		this.upCity = upCity;
	}

	public void setUpDisabledDate(java.util.Date upDisabledDate) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpDisabledDate() 0006 ");
		/*debug*/
		this.upDisabledDate.setTime(upDisabledDate.getTime()); 
	}

	public void setUpDisabledYn(String upDisabledYn) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpDisabledYn() 0007 ");
		/*debug*/
		this.upDisabledYn = upDisabledYn;
	}

	public void setUpdob(java.util.Date updob) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpdob() 0008 ");
		/*debug*/
		this.updob = updob;
	}

	public void setUpEmail(String upEmail) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpEmail() 0009 ");
		/*debug*/
		this.upEmail = upEmail;
	}

	public void setUpFax(String upFax) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpFax() 000A ");
		/*debug*/
		this.upFax = upFax;
	}

	public void setUpFirstName(String upFirstName) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpFirstName() 000B ");
		/*debug*/
		this.upFirstName = upFirstName;
	}

	public void setUpHomeMarket(short upHomeMarket) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpHomeMarket() 000C ");
		/*debug*/
		this.upHomeMarket = upHomeMarket;
	}

	public void setUpLastLoginDate(java.util.Date upLastLoginDate) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpLastLoginDate() 000D ");
		/*debug*/
		this.upLastLoginDate.setTime(upLastLoginDate.getTime()); 
	}

	public void setUpLastName(String upLastName) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpLastName() 000E ");
		/*debug*/
		this.upLastName = upLastName;
	}

	public void setUpPhone(String upPhone) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpPhone() 000F ");
		/*debug*/
		this.upPhone = upPhone;
	}

	public void setUpPwdAttempts(short upPwdAttempts) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpPwdAttempts() 0010 ");
		/*debug*/
		this.upPwdAttempts = upPwdAttempts;
	}

	public void setUpPwdexpd(java.util.Date upPwdexpd) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpPwdexpd() 0011 ");
		/*debug*/
		this.upPwdexpd = upPwdexpd;
	}

	public void setUprrn(long uprrn) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUprrn() 0012 ");
		/*debug*/
		this.uprrn = uprrn;
	}

	public void setUpssn(String upssn) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpssn() 0013 ");
		/*debug*/
		this.upssn = upssn;
	}

	public void setUpState(String upState) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpState() 0014 ");
		/*debug*/
		this.upState = upState;
	}

	public void setUptoken(String uptoken) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUptoken() 0015 ");
		/*debug*/
		this.uptoken = uptoken;
	}

	public void setUpUserid(String upUserid) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpUserid() 0016 ");
		/*debug*/
		this.upUserid = upUserid;
	}

	public void setUpZip(String upZip) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpZip() 0017 ");
		/*debug*/
		this.upZip = upZip;
	}

	public void setUpZip4(String upZip4) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpZip4() 0018 ");
		/*debug*/
		this.upZip4 = upZip4;
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
