package com.yardi.QSECOFR;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * A request for a new token. 
 * @author Jim
 */

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonPropertyOrder({"msgID", "msgDescription", "action", "password", "hash", "verifiedYn"})
public class TokenRequest {
	/**
	 * New password
	 */
	private String password="";
	/**
	 * Message ID
	 */
	private String msgID="";
	/**
	 * Message description
	 */
	private String msgDescription="";
	
	/*
	 * Requested action "hash": hash the given password
	 * Requested action "verify": given a password and a hash verify that the given password generates the same hash as the given hash
	 */
	private String action="";
	
	/**
	 * base64 hash value 
	 */
	private String hash="";
	
	/**
	 * Indicates whether the verify request succeeded or failed. y=yes, n=no.
	 */
	private String verifiedYn="";
	
	/**
	 * Default constructor
	 */
	public TokenRequest() {
	}

	/**
	 * Constructor using all fields
	 * @param password new password
	 * @param msgID message ID
	 * @param msgDescription message description
	 * @param action requested action: "hash" hash a password, "verify" verify that the given password generates the given hash
	 * @param hash when verifying a password this is the hash to compare to
	 * @param verifiedYn indicates whether the verify request succeeded or failed
	 */
	public TokenRequest(String password, String msgID, String msgDescription, String action, String hash, String verifiedYn) {
		this.password = password;
		this.msgID = msgID;
		this.msgDescription = msgDescription;
		this.action=action;
		this.hash=hash;
		this.verifiedYn=verifiedYn;
	}

	/**
	 * Return the requested action. "hash" to hash the given password. "verify" to verify that the given password generates the given hash.
	 * @return requested action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Get the requested action. "hash" to hash the given password. "verify" to verify that the given password generates the given hash.
	 * @return requested action
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * Return message description
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
	 * Return new password
	 * @return new password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Get the flag which indicates whether the verify request succeeded or failed  
	 */
	public String getVerifiedYn() {
		return verifiedYn;
	}

	/**
	 * Return new password as <code>char[]</code>
	 * @return new password as <code>char[]</code>
	 */
	public char[] passwordToChar() {
		return password.toCharArray();
	}

	/**
	 * Set the requested action. "hash" to hash the given password. "verify" to verify that the given password generates the given hash.
	 * @param action action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Set the hash value resulting from hashing the given password
	 * @param hash hash value to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}


	/**
	 * Set message description
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
	 * Set new password
	 * @param password new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Set the flag which indicates whether the verify request succeeded or failed
	 * @param verifiedYn "y"=verify request was successful. "n"=verify request failed
	 */
	public void setVerifiedYn(String verifiedYn) {
		this.verifiedYn = verifiedYn;
	}

	/**
	 * Return string containing all fields of this container
	 */
	@Override
	public String toString() {
		return "TokenRequest [password=" + password + ", msgID=" + msgID + ", msgDescription=" + msgDescription
				+ ", action=" + action + ", hash=" + hash + ", verifiedYn=" + verifiedYn + "]";
	}
}
