package com.yardi.ejb.model;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * Entity implementation class for Entity: Pwd_Policy.<p>
 *
 * Password policy is a set of rules established by the admin which control how a user logs in and changes their password. There is a single instance of 
 * password policy that is shared with clients. A client uses com.yardi.ejb.PasswordPolicyBean.getPwdPolicy() to get a reference to password policy.
 * 
 * <pre>
 * Database table: PWD_POLICY
 * Schema: DB2ADMIN
 * </pre>
 */
@Entity
@Table(name="PWD_POLICY", schema="DB2ADMIN")
@NamedQuery(name="Pwd_Policy.findAll", query="SELECT p FROM Pwd_Policy p")
public class Pwd_Policy implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Column: PP_DAYS<p>
	 * 
	 * Password life in days.
	 */
	@Column(name="PP_DAYS")
	private short ppDays;

	/**
	 * Column: PP_NBR_UNIQUE<p>
	 * 
	 * Enforce unique tokens. Enter a positive value to define the number of unique tokens to store per user. Enter zero if not enforcing 
	 * unique tokens. 
	 */
	@Column(name="PP_NBR_UNIQUE")
	private short ppNbrUnique;

	/**
	 * Column: PP_MAX_SIGNON_ATTEMPTS<p>
	 * 
	 * The allowed number of invalid password attempts since the most recent successful login. When the number of unsuccessful login attempts reaches 
	 * this number, the user profile entity is disabled and the user cant login until an admin resets the password. Enter a value greater than zero.
	 */
	@Column(name="PP_MAX_SIGNON_ATTEMPTS")
	private short ppMaxSignonAttempts;

	/**
	 * Column: PP_PWD_MIN_LEN<P>
	 * 
	 * The minimum password length. Enter a value greater than zero to define the minimum password length.
	 */
	@Column(name="PP_PWD_MIN_LEN")
	private short ppPwdMinLen;
	
	/**
	 * Boolean representation of field <i>pp_upper_rqd</i> provided for convenience.<p> Field <i>ppUpperRqd</i> is tested to determine whether at 
	 * least one upper case character is required in the new password. Not stored in PWD_POLICY database table.
	 */
	@Transient private Boolean ppUpperRqd; 
	
	/**
	 * Column: PP_UPPER_RQD<p>
	 * 
	 * Determines whether upper case characters are required in the new password. The y/n value of this field has a Boolean representation, <i>ppUpperRqd</i>, 
	 * that is provided for convenience.
	 */
	@Column(name = "PP_UPPER_RQD")
	private String pp_upper_rqd;
	
	/**
	 * Boolean representation of field <i>pp_lower_rqd</i> provided for convenience.<p> Field <i>ppLowerRqd</i> is tested to determine whether 
	 * at least one lower case character is required in the new password. Not stored in PWD_POLICY database table.
	 */
	@Transient private Boolean ppLowerRqd;
	
	/**
	 * Column: PP_LOWER_RQD<p>
	 * 
	 * Determines whether lower case characters are required in the new password. The y/n value of this field has a Boolean representation, <i>ppLowerRqd</i>,
	 * that is provided for convenience.
	 */
	@Column(name = "PP_LOWER_RQD")
	private String pp_lower_rqd;
	
	/**
	 * Boolean representation of field <i>pp_number_rqd</i> provided for convenience.<p> Field <i>ppNumberRqd</i> is tested to determine whether at least 
	 * one number is required in the new password. Not stored in PWD_POLICY database table.
	 */
	@Transient private Boolean ppNumberRqd; 

	/**
	 * Column: PP_NUMBER_RQD<p>
	 * 
	 * Indicates that at least one number is required in the new password.<p>
	 * The y/n value of this field has a Boolean representation, <i>ppNumberRqd</i>, that is provided for convenience.
	 */
	@Column(name = "PP_NUMBER_RQD")
	private String pp_number_rqd;
	
	/**
	 * Boolean representation of field <i>pp_special_rqd</i> provided for convenience.<p> Field <i>ppSpecialRqd</i> is tested to determine whether 
	 * at least one special character is required in the new password. Not stored in PWD_POLICY database table.
	 */
	@Transient private Boolean ppSpecialRqd;
	
	/**
	 * Column: PP_SPECIAL_RQD<p>
	 * 
	 * Determines whether special characters are required in the new password. The y/n value of this field has a Boolean representation, <i>ppSpecialRqd</i>,
	 * that is provided for convenience.
	 */
	@Column(name = "PP_SPECIAL_RQD")
	private String pp_special_rqd;

	/**
	 * Column: PP_MAX_PWD_LEN<p>
	 * 
	 * Maximum length of new password. Set to <i>null</i> if this rule is not being enforced.
	 */
	@Column(name="PP_MAX_PWD_LEN")
	private Short ppMaxPwdLen;
	
	/**
	 * Column: PP_MAX_REPEAT_CHAR<p>
	 * 
	 * The maximum number of repeated characters allowed in the new password. Set to <i>null</i> if this rule is not being enforced.
	 */
	@Column(name="PP_MAX_REPEAT_CHAR")
	private Short ppMaxRepeatChar;

	/**
	 * Column: PP_NBR_DIGITS<p>
	 * 
	 * If digits are required in the new password, then new passwords must contain at least the number of digits defined by field <i>ppNbrDigits</i>.<p>
	 * Set to <i>null</i> if this rule is not being enforced.
	 */
	@Column(name="PP_NBR_DIGITS")
	private Short ppNbrDigits;
	
	/**
	 * Column: PP_NBR_UPPER<p>
	 * 
	 * If upper case characters are required in the new password then new passwords must contain at least the number of upper case characters defined 
	 * by field <i>ppNbrUpper</i>. Set to <i>null</i> if this rule is not being enforced.
	 */
	@Column(name="PP_NBR_UPPER")
	private Short ppNbrUpper;
	
	/**
	 * Column: PP_NBR_LOWER<p>
	 * 
	 * If lower case characters are required in the new password then new passwords must contain at least the number of lower case characters defined 
	 * by field <i>ppNbrLower</i>. Set to <i>null</i> if this rule is not being enforced.
	 */
	@Column(name="PP_NBR_LOWER")
	private Short ppNbrLower;
	
	/**
	 * Column: PP_NBR_SPECIAL<p>
	 * 
	 * If special characters are required in the new password then the new password must contain at least the number of special characters defined by 
	 * field <i>ppNbrSpecial</i>. Set to <i>null</i> if this rule is not being enforced.
	 */
	@Column(name="PP_NBR_SPECIAL")
	private Short ppNbrSpecial;
	
	/**
	 * Column: PP_CANT_CONTAIN_ID<p>
	 * 
	 * Determines whether the new password may contain the user ID in any case. The y/n value of this field has a Boolean representation, <i>ppCantContainId</i>,
	 * that is provided for convenience.
	 */
	@Column(name="PP_CANT_CONTAIN_ID")
	private String pp_cant_contain_id;
	
	/**
	 * Boolean representation of field <i>pp_cant_contain_id</i> provided for convenience. Field <i>ppCantContainId</i> is tested to determine whether 
	 * the new password may contain the user ID in any case. Not stored in PWD_POLICY database table.
	 */
	@Transient private boolean ppCantContainId; 

	/**
	 * Column: PP_CANT_CONTAIN_PWD<p>
	 * 
	 * Determines whether the new password may contain the current password. The y/n value of this field has a Boolean representation, <i>ppCantContainPwd</i>,
	 * that is provided for convenience.
	 */
	@Column(name="PP_CANT_CONTAIN_PWD")
	private String pp_cant_contain_pwd;
	
	/**
	 * Boolean representation of field <i>pp_cant_contain_pwd</i> provided for convenience. Field <i>ppCantContainPwd</i> is tested to determine whether 
	 * the new password may contain the current password. Not stored in PWD_POLICY database table.
	 */
	@Transient private boolean ppCantContainPwd;
		
	/**
	 * Column: PP_RRN<p>
	 * 
	 * Primary key. Relative record number. Sequence is generated by the database. 
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PP_RRN")
	private Long ppRrn;

	public Pwd_Policy() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy Pwd_Policy() 000A ");
		//debug
	}

	/**
	 * Constructor for constructing a Pwd_Policy instance that uses all columns of PWD_POLICY database table except PP_RRN.
	 * @param ppDays Password life in days
	 * @param ppNbrUnique Enforce unique tokens
	 * @param ppMaxSignonAttempts The allowed number of invalid password attempts since the most recent successful login
	 * @param ppPwdMinLen The minimum password length
	 * @param pp_upper_rqd Determines whether upper case characters are required in the new password
	 * @param pp_lower_rqd Determines whether lower case characters are required in the new password
	 * @param pp_number_rqd Determines whether digits are required in the new password
	 * @param pp_special_rqd Determines whether special characters are required in the new password
	 * @param ppMaxPwdLen Maximum length of new password
	 * @param ppMaxRepeatChar The maximum number of repeated characters allowed in the new password
	 * @param ppNbrDigits Defines the minimum number of digits required in the new password
	 * @param ppNbrUpper Defines the minimum number of upper case characters required in the new password
	 * @param ppNbrLower Defines the minimum number of lower case characters required in the new password
	 * @param ppNbrSpecial Defines the minimum number of special characters required in the new password
	 * @param pp_cant_contain_id Determines whether the new password may contain the user ID in any case
	 * @param pp_cant_contain_pwd Determines whether the new password may contain the current password
	 */
	public Pwd_Policy(short ppDays, short ppNbrUnique, short ppMaxSignonAttempts, short ppPwdMinLen, 
			String pp_upper_rqd, String pp_lower_rqd, String pp_number_rqd,
			String pp_special_rqd, short ppMaxPwdLen, short ppMaxRepeatChar, short ppNbrDigits,
			short ppNbrUpper, short ppNbrLower, short ppNbrSpecial, String pp_cant_contain_id, String pp_cant_contain_pwd
			) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy Pwd_Policy() 0000 ");
		//debug
		this.ppDays = ppDays;
		this.ppNbrUnique = ppNbrUnique;
		this.ppMaxSignonAttempts = ppMaxSignonAttempts;
		this.ppPwdMinLen = ppPwdMinLen;
		setPp_upper_rqd(pp_upper_rqd);
		setPp_lower_rqd(pp_lower_rqd);
		setPp_number_rqd(pp_number_rqd);
		setPp_special_rqd(pp_special_rqd);
		this.ppMaxPwdLen = ppMaxPwdLen;
		this.ppMaxRepeatChar = ppMaxRepeatChar;
		this.ppNbrDigits = ppNbrDigits;
		this.ppNbrUpper = ppNbrUpper;
		this.ppNbrLower = ppNbrLower;
		this.ppNbrSpecial = ppNbrSpecial;
		setPp_cant_contain_id(pp_cant_contain_id);
		setPp_cant_contain_pwd(pp_cant_contain_pwd);
	}
	
	/**
	 * Return password cant contain user ID indicator
	 * @return value of password cant contain user ID
	 */
	public String getPp_cant_contain_id() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPp_cant_contain_id() 002F ");
		//debug
		return pp_cant_contain_id;
	}

	/**
	 * Return password cant contain current password indicator
	 * @return value of password cant contain current password
	 */
	public String getPp_cant_contain_pwd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPp_cant_contain_pwd() 0037 ");
		//debug
		return pp_cant_contain_pwd;
	}

	/**
	 * Return password must contain at least one lower case character indicator
	 * @return value of password must contain lower case
	 */
	public String getPp_lower_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPp_lower_rqd() 0016 ");
		//debug
		return pp_lower_rqd;
	}

	/**
	 * Return password must contain at least one number indicator
	 * @return password must contain at least one number
	 */
	public String getPp_number_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPp_number_rqd() 0019 ");
		//debug
		return pp_number_rqd;
	}

	/**
	 * Return password must contain at least one special character indicator<p>
	 * @return value of password must contain special character
	 */
	public String getPp_special_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPp_special_rqd() 001C ");
		//debug
		return pp_special_rqd;
	}

	/**
	 * Return password must contain at least one upper case character indicator<p>
	 * @return value of password must have upper case character
	 */
	public String getPp_upper_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPp_upper_rqd() 0013 ");
		//debug
		return pp_upper_rqd;
	}

	/**
	 * Return password life in days
	 * @return value of password life in days
	 */
	public short getPpDays() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpDays() 000D ");
		//debug
		return this.ppDays;
	}

	/**
	 * Return the rule for password must have at least one lower case character
	 * @return value of password must have lower case character
	 */
	public Boolean getPpLowerRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpLowerRqd() 0004 ");
		//debug
		return ppLowerRqd;
	}

	/**
	 * Return maximum password length
	 * @return value of maximum password length
	 */
	public Short getPpMaxPwdLen() {
		return ppMaxPwdLen;
	}

	/**
	 * Return maximum number of repeated characters allowed in the password 
	 * @return value of maximum number of repeated characters allowed
	 */
	public Short getPpMaxRepeatChar() {
		return ppMaxRepeatChar;
	}

	/**
	 * Return number of invalid login attempts allowed since the most recent successful login
	 * @return number of invalid login attempts allowed
	 */
	public short getPpMaxSignonAttempts() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpMaxSignonAttempts() 000F ");
		//debug
		return this.ppMaxSignonAttempts;
	}

	/**
	 * Return number of digits required in the password  
	 * @return number of digits required
	 */
	public Short getPpNbrDigits() {
		return ppNbrDigits;
	}

	/**
	 * Return number of lower case characters required in the password
	 * @return number of lower case characters required 
	 */
	public Short getPpNbrLower() {
		return ppNbrLower;
	}

	/**
	 * Return number of special characters required in the password
	 * @return number of special characters required
	 */
	public Short getPpNbrSpecial() {
		return ppNbrSpecial;
	}

	/**
	 * Return number of unique tokens to store per user
	 * @return number of unique tokens to store
	 */
	public short getPpNbrUnique() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpNbrUnique() 0011 ");
		//debug
		return this.ppNbrUnique;
	}

	/**
	 * Return number of upper case characters required in the password 
	 * @return number of upper case characters required
	 */
	public Short getPpNbrUpper() {
		return ppNbrUpper;
	}

	/**
	 * Return the rule for password must have at least one number 
	 * @return rule for password must have at least one number 
	 */
	public Boolean getPpNumberRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpNumberRqd() 0006 ");
		//debug
		return ppNumberRqd;
	}

	/**
	 * Return minimum password length 
	 * @return minimum password length value
	 */
	public short getPpPwdMinLen() {
		//debug 
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpPwdMinLen() 0000 ");
		//debug
		return ppPwdMinLen;
	}

	/**
	 * Return the relative record number
	 * @return relative record number value
	 */
	public long getPpRrn() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpRrn() 000B ");
		//debug
		return this.ppRrn;
	}

	/**
	 * Return the rule for at least one special character is required in the password  
	 * @return Boolean indicating whether special character required
	 */
	public Boolean getPpSpecialRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpSpecialRqd() 0008 ");
		//debug
		return ppSpecialRqd;
	}

	/**
	 * Return the rule for at least one upper case character is required in the password 
	 * @return Boolean indicating whether upper case is required
	 */
	public Boolean getPpUpperRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpUpperRqd() 0002 ");
		//debug
		return ppUpperRqd;
	}

	/**
	 * Return the rule for password cant contain user ID
	 * @return password cant contain user ID
	 */
	public boolean isPpCantContainId() {
		return ppCantContainId;
	}

	/**
	 * Return the rule for new password cant contain current password.
	 * @return new password cant contain current password
	 */
	public boolean isPpCantContainPwd() {
		return ppCantContainPwd;
	}

	/**
	 * Password cant contain user ID is set based on the value of field <i>ppCantContainId</i>.<p> 
	 * Set field <i>pp_cant_contain_id</i> to "y" if <i>ppCantContainId</i> is true.<br> 
	 * Set field <i>pp_cant_contain_id</i> to "n" if <i>ppCantContainId</i> is false.
	 */
	void setPp_cant_contain_id() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_id() 0030 ");
		//debug
		if (ppCantContainId) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_id() 0031 ");
			//debug
			pp_cant_contain_id = "y";
		} else {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_id() 0032 ");
			//debug
			pp_cant_contain_id = "n";
		}
	}

	/**
	 * Password cant contain user ID is set to the given String.<p>
	 * Also call setter for <i>ppCantContainId</i> because these two fields are closely related.
	 * @param pp_cant_contain_id the value to set
	 */
	public void setPp_cant_contain_id(String pp_cant_contain_id) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_id() 0033 ");
		//debug
		this.pp_cant_contain_id = pp_cant_contain_id;
		setPpCantContainId();	
	}

	/**
	 * New password cant contain current password is set based on the value of field <i>ppCantContainPwd</i>.<p> 
	 * Set field <i>pp_cant_contain_pwd</i> to "y" if <i>ppCantContainPwd</i> is true.<br> 
	 * Set field <i>pp_cant_contain_pwd</i> to "n" if <i>ppCantContainPwd</i> is false.
	 */
	void setPp_cant_contain_pwd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_pwd() 0038 ");
		//debug
		if (ppCantContainPwd) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_pwd() 0039 ");
			//debug
			pp_cant_contain_pwd = "y";
		} else {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_pwd() 003A ");
			//debug
			pp_cant_contain_pwd = "n";
		}
	}

	/**
	 * New password cant contain current password is set to the given String.<p>
	 * Also call setter for <i>ppCantContainPwd</i> because these two fields are closely related.
	 * @param pp_cant_contain_pwd the value to set
	 */
	public void setPp_cant_contain_pwd(String pp_cant_contain_pwd) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_pwd() 003B ");
		//debug
		this.pp_cant_contain_pwd = pp_cant_contain_pwd;
		setPpCantContainPwd();	
	}

	/**
	 * Lower case character required is set based on the value of field <i>ppLowerRqd</i>.<p>
	 * Set field <i>pp_lower_rqd</i> to "y" if <i>ppLowerRqd</i> is true.<br> 
	 * Set field <i>pp_lower_rqd</i> to "n" if <i>ppLowerRqd</i> is false.
	 */
	void setPp_lower_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_lower_rqd() 0018 ");
		//debug
		if (ppLowerRqd) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_lower_rqd() 0029 ");
			//debug
			pp_lower_rqd = "y";
		} else {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_lower_rqd() 002A ");
			//debug
			pp_lower_rqd = "n";
		}
	}

	/**
	 * Lower case character required is set to the given String.<p>
	 * Also call setter for <i>ppLowerRqd</i> because these two fields are closely related.
	 * @param pp_lower_rqd the value to set
	 */
	public void setPp_lower_rqd(String pp_lower_rqd) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_lower_rqd() 0017 ");
		//debug
		this.pp_lower_rqd = pp_lower_rqd;
		setPpLowerRqd();	
	}

	/**
	 * At lease one number is required is set based on the value of field <i>ppNumberRqd</i>.<p> 
	 * Set field <i>pp_number_rqd</i> to "y" if <i>ppNumberRqd</i> is true.<br> 
	 * Set field <i>pp_number_rqd</i> to "n" if <i>ppNumberRqd</i> is false.
	 */
	void setPp_number_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_number_rqd() 001B ");
		//debug
		if (ppNumberRqd) 
			{
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_number_rqd() 002B ");
			//debug
			pp_number_rqd = "y";} 
		else
			{
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_number_rqd() 002C ");
			//debug
			pp_number_rqd = "n";} 
	}

	/**
	 * At least one number is required is set to the given String.<p>
	 * Also call setter for <i>ppNumberRqd</i> because these two fields are closely related.
	 * @param pp_number_rqd the value to set
	 */
	public void setPp_number_rqd(String pp_number_rqd) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_number_rqd() 001A ");
		//debug
		this.pp_number_rqd = pp_number_rqd;
		setPpNumberRqd();
	}

	/**
	 * Special character required is set based on the value of field <i>ppSpecialRqd</i>.<p> 
	 * Set field <i>pp_special_rqd</i> to "y" if <i>ppSpecialRqd</i> is true.<br> 
	 * Set field <i>pp_special_rqd</i> to "n" if <i>ppSpecialRqd</i> is false.
	 */
	void setPp_special_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_special_rqd() 001E ");
		//debug
		if (ppSpecialRqd) 
			{
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_special_rqd() 002D ");
			//debug
			pp_special_rqd = "y";} 
		else 
			{
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_special_rqd() 002E ");
			//debug
			pp_special_rqd = "n";}
	}

	/**
	 * Special character required is set to the given String.<p>
	 * Also call setter for <i>ppSpecialRqd</i> because these two fields are closely related.
	 * @param pp_special_rqd the value to set
	 */
	public void setPp_special_rqd(String pp_special_rqd) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_special_rqd() 001D ");
		//debug
		this.pp_special_rqd = pp_special_rqd;
		setPpSpecialRqd();
	}
	
	/**
	 * Upper case character required is set based on the value of field <i>ppUpperRqd</i>.<p> 
	 * Set field <i>pp_upper_rqd</i> to "y" if <i>ppUpperRqd</i> is true.<br> 
	 * Set field <i>pp_upper_rqd</i> to "n" if <i>ppUpperRqd</i> is false.
	 */
	void setPp_upper_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_upper_rqd() 0014 ");
		//debug
		if (ppUpperRqd) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_upper_rqd() 0027 ");
			//debug
			pp_upper_rqd = "y";} 
		else {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_upper_rqd() 0028 ");
			//debug
			pp_upper_rqd = "n";}
	}
	
	/**
	 * Upper case character required is set to the given String.<p>
	 * Also call setter for <i>ppUpperRqd</i> because these two fields are closely related.
	 * @param pp_upper_rqd the value to set
	 */
	public void setPp_upper_rqd(String pp_upper_rqd) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_upper_rqd() 0015 ");
		//debug
		this.pp_upper_rqd = pp_upper_rqd;
		setPpUpperRqd();
	}

	/**
	 * Password cant contain user ID is set based on the value of field <i>pp_cant_contain_id</i>.<p> 
	 * Enforces password cant contain user ID.<br>
	 * Set field <i>ppCantContainId</i> to true  if <i>pp_cant_contain_id</i> is "y".<br> 
	 * Set field <i>ppCantContainId</i> to false if <i>pp_cant_contain_id</i> is "n".
	 */
	void setPpCantContainId() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpCantContainId() 0034 ");
		//debug
		ppCantContainId = false;
		if (pp_cant_contain_id.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpCantContainId() 0035 ");
			//debug
			ppCantContainId = true;
		}
		if (pp_cant_contain_id.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpCantContainId() 0036 ");
			//debug
			ppCantContainId = false;
		}
	}

	/**
	 * New password cant contain current password is set based on the value of field <i>pp_cant_contain_pwd</i>.<p>
	 * Enforces new password cant contain current password.<br>
	 * Set field <i>ppCantContainPwd</i> to true  if <i>pp_cant_contain_pwd</i> is "y".<br> 
	 * Set field <i>ppCantContainPwd</i> to false if <i>pp_cant_contain_pwd</i> is "n".
	 */
	void setPpCantContainPwd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpCantContainPwd() 003C  ");
		//debug
		ppCantContainPwd = false;
		if (pp_cant_contain_pwd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpCantContainPwd() 003D ");
			//debug
			ppCantContainPwd = true;
		}
		if (pp_cant_contain_pwd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpCantContainPwd() 003E ");
			//debug
			ppCantContainPwd = false;
		}
	}

	/**
	 * Set password life in days to the given short. 
	 * @param ppDays the value to set
	 */
	public void setPpDays(short ppDays) {
		//debug 
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpDays() 000E ");
		//debug
		this.ppDays = ppDays;
	}
	
	/**
	 * Lower case character required is set based on the value of field <i>pp_lower_rqd</i>.<p> 
	 * Enforces at least one lower case character required.<br>
	 * Set field <i>ppLowerRqd</i> to true  if <i>pp_lower_rqd</i> is "y".<br> 
	 * Set field <i>ppLowerRqd</i> to false if <i>pp_lower_rqd</i> is "n".
	 */
	void setPpLowerRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpLowerRqd() 0005 ");
		//debug
		ppLowerRqd = false;
		if (pp_lower_rqd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpLowerRqd() 0021 ");
			//debug
			ppLowerRqd = true;
		}
		if (pp_lower_rqd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpLowerRqd() 0022 ");
			//debug
			ppLowerRqd = false;
		}
	}

	/**
	 * Maximum password length is set to the given short.
	 * @param ppMaxPwdLen the value to set
	 */
	public void setPpMaxPwdLen(short ppMaxPwdLen) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpMaxPwdLen() 0040 ");
		//debug
		this.ppMaxPwdLen = ppMaxPwdLen;
	}

	/**
	 * If required, set maximum password length to <i>null</i> to indicate that the rule is not enforced.<p> Setting a value by using a parm that 
	 * has a <i>null</i> value causes NullPointerException.
	 */
	public void setPpMaxPwdLenNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpMaxPwdLenNull() 0041 ");
		//debug
		ppMaxPwdLen = null;
	}

	/** 
	 * Maximum repeated characters is set to the given Short.
	 * @param ppMaxRepeatChar the value to set
	 */
	public void setPpMaxRepeatChar(Short ppMaxRepeatChar) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpMaxRepeatChar() 0042 ");
		//debug
		this.ppMaxRepeatChar = ppMaxRepeatChar;
	}

	/**
	 * If required, set maximum repeated characters to <i>null</i> to indicate that the rule is not enforced.<p> Setting a value by using a parm that 
	 * has a <i>null</i> value causes NullPointerException.
	 */
	public void setPpMaxRepeatCharNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpMaxRepeatCharNull() 0043 ");
		//debug
		ppMaxRepeatChar = null;
	}
	
	/**
	 * Maximum number of invalid login attempts since the most recent successful login is set to the given short.
	 * @param ppMaxSignonAttempts the value to set
	 */
	public void setPpMaxSignonAttempts(short ppMaxSignonAttempts) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpMaxSignonAttempts() 0010 ");
		//debug
		this.ppMaxSignonAttempts = ppMaxSignonAttempts;
	}
	
	/**
	 * Number of digits required is set to the given short.
	 * @param ppNbrDigits the value to set
	 */
	public void setPpNbrDigits(short ppNbrDigits) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrDigits() 0044 ");
		//debug
		this.ppNbrDigits = ppNbrDigits;
	}

	/**
	 * If required, set number of digits required to <i>null</i> to indicate that the rule is not enforced.<p> Setting a value by using a parm that 
	 * has a <i>null</i> value causes NullPointerException.
	 */
	public void setPpNbrDigitsNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrDigitsNull() 0045 ");
		//debug
		ppNbrDigits = null;
	}

	/**
	 * Number of lower case characters required is set to the given Short.
	 * @param ppNbrLower the value to set
	 */
	public void setPpNbrLower(Short ppNbrLower) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrLower() 0048 ");
		//debug
		this.ppNbrLower = ppNbrLower;
	}

	/**
	 * If required, set number of lower case characters required to <i>null</i> to indicate that the rule is not enforced.<p> Setting a value by using a parm that 
	 * has a <i>null</i> value causes NullPointerException.
	 */
	public void setPpNbrLowerNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrLowerNull() 0049 ");
		//debug
		ppNbrLower = null;
	}

	/**
	 * Number of special characters required is set to the given Short.
	 * @param ppNbrSpecial the value to set
	 */
	public void setPpNbrSpecial(Short ppNbrSpecial) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrSpecial() 004A ");
		//debug
		this.ppNbrSpecial = ppNbrSpecial;
	}

	/**
	 * If required, set number of special characters required to <i>null</i> to indicate that the rule is not enforced.<p> Setting a value by using a parm that 
	 * has a <i>null</i> value causes NullPointerException.
	 */
	public void setPpNbrSpecialNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrSpecialNull() 004B ");
		//debug
		ppNbrSpecial = null;
	}
	
	/**
	 * Number of unique tokens to store per user is set to the given Short.<p> Enforces unique passwords.
	 * @param ppNbrUnique the value to set
	 */
	public void setPpNbrUnique(short ppNbrUnique) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrUnique() 0012 ");
		//debug
		this.ppNbrUnique = ppNbrUnique;
	}

	/**
	 * Number of upper case characters required is set to the given short.
	 * @param ppNbrUpper the value to set
	 */
	public void setPpNbrUpper(short ppNbrUpper) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrUpper() 0046 ");
		//debug
		this.ppNbrUpper = ppNbrUpper;
	}

	/**
	 * If required, set number of upper case characters required to <i>null</i> to indicate that the rule is not enforced.<p> Setting a value by using a parm that 
	 * has a <i>null</i> value causes NullPointerException.
	 */
	public void setPpNbrUpperNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrUpperNull() 0047 ");
		//debug
		ppNbrUpper = null;
	}
	
	/**
	 * At least one number is required is set based on the value of field <i>pp_number_rqd</i>.<p> 
	 * Enforces number required.<br>
	 * Set field <i>ppNumberRqd</i> to true  if <i>pp_number_rqd</i> is "y".<br> 
	 * Set field <i>ppNumberRqd</i> to false if <i>pp_number_rqd</i> is "n".
	 */
	void setPpNumberRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNumberRqd() 0007 ");
		//debug
		ppNumberRqd = false;
		if (pp_number_rqd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNumberRqd() 0023 ");
			//debug
			ppNumberRqd = true;
		}
		if (pp_number_rqd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNumberRqd() 0024 ");
			//debug
			ppNumberRqd = false;
		}
	}
	
	/**
	 * The minimum password length is set to the given short.
	 * @param ppPwdMinLen the value to set
	 */
	public void setPpPwdMinLen(short ppPwdMinLen) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpPwdMinLen() 0001 ");
		//debug
		this.ppPwdMinLen = ppPwdMinLen;
	}

	/**
	 * The relative record number (sequence) is set to the given long. 
	 * @param ppRrn the value to set
	 */
	public void setPpRrn(long ppRrn) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpRrn() 000C ");
		//debug
		this.ppRrn = ppRrn;
	}

	/**
	 * The relative record number (sequence) is set to null.<p>
	 * Setting a value by using a parm that has a <i>null</i> value causes NullPointerException.
	 */
	public void setPpRrnNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpRrnNull() 003F ");
		//debug
		ppRrn = null;
	}
	
	/**
	 * At least one special character is required is set based on the value of field <i>pp_special_rqd</i>.<p> 
	 * Enforces at least one special character required.<br>
	 * Set field <i>ppSpecialRqd</i> to true  if <i>pp_special_rqd</i> is "y".<br> 
	 * Set field <i>ppSpecialRqd</i> to false if <i>pp_special_rqd</i> is "n".
	 */
	void setPpSpecialRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpSpecialRqd() 0009 ");
		//debug
		ppSpecialRqd = false;
		if (pp_special_rqd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpSpecialRqd() 0025 ");
			//debug
			ppSpecialRqd = true;
		}
		if (pp_special_rqd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpSpecialRqd() 0026 ");
			//debug
			ppSpecialRqd = false;
		}
	}

	/**
	 * At least one upper case character is required is set based on the value of field <i>pp_upper_rqd</i>.<p> 
	 * Enforces at least one upper case character required.<br>
	 * Set field <i>ppUpperRqd</i> to true  if <i>pp_upper_rqd</i> is "y".<br> 
	 * Set field <i>ppUpperRqd</i> to false if <i>pp_upper_rqd</i> is "n".
	 */
	void setPpUpperRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpUpperRqd() 0003 ");
		//debug
		ppUpperRqd = false;
		if (pp_upper_rqd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpUpperRqd() 001F ");
			//debug
			ppUpperRqd = true;
		}
		if (pp_upper_rqd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpUpperRqd() 0020 ");
			//debug
			ppUpperRqd = false;
		}
	}

	@Override
	public String toString() {
		return "Pwd_Policy [ppDays=" + ppDays + ", ppNbrUnique=" + ppNbrUnique + ", ppMaxSignonAttempts=" + ppMaxSignonAttempts 
				+ ", ppPwdMinLen=" + ppPwdMinLen + ", pp_upper_rqd=" + pp_upper_rqd + ", pp_lower_rqd=" + pp_lower_rqd 
				+ ", pp_number_rqd=" + pp_number_rqd + ", pp_special_rqd=" + pp_special_rqd	+ ", ppUpperRqd=" + ppUpperRqd 
				+ ", ppLowerRqd=" + ppLowerRqd + ", ppNumberRqd=" + ppNumberRqd + ", ppSpecialRqd=" + ppSpecialRqd 
				+ ", ppPwdMaxLen=" + ppMaxPwdLen + ", ppMaxRepeatChar=" + ppMaxRepeatChar + ", ppNbrDigits=" + ppNbrDigits 
				+ ", ppNbrUpper=" + ppNbrUpper + ", ppNbrLower=" + ppNbrLower + ", ppNbrSpecial=" + ppNbrSpecial 
				+ ", ppCantContainID=" + ppCantContainId + ", ppCantContainPwd=" + ppCantContainPwd
				+ ", ppRrn=" + ppRrn 
				+ "]";
	}
}
