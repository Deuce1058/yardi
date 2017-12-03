package com.yardi.ejb;

import javax.ejb.Remote;

@Remote
public interface SessionsTableSessionBeanRemote {
	int persist(
			String userID, 
			String sessionID, 
			String sessionToken, 
			String lastRequest, 
			java.util.Date lastActive
			);
	int clear();
	SessionsTable findSession(String sessionID);
	SessionsTable findUser(String userID);
	int update(
			String userID,
			String sessionID,
			String sessionToken, 
			String lastRequest, 
			java.util.Date lastActive
			);

}
