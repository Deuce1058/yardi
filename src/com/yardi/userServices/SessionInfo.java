package com.yardi.userServices;

/**
 * Container to hold information about the session. When html pages need information about the session they will do a post to 
 * SessionInfoSerice which will update this container and return it as JSON back to the html page.  
 * @author Jim
 *
 */
public class SessionInfo {
	/**
	 * Type of request being made 
	 */
	private String request;
	/**
	 * User ID
	 */
	private String userID;
	/**
	 * Session ID
	 */
	private String sessionID;
	/**
	 * Session Token
	 */
	private String sessionToken;
	/**
	 * Most recent request 
	 */
	private String lastRequest;
	/**
	 * Date of most recent activity
	 */
	private java.util.Date lastActive;

	/**
	 * Default constructor
	 */
	public SessionInfo() {
	}

	/**
	 * Return the date of most recent activity 
	 * @return date of most recent activity
	 */
	public java.util.Date getLastActive() {
		return lastActive;
	}

	/**
	 * Return most recent request
	 * @return most recent request
	 */
	public String getLastRequest() {
		return lastRequest;
	}

	/**
	 * Return type of request being made
	 * @return type of request being made
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * Return session ID
	 * @return session ID
	 */
	public String getSessionID() {
		return sessionID;
	}

	/**
	 * Return session token 
	 * @return session token
	 */
	public String getSessionToken() {
		return sessionToken;
	}

	/**
	 * Return the user ID
	 * @return user ID
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * Set the date on which the session was last active  
	 * @param lastActive date on which the session was last active
	 */
	public void setLastActive(java.util.Date lastActive) {
		this.lastActive = lastActive;
	}

	/**
	 * Set the most recent request
	 * @param lastRequest most recent request
	 */
	public void setLastRequest(String lastRequest) {
		this.lastRequest = lastRequest;
	}

	/**
	 * Set the type of request being made 
	 * @param request type of request being made 
	 */
	public void setRequest(String request) {
		this.request = request;
	}
	
	/**
	 * Set the session ID
	 * @param sessionID session ID
	 */
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * Set the session token
	 * @param sessionToken session token
	 */
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	/**
	 * Set the user ID
	 * @param userID user ID
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String toString() {
		return "SessionInfo [request=" + request + ", userID=" + userID + ", sessionID=" + sessionID + ", sessionToken="
				+ sessionToken + ", lastRequest=" + lastRequest + ", lastActive=" + lastActive + "]";
	}
}
