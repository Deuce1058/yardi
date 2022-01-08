package com.yardi.ejb;

import jakarta.ejb.Local;

import com.yardi.ejb.model.Login_Sessions_Table;

@Local
public interface LoginSessionsTable {
	int clear();
	void persist(
			String userID, 
			String sessionID, 
			String lastRequest, 
			java.sql.Timestamp lastActive
			);
	int resetSeq();
	void update(
			Login_Sessions_Table sessionsTable,
			String sessionID,
			String lastRequest, 
			java.sql.Timestamp lastActive
			);
}
