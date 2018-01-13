package com.yardi.userServices;

/**
 * Container to hold information about the session. When html pages need information about the session they will do a post to 
 * SessionInfoSerice which will update this container and return it as JSON back to the html page.  
 * @author Jim
 *
 */
public class SessionInfo {
	private String request;
	private String userID;
	private String sessionID;
	private String sessionToken;
	private String lastRequest;
	private java.util.Date lastActive;
		
	public SessionInfo() {
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public String getLastRequest() {
		return lastRequest;
	}

	public void setLastRequest(String lastRequest) {
		this.lastRequest = lastRequest;
	}

	public java.util.Date getLastActive() {
		return lastActive;
	}

	public void setLastActive(java.util.Date lastActive) {
		this.lastActive = lastActive;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String toString() {
		return "SessionInfo [request=" + request + ", userID=" + userID + ", sessionID=" + sessionID + ", sessionToken="
				+ sessionToken + ", lastRequest=" + lastRequest + ", lastActive=" + lastActive + "]";
	}
}
