package com.yardi.ejb;

import javax.ejb.Remote;

@Remote
public interface SessionsTable {
	int clear();
	Sessions_Table findSession(String sessionID);
	Sessions_Table findUser(String userID);
	int persist(
			String userID, 
			String sessionID, 
			String sessionToken, 
			String lastRequest, 
			java.util.Date lastActive
			);
	int resetSeq();
	int update(
			String userID,
			String sessionID,
			String sessionToken, 
			String lastRequest, 
			java.util.Date lastActive
			);
}
