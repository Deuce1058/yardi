package com.yardi.userServices;

/**
 * Container to hold information about the session. When html pages need information about the session they will do a post to 
 * SessionInfoSerice which will update this container and return it as JSON back to the html page.  
 * @author Jim
 *
 */
public class SessionInfo {
	private String userID;
	
	public SessionInfo() {
	}

	public SessionInfo(String userID) {
		this.userID = userID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
}
