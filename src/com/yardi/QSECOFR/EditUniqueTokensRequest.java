package com.yardi.QSECOFR;

import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.yardi.ejb.Unique_Tokens;

/**
 * Data transfer object for holding request data coming from uniqueTokens_CRUD.html and response data going to 
 * uniqueTokens_CRUD.html.
 * <br><br>
 * EditUniqueTokensService will build a vector of all the user tokens, covert it to JSON and store the list in 
 * uniqueTokensString.
 * <br><br>
 * uniqueTokens_CRUD.html will parse uniqueTokensString to build the display.
 * <br><br>
 * To send data back to the server, uniqueTokens_CRUD.html will stringify the unique tokens HTML table and store this as 
 * JSON in uniqueTokensString. Java uses uniqueTokensString to construct a vector of EditUniqueTokensRequest.
 * <br><br> 
 * When new rows are being added, uniqueTokens_CRUD.html will iterate through the screen &lt;rows&gt; searching for occurrences 
 * of up1Rrn.equals("0"). These elements are considered to be add requests. The individual instance variables are used to 
 * insert a new row in UNIQUE_TOKENS.
 * <br><br>
 * Data in existing screen &lt;rows&gt; are compared to the original data stored in the vector of unique tokens. If something 
 * changed, the corresponding row in UNIQUE_TOKENS is updated.
 * <br><br>
 * When the user checks the delete box on a screen &lt;row&gt; the corresponding row in UNIQUE_TOKENS is deleted.   
 * 
 * @author Jim
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class EditUniqueTokensRequest {
	@JsonIgnore
	private java.util.Date tokenAddedDate;
	@JsonIgnore
	private Long rrn;
	@JsonIgnore
	private Vector<Unique_Tokens> uniqueTokens;
	private String action;
	private String msgID;
	private String msgDescription;
	private String findUser;
	/**
	 * Used to deserialize/serialize data coming from/to uniqueTokens_CRUD.html 
	 */
	private String uniqueTokensString;
	/**
	 * Used to deserialize/serialize data coming from/to uniqueTokens_CRUD.html 
	 */
	private String up1UserName;
	/**
	 * Used to deserialize data coming from uniqueTokens_CRUD.html
	 */
	private String up1Token;
	/**
	 * Used to deserialize data coming from uniqueTokens_CRUD.html
	 */
	private String up1DateAdded;
	/**
	 * Used to deserialize/serialize data coming from/to uniqueTokens_CRUD.html 
	 */
	private String up1Rrn;
	/**
	 * Used to deserialize/serialize data coming from/to uniqueTokens_CRUD.html 
	 */
	private String deleteToken;

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

	public String getUp1Token() {
		return up1Token;
	}

	public void setUp1Token(String up1Token) {
		this.up1Token = up1Token;
	}

	public String getUp1DateAdded() {
		return up1DateAdded;
	}

	public void setUp1DateAdded(String up1DateAdded) {
		this.up1DateAdded = up1DateAdded;
	}

	public java.util.Date getTokenAddedDate() {
		return tokenAddedDate;
	}

	public void setTokenAddedDate(java.util.Date tokenAddedDate) {
		this.tokenAddedDate = tokenAddedDate;
	}

	public String getUp1Rrn() {
		return up1Rrn;
	}

	public void setUp1Rrn(String up1Rrn) {
		this.up1Rrn = up1Rrn;
	}

	public Long getRrn() {
		return rrn;
	}

	public void setRrn(Long rrn) {
		this.rrn = rrn;
	}

	public String getDeleteToken() {
		return deleteToken;
	}

	public void setDeleteToken(String deleteToken) {
		this.deleteToken = deleteToken;
	}

	public String getUniqueTokensString() {
		return uniqueTokensString;
	}

	public void setUniqueTokensString(String uniqueTokensList) {
		this.uniqueTokensString = uniqueTokensList;
	}

	public Vector<Unique_Tokens> getUniqueTokens() {
		return uniqueTokens;
	}

	public void setUniqueTokens(Vector<Unique_Tokens> uniqueTokens) {
		this.uniqueTokens = uniqueTokens;
	}

	public String getUp1UserName() {
		return up1UserName;
	}

	public void setUp1UserName(String up1UserName) {
		this.up1UserName = up1UserName;
	}

	@Override
	public String toString() {
		return "EditUniqueTokensRequest [tokenAddedDate=" + tokenAddedDate + ", rrn=" + rrn + ", uniqueTokens="
				+ uniqueTokens + ", action=" + action + ", msgID=" + msgID + ", msgDescription=" + msgDescription
				+ ", findUser=" + findUser + ", uniqueTokensString=" + uniqueTokensString + ", up1UserName="
				+ up1UserName + ", up1Token=" + up1Token + ", up1DateAdded=" + up1DateAdded + ", up1Rrn=" + up1Rrn
				+ ", deleteToken=" + deleteToken + "]";
	}
}
