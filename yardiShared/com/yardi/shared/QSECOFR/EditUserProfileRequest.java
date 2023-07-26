package com.yardi.shared.QSECOFR;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date; 
import java.util.GregorianCalendar;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Represents the request to edit the user profile. 
 * @author Jim
 *
 */

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * Java representation of the web request to edit the user profile and the response to that request.<p>
 * 
 * The web request comes in as JSON and gets mapped to this class to make it easier to work with the request. When responding to the request, select 
 * fields are mapped to JSON to become the response. Fields annotated <i>@JsonIgnore</i> are not mapped in either the request or the response.
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class EditUserProfileRequest {
	/**
	 * The the type of edit being requested.<p>
	 * All edit requests initially start as a request to find the user profile. The outcome of the find request determines what edit that can be performed. 
	 */
	private String action;
	/**
	 * ID of the user profile to edit.<p> For presentation purposes.
	 */
	private String findUser;
	/**
	 * Message ID.<p> To the web.
	 */
	private String msgID;
	/**
	 * Message description.<p> To the web. 
	 */
	private String msgDescription;
	/**
	 * User profile first name.<p> From and to the web for presentation purposes. 
	 */
	private String firstName;
	/**
	 * User profile last name.<p> From and to the web for presentation purposes. 
	 */
	private String lastName;
	/**
	 * User profile address line <span style="font-family:times;">1</span>.<p> From and to the web for presentation purposes. 
	 */
	private String address1;
	/**
	 * User profile address line <span style="font-family:times;">2</span>.<p> From and to the web for presentation purposes. 
	 */
	private String address2;
	/**
	 * User profile city.<p> From and to the web for presentation purposes. 
	 */
	private String city;
	/** 
	 * User profile state.<p> From and to the web for presentation purposes. 
	 */
	private String state;
	/** 
	 * User profile zip.<p> From and to the web for presentation purposes. 
	 */
	private String zip;
	/**
	 * User profile zip <span style="font-family:times;">4</span>.<p> From and to the web for presentation purposes. 
	 */
	private String zip4;
	/**
	 * User profile phone.<p> From and to the web for presentation purposes.
	 */
	private String phone;
	/**
	 * User profile fax.<p> From and to the web for presentation purposes. 
	 */
	private String fax;
	/**
	 * User profile email address.<p> From and to the web for presentation purposes. 
	 */
	private String email;
	/**
	 * User profile ssn.<p> From and to the web for presentation purposes.
	 */
	private String ssn;
	/** 
	 * User profile birth date.<p> From and to the web for presentation purposes. Format is MM/DD/CCYY.
	 */
	private String dob;
	/**
	 * User profile home market.<p> From and to the web for presentation purposes.
	 */
	private String homeMarket;
	/**
	 * User profile active flag. Y is active. N is inactive.<p> From and to the web for presentation purposes.
	 */
	private String activeYN;
	/**
	 * User profile password expiration date.<p> The password must be changed on or after this date. From and to the web for presentation purposes.
	 * Format is MM/DD/CCYY.
	 */
	private String pwdExpDate;
	/**
	 * User profile time when the profile was disabled due to too many invalid password attempts since the last login.<p> 
	 * From and to the web for presentation purposes. Format is hh:mm:ss.
	 */
	private String disabledTime;
	/** 
	 * User profile date when the profile was disabled due to too many invalid password attempts since the last login.<p> 
	 * From and to the web for presentation purposes. Format is MM/DD/CCYY.
	 */
	private String disabledDate;
	/**
	 * User profile number of invalid password attempts since the last login.<p> From and to the web for presentation purposes.
	 */
	private String pwdAttempts;
	/**
	 * User profile hashed password.<p> From and to the web for presentation purposes.
	 */
	private String currentToken;
	/**
	 * User profile date of most recent successful login.<p> From and to the web for presentation purposes. Format is MM/DD/CCYY.
	 */
	private String lastLogin;
	/**
	 * User profile time of most recent successful login.<p> From and to the web for presentation purposes. Format is hh:mm:ss.
	 */
	private String lastLoginTime;
	/**
	 * <i>@JsonIgnore</i> Birth date<p>
	 * This field is not mapped to JSON or from JSON. A GregorianCalendar is constructed from the century, year, month and day components of field <i>dob</i>.
	 * <i>birthDate</i> is then set from the GregorianCalendar mills.
	 */
	@JsonIgnore
	private java.util.Date birthDate;
	/**
	 * <i>@JsonIgnore</i> Home market<p>
	 * This field is not mapped to JSON or from JSON. Field <i>homeMarket</i> is parsed to short to set the value for <i>upHomeMarket</i>.
	 */
	@JsonIgnore
	private short upHomeMarket;
	/**
	 * <i>@JsonIgnore</i> Password expiration date.<p>
	 * This field is not mapped to JSON or from JSON. The password must be changed on or after this date. A GregorianCalendar is constructed 
	 * from the century, year, month and day components of field <i>pwdExpDate</i>. <i>passwordExpirationDate</i> is then set from the GregorianCalendar mills.
	 */
	@JsonIgnore
	private java.util.Date passwordExpirationDate;
	/**
	 * <i>@JsonIgnore</i> User profile disabled Timestamp.<p>
	 * This field is not mapped to JSON or from JSON. The date and time when the profile was disabled due to too many invalid password attempts since the 
	 * last successful login. A GregorianCalendar is constructed using the century, year, month and day components of field <i>disabledDate</i>. The 
	 * GregorianCalendar time is set to the hour, minute and second components of field <i>disabledTime</i>. Field <i>profileDisabledDate</i> is set from 
	 * the GregorianCalendar mills.
	 */
	@JsonIgnore
	private java.sql.Timestamp profileDisabledDate;
	/**
	 * <i>@JsonIgnore</i> Date and time of the most recent successful login.<p>
	 * This field is not mapped to JSON or from JSON. A GregorianCalendar is constructed using the century, year, month and day components of 
	 * field <i>lastLogin</i>. The GregorianCalendar time is set to the hour, minute and second components of field <i>lastLoginTime</i>. Field 
	 * <i>lastLoginDate</i> is set from the GregorianCalendar mills.
	 */
	@JsonIgnore
	private java.sql.Timestamp lastLoginDate;
	/**
	 * <i>@JsonIgnore</i> Number of invalid password attempts since the most recent successful login.<p>
	 * This field is not mapped to JSON or from JSON. Field <i>pwdAttempts</i> is parsed to short to set the value for <i>passwordAttempts</i>.  
	 */
	@JsonIgnore
	private short passwordAttempts;
	
	public EditUserProfileRequest() {
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.EditUserProfileRequest()");
	}
	
	/**
	 * Constructor used when responding to the web request.<p>
	 * All parms set fields used for presentation purposes. To respond to the web request, values from Full_User_Profile entity are mapped to the 
	 * parms of this constructor which will set fields of the same name. The fields having the same name as these parms are then mapped to JSON before responding.   
	 * @param action the requested type of edit 
	 * @param findUser ID of the user profile to edit
	 * @param msgID message ID
	 * @param msgDescription message description
	 * @param firstName user profile first name
	 * @param lastName user profile last name
	 * @param address1 user profile address line <span style="font-family:times;">1</span>
	 * @param address2 user profile address line <span style="font-family:times;">2</span>
	 * @param city user profile city
	 * @param state user profile state
	 * @param zip user profile zip
	 * @param zip4 user profile zip<span style="font-family:times;">4</span>
	 * @param phone user profile phone number
	 * @param fax user profile fax number
	 * @param email user profile email address
	 * @param ssn user profile ssn
	 * @param dob user profile birth date
	 * @param homeMarket user profile home market
	 * @param activeYN indicates whether user profile is active. Y is active. N is inactive
	 * @param pwdExpDate user profile password expiration date. Password must be changed on or after this date. 
	 * @param disabledDate date on which the user profile was disabled due to too many invalid password attempts since the most recent successful login 
	 * @param disabledTime time when the user profile was disabled due to too many invalid password attempts since the most recent successful login
	 * @param pwdAttempts number of invalid password attempts since the most recent successful login
	 * @param currentToken hashed password
	 * @param lastLogin date of the most recent successful login
	 * @param lastLoginTime time of the most recent successful login
	 */
	public EditUserProfileRequest(String action, String findUser, String msgID, String msgDescription, String firstName,
			String lastName, String address1, String address2, String city, String state, String zip, String zip4,
			String phone, String fax, String email, String ssn, String dob, String homeMarket, String activeYN,
			String pwdExpDate, String disabledDate, String disabledTime, String pwdAttempts, String currentToken, 
			String lastLogin, String lastLoginTime) {
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.EditUserProfileRequest() 0008");
		this.action = action;
		this.findUser = findUser;
		this.msgID = msgID;
		this.msgDescription = msgDescription;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.zip4 = zip4;
		this.phone = phone;
		this.fax = fax;
		this.email = email;
		this.ssn = ssn;
		this.dob = dob;
		this.homeMarket = homeMarket;
		this.activeYN = activeYN;
		this.pwdExpDate = pwdExpDate;
		this.disabledDate = disabledDate;
		this.disabledTime = disabledTime;
		this.pwdAttempts = pwdAttempts;
		this.currentToken = currentToken;
		this.lastLogin = lastLogin;
		this.lastLoginTime = lastLoginTime;
		setUpHomeMarket(Short.parseShort(homeMarket));
		setBirthDate(toDate(dob, "", false));
		setPasswordExpirationDate(toDate(pwdExpDate, "", false));
		setProfileDisabledDate(toDate(disabledDate, disabledTime, true));
		setLastLoginDate(toDate(lastLogin, lastLoginTime, true));
		setPasswordAttempts(Short.parseShort(pwdAttempts));
	}

	/**
	 * Return the type of edit being requested.
	 * @return the type of edit being requested
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Return the active status of the user profile. Y is active. N is inactive. For presentation purposes.
	 * @return the active status of the user profile.
	 */
	public String getActiveYN() {
		return activeYN;
	}
	
	/**
	 * Return user profile address line <span style="font-family:times;">1</span> for presentation purposes.
	 * @return user profile address line <span style="font-family:times;">1</span>
	 */
	public String getAddress1() {
		return address1;
	}
	
	/**
	 * Return user profile address line <span style="font-family:times;">2</span> for presentation purposes.
	 * @return user profile address line <span style="font-family:times;">2</span>
	 */
	public String getAddress2() {
		return address2;
	}
	
	/**
	 * Return a Date which is used to set date of birth in the Full_User_Profile entity.
	 * @return the date of birth which is used to set date of birth in the Full_User_Profile entity
	 */
	public java.util.Date getBirthDate() {
		return birthDate;
	}

	/**
	 * Return the user profile city for presentation purposes.
	 * @return user profile city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Return the user profile hashed password for presentation purposes. 
	 * @return user profile hashed password
	 */
	public String getCurrentToken() {
		return currentToken;
	}
	
	/**
	 * Return the user profile disabled date for presentation purposes. Format is MM/DD/CCYY.
	 * @return user profile disabled date
	 */
	public String getDisabledDate() {
		return disabledDate;
	}
	
	/**
	 * Return the user profile disabled time for presentation purposes. Format is hh:mm:ss.
	 * @return user profile disabled time
	 */
	public String getDisabledTime() {
		return disabledTime;
	}
	
	/**
	 * Return the user profile date of birth for presentation purposes. Format is MM/DD/CCYY.
	 * @return user profile date of birth
	 */
	public String getDob() {
		return dob;
	}

	/**
	 * Return user profile email address for presentation purposes.
	 * @return user profile email address 
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Return user profile fax for presentation purposes.
	 * @return user profile fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * Return ID of the user profile to edit for presentation purposes.
	 * @return ID of the user profile to edit
	 */
	public String getFindUser() {
		return findUser;
	}

	/**
	 * Return user profile first name for presentation purposes.
	 * @return user profile first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Return user profile home market for presentation purposes.
	 * @return user profile home market
	 */
	public String getHomeMarket() {
		return homeMarket;
	}

	/**
	 * Return user profile date of most recent successful login for presentation purposes. Format is MM/DD/CCYY.
	 * @return date of most recent successful login
	 */
	public String getLastLogin() {
		return lastLogin;
	}

	/**
	 * Return a Timestamp which is used to set the date of most recent successful login in the Full_User_Profile entity.
	 * @return a Timestamp which is used to set the date of most recent successful login in the Full_User_Profile entity.
	 */
	public java.sql.Timestamp getLastLoginDate() {
		return lastLoginDate;
	}

	/**
	 * Return user profile time of most recent successful login for presentation purposes. Format is hh:mm:ss.
	 * @return user profile time of most recent successful login
	 */
	public String getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * Return user profile last name for presentation purposes.
	 * @return user profile last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Return message description for presentation purposes.
	 * @return message description
	 */
	public String getMsgDescription() {
		return msgDescription;
	}

	/**
	 * Return message ID.
	 * @return message ID
	 */
	public String getMsgID() {
		return msgID;
	}

	/**
	 * Return the number of invalid password attempts since the most recent successful login.
	 * @return The number of invalid password attempts since the most recent successful login.
	 */
	public short getPasswordAttempts() {
		return passwordAttempts;
	}

	/**
	 * Return the password expiration date. The password must be changed on or after this date. Format is MM/DD/CCYY. 
	 * @return The password expiration date.
	 */
	public java.util.Date getPasswordExpirationDate() {
		return passwordExpirationDate;
	}
	
	/**
	 * Return the user profile phone number for presentation purposes.
	 * @return user profile phone number
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Return the date and time when the profile was disabled due to too many invalid password attempts since the most recent login.
	 * @return date and time when the profile was disabled
	 */
	public java.sql.Timestamp getProfileDisabledDate() {
		return profileDisabledDate;
	}

	/**
	 * Return the number of invalid password attempts since the most recent successful login for presentation purposes.
	 * @return number of invalid password attempts
	 */
	public String getPwdAttempts() {
		return pwdAttempts;
	}

	/**
	 * Return the password expiration date for presentation purposes. Format is MM/DD/CCYY.
	 * @return password expiration date
	 */
	public String getPwdExpDate() {
		return pwdExpDate;
	}

	/**
	 * Return the ssn for presentation purposes.
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * Return user profile state for presentation purposes.
	 * @return user profile state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Return the home market. Field <i>homeMarket</i> is parsed to short to set the value for <i>upHomeMarket</i>.
	 * @return home market
	 */
	public short getUpHomeMarket() {
		return upHomeMarket;
	}

	/**
	 * Return user profile zip code for presentation purposes.
	 * @return user profile zip code
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * Return user profile zip<span style="font-family:consolas;">4</span> for presentation purposes.
	 * @return the zip<span style="font-family:consolas;">4</span>
	 */
	public String getZip4() {
		return zip4;
	}

	/**
	 * Initialize field <i>birthDate</i> to the value of field <i>dob</i> converted to Date.<p> The conversion happens only if <i>dob</i> is not an empty string.
	 */
	private void inzBirthDate() {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inzBirthDate() 000F ");
		/*debug*/
		if (!(getDob().equals(""))) {
			setBirthDate(toDate(getDob(), "", false));
			/*debug*/
	        System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inzBirthDate() 0010 ");
			/*debug*/
		}
	}

	/**
	 * Initialize message ID, message description, numeric fields and date fields.<p> The remaining String fields don't need initialization.
	 */
	public void inzEditRequest() {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inzEditRequest() 000A ");
		/*debug*/
		String feedback [] = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("="); 
		setMsgID(feedback[0]);
		setMsgDescription(feedback[1]);
		inzUpHomeMarket();
		inzPasswordAttempts();
		inzBirthDate();
		inzPasswordExpirationDate();
		inzProfileDisabledDate();
		inztLastLoginDate();
	}

	/**
	 * Initialize field <i>passwordAttempts</i> to the value of field <i>pwdAttempts</i> converted to short.<p> The conversion happens only if
	 * field <i>pwdAttempts</i> is not an empty string.
	 */
	private void inzPasswordAttempts() {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inzPasswordAttempts() 000D ");
		/*debug*/
		if (!(getPwdAttempts().equals(""))) {
			setPasswordAttempts(Short.parseShort(getPwdAttempts()));
	        System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inzPasswordAttempts() 0017 ");
			/*debug*/
			System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inzPasswordAttempts() 000E ");
			/*debug*/
		}
	}

	/**
	 * Initialize field <i>passwordExpirationDate</i> to the value of field <i>pwdExpDate</i> converted to Date.<p> The conversion happens only if 
	 * field <i>pwdExpDate</i> is not an empty string.
	 */
	private void inzPasswordExpirationDate() {
		/*debug*/
        System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inzPasswordExpirationDate() 0011 ");
		/*debug*/
		if (!(getPwdExpDate().equals(""))) {
			setPasswordExpirationDate(toDate(getPwdExpDate(), "", false));
			/*debug*/
	        System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inzPasswordExpirationDate() 0012 ");
			/*debug*/
		}
	}

	/**
	 * Initialize the field <i>profileDisabledDate</i> to the value of fields <i>disabledDate</i> and <i>disabledTime</i> converted to Date.<p>
	 * The conversion happens only if field <i>profileDisabledDate</i> is not an empty string.
	 */
	private void inzProfileDisabledDate() {
		/*debug*/
        System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inzProfileDisabledDate() 0013 ");
		/*debug*/
		if (!(getDisabledDate().equals(""))) {
			setProfileDisabledDate(toDate(getDisabledDate(), getDisabledTime(), true));  
			/*debug*/
	        System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inzProfileDisabledDate() 0014 ");
			/*debug*/
		}
	}

	/**
	 * Initialize the field <i>lastLoginDate</i> to the value of fields <i>lastLogin</i> and <i>lastLoginTime</i> converted to Date.<p>
	 * The conversion happens only if field <i>lastLogin</i> is not an empty string.
	 */
	private void inztLastLoginDate() {
		/*debug*/
        System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inztLastLoginDate() 0015 ");
		/*debug*/
		if (!(getLastLogin().equals(""))) {
			setLastLoginDate(toDate(getLastLogin(), getLastLoginTime(), true));
			/*debug*/
	        System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inztLastLoginDate() 0016 ");
			/*debug*/
		}		
	}

	/**
	 * Initialize the field <i>upHomeMarket</i> to the value of field <i>homeMarket</i> parsed to short.<p> The conversion happens only if the field 
	 * <i>homeMarket</i> is not an empty string. 
	 */
	private void inzUpHomeMarket() {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inzUpHomeMarket() 000B ");
		/*debug*/
		if (!(getHomeMarket().equals(""))) {
			setUpHomeMarket(Short.parseShort(getHomeMarket()));
			/*debug*/
			System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.inzUpHomeMarket() 000C ");
			/*debug*/
		}
	}

	/**
	 * Set the type of edit being requested.<p> This setter is called by the JSON mapper com.fasterxml.jackson.databind.ObjectMapper 
	 * when it mapps the web request.
	 * @param action the value to set
	 */
	public void setAction(String action) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setAction() 0018 ");
		/*debug*/
		this.action = action;
	}

	/**
	 * Set the value of field <i>activeYN</i> for presentation purposes.
	 * @param activeYN the value to set. Y is active. N is inactive.
	 */
	public void setActiveYN(String activeYN) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setActiveYN() 0019 ");
		/*debug*/
		this.activeYN = activeYN;
	}

	/**
	 * Set the value of address line <span style="font-family:consolas;">1</span> for presentation purposes.
	 * @param address1 the value to set.
	 */
	public void setAddress1(String address1) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setAddress1() 001A ");
		/*debug*/
		this.address1 = address1;
	}

	/**
	 * Set the value of address line <span style="font-family:consolas;">2</span> for presentation purposes.
	 * @param address2 the value to set.
	 */
	public void setAddress2(String address2) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setAddress2() 001B ");
		/*debug*/
		this.address2 = address2;
	}

	/**
	 * Set the value of field <i>birthDate</i> to the given birth date.
	 * @param birthDate the Date to set.
	 */
	public void setBirthDate(Date birthDate) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setBirthDate() 001C ");
		/*debug*/
		this.birthDate = birthDate;
	}

	/**
	 * Set the value of field <i>city</i> to the given city for presentation purposes.
	 * @param city the value to set.
	 */
	public void setCity(String city) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setCity() 001D ");
		/*debug*/
		this.city = city;
	}

	/**
	 * Set the hashed password to the given token for presentation purposes.
	 * @param currentToken the hashed password to set.
	 */
	public void setCurrentToken(String currentToken) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setCurrentToken() 001E ");
		/*debug*/
		this.currentToken = currentToken;
	}

	/**
	 * Set the date on which the profile was disabled due to too many invalid password attempts since the last successful login.<p>
	 * For presentation purposes. Date format is MM/DD/CCYY.
	 * @param disabledDate the date value to set.
	 */
	public void setDisabledDate(String disabledDate) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setDisabledDate() 001F ");
		/*debug*/
		this.disabledDate = disabledDate;
	}

	/**
	 * Set the date on which the profile was disabled due to too many invalid password attempts since the last successful login.<p>
	 * For presentation purposes. Format is hh:mm:ss.
	 * @param disabledTime the value to set.
	 */
	public void setDisabledTime(String disabledTime) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setDisabledTime() 0020 ");
		/*debug*/
		this.disabledTime = disabledTime;
	}

	/**
	 * Set date of birth for presentation purposes.<p> Format is MM/DD/CCYY.
	 * @param dob the date string to set. 
	 */
	public void setDob(String dob) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setDob() 0021 ");
		/*debug*/
		this.dob = dob;
	}

	/**
	 * Set email address for presentation purposes.
	 * @param email the email address to set.
	 */
	public void setEmail(String email) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setEmail() 0022 ");
		/*debug*/
		this.email = email;
	}

	/**
	 * Set the fax number for presentation purposes.
	 * @param fax the fax number to set.
	 */
	public void setFax(String fax) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setFax() 0023 ");
		/*debug*/
		this.fax = fax;
	}

	/**
	 * Set the user profile ID for presentation purposes.
	 * @param findUser user profile ID
	 */
	public void setFindUser(String findUser) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setFindUser() 0024 ");
		/*debug*/
		this.findUser = findUser;
	}

	/**
	 * Set user profile first name for presentation purposes. 
	 * @param firstName user profile first name
	 */
	public void setFirstName(String firstName) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setFirstName() 0025 ");
		/*debug*/
		this.firstName = firstName;
	}

	/**
	 * Set home market for presentation purposes.
	 * @param homeMarket home market
	 */
	public void setHomeMarket(String homeMarket) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setHomeMarket() 0026 ");
		/*debug*/
		this.homeMarket = homeMarket;
	}

	/**
	 * Set the date of the most recent successful login for presentation purposes.<p> Format is MM/DD/CCYY.
	 * @param lastLogin date of the most recent successful login
	 */
	public void setLastLogin(String lastLogin) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setLastLogin() 0027 ");
		/*debug*/
		this.lastLogin = lastLogin;
	}

	/**
	 * Set the date and time of the most recent successful login using the mills of the given Date.<p> The Timestamp field <i>lastLoginDate</i> 
	 * is used to set the last login date in the Full_User_Profile entity. 
	 * @param lastLoginDate Date of the most recent successful login
	 */
	public void setLastLoginDate(Date lastLoginDate) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setLastLoginDate() 0028 ");
		/*debug*/
		this.lastLoginDate = new java.sql.Timestamp(lastLoginDate.getTime());
	}

	/**
	 * Set the time of the most recent successful login for presentation purposes.<p> Format hh:mm:ss. 
	 * @param lastLoginTime time of the most recent successful login
	 */
	public void setLastLoginTime(String lastLoginTime) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setLastLoginTime() 0029 ");
		/*debug*/
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * Set user profile last name for presentation purposes.
	 * @param lastName last name
	 */
	public void setLastName(String lastName) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setLastName() 002A ");
		/*debug*/
		this.lastName = lastName;
	}

	/**
	 * Set message description for presentation purposes. 
	 * @param msgDescription message description
	 */
	public void setMsgDescription(String msgDescription) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setMsgDescription() 002B ");
		/*debug*/
		this.msgDescription = msgDescription;
	}

	/**
	 * Set message ID.
	 * @param msgID message ID
	 */
	public void setMsgID(String msgID) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setMsgID() 002C ");
		/*debug*/
		this.msgID = msgID;
	}

	/**
	 * Set the number of invalid password attempts since the most recent successful login.<p> Field <i>pwdAttempts</i> is parsed to short and 
	 * used as the value for <span style="font-family:consolas;">passwordAttempts</span>
	 * @param passwordAttempts number of invalid password attempts
	 */
	public void setPasswordAttempts(short passwordAttempts) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setPasswordAttempts() 002D ");
		/*debug*/
		this.passwordAttempts = passwordAttempts;
	}

	/**
	 * Set the password expiration date.<p> Password must be changed on or after this date. Field <i>pwdExpDate</i> is converted to Date and used
	 * as the value for <span style="font-family:consolas;">passwordExpirationDate</span>
	 * @param passwordExpirationDate password expiration Date
	 */
	public void setPasswordExpirationDate(Date passwordExpirationDate) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setPasswordExpirationDate() 002E ");
		/*debug*/
		this.passwordExpirationDate = passwordExpirationDate;
	}

	/**
	 * Set phone number for presentation purposes.
	 * @param phone phone number
	 */
	public void setPhone(String phone) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setPhone() 002F ");
		/*debug*/
		this.phone = phone;
	}

	/**
	 * Set the date and time when the profile was disabled due to too many invalid password attempts since most recent successful login.<p>
	 * A GregorianCalendar constructed from the century, year, month and day components of field <i>disabledDate</i>. The GregorianCalendar time 
	 * is set to the hour, minute and second components of field <i>disabledTime</i>. Parm <span style="font-family:consolas;">profileDisabledDate</span>
	 * gets its value from the GregorianCalendar mills.  
	 * @param profileDisabledDate date and time when the profile was disabled
	 */
	public void setProfileDisabledDate(Date profileDisabledDate) {
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest setProfileDisabledDate() 0002"
			+ "\n"
			+ "   profileDisabledDate="
			+ profileDisabledDate
			);
		this.profileDisabledDate = new java.sql.Timestamp(profileDisabledDate.getTime()); 
	}

	/**
	 * Set number of invalid password attempts since the most recent successful login for presentation purposes.
	 * @param pwdAttempts number of invalid password attempts
	 */
	public void setPwdAttempts(String pwdAttempts) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setPwdAttempts() 0030 ");
		/*debug*/
		this.pwdAttempts = pwdAttempts;
	}

	/**
	 * Set password expiration date for presentation purposes.<p> The password must be changed on or after this date. Format is MM/DD/CCYY.
	 * @param pwdExpDate password expiration date
	 */
	public void setPwdExpDate(String pwdExpDate) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setPwdExpDate() 0031 ");
		/*debug*/
		this.pwdExpDate = pwdExpDate;
	}

	/**
	 * Set user profile ssn for presentation purposes.
	 * @param ssn the value to set
	 */
	public void setSsn(String ssn) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setSsn() 0032 ");
		/*debug*/
		this.ssn = ssn;
	}

	/**
	 * Set the user profile state for presentation purposes.
	 * @param state the value to set
	 */
	public void setState(String state) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setState() 0033 ");
		/*debug*/
		this.state = state;
	}

	/**
	 * Set user profile home market for presentation purposes.
	 * @param upHomeMarket home market
	 */
	public void setUpHomeMarket(short upHomeMarket) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setUpHomeMarket() 0034 ");
		/*debug*/
		this.upHomeMarket = upHomeMarket;
	}

	/**
	 * Set user profile ZIP code for presentation purposes.
	 * @param zip ZIP code 
	 */
	public void setZip(String zip) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setZip() 0035 ");
		/*debug*/
		this.zip = zip;
	}

	/**
	 * Set user profile zip 4 for presentation purposes.
	 * @param zip4 the vaule to set.
	 */
	public void setZip4(String zip4) {
		/*debug*/
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest.setZip4() 0036 ");
		/*debug*/
		this.zip4 = zip4;
	}

	/**
	 * If the type of edit being requested is find, delete or remove, initialize strings to empty strings, dates to zero, 
	 * shorts to zero and timestamps to zero. 
	 */
	public void specialInzsr() {
		if (	action.equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_FIND) 
				|| 	action.equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_DELETE)
				|| 	action.equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_REMOVE)
			) {
			/*
			 * The web page is giving us more than we need. We just need a user name for find and delete so
			 * clear out the other fields. Also, for find and delete, the other fields we dont need will have 
			 * garbage left over from the previous request.    
			 */
			System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest specialInzsr() 0006");
			msgID 				   = "";
			msgDescription 		   = "";
			firstName 			   = "";
			lastName 			   = "";
			address1 			   = "";
			address2 			   = "";
			city 				   = "";
			state 				   = "";
			zip 				   = "";
			zip4 				   = "";
			phone 				   = "";
			fax 				   = "";
			email 				   = "";
			ssn 				   = "";
			dob 				   = "";
			birthDate              = new java.util.Date(0L); 
			homeMarket 			   = "";
			upHomeMarket 		   = 0;
			activeYN 			   = "";
			pwdExpDate 			   = "";
			passwordExpirationDate = new java.util.Date(0L);
			disabledDate 		   = "";
			profileDisabledDate    = new java.sql.Timestamp(0L);
			disabledTime 		   = "";
			pwdAttempts 		   = "";
			passwordAttempts 	   = 0;
			currentToken 		   = "";
			lastLogin 			   = "";
			lastLoginDate          = new java.sql.Timestamp(0L);
			lastLoginTime 		   = "";
		} 
	}

	/**
	 * Convert the given Timestamp to a date String and a time String for presentation purposes.<p> Date String is in MM/DD/CCYY format. 
	 * Time String is in hh:mm:ss format.
	 * 
	 * @param date the Timestamp to convert.
	 * @return String [] containing the string representation of the date and time components of the given Timestamp. Element zero is the date String.
	 * Element one is the time String.
	 */
	public String [] stringify(java.sql.Timestamp date) {
		//https://www.mkyong.com/java/java-enum-example/
		//timestamp=2018-01-08 23:03:27.007
		String cymdHmsMils[] = date.toString().split(" ");
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest stringify(java.sql.Timestamp) 0000"
			    + "\n"
			    + "   date="
			    + date
			    + "\n"
			    + "   cymdHmsMils="
			    + Arrays.toString(cymdHmsMils)
			    );
		String cymd[] = cymdHmsMils[0].split("-");
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest stringify(java.sql.Timestamp) 0001"
			    + "\n"
			    + "   cymd="
			    + Arrays.toString(cymd)
			    );
		String dateTime[] = new String [2];
		dateTime[0] = cymd[1] + "/" + cymd[2] + "/" + cymd[0]; 
		dateTime[1] = cymdHmsMils[1];
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest stringify(java.sql.Timestamp) 0009"
			    + "\n"
			    + "   dateTime="
			    + Arrays.toString(dateTime)
			    );
		return dateTime;
	}

	/**
	 * Convert the given Date to string for presentation purposes.<p> Date String is in MM/DD/CCYY format.
	 * @param date the Date to convert.
	 * @return String representation of the given Date 
	 */
	public String stringify(java.util.Date date) {
		//https://www.mkyong.com/java/java-enum-example/
		//date=Mon Jan 08 23:03:27 EST 2018
		String fields[] = date.toString().split(" ");
		int mm = 99;
		String month = fields[1];
		int dd = Integer.parseInt(fields[2]);
		int yyyy = Integer.parseInt(fields[5]);
		
		for (MonthNameAbbr m : MonthNameAbbr.values()) {
			if(m.toString().equalsIgnoreCase(month)) {
				mm = m.getOrdinal();
			};
		}

		return mm + "/" + dd + "/" + yyyy;
	}

	/**
	 * Convert the given Strings to a Date with an optional time component.<p> Note that it is unnecessary to return a Timestamp since
	 * the mills of the returned date can be used to construct a new Timestamp.
	 *  
	 * @param dateString String representation of a date in MM/DD/CCYY format
	 * @param timeString String representation of a time in hh:mm:ss format.
	 * @param withTime boolean that indicates that the returned Date will have a time component.
	 * @return Date with an optional time component
	 */
	public Date toDate(String dateString, String timeString, boolean withTime) {
		String mmDdYyyy[] = dateString.split("\\/");
		/* 
		 * date=Mon Jan 08 23:03:27 EST 2018
		 * ^ marks the beginning of the pattern string
		 * $ marks the end of the pattern string
		 * d{1,2} means digit occurs 1 or 2 times
		 * d{4} means digit occurs 4 times
		 */
		String pattern = "^\\d{1,2}\\/\\d{1,2}\\/\\d{4}$";
		Pattern n = Pattern.compile(pattern);
		Matcher m = n.matcher(dateString);
		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest toDate() 0003"
			+ "\n"
			+ "   dateString="
			+ dateString
			+ "\n"
			+ "   timeString="
			+ timeString
			+ "\n"
			+ "   withTime="
			+ withTime
			);
		GregorianCalendar gc = new GregorianCalendar();
		
		if (m.find()) {
			String month = mmDdYyyy[0]; 
			String day = mmDdYyyy[1]; 
			String year = mmDdYyyy[2];
			System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest toDate() 0004"
					+ "\n"
					+ "   mmDdYyyy="
					+ Arrays.toString(mmDdYyyy)
					+ "\n"
					+ "   month="
					+ month
					+ "\n"
					+ "   day="
					+ day
					+ "\n"
					+ "   year="
					+ year
					);
			
			if (withTime) {
				String hms [] = new String [3];
				hms = timeString.split(":"); 
				System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest toDate() 0005"
						+ "\n"
						+ "   hms="
						+ Arrays.toString(hms)
						+ "\n"
						+ "   timeString="
						+ timeString
						);
				gc.set(Calendar.HOUR, Integer.parseInt(hms[0]));
				gc.set(Calendar.MINUTE, Integer.parseInt(hms[1]));
				gc.set(Calendar.SECOND, Integer.parseInt(hms[2]));
				gc.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hms[0]));
			} else {
				gc.set(Calendar.HOUR, 0);
				gc.set(Calendar.MINUTE, 0);
				gc.set(Calendar.SECOND, 0);
				gc.set(Calendar.HOUR_OF_DAY, 0);
			}

			gc.set(Calendar.YEAR, Integer.parseInt(year));
			gc.set(Calendar.MONTH, Integer.parseInt(month) - 1);
			gc.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		}	

		System.out.println("com.yardi.shared.QSECOFR.EditUserProfileRequest toDate() 0007"
				+ "\n"
				+ "   gc="
				+ gc
				+ "\n"
				+ "   new Date="
				+ new Date(gc.getTimeInMillis())
				);
		return new Date(gc.getTimeInMillis());
	}
	
	@Override
	public String toString() {
		return "EditUserProfileRequest [action=" + action + ", findUser=" + findUser + ", msgID=" + msgID
				+ ", msgDescription=" + msgDescription + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", address1=" + address1 + ", address2=" + address2 + ", city=" + city + ", state=" + state + ", zip="
				+ zip + ", zip4=" + zip4 + ", phone=" + phone + ", fax=" + fax + ", email=" + email + ", ssn=" + ssn
				+ ", dob=" + dob + ", homeMarket=" + homeMarket + ", activeYN=" + activeYN + ", pwdExpDate="
				+ pwdExpDate + ", disabledTime=" + disabledTime + ", disabledDate=" + disabledDate + ", pwdAttempts="
				+ pwdAttempts + ", currentToken=" + currentToken + ", lastLogin=" + lastLogin + ", lastLoginTime="
				+ lastLoginTime + ", birthDate=" + birthDate + ", upHomeMarket=" + upHomeMarket
				+ ", passwordExpirationDate=" + passwordExpirationDate + ", profileDisabledDate=" + profileDisabledDate
				+ ", lastLoginDate=" + lastLoginDate + ", passwordAttempts=" + passwordAttempts + "]";
	}
}
