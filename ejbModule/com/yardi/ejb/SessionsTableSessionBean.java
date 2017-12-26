package com.yardi.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

/**
 * Session Bean implementation class SessionsTableSessionBean
 */
@Stateless
public class SessionsTableSessionBean implements SessionsTableSessionBeanRemote {
	@PersistenceContext(unitName="sessionsTable")
	private EntityManager em;

    public SessionsTableSessionBean() {
    }
	
	public int persist(
			String userID, 
			String sessionID, 
			String sessionToken, 
			String lastRequest, 
			java.util.Date lastActive
			) {
		
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("sessionsTable");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
    	Query qry = em.createNativeQuery("INSERT INTO DB2ADMIN.SESSIONS_TABLE "
    		+ "("
    		+ "ST_USER_ID, "
    		+ "ST_SESSSION_ID, "
    		+ "ST_SESSION_TOKEN, "
    		+ "ST_LAST_REQUEST, "
    		+ "ST_LAST_ACTIVE "
    		+ ") VALUES("
    		+ "?, ?, ?, ?, ?"
    		+ ")"
    		);
    	int rows = qry
       		.setParameter(1, userID)
       		.setParameter(2, sessionID)
       		.setParameter(3, sessionToken)
       		.setParameter(4, lastRequest)
       		.setParameter(5, lastActive, TemporalType.TIMESTAMP)
       		.executeUpdate();
    	return rows;
	}
	
	public int clear() {
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("sessionsTable");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
    	Query qry = em.createQuery("DELETE FROM SessionsTable");
    	int rows = qry.executeUpdate();
    	return rows;
	}
	
	public SessionsTable findSession(String sessionID) {
		/*
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("sessionsTable");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED);
    	*/ 
		SessionsTable sessionsTable = null;
		TypedQuery<SessionsTable> qry = em.createQuery("SELECT s from SessionsTable s "
			+ "WHERE s.stSesssionId = :sessionID",
			SessionsTable.class);
		try {
			sessionsTable = qry
				.setParameter("sessionID", sessionID)
				.getSingleResult();
		} catch (Exception e) {
			//getSingleResult() may not find anything
			System.out.println("com.yardi.ejb.SessionsTableSessionBean findSession() 0000 exception");
			e.printStackTrace();
		}
		return sessionsTable;
	}
	
	public SessionsTable findUser(String userID) {
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("sessionsTable");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
		SessionsTable sessionsTable = null;
		TypedQuery<SessionsTable> qry = em.createQuery("SELECT s from SessionsTable s "
			+ "WHERE s.stUserId = :userID",
			SessionsTable.class);
		sessionsTable = qry
			.setParameter("userID", userID)
			.getSingleResult();
		return sessionsTable;
	}

	public int update(
			String userID,
			String sessionID,
			String sessionToken, 
			String lastRequest, 
			java.util.Date lastActive
			) {
		
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("sessionsTable");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
    	Query qry = em.createQuery("UPDATE SessionsTable "
    		+ "SET stUserId = :userID, "
    		+ "s.stSessionToken = :sessionToken, "
    		+ "stLastRequest = :lastRequest, "
    		+ "stLastActive = :stLastActive "
    		+ "WHERE "
    		+ "s.stSesssionId = :sessionID"
    		);
    	int rows = qry
       		.setParameter("userID", userID)
       		.setParameter("sessionToken", sessionToken)
       		.setParameter("lastRequest", lastRequest)
       		.setParameter("lastActive", lastActive, TemporalType.TIMESTAMP)
       		.setParameter("sessionID", sessionID)
       		.executeUpdate();
    	return rows;
	}
}
