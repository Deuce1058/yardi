package com.yardi.ejb;

import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.Vector;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.yardi.ejb.model.Full_Sessions_Table;
import com.yardi.ejb.model.Sessions_Table;
import com.yardi.shared.userServices.PasswordAuthentication;

/**
 * Session Bean implementation of methods for working with the SESSIONS_TABLE
 */
@Stateless
public class SessionsTableBean implements SessionsTable {
	@PersistenceContext(unitName="yardi")
	private EntityManager em;

    public SessionsTableBean() {
    	//debug
    	System.out.println("com.yardi.ejb.SessionsTableBean.SessionsTableBean() 0000 ");
    	//debug
    }
	
	/**
	 * Clear the SESSIONS_TABLE.<p>
	 * 
	 * SESSIONS_TABLE database table is cleared when the application is starting to reflect that no users have logged in yet.
	 * 
	 * @return number of rows deleted 
	 */
	public int clear() {
		System.out.println("com.yardi.ejb.SessionsTableBean.clear() 0001 "); 
    	Query qry = em.createQuery("DELETE FROM Sessions_Table");
    	int rows = qry.executeUpdate();
    	return rows;
	}
	
	/**
	 * Find the single Sessions_Table entity that matches the given session ID.<p>
	 *  
	 * @param sessionId Sessions_Table entity to find.
	 * @return Sessions_Table entity matching the given session ID. Returns null if the persistence context has no Sessions_Table entity
	 * that matches the given session ID and SESSIONS_TABLE database table has no row for the given session ID.
	 */
	@Override
	public Sessions_Table find(String sessionId) {
		System.out.println("com.yardi.ejb.SessionsTableBean.find() 0002 ");
		return em.find(Sessions_Table.class, sessionId);
	}
	
	/**
	 * Find all Full_Sessions_Table entities for the given user ID.<p> 
	 * 
	 * @param userID specifies which entities to find. 
	 * @return Vector of Full_Sessions_Table entities matching the given userID. Returns an empty Vector if the persistence context contains no Full_Sessions_Table
	 * entities matching the given user ID and the SESSIONS_TABLE database table has no rows matching the given user ID.
	 */
	public Vector<Full_Sessions_Table> findFull_Sessions_Table(String userID) {
		/*debug*/
		System.out.println("com.yardi.ejb.SessionsTableBean.findFull_Sessions_Table() 000B ");
		/*debug*/
		isJoined();
		Vector<Full_Sessions_Table> userSessions = new Vector<Full_Sessions_Table> ();
		TypedQuery<Full_Sessions_Table> qry = em.createQuery( 
	    		  "SELECT s from Full_Sessions_Table s "
	    		    		+ "WHERE s.stUserId = :userID ",
	    		    		Full_Sessions_Table.class
				);
		userSessions = (Vector<Full_Sessions_Table>)qry
				.setParameter("userID", userID)
				.getResultList();
		
		if (userSessions.size() > 0) {
			/*debug*/
			System.out.println("com.yardi.ejb.SessionsTableBean.findFull_Sessions_Table() 000F ");
			/*debug*/

			for (Full_Sessions_Table s : userSessions ) {
				/*debug*/
				System.out.println("com.yardi.ejb.SessionsTableBean.findFull_Sessions_Table() 0010 "
					+ "\n"
					+ "    Full_Sessions_Table=["
					+ "stUserId "
					+ s.getStUserId()
					+ ", stSesssionId "
					+ s.getStSessionId()
					+ ", stSessionToken "
					+ s.getStSessionToken()
					+ ", stLastRequest"
					+ s.getStLastRequest()
					+ ", stLastActive"
					+ s.getStLastActive()
					+ ", stRrn"
					+ s.getStRrn()
					+ "]"
				);
				/*debug*/
				isManaged(s);			
			}
		} else {
			/*debug*/
			System.out.println("com.yardi.ejb.SessionsTableBean.findFull_Sessions_Table() 0011 "
				+ "\n"
				+ "    userSessions is empty"
			);
			/*debug*/			
		}
		
		return userSessions;
	}
	
	
	/**
	 * Test whether given instance is an entity.<p>
	 * 
	 * @param clazz the instance to test. 
	 * @return boolean indicating whether the given instance is an entity.
	 */
	private boolean isEntity(Class<?> clazz) {
		System.out.println("com.yardi.ejb.SessionsTableBean.isEntity() 0014 ");
	    boolean foundEntity = false;
	    Set<EntityType<?>> entities = em.getMetamodel().getEntities();
	    
	    for(EntityType<?> entityType :entities) {
	        Class<?> entityClass = entityType.getJavaType();
	        
	        if(entityClass.equals(clazz)) {
	            foundEntity = true;
	        }
	    }
	    
		System.out.println("com.yardi.ejb.SessionsTableBean.isEntity() 0015 " + foundEntity);
	    return foundEntity;
	}
	
