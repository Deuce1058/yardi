package com.yardi.QSECOFR;

/**
 * Represents the request to edit the user profile. 
 * @author Jim
 *
 */

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class EditUserProfileRqeuest {
	private String action;
	private String findUser;
	private String msgID;
	private String msgDescription;
	private String firstName;
	private String lastName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String zip4;
	private String phone;
	private String fax;
	private String email;
	private String ssn;
	private String dob;
	private String homeMarket;
	private String activeYN;
	private String pwdExpDate;
	private String disabledDate;
	private String pwdAttempts;
	private String currentToken;
	private String lastLogin;
	
	public EditUserProfileRqeuest() {
	}
	
	public EditUserProfileRqeuest(String action, String findUser, String msgID, String msgDescription, String firstName,
			String lastName, String address1, String address2, String city, String state, String zip, String zip4,
			String phone, String fax, String email, String ssn, String dob, String homeMarket, String activeYN,
			String pwdExpDate, String disabledDate, String pwdAttempts, String currentToken, String lastLogin) {
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
		this.pwdAttempts = pwdAttempts;
		this.currentToken = currentToken;
		this.lastLogin = lastLogin;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getFindUser() {
		return findUser;
	}

	public void setFindUser(String findUser) {
		this.findUser = findUser;
	}

	public String getMsgID() {
		return msgID;
	}

	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	public String getMsgDescription() {
		return msgDescription;
	}

	public void setMsgDescription(String msgDescription) {
		this.msgDescription = msgDescription;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getZip4() {
		return zip4;
	}

	public void setZip4(String zip4) {
		this.zip4 = zip4;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getHomeMarket() {
		return homeMarket;
	}

	public void setHomeMarket(String homeMarket) {
		this.homeMarket = homeMarket;
	}

	public String getActiveYN() {
		return activeYN;
	}

	public void setActiveYN(String activeYN) {
		this.activeYN = activeYN;
	}

	public String getPwdExpDate() {
		return pwdExpDate;
	}

	public void setPwdExpDate(String pwdExpDate) {
		this.pwdExpDate = pwdExpDate;
	}

	public String getDisabledDate() {
		return disabledDate;
	}

	public void setDisabledDate(String disabledDate) {
		this.disabledDate = disabledDate;
	}

	public String getPwdAttempts() {
		return pwdAttempts;
	}

	public void setPwdAttempts(String pwdAttempts) {
		this.pwdAttempts = pwdAttempts;
	}

	public String getCurrentToken() {
		return currentToken;
	}

	public void setCurrentToken(String currentToken) {
		this.currentToken = currentToken;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String toString() {
		return "EditUserProfileRqeuest [action=" + action + ", findUser=" + findUser + ", msgID=" + msgID
				+ ", msgDescription=" + msgDescription + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", address1=" + address1 + ", address2=" + address2 + ", city=" + city + ", state=" + state + ", zip="
				+ zip + ", zip4=" + zip4 + ", phone=" + phone + ", fax=" + fax + ", email=" + email + ", ssn=" + ssn
				+ ", dob=" + dob + ", homeMarket=" + homeMarket + ", activeYN=" + activeYN + ", pwdExpDate="
				+ pwdExpDate + ", disabledDate=" + disabledDate + ", pwdAttempts=" + pwdAttempts + ", currentToken="
				+ currentToken + ", lastLogin=" + lastLogin + "]";
	}
}
