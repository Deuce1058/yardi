package com.yardi.ejb.model;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.*;

/**
 * Entity implementation class for database table SESSIONS_TABLE.<br><br>
 * 
 * <pre>Database table: SESSIONS_TABLE
 *Schema: DB2ADMIN</pre>
 */
@Entity
@Table(name="SESSIONS_TABLE", schema="DB2ADMIN")
public class Full_Sessions_Table implements Serializable {
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Column: ST_USER_ID 
	 */
	@Column(name="ST_USER_ID")
	private String stUserId;

	/**
	 * Column: ST_SESSSION_ID<p>
	 * ID column<br><br>
	 * 
	 * Contains <code>jakarta.servlet.http.HttpServletRequest.getSession().getId()</code>
	 */
	@Id
	@Column(name="ST_SESSSION_ID")
	private String stSesssionId;

	/**
	 * Column: ST_SESSION_TOKEN<p>
	 * Hashed session ID
	 */
	@Column(name="ST_SESSION_TOKEN")
	private String stSessionToken;

	/**
	 * Column: ST_LAST_REQUEST<p>
	 * 
	 * The most recent request. 
	 */
	@Column(name="ST_LAST_REQUEST")
	private String stLastRequest;

	/**
	 * Column: ST_LAST_ACTIVE<p>
	 * 
	 * Timestamp of most recent request.
	 */
	@Column(name="ST_LAST_ACTIVE")
	private java.sql.Timestamp stLastActive;

	/**
	 * Column: ST_RRN. <br>
	 * 
	 * <pre>
	 * YARDISEQ            Sequence table
	 * DB2ADMIN            Sequence table schema
	 * SEQNAME             Sequence table primary key column
	 * sessionsTableSeq    Sequence table primary key value
	 * SEQVALUE            Column that stores the last value generated
	 * </pre> 
	 */
	@TableGenerator(name="sessionsTableSeq",
			schema="DB2ADMIN",
			table="YARDISEQ",
			pkColumnName="SEQNAME",
			valueColumnName="SEQVALUE")
	@GeneratedValue(generator="sessionsTableSeq")
	@Column(name="ST_RRN")
	private long stRrn;
	
	/**
	 * Default constructor
	 */
	public Full_Sessions_Table() {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_Sessions_Table.Full_Sessions_Table() 0000 ");
		/*debug*/
	}

	/**
	 * Constructor using all fields.
	 * @param userID user ID
	 * @param sessionID the session ID. Equivalent to <code>jakarta.servlet.http.HttpServletRequest.getSession().getId()</code> 
	 * @param sessionToken the hashed sessionID
	 * @param lastRequest the most recent requested page
	 * @param lastActive Timestamp of the most recent activity
	 */
	public Full_Sessions_Table(
			String userID, 
			String sessionID, 
			String sessionToken, 
			String lastRequest, 
			java.sql.Timestamp lastActive
			) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_Sessions_Table.Full_Sessions_Table() 0001 ");
		/*debug*/
		stUserId       = userID;
		stSesssionId   = sessionID;
		stSessionToken = sessionToken;
		stLastRequest  = lastRequest;
		stLastActive   = new java.sql.Timestamp(lastActive.getTime());
	}

	/**
	 * Return date and time of most recent activity  
	 * @return date and time of most recent activity
	 */
	public Timestamp getStLastActive() {
		return this.stLastActive;
	}

	/**
	 * Return most recent request
	 * @return most recent request
	 */
	public String getStLastRequest() {
		return this.stLastRequest;
	}

	/**
	 * Return relative record number
	 * @return relative record number
	 */
	public long getStRrn() {
		return this.stRrn;
	}

	/** 
	 * Return session ID. Equivalent to <code>jakarta.servlet.http.HttpServletRequest.getSession().getId()</code>
	 * @return session ID
	 */
	public String getStSessionId() {
		return this.stSesssionId;
	}

	/**
	 * Return session token
	 * @return session token
	 */
	public String getStSessionToken() {
		return this.stSessionToken;
	}

	/**
	 * Return user ID
	 * @return user ID
	 */
	public String getStUserId() {
		return this.stUserId;
	}

	/**
	 * Set the date and time of the most recent activity to the given <code>Timestamp</code>
	 * @param stLastActive date and time of the most recent activity
	 */
	public void setStLastActive(java.sql.Timestamp stLastActive) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_Sessions_Table.setStLastActive() 0002 ");
		/*debug*/
		this.stLastActive = stLastActive;
	}

	/**
	 * Set most recent request to the given String
	 * @param stLastRequest most recent request
	 */
	public void setStLastRequest(String stLastRequest) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_Sessions_Table.setStLastRequest() 0003 ");
		/*debug*/
		this.stLastRequest = stLastRequest;
	}

	/**
	 * Set session token to the given <code>String</code>
	 * @param stSessionToken session token
	 */
	public void setStSessionToken(String stSessionToken) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_Sessions_Table.setStSessionToken() 0004 ");
		/*debug*/
		this.stSessionToken = stSessionToken;
	}

	/**
	 * Set session ID to the given String.<p>
	 * Session ID is equivalent to <code>jakarta.servlet.http.HttpServletRequest.getSession().getId()</code>
	 * @param stSesssionId session ID
	 */
	public void setStSesssionId(String stSesssionId) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_Sessions_Table.setStSesssionId() 0005 ");
		/*debug*/
		this.stSesssionId = stSesssionId;
	}

	/**
	 * Set user ID to the given String
	 * @param stUserId user ID
	 */
	public void setStUserId(String stUserId) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_Sessions_Table.setStUserId() 0006 ");
		/*debug*/
		this.stUserId = stUserId;
	}

	@Override
	public String toString() {
		return "Full_Sessions_Table [stUserId=" + stUserId + ", stSesssionId=" + stSesssionId + ", stSessionToken="
				+ stSessionToken + ", stLastRequest=" + stLastRequest + ", stLastActive=" + stLastActive + ", stRrn="
				+ stRrn + "]";
	}
}
