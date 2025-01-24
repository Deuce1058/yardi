package com.yardi.ejb.model;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.*;

/**
 * Entity implementation class for entity: Full_User_Profile.<p>
 * Accesses all columns<br>
 * 
 * <pre>
 * Database table: USER_PROFILE
 * Schema:         DB2ADMIN
 * </pre>
 *
 */
@Entity

@Table(name="USER_PROFILE", schema="DB2ADMIN")
@NamedQuery(name="User_Profile.findAll", query="SELECT u FROM User_Profile u")
public class Full_User_Profile implements Serializable {
	/**
	 * Serial version ID
	 */
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
	@Column(name="UP_PWDEXPD")
	private Timestamp upPwdexpd;

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

	/**
	 * Default constructor 
	 */
	public Full_User_Profile() {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile() 0000");
		/*debug*/
	}

	/**
	 * Construct a Full_User_Profile using all fields.
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

	/**
	 * Column:<span style="font-family:consolas;"> UP_ACTIVE_YN</span><p>
	 * Return user profile active flag. Y for active. N for inactive. User cant login unless this column is Y. Only an admin can set this column to Y. 
	 * @return user profile active flag
	 */
	public String getUpActiveYn() {
		return this.upActiveYn;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_ADDRESS1</span>
	 * @return User address line <span style="font-family:consolas;">1</span>
	 */
	public String getUpAddress1() {
		return this.upAddress1;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_ADDRESS2</span>
	 * @return User address line <span style="font-family:consolas;">2</span>
	 */
	public String getUpAddress2() {
		return this.upAddress2;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_CITY</span>
	 * @return User's city
	 */
	public String getUpCity() {
		return this.upCity;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_DISABLED_DATE</span><p>
	 * Return date and time when the user profile became disabled due to too many invalid password attempts since the last successful login.
	 * @return date and time when the user profile became disabled
	 */
	public java.sql.Timestamp getUpDisabledDate() {
		return this.upDisabledDate;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_DISABLED_YN</span>
	 * @return user profile disabled indicator 
	 */
	public String getUpDisabledYn() {
		return this.upDisabledYn;
	}

	/**
	 * Column: <span style="font-family:consolas;">UPDOB</span>
	 * @return User's birth date 
	 */
	public java.util.Date getUpdob() {
		return this.updob;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_EMAIL</span>
	 * @return Email address
	 */
	public String getUpEmail() {
		return this.upEmail;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_FAX</span>
	 * @return Fax number
	 */
	public String getUpFax() {
		return this.upFax;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_FIRST_NAME</span>
	 * @return User's first name.
	 */
	public String getUpFirstName() {
		return this.upFirstName;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_HOME_MARKET</span>
	 * @return Home market.
	 */
	public short getUpHomeMarket() {
		return this.upHomeMarket;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_LAST_LOGIN_DATE</span>
	 * @return Date and time of last successful login.
	 */
	public java.sql.Timestamp getUpLastLoginDate() {
		return this.upLastLoginDate;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_LAST_NAME</span>
	 * @return User's last name.
	 */
	public String getUpLastName() {
		return this.upLastName;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_PHONE</span>
	 * @return User's phone number
	 */
	public String getUpPhone() {
		return this.upPhone;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_PWD_ATTEMPTS</span>
	 * @return Number of invalid password attempts since the last successful login. 
	 */
	public short getUpPwdAttempts() {
		return this.upPwdAttempts;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_PWDEXPD</span>
	 * The date and time when the password must be changed.
	 * @return Password expiration Timestamp. 
	 */
	public Timestamp getUpPwdexpd() {
		return this.upPwdexpd;
	}

	/**
	 * Column: <span style="font-family:consolas;">UPRRN</span><pre>
	 * <span style="font-family:consolas;">YARDISEQ</span>          Sequence table
	 * <span style="font-family:consolas;">DB2ADMIN</span>          Sequence table schema
	 * <span style="font-family:consolas;">SEQNAME</span>           Sequence table primary key column
	 * <span style="font-family:consolas;">userProfileSeq</span>    Sequence table primary key value
	 * <span style="font-family:consolas;">SEQVALUE</span>          Column that stores the last value generated</pre>
	 * 
	 * @return Sequence column.
	 */
	public long getUprrn() {
		return this.uprrn;
	}

	/**
	 * Column: <span style="font-family:consolas;">UPSSN</span>
	 * @return User's SSN
	 */
	public String getUpssn() {
		return this.upssn;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_STATE</span>
	 * @return User's state
	 */
	public String getUpState() {
		return this.upState;
	}

	/**
	 * Column: <span style="font-family:consolas;">UPTOKEN</span>
	 * @return The user's hashed password.
	 */
	public String getUptoken() {
		return this.uptoken;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_USERID</span>
	 * @return ID column 
	 */
	public String getUpUserid() {
		return this.upUserid;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_ZIP</span>
	 * @return User's zip code
	 */
	public String getUpZip() {
		return this.upZip;
	}

	/**
	 * Column: <span style="font-family:times;">UP_ZIP4</span>
	 * @return 4 digit zip code extension 
	 */
	public String getUpZip4() {
		return this.upZip4;
	}

	/**
	 * Column:<span style="font-family:consolas;"> UP_ACTIVE_YN</span><p>
	 * Set user profile active flag to the given String. Y for active. N for inactive. User cant login unless this column is Y. Only an admin can set this column to Y. 
	 * @param upActiveYn user profile active flag value to set
	 */
	public void setUpActiveYn(String upActiveYn) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpActiveYn() 0002 ");
		/*debug*/
		this.upActiveYn = upActiveYn;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_ADDRESS1</span><p>
	 * Set user address line <span style="font-family:consolas;">1</span> to the given string.
	 * @param upAddress1 User address line <span style="font-family:consolas;">1</span> value to set 
	 */
	public void setUpAddress1(String upAddress1) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpAddress1() 0003 ");
		/*debug*/
		this.upAddress1 = upAddress1;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_ADDRESS2</span><p>
	 * Set user address line <span style="font-family:consolas;">2</span> to the given string.
	 * @param upAddress2 User address line <span style="font-family:consolas;">2</span> value to set
	 */
	public void setUpAddress2(String upAddress2) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpAddress2() 0004 ");
		/*debug*/
		this.upAddress2 = upAddress2;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_CITY</span><p>
	 * Set User's city to the given String.
	 * @param upCity User's city value to set
	 */
	public void setUpCity(String upCity) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpCity() 0005 ");
		/*debug*/
		this.upCity = upCity;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_DISABLED_DATE</span><p>
	 * Set date and time when the user profile became disabled due to too many invalid password attempts since the last successful login.
	 * @param upDisabledDate date and time when the user profile became disabled
	 */
	public void setUpDisabledDate(java.util.Date upDisabledDate) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpDisabledDate() 0006 ");
		/*debug*/
		this.upDisabledDate.setTime(upDisabledDate.getTime()); 
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_DISABLED_YN</span><p>
	 * Set user profile disabled indicator to the given String 
	 * @param upDisabledYn user profile disabled indicator value to set
	 */
	public void setUpDisabledYn(String upDisabledYn) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpDisabledYn() 0007 ");
		/*debug*/
		this.upDisabledYn = upDisabledYn;
	}

	/**
	 * Column: <span style="font-family:consolas;">UPDOB</span><p>
	 * Set user's birth date to the given Date 
	 * @param updob user's birth date to set
	 */
	public void setUpdob(java.util.Date updob) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpdob() 0008 ");
		/*debug*/
		this.updob = updob;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_EMAIL</span><p>
	 * Set Email address to the given String
	 * @param upEmail Email address value to set
	 */
	public void setUpEmail(String upEmail) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpEmail() 0009 ");
		/*debug*/
		this.upEmail = upEmail;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_FAX</span><p>
	 * Set fax number to the given String
	 * @param upFax fax number value to set
	 */
	public void setUpFax(String upFax) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpFax() 000A ");
		/*debug*/
		this.upFax = upFax;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_FIRST_NAME</span><p>
	 * Set user's first name to the given String
	 * @param upFirstName user's first name value to set
	 */
	public void setUpFirstName(String upFirstName) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpFirstName() 000B ");
		/*debug*/
		this.upFirstName = upFirstName;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_HOME_MARKET</span><p>
	 * Set home market to the given short
	 * @param upHomeMarket home market value to set
	 */
	public void setUpHomeMarket(short upHomeMarket) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpHomeMarket() 000C ");
		/*debug*/
		this.upHomeMarket = upHomeMarket;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_LAST_LOGIN_DATE</span><p>
	 * Set date and time of last successful login to the given Date
	 * @param upLastLoginDate date and time of last successful login
	 */
	public void setUpLastLoginDate(java.util.Date upLastLoginDate) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpLastLoginDate() 000D ");
		/*debug*/
		this.upLastLoginDate.setTime(upLastLoginDate.getTime()); 
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_LAST_NAME</span><p>
	 * Set user's last name to the given String
	 * @param upLastName last name value to set
	 */
	public void setUpLastName(String upLastName) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpLastName() 000E ");
		/*debug*/
		this.upLastName = upLastName;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_PHONE</span><p>
	 * Set user's phone number to the given String
	 * @param upPhone phone number value to set
	 */
	public void setUpPhone(String upPhone) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpPhone() 000F ");
		/*debug*/
		this.upPhone = upPhone;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_PWD_ATTEMPTS</span><p>
	 * Set number of invalid password attempts since the last successful login to the given short
	 * @param upPwdAttempts password attempts value to set
	 */
	public void setUpPwdAttempts(short upPwdAttempts) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpPwdAttempts() 0010 ");
		/*debug*/
		this.upPwdAttempts = upPwdAttempts;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_PWDEXPD</span><p>
	 * Set password expiration date and time when the password must be changed to the given Timestamp 
	 * @param upPwdexpd expiration Timestamp to set
	 */
	public void setUpPwdexpd(Timestamp upPwdexpd) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpPwdexpd() 0011 ");
		/*debug*/
		this.upPwdexpd = upPwdexpd;
	}

	/**
	 * Column: <span style="font-family:consolas;">UPRRN</span><p>
	 * Set the sequence column to the given long.<br><br>
	 * <pre>
	 * YARDISEQ          Sequence table
	 * DB2ADMIN          Sequence table schema 
	 * SEQNAME           Sequence table primary key column
	 * userProfileSeq    Sequence table primary key value
	 * SEQVALUE          Column that stores the last value generated
	 * </pre>
	 * @param uprrn sequence value to set
	 */
	public void setUprrn(long uprrn) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUprrn() 0012 ");
		/*debug*/
		this.uprrn = uprrn;
	}

	/**
	 * Column: <span style="font-family:consolas;">UPSSN</span><p>
	 * Set social security number to the given String 
	 * @param upssn social security number value to set
	 */
	public void setUpssn(String upssn) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpssn() 0013 ");
		/*debug*/
		this.upssn = upssn;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_STATE</span><p>
	 * Set user's state to the given String
	 * @param upState state value to set
	 */
	public void setUpState(String upState) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpState() 0014 ");
		/*debug*/
		this.upState = upState;
	}

	/**
	 * Column: <span style="font-family:consolas;">UPTOKEN</span><p>
	 * Set the user's hashed password to the given String
	 * @param uptoken the token value to set
	 */
	public void setUptoken(String uptoken) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUptoken() 0015 ");
		/*debug*/
		this.uptoken = uptoken;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_USERID</span><p>
	 * Set ID column to the given String 
	 * @param upUserid user ID value to set
	 */
	public void setUpUserid(String upUserid) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpUserid() 0016 ");
		/*debug*/
		this.upUserid = upUserid;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_ZIP</span><p>
	 * Set user's zip code to the given String
	 * @param upZip zip code value to set
	 */
	public void setUpZip(String upZip) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_User_Profile.setUpZip() 0017 ");
		/*debug*/
		this.upZip = upZip;
	}

	/**
	 * Column: <span style="font-family:consolas;">UP_ZIP4</span><p>
	 * Set <span style="font-family:times;">Zip 4</span> value to the given String
	 * @param upZip4 <span style="font-family:times;">Zip 4</span> value to set
	 */
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
