package com.yardi.QSECOFR;

import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.yardi.ejb.Unique_Tokens;
import com.yardi.shared.QSECOFR.MonthNameAbbr;

/**
 * Data transfer object containing request/response data for the edit user tokens application.<p>
 * 
 * When {@link com.yardi.QSECOFR.EditUniqueTokensService#EditUniqueTokensService() com.yardi.QSECOFR.EditUniqueTokensService} receives a find request,
 * it builds Vector <code>uniqueTokens</code> with all the tokens for one user. Vector <code>uniqueTokens</code> is mapped to JSON and stored in 
 * <code>uniqueTokensString</code>. Then uniqueTokens_CRUD.html parses <code>uniqueTokensString</code> to build the display when it receives the response.<p>
 * 
 * On an update request <code>com.yardi.QSECOFR.EditUniqueTokensService</code> receives the updated tokens in <code>uniqueTokensString</code>. Note the JSON 
 * representation of <code>up1DateAdded</code> is a string in the format mm/dd/ccyy and it is mapped to a java Date manually. An update requests may be a combination
 * of deleting existing tokens, updating existing tokens and adding new tokens. Newly added tokens are distinguished from existing tokens by a negative 
 * <code>up1Rrn</code> value. A value of greater than zero in <code>up1Rrn</code> when <code>deleteToken</code> is <i>false</i> indicates the token is being updated.
 * A value of greater than zero in <code>up1Rrn</code> when <code>deleteToken</code> is <i>true</i> indicates the token will be deleted.     
 * 
 *  
 *  
 *  
 *  
 *  
 *  
 *  
 * 
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
	/**
	 * Date the token was added
	 */
	@JsonIgnore
	private java.util.Date tokenAddedDate;
	/**
	 * Relative record number
	 */
	@JsonIgnore
	private Long rrn;
	/**
	 * All tokens for one user from database table UNIQUE_TOKENS.<p> The web page making the request has no equivalent to a Java Vector so this field is ignored when the 
	 * request comes from the web  
	 */
	@JsonIgnore
	private Vector<Unique_Tokens> uniqueTokens;
	/**
	 * The action to perform, update or find.
	 */
	private String action;
	/**
	 * Message ID
	 */
	private String msgID;
	/**
	 * Message description
	 */
	private String msgDescription;
	/**
	 * Identifies the user
	 */
	private String findUser;
	/**
	 * JSON formatted string containing all tokens for one user.<p> On the response to a find request, this field contains the JSON representation of 
	 * field <code>uniqueTokens</code>.  
	 */
	private String uniqueTokensString;
	/**
	 * User ID from database table UNIQUE_TOKENS 
	 */
	private String up1UserName;
	/**
	 * Column UP1_TOKEN from database table UNIQUE_TOKENS.<p> The user's token.
	 */ 
	private String up1Token;
	/**
	 * Column UP1_DATE_ADDED from database table UNIQUE_TOKENS.<p> Date token was added.
	 */
	private String up1DateAdded;
	/**
	 * Column UP1_RRN from database table UNIQUE_TOKENS.<p> Relative record number 
	 */
	private String up1Rrn;
	/**
	 * Indicates whether the token is being deleted.<p> Has a value of true when the user has checked the delete box.   
	 */
	private String deleteToken;

	/**
	 * Return the action requested, find or update.
	 * @return action requested
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Return the value of the delete check box.<p> A value of true indicates the token is being deleted.
	 * @return value of the delete check box
	 */
	public String getDeleteToken() {
		return deleteToken;
	}

	/**
	 * Return the user name. 
	 * @return user's name
	 */
	public String getFindUser() {
		return findUser;
	}

	/**
	 * Return the message description
	 * @return message description
	 */
	public String getMsgDescription() {
		return msgDescription;
	}

	/**
	 * Return message ID
	 * @return message ID
	 */
	public String getMsgID() {
		return msgID;
	}
	
	/**
	 * Return relative record number
	 * @return relative record number
	 */
	public Long getRrn() {
		return rrn;
	}

	/**
	 * Return the date token was added as a Java Date
	 * @return date token was added
	 */
	public java.util.Date getTokenAddedDate() {
		return tokenAddedDate;
	}

	/**
	 * Returns Vector of the user's tokens from database table UNIQUE_TOKENS 
	 * @return user's tokens Vector
	 */
	public Vector<Unique_Tokens> getUniqueTokens() {
		return uniqueTokens;
	}

	/**
	 * Returns JSON representation of the user's tokens from database table UNIQUE_TOKENSS
	 * @return JSON representation of the user's tokens
	 */
	public String getUniqueTokensString() {
		return uniqueTokensString;
	}

	/**
	 * Returns date token was added
	 * @return date token was added
	 */
	public String getUp1DateAdded() {
		return up1DateAdded;
	}

	/**
	 * Returns relative record number
	 * @return relative record number
	 */
	public String getUp1Rrn() {
		return up1Rrn;
	}

	/**
	 * Returns the user's token
	 * @return user's token
	 */
	public String getUp1Token() {
		return up1Token;
	}

	/**
	 * Returns user name from table UNIQUE_TOKENS
	 * @return user name from table UNIQUE_TOKENS
	 */
	public String getUp1UserName() {
		return up1UserName;
	}

	/**
	 * Set the action to perform, find or update
	 * @param action to perform
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Set delete token indicator. When this has a value of <i>true</i>, it indicates the token is being deleted 
	 * @param deleteToken value to set
	 */
	public void setDeleteToken(String deleteToken) {
		this.deleteToken = deleteToken;
	}

	/**
	 * Set the user's name to find
	 * @param findUser user's name to find
	 */
	public void setFindUser(String findUser) {
		this.findUser = findUser;
	}

	/**
	 * Set the message description
	 * @param msgDescription message description
	 */
	public void setMsgDescription(String msgDescription) {
		this.msgDescription = msgDescription;
	}

	/**
	 * Set message ID
	 * @param msgID message ID
	 */
	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}

	/**
	 * Set the relative record number
	 * @param rrn relative record number
	 */
	public void setRrn(Long rrn) {
		this.rrn = rrn;
	}

	/**
	 * Set date token added
	 * @param tokenAddedDate token added date
	 */
	public void setTokenAddedDate(java.util.Date tokenAddedDate) {
		this.tokenAddedDate = tokenAddedDate;
	}

	/**
	 * Set the user's unique tokens Vector
	 * @param uniqueTokens unique tokens
	 */
	public void setUniqueTokens(Vector<Unique_Tokens> uniqueTokens) {
		this.uniqueTokens = uniqueTokens;
	}

	/**
	 * Set the user's tokens 
	 * @param uniqueTokensList string containing the JSON formatted tokens 
	 */
	public void setUniqueTokensString(String uniqueTokensList) {
		this.uniqueTokensString = uniqueTokensList;
	}

	/**
	 * Set the date token was added
	 * @param up1DateAdded date token was added
	 */
	public void setUp1DateAdded(String up1DateAdded) {
		this.up1DateAdded = up1DateAdded;
	}

	/**
	 * Set relative record number
	 * @param up1Rrn relative record number
	 */
	public void setUp1Rrn(String up1Rrn) {
		this.up1Rrn = up1Rrn;
	}

	/**
	 * Set the user's token 
	 * @param up1Token user's token
	 */
	public void setUp1Token(String up1Token) {
		this.up1Token = up1Token;
	}

	/**
	 * Set the user name
	 * @param up1UserName user name
	 */
	public void setUp1UserName(String up1UserName) {
		this.up1UserName = up1UserName;
	}

	/**
	 * Map the given Date to String having the format MM/DD/CCYY
	 * @param date value to map
	 * @return String having the format MM/DD/CCYY
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

	@Override
	public String toString() {
		return "EditUniqueTokensRequest [tokenAddedDate=" + tokenAddedDate + ", rrn=" + rrn + ", uniqueTokens="
				+ uniqueTokens + ", action=" + action + ", msgID=" + msgID + ", msgDescription=" + msgDescription
				+ ", findUser=" + findUser + ", uniqueTokensString=" + uniqueTokensString + ", up1UserName="
				+ up1UserName + ", up1Token=" + up1Token + ", up1DateAdded=" + up1DateAdded + ", up1Rrn=" + up1Rrn
				+ ", deleteToken=" + deleteToken + "]";
	}
}
