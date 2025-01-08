package com.yardi.shared.QSECOFR;

/**
 * Container which holds all of the password policy elements that can be edited. This class serves as the data transfer object between the web and the application.
 * @see com.yardi.ejb.model.Pwd_Policy com.yardi.ejb.model.Pwd_Policy
 */
public class EditPwdPolicyRequest {
	/**
	 * The action to perform on the password policy: find, add or update.
	 */
	private String action;
	/**
	 * Determines whether the new password may contain the user ID in any case. 
	 */
	private String cantContainId;
	/**
	 * Determines whether the new password may contain the current password.
	 */
	private String cantContainPwd;
	/**
	 * Determines whether at least one lower case character is required in the new password. 
	 */
	private String lowerRqd;
	/**
	 * Maximum length of new password. Set to <i>null</i> if this rule is not being enforced.
	 */
	private String maxPwdLen;
	/**
	 * The maximum number of repeated characters allowed in the new password. Set to <i>null</i> if this rule is not being enforced.
	 */
	private String maxRepeatChar;
	/**
	 * The allowed number of invalid password attempts since the most recent successful login. When the number of unsuccessful login attempts reaches 
	 * this number, the user profile entity is disabled and the user cant login until an admin resets the password. Enter a value greater than zero.
	 */
	private String maxSignonAttempts;
	/**
	 * Message description 
	 */
	private String msgDescription;
	/**
	 * Message identifier
	 */
	private String msgId;
	/**
	 * If digits are required in the new password, then new passwords must contain at least the number of digits defined by this field.
	 * Set to <i>null</i> if this rule is not being enforced.
	 */
	private String nbrDigits;
	/**
	 * If lower case characters are required in the new password then new passwords must contain at least the number of lower case characters defined 
	 * by this field. Set to <i>null</i> if this rule is not being enforced.
	 */
	private String nbrLower;
	/**
	 * Determine whether at least one digit is required in the new password.
	 */
	private String nbrRqd;
	/**
	 * If special characters are required in the new password then the new password must contain at least the number of special characters defined by 
	 * this field. Set to <i>null</i> if this rule is not being enforced.
	 */
	private String nbrSpecial;
	/**
	 * Enforce unique tokens. Enter a positive value to define the number of unique tokens to store per user. Enter zero if not enforcing 
	 * unique tokens. Used to prevent password reuse.
	 */
	private String nbrUnique;
	/**
	 * If upper case characters are required in the new password then new passwords must contain at least the number of upper case characters defined 
	 * by this field. Set to <i>null</i> if this rule is not being enforced.
	 */
	private String nbrUpper;
	/**
	 * Password life in days
	 */
	private String pwdLifeInDays;
	/**
	 * The minimum password length. Enter a value greater than zero to define the minimum password length.
	 */
	private String pwdMinLen;
	/**
	 * Determines whether at least one special character is required in the new password.
	 */
	private String specialRqd;
	/**
	 * Temporary password life in minutes
	 */
	private String temporaryPwdLifeMins;
	/**
	 * Determines whether at least one upper case character is required in the new password. 
	 */
	private String upperRqd;

	/**
	 * Default constructor
	 */
	public EditPwdPolicyRequest() {
	}

	/**
	 * Constructor using all fields.
	 * @param action Action to perform: find, add or update.
	 * @param msgId Message identifier
	 * @param msgDescription Message description
	 * @param pwdLifeInDays Password life in days
	 * @param temporaryPwdLifeMins temporary password life in minutes 
	 * @param nbrUnique Number of unique tokens to store per user
	 * @param maxSignonAttempts The allowed number of invalid password attempts since the most recent successful login
	 * @param pwdMinLen Minimum password length
	 * @param upperRqd Determines whether at least one upper case character is required
	 * @param lowerRqd Determines whether at least one lower case character is required
	 * @param nbrRqd Determines whether at least one digit is required
	 * @param specialRqd Determines whether at least one special character is required
	 * @param maxPwdLen Maximum length of new password
	 * @param maxRepeatChar Maximum number of repeated characters allowed in the new password
	 * @param nbrDigits New passwords must contain the number of digits defined by this field
	 * @param nbrUpper Number of upper case characters required
	 * @param nbrLower Number of lower case characters required
	 * @param nbrSpecial Number of special characters required
	 * @param cantContainId Determines whether the new password may contain the user ID
	 * @param cantContainPwd Determines whether the new password may contain the current password
	 */
	public EditPwdPolicyRequest(String action, String msgId, String msgDescription, String pwdLifeInDays, String temporaryPwdLifeMins,
			String nbrUnique, String maxSignonAttempts, String pwdMinLen, String upperRqd, String lowerRqd,
			String nbrRqd, String specialRqd, String maxPwdLen, String maxRepeatChar, String nbrDigits, String nbrUpper,
			String nbrLower, String nbrSpecial, String cantContainId, String cantContainPwd) {
		this.action = action;
		this.msgId = msgId;
		this.msgDescription = msgDescription;
		this.pwdLifeInDays = pwdLifeInDays;
		this.temporaryPwdLifeMins = temporaryPwdLifeMins;
		this.nbrUnique = nbrUnique;
		this.maxSignonAttempts = maxSignonAttempts;
		this.pwdMinLen = pwdMinLen;
		this.upperRqd = upperRqd;
		this.lowerRqd = lowerRqd;
		this.nbrRqd = nbrRqd;
		this.specialRqd = specialRqd;
		this.maxPwdLen = maxPwdLen;
		this.maxRepeatChar = maxRepeatChar;
		this.nbrDigits = nbrDigits;
		this.nbrUpper = nbrUpper;
		this.nbrLower = nbrLower;
		this.nbrSpecial = nbrSpecial;
		this.cantContainId = cantContainId;
		this.cantContainPwd = cantContainPwd;
	}