	/**
     * Log whether the entity manager is participating in a transaction.<p>
     * 
     * @return boolean indicating whether the entity manager is participating in a transaction. 
     */
	private boolean isJoined() {
  		System.out.println("com.yardi.ejb.SessionsTableBean.isJoined() 0003 "
  				+ "\n"
  				+ "   isJoined="
  				+ em.isJoinedToTransaction()
  				);
		return em.isJoinedToTransaction();
	}
	
	/**
	 * Log whether the given Full_Sessions_Table entity is managed.<p>
	 * 
	 * @param sessionsTable the instance to test.
	 *  
	 * @return boolean indicating whether the given Full_Sessions_Table is managed. Returns false if Full_Sessions_Table is null or 
	 * if Full_Sessions_Table is not an entity.
	 */			
	private boolean isManaged(Full_Sessions_Table sessionsTable) {
  		System.out.println("com.yardi.ejb.SessionsTableBean.isManaged() 0017 ");
		
		if (sessionsTable == null) {
	  		System.out.println("com.yardi.ejb.SessionsTableBean.isManaged() 0012 "
	  				+ "\n"
	  				+ "   em.contains(Full_Sessions_Tabe)=false"
	  				);
			return false;
		} 
		
  		if (isEntity(sessionsTable.getClass())==false) {
  	  		System.out.println(
	  				  "com.yardi.ejb.SessionsTableBean.isManaged() 0016 "
	  				+ "\n"
	  				+ "   em.contains(Full_Sessions_Tabe)=false"
	  				);
	  		return false;
  		}

  		System.out.println("com.yardi.ejb.SessionsTableBean.isManaged() 000C "
  				+ "\n"
  				+ "   em.contains(Full_Sessions_Tabe)="
  				+ em.contains(sessionsTable)
  				);
		return em.contains(sessionsTable);
	}

