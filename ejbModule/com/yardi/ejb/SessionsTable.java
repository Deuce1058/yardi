package com.yardi.ejb;

import javax.ejb.Remote;

@Remote
public interface SessionsTable {
	int persist(
			String userID, 
			String sessionID, 
			String sessionToken, 
			String lastRequest, 
			java.util.Date lastActive
			);
	int clear();
	Sessions_Table findSession(String sessionID);
	Sessions_Table findUser(String userID);
	int update(
			String userID,
			String sessionID,
			String sessionToken, 
			String lastRequest, 
			java.util.Date lastActive
			);
}