	/**
	 * Return action to perform: find, add or update
	 * @return Action to perform: find, add or update.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Return new password cant contain the user ID value.
	 * @return New password cant contain the user ID value. 
	 */
	public String getCantContainId() {
		return cantContainId;
	}

	/**
	 * Return new password cant contain the current password value.
	 * @return New password cant contain the current password value.
	 */
	public String getCantContainPwd() {
		return cantContainPwd;
	}

	/**
	 * Return value of lower case characters are required in the new password.
	 * @return Value of lower case characters are required in the new password. 
	 */
	public String getLowerRqd() {
		return lowerRqd;
	}

	/**
	 * Return max length of new password.
	 * @return Max length of new password.
	 */
	public String getMaxPwdLen() {
		return maxPwdLen;
	}

	/**
	 * Return max number of repeated characters in the new password.
	 * @return Max number of repeated characters in the new password.
	 */
	public String getMaxRepeatChar() {
		return maxRepeatChar;
	}

	/**
	 * Return max number of invalid signon attempts since the most recent successful login.
	 * @return Max number of invalid signon attempts since the most recent successful login.
	 */
	public String getMaxSignonAttempts() {
		return maxSignonAttempts;
	}

	/**
	 * Return message description.
	 * @return Message description.
	 */
	public String getMsgDescription() {
		return msgDescription;
	}

	/**
     * Return message description.
	 * @return Message ID
	 */
	public String getMsgId() {
		return msgId;
	}

	/**
	 * Return minimum number of digits required in the new password.
	 * @return Minimum number of digits required in the new password.
	 */
	public String getNbrDigits() {
		return nbrDigits;
	}

	/**
	 * Return minimum number of lower case characters required in the new password.
	 * @return Minimum number of lower case characters required in the new password.
	 */
	public String getNbrLower() {
		return nbrLower;
	}

	/**
	 * Return value of whether digits are required in the new password.
	 * @return Value of whether digits are required in the new password.
	 */
	public String getNbrRqd() {
		return nbrRqd;
	}

	/**
	 * Return minimum number of digits required in the new password.
	 * @return Minimum number of digits required in the new password.
	 */
	public String getNbrSpecial() {
		return nbrSpecial;
	}

	/**
	 * Return number of unique tokens to store per user. Controls password reuse.
	 * @return Number of unique tokens to store per user. Controls password reuse. 
	 */
	public String getNbrUnique() {
		return nbrUnique;
	}

	/**
	 * Return minimum number of upper case characters required in the new password.
	 * @return Minimum number of upper case characters required in the new password.
	 */
	public String getNbrUpper() {
		return nbrUpper;
	}

	/**
	 * Return password life in days.
	 * @return Password life in days.
	 */
	public String getPwdLifeInDays() {
		return pwdLifeInDays;
	}

	/**
	 * Return minimum password length
	 * @return Minimum password length
	 */
	public String getPwdMinLen() {
		return pwdMinLen;
	}

	/**
	 * Return minimum number of special characters required in the new password.
	 * @return Minimum number of special characters required in the new password.
	 */
	public String getSpecialRqd() {
		return specialRqd;
	}

	/** 
	 * Return temporary password life in minutes
	 * @return temporary password life in minutes
	 */
	public String getTemporaryPwdLifeMins() {
		return temporaryPwdLifeMins;
	}

	/**
	 * Return value of upper case required in new password rule.
	 * @return Value of upper case required in new password rule. 
	 */
	public String getUpperRqd() {
		return upperRqd;
	}

	/**
	 * Set action to perform to the value of the given String.
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Set password cant contain user ID rule to the value of the given String.
	 * @param cantContainId password cant contain user ID value
	 */
	public void setCantContainId(String cantContainId) {
		this.cantContainId = cantContainId;
	}

	/**
	 * Set new password cant contain current password to the value of the given String  
	 * @param cantContainPwd new password cant contain current password value
	 */
	public void setCantContainPwd(String cantContainPwd) {
		this.cantContainPwd = cantContainPwd;
	}