	/**
	 * Log whether the given Sessions_Table entity is managed.<p>
	 * 
	 * @param sessionsTable the instance to test.
	 * 
	 * @return boolean indicating whether the given Sessions_Table entity is managed. Returns false if Sessions_Table is null or 
	 * if Sessions_Table is not an entity.
	 */
	private boolean isManaged(Sessions_Table sessionsTable) {
  		System.out.println("com.yardi.ejb.SessionsTableBean.isManaged() 0018 ");
		
		if (sessionsTable == null) {
	  		System.out.println("com.yardi.ejb.SessionsTableBean.isManaged() 0013 "
	  				+ "\n"
	  				+ "   em.contains(Sessions_Table)=false"
	  				);
			return false;
		} 
			
  		if (isEntity(sessionsTable.getClass())==false) {
  	  		System.out.println(
	  				  "com.yardi.ejb.SessionsTableBean.isManaged() 0019 "
	  				+ "\n"
	  				+ "   em.contains(Sessions_Table)=false"
	  				);
	  		return false;
  		}

  		System.out.println("com.yardi.ejb.SessionsTableBean.isManaged() 0004 "
  				+ "\n"
  				+ "   em.contains(Sessions_Table)="
  				+ em.contains(sessionsTable)
  				);
		return em.contains(sessionsTable);
	}
	
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
	public void persist(
			String userID, 
			String sessionID, 
			String lastRequest, 
			java.sql.Timestamp lastActive
			) {
		//debug
		System.out.println("com.yardi.ejb.SessionsTableBean.persist() 0005 ");
		isJoined();
		//debug
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		String sessionToken="";
		try {
			sessionToken = passwordAuthentication.hash(sessionID.toCharArray());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		Sessions_Table sessionsTable = new Sessions_Table(
				userID, 
				sessionID, 
				sessionToken, 
				lastRequest, 
				lastActive
				); 
		System.out.println("com.yardi.ejb.SessionsTableBean.persist() 0006 "
				+ "\n"
				+ "   Sessions_Table=["
				+ "stUserId="
				+ sessionsTable.getStUserId()
				+ ", stSesssionId="
				+ sessionsTable.getStSessionId()
				+ ", stSessionToken="
				+ sessionsTable.getStSessionToken()
				+ ", stLastRequest="
				+ sessionsTable.getStLastRequest()
				+ ", stLastActive="
				+ sessionsTable.getStLastActive()
				+ "]"
				); 
		em.persist(sessionsTable);
		System.out.println("com.yardi.ejb.SessionsTableBean.persist() 0007 ");
		isJoined();
		isManaged(sessionsTable);
	}
    
	@PostConstruct
	private void postConstructCallback() {
		System.out.println("com.yardi.ejb.SessionsTableBean.postConstructCallback() 0008 ");
	}

	/**
	 * Remove the given Full_Sessions_Table entity.<p>
	 * 
	 * @param sessionsTable the Full_Sessions_Table entity to remove.
	 */
	public void remove(Full_Sessions_Table sessionsTable) {
		/*debug*/
		System.out.println("com.yardi.ejb.SessionsTableBean.remove() 000D ");
		/*debug*/
		isJoined();
		
		if (sessionsTable!=null) {
			/*debug*/
			System.out.println("com.yardi.ejb.SessionsTableBean.remove() 000E ");
			/*debug*/
			em.remove(sessionsTable);
			isManaged(sessionsTable);
		}
	}
	
	/**
	 * Reset the sequence column in YARDISEQ database table.<p>
	 * 
	 * SESSIONS_TABLE database table is cleared during application startup to reflect that no users have logged in yet. The value for the sequence 
	 * column is taken from YARDISEQ database table. Set the SEQVALUE column in YARDISEQ database table for the initial session.<br><br>
	 * 
	 * @return number of rows updated.
	 */
    public int resetSeq() {
		System.out.println("com.yardi.ejb.SessionsTableBean.resetSeq() 0009 "); 
    	Query qry = em.createQuery("UPDATE Yardi_Seq SET seqValue = 0 WHERE seqName = 'sessionsTableSeq'");
    	int rows = qry.executeUpdate();
    	return rows;
    }
	
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
	public void update(
			Sessions_Table sessionsTable,
			String sessionID,
			String lastRequest, 
			java.sql.Timestamp lastActive
			) {
		//debug
		System.out.println("com.yardi.ejb.SessionsTableBean.update() 000A "
				+ "\n"
				+ "    sessionID="
				+ sessionID
				+ "\n"
				+ "    lastRequest="
				+ lastRequest
				+ "\n"
				+ "    lastActive="
				+ lastActive
		);
		//debug
		isJoined();
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		String sessionToken="";
		try {
			sessionToken = passwordAuthentication.hash(sessionID.toCharArray());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		sessionsTable.setStSesssionId(sessionID);
		sessionsTable.setStSessionToken(sessionToken);
		sessionsTable.setStLastRequest(lastRequest);
		sessionsTable.setStLastActive(new java.sql.Timestamp(new java.util.Date().getTime()));
		Sessions_Table managedSessionsTable = em.merge(sessionsTable);
		managedSessionsTable.setStSesssionId(sessionsTable.getStSessionId());
		managedSessionsTable.setStSessionToken(sessionsTable.getStSessionToken());
		managedSessionsTable.setStLastRequest(sessionsTable.getStLastRequest());
		managedSessionsTable.setStLastActive(sessionsTable.getStLastActive());
		isManaged(managedSessionsTable);
	}
}
