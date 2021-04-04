package com.yardi.ejb;

import java.security.NoSuchAlgorithmException;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.yardi.ejb.model.Login_Sessions_Table;
import com.yardi.shared.userServices.PasswordAuthentication;

/**
 * Session Bean implementation class SessionsTableBean
 */
@Stateless
public class LoginSessionsTableBean implements LoginSessionsTable {
	@PersistenceContext(unitName="yardi")
	private EntityManager em;

    public LoginSessionsTableBean() {
    	//debug
    	System.out.println("com.yardi.ejb.LoginSessionsTableBean LoginSessionsTableBean() 0001 ");
    	//debug
    }
	
	public int clear() {
		System.out.println("com.yardi.ejb.LoginSessionsTableBean clear() 0002 "); 
    	Query qry = em.createQuery("DELETE FROM Sessions_Table");
    	int rows = qry.executeUpdate();
    	return rows;
	}
	
	private boolean isJoined() {
  		System.out.println("com.yardi.ejb.LoginSessionsTableBean isJoined() 0005 "
  				+ "\n"
  				+ "   isJoined="
  				+ em.isJoinedToTransaction()
  				);
		return em.isJoinedToTransaction();
	}
	
	private boolean isManaged(Login_Sessions_Table sessionsTabe) {
  		System.out.println("com.yardi.ejb.LoginSessionsTableBean isManaged() 0004 "
  				+ "\n"
  				+ "   em.contains(sessionsTabe)="
  				+ em.contains(sessionsTabe)
  				);
		return em.contains(sessionsTabe);
	}
	
	public void persist(
			String userID, 
			String sessionID, 
			String lastRequest, 
			java.sql.Timestamp lastActive
			) {
		//debug
		System.out.println("com.yardi.ejb.LoginSessionsTableBean persist() 0006 ");
		isJoined();
		//debug
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		String sessionToken="";
		try {
			sessionToken = passwordAuthentication.hash(sessionID.toCharArray());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		Login_Sessions_Table sessionsTable = new Login_Sessions_Table(
				userID, 
				sessionID, 
				sessionToken, 
				lastRequest, 
				lastActive
				); 
		System.out.println("com.yardi.ejb.LoginSessionsTableBean persist() 0003 "
				+ "\n"
				+ "   Login_Sessions_Table=["
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
		System.out.println("com.yardi.ejb.LoginSessionsTableBean persist() 0000 ");
		isJoined();
		isManaged(sessionsTable);
	}
	
	/**
     * Since the SESSIONS_TABLE is cleared at startup, reset sequence column in YARDISEQ
     * 
     * @return rows - number of rows updated
     */
    public int resetSeq() {
		System.out.println("com.yardi.ejb.LoginSessionsTableBean resetSeq() 0008 "); 
    	Query qry = em.createQuery("UPDATE Yardi_Seq SET seqValue = 0 WHERE seqName = 'sessionsTableSeq'");
    	int rows = qry.executeUpdate();
    	return rows;
    }
    
	public void update(
			Login_Sessions_Table sessionsTable,
			String sessionID,
			String lastRequest, 
			java.sql.Timestamp lastActive
			) {
		//debug
		System.out.println("com.yardi.ejb.LoginSessionsTableBean update() 0007 "
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
		Login_Sessions_Table managedSessionsTable = em.merge(sessionsTable);
		managedSessionsTable.setStSesssionId(sessionsTable.getStSessionId());
		managedSessionsTable.setStSessionToken(sessionsTable.getStSessionToken());
		managedSessionsTable.setStLastRequest(sessionsTable.getStLastRequest());
		managedSessionsTable.setStLastActive(sessionsTable.getStLastActive());
		isManaged(managedSessionsTable);
	}
}