	/**
	 * Set at least one lower case character required to the value of the given String
	 * @param lowerRqd lower case required value
	 */
	public void setLowerRqd(String lowerRqd) {
		this.lowerRqd = lowerRqd;
	}

	/**
	 * Set maximum password length to the value of the given String
	 * @param maxPwdLen maximum password length value
	 */
	public void setMaxPwdLen(String maxPwdLen) {
		this.maxPwdLen = maxPwdLen;
	}

	/**
	 * Set maximum number of repeated characters value to the given String
	 * @param maxRepeatChar max repeated characters value
	 */
	public void setMaxRepeatChar(String maxRepeatChar) {
		this.maxRepeatChar = maxRepeatChar;
	}

	/**
	 * Set maximum number of signon attempts since the most recent successful login value to the given String
	 * @param maxSignonAttempts max number of signon attempts value
	 */
	public void setMaxSignonAttempts(String maxSignonAttempts) {
		this.maxSignonAttempts = maxSignonAttempts;
	}

	/**
	 * Set message description to the value of the given String
	 * @param msgDescription message description value
	 */
	public void setMsgDescription(String msgDescription) {
		this.msgDescription = msgDescription;
	}

	/**
	 * Set message ID value to the given String
	 * @param msgId message ID value
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	/**
	 * Set number of digits required in the new password to the given String
	 * @param nbrDigits number of digits required value
	 */
	public void setNbrDigits(String nbrDigits) {
		this.nbrDigits = nbrDigits;
	}
	
	/**
	 * Set number of lower case characters required in the new password value to the given String
	 * @param nbrLower number of lower case characters required value
	 */
	public void setNbrLower(String nbrLower) {
		this.nbrLower = nbrLower;
	}

	/**
	 * Set at least one digit is required in the new password value the given String
	 * @param nbrRqd at least one digit required value
	 */
	public void setNbrRqd(String nbrRqd) {
		this.nbrRqd = nbrRqd;
	}

	/**
	 * Set number of special characters required in the new password value to the given String
	 * @param nbrSpecial number of special characters required value
	 */
	public void setNbrSpecial(String nbrSpecial) {
		this.nbrSpecial = nbrSpecial;
	}

	/**
	 * Set number of unique tokens to store per user value to the given String
	 * @param nbrUnique number of unique tokens to store per user value
	 */
	public void setNbrUnique(String nbrUnique) {
		this.nbrUnique = nbrUnique;
	}

	/**
	 * Set number of upper case characters required in the new password value to the given String
	 * @param nbrUpper number of upper case characters required value
	 */
	public void setNbrUpper(String nbrUpper) {
		this.nbrUpper = nbrUpper;
	}

	/**
	 * Set password life in days value to the given String
	 * @param pwdLifeInDays password life in days value
	 */
	public void setPwdLifeInDays(String pwdLifeInDays) {
		this.pwdLifeInDays = pwdLifeInDays;
	}

	/**
	 * Set minimum password length value to the given String
	 * @param pwdMinLen minimum password length value
	 */
	public void setPwdMinLen(String pwdMinLen) {
		this.pwdMinLen = pwdMinLen;
	}

	/**
	 * Set at least one special character required in the new password value to the given String 
	 * @param specialRqd special character required value
	 */
	public void setSpecialRqd(String specialRqd) {
		this.specialRqd = specialRqd;
	}

	/**
	 * Set temporary password life in minutes to the given String
	 * @param temporaryPwdLifeMins temporary password life in minutes
	 */
	public void setTemporaryPwdLifeMins(String temporaryPwdLifeMins) {
		this.temporaryPwdLifeMins = temporaryPwdLifeMins;
	}

	/**
	 * Set at least one upper case character required in the new password value to the given String
	 * @param upperRqd at least one upper case character required value
	 */
	public void setUpperRqd(String upperRqd) {
		this.upperRqd = upperRqd;
	}

	@Override
	public String toString() {
		return "EditPwdPolicyRequest [action=" + action + ", cantContainId=" + cantContainId + ", cantContainPwd="
				+ cantContainPwd + ", lowerRqd=" + lowerRqd + ", maxPwdLen=" + maxPwdLen + ", maxRepeatChar="
				+ maxRepeatChar + ", maxSignonAttempts=" + maxSignonAttempts + ", msgDescription=" + msgDescription
				+ ", msgId=" + msgId + ", nbrDigits=" + nbrDigits + ", nbrLower=" + nbrLower + ", nbrRqd=" + nbrRqd
				+ ", nbrSpecial=" + nbrSpecial + ", nbrUnique=" + nbrUnique + ", nbrUpper=" + nbrUpper
				+ ", pwdLifeInDays=" + pwdLifeInDays + ", pwdMinLen=" + pwdMinLen + ", specialRqd=" + specialRqd
				+ ", temporaryPwdLifeMins=" + temporaryPwdLifeMins + ", upperRqd=" + upperRqd + "]";
	}
}
