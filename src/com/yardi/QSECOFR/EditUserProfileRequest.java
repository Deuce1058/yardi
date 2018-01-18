package com.yardi.QSECOFR;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the request to edit the user profile. 
 * @author Jim
 *
 */

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class EditUserProfileRequest {
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
	private String disabledTime;
	private String disabledDate;
	private String pwdAttempts;
	private String currentToken;
	private String lastLogin;
	private String lastLoginTime;
	@JsonIgnore
	private java.util.Date birthDate;
	@JsonIgnore
	private short upHomeMarket;
	@JsonIgnore
	private java.util.Date passwordExpirationDate;
	@JsonIgnore
	private java.sql.Timestamp profileDisabledDate;
	@JsonIgnore
	private java.sql.Timestamp lastLoginDate;
	@JsonIgnore
	private short passwordAttempts;
	
	public EditUserProfileRequest() {
	}
	
	public EditUserProfileRequest(String action, String findUser, String msgID, String msgDescription, String firstName,
			String lastName, String address1, String address2, String city, String state, String zip, String zip4,
			String phone, String fax, String email, String ssn, String dob, String homeMarket, String activeYN,
			String pwdExpDate, String disabledDate, String disabledTime, String pwdAttempts, String currentToken, 
			String lastLogin, String lastLoginTime) {
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
		System.out.println("com.yardi.QSECOFR.EditUserProfileRequest toDate() 0003"
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
			System.out.println("com.yardi.QSECOFR.EditUserProfileRequest toDate() 0004"
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
				System.out.println("com.yardi.QSECOFR.EditUserProfileRequest toDate() 0005"
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
			gc.set(Calendar.MONTH, Integer.parseInt(month));
			gc.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		}	

		return new Date(gc.getTimeInMillis());
	}
	
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
	
	public String [] stringify(java.sql.Timestamp date) {
		//https://www.mkyong.com/java/java-enum-example/
		//timestamp=2018-01-08 23:03:27.007
		String cymdHmsMils[] = date.toString().split(" ");
		System.out.println("com.yardi.QSECOFR.EditUserProfileRequest stringify(java.sql.Timestamp) 0000"
			    + "\n"
			    + "   date="
			    + date
			    + "\n"
			    + "   cymdHmsMils="
			    + Arrays.toString(cymdHmsMils)
			    );
		String cymd[] = cymdHmsMils[0].split("-");
		System.out.println("com.yardi.QSECOFR.EditUserProfileRequest stringify(java.sql.Timestamp) 0001"
			    + "\n"
			    + "   cymd="
			    + Arrays.toString(cymd)
			    );
		String dateTime[] = new String [2];
		dateTime[0] = cymd[1] + "/" + cymd[2] + "/" + cymd[0]; 
		dateTime[1] = cymdHmsMils[1];
		System.out.println("com.yardi.QSECOFR.EditUserProfileRequest stringify(java.sql.Timestamp) 0009"
			    + "\n"
			    + "   dateTime="
			    + Arrays.toString(dateTime)
			    );
		return dateTime;
	}
	
	public short getUpHomeMarket() {
		return upHomeMarket;
	}

	public void setUpHomeMarket(short upHomeMarket) {
		this.upHomeMarket = upHomeMarket;
	}

	public java.util.Date getPasswordExpirationDate() {
		return passwordExpirationDate;
	}

	public void setPasswordExpirationDate(Date passwordExpirationDate) {
		this.passwordExpirationDate = passwordExpirationDate;
	}

	public java.sql.Timestamp getProfileDisabledDate() {
		return profileDisabledDate;
	}

	public void setProfileDisabledDate(Date profileDisabledDate) {
		this.profileDisabledDate.setTime(profileDisabledDate.getTime()); 
	}

	public java.sql.Timestamp getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate.setTime(lastLoginDate.getTime());
	}

	public short getPasswordAttempts() {
		return passwordAttempts;
	}

	public void setPasswordAttempts(short passwordAttempts) {
		this.passwordAttempts = passwordAttempts;
	}

	public java.util.Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
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

	public String getDisabledTime() {
		return disabledTime;
	}

	public void setDisabledTime(String disabledTime) {
		this.disabledTime = disabledTime;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public void specialInzsr() {
		if (	action.equals(com.yardi.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_FIND) 
			|| 	action.equals(com.yardi.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_DELETE)) {
			/*
			 * The web page is giving us more than we need. We just need a user name for find and delete so
			 * clear out the other fields. Also, for find and delete, the other fields we dont need will have 
			 * garbage left over from the previous request.    
			 */
			System.out.println("com.yardi.QSECOFR.EditUserProfileRequest specialInzsr() 0006");
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
