package com.yardi.ejb;

import jakarta.ejb.Local;

import com.yardi.ejb.model.Sessions_Table;

@Local
public interface SessionsTable {
	int clear();
	Sessions_Table find(String id);
	void persist(
			String userID, 
			String sessionID, 
			String lastRequest, 
			java.sql.Timestamp lastActive
			);
	int resetSeq();
	void update(
			Sessions_Table sessionsTable,
			String sessionID,
			String lastRequest, 
			java.sql.Timestamp lastActive
			);
}
