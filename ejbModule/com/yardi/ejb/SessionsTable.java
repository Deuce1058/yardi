package com.yardi.ejb;

import jakarta.ejb.Local;

import java.util.Vector;

import com.yardi.ejb.model.Full_Sessions_Table;
import com.yardi.ejb.model.Sessions_Table;

@Local
public interface SessionsTable {
	/**
	 * Clear the SESSIONS_TABLE.<p>
	 * 
	 * SESSIONS_TABLE database table is cleared when the application is starting to reflect that no users have logged in yet.
	 * 
	 * @return number of rows deleted 
	 */
	int clear();
	/**
	 * Find the single Sessions_Table entity that matches the given session ID.<p>
	 *  
	 * @param id Sessions_Table entity to find.
	 * @return Sessions_Table entity matching the given id. Returns null if the persistence context has no Sessions_Table entity
	 * that matches the given id and SESSIONS_TABLE database table has no row matching the given id.
	 */
	Sessions_Table find(String id);
	/**
	 * Find all Full_Sessions_Table entities for the given user ID.<p> 
	 * 
	 * @param userID specifies which entities to find. 
	 * @return Vector of Full_Sessions_Table entities matching the given userID. Returns an empty Vector if the persistence context contains no Full_Sessions_Table
	 * entities matching the given user ID and the SESSIONS_TABLE database table has no rows matching the given user ID.
	 */
	Vector<Full_Sessions_Table> findFull_Sessions_Table(String userID);
	/**
	 * Persist a new Sessions_Table entity constructed from the parms.<p>
	 * 
	 * Each parm corresponds to a column in SESSIONS_TABLE database table.
	 * 
	 * @param userID specifies the user name.
	 * @param sessionID a String containing the unique identifier assigned to this session. This value should be the same as HttpServletRequest.getSession().getId(). 
	 * @param lastRequest most recent page that was requested.
	 * @param lastActive Timestamp of most recent activity. 
	 */
	void persist(
			String userID, 
			String sessionID, 
			String lastRequest, 
			java.sql.Timestamp lastActive
			);
	/**
	 * Remove the given Full_Sessions_Table entity.<p>
	 * 
	 * @param sessionsTable the Full_Sessions_Table entity to remove.
	 */
	void remove(Full_Sessions_Table sessionsTable);
	/**
	 * Reset the sequence column in YARDISEQ database table.<p>
	 * 
	 * SESSIONS_TABLE database table is cleared during application startup to reflect that no users have logged in yet. The value for the sequence 
	 * column is taken from YARDISEQ database table. Set the SEQVALUE column in YARDISEQ database table for the initial session.<br><br>
	 * 
	 * @return number of rows updated.
	 */
	int resetSeq();
	/**
	 * Update the Sessions_Table entity.<p>
	 * 
	 * The given Sessions_Table entity is updated using the parms passed and then merged into the persistence context.
	 *  
	 * @param sessionsTable the Sessions_Table entity to update and merge.
	 * @param sessionID a string containing the unique identifier assigned to this session. The value should be the same as HttpServletReqest.getSession().getId(). 
	 * @param lastRequest most recent page viewed. 
	 * @param lastActive Timestamp of most recent activity.
	 */
	void update(
			Sessions_Table sessionsTable,
			String sessionID,
			String lastRequest, 
			java.sql.Timestamp lastActive
			);

}
