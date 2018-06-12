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
 * Session Bean implementation class SessionsTableBean
 */
@Stateless
public class SessionsTableBean implements SessionsTable {
	@PersistenceContext(unitName="yardi")
	private EntityManager em;

    public SessionsTableBean() {
    }
	
	public int persist(
			String userID, 
			String sessionID, 
			String sessionToken, 
			String lastRequest, 
			java.util.Date lastActive
			) {
		
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("yardi");
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
		System.out.println("com.yardi.ejb.SessionsTableBean clear() 0002"); 
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("yardi");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
    	Query qry = em.createQuery("DELETE FROM Sessions_Table");
    	int rows = qry.executeUpdate();
    	return rows;
	}
	
	public Sessions_Table findSession(String sessionID) {
		/*
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("yardi");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED);
    	*/ 
		Sessions_Table sessionsTable = null;
		TypedQuery<Sessions_Table> qry = em.createQuery("SELECT s from Sessions_Table s "
			+ "WHERE s.stSesssionId = :sessionID",
			Sessions_Table.class);
		try {
			sessionsTable = qry
				.setParameter("sessionID", sessionID)
				.getSingleResult();
		} catch (Exception e) {
			//getSingleResult() may not find anything
			System.out.println("com.yardi.ejb.SessionsTableBean findSession() 0000 exception");
			e.printStackTrace();
		}
		return sessionsTable;
	}
	
	public Sessions_Table findUser(String userID) {
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("yardi");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
		Sessions_Table sessionsTable = null;
		TypedQuery<Sessions_Table> qry = em.createQuery("SELECT s from Sessions_Table s "
			+ "WHERE s.stUserId = :userID",
			Sessions_Table.class);
		try {
			sessionsTable = qry
				.setParameter("userID", userID)
				.getSingleResult();
		} catch (Exception e) {
			System.out.println("com.yardi.ejb.SessionsTableBean findUser() 0001  exception"); 
			e.printStackTrace();
		}
		return sessionsTable;
	}

	public int update(
			String userID,
			String sessionID,
			String sessionToken, 
			String lastRequest, 
			java.util.Date lastActive
			) {
		
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory("yardi");
    	EntityManager em = emf.createEntityManager(SynchronizationType.SYNCHRONIZED); 
    	Query qry = em.createQuery("UPDATE Sessions_Table "
    		+ "SET stUserId = :userID, "
    		+ "stSessionToken = :sessionToken, "
    		+ "stLastRequest = :lastRequest, "
    		+ "stLastActive = :lastActive "
    		+ "WHERE "
    		+ "stSesssionId = :sessionID"
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
