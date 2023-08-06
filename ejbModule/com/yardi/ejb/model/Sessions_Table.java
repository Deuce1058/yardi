package com.yardi.ejb.model;

/*
 * 2020 1104
 * added bidirectional OneToMany association with User_Groups
 * removed association for User_Profile
 */
import java.io.Serializable;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the SESSIONS_TABLE database table.
 * 
 * <pre>Database table: SESSIONS_TABLE
 *Schema:         DB2ADMIN</pre>
 */
@Entity
@Table(name="SESSIONS_TABLE", schema="DB2ADMIN")
public class Sessions_Table implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Column: ST_USER_ID User ID
	 */
	@Column(name="ST_USER_ID")
	private String stUserId;

	/**
	 * Column: ST_SESSSION_ID 
	 * Session ID.
	 * Primary key
	 */
	@Id
	@Column(name="ST_SESSSION_ID")
	private String stSesssionId;

	/**
	 * Column: ST_SESSION_TOKEN
	 * Session token 
	 */
	@Column(name="ST_SESSION_TOKEN")
	private String stSessionToken;

	/**
	 * Column: ST_LAST_REQUEST
	 * Most recent request
	 */
	@Column(name="ST_LAST_REQUEST")
	private String stLastRequest;

	/**
	 * Column: ST_LAST_ACTIVE
	 * Date and time of most recent activity 
	 */
	@Column(name="ST_LAST_ACTIVE")
	private java.sql.Timestamp stLastActive;

	/**
	 * Relative record number
     *
	 * <pre>
	 * Column name:                 ST_RRN
	 * Sequence table:              YARDISEQ
	 * Sequence table schema:       DB2ADMIN
	 * Sequence table primary key:  SEQNAME
	 * Sequence table generator:    sessionsTableSeq
	 * Sequence table value column: SEQVALUE
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
	 * Reference to User_Groups entity.
	 * <pre>
	 * Database tables: USER_GROUPS
	 * Schema:          DB2ADMIN
	 * Mapped by:       ugSessionsTable
	 * </pre> 
	 * 
	 * Sessions_Table has a bidirectional, one to many relationship with User_Groups entity. Field <i>stUserGroups</i> defines the inverse side.
	 */
	@OneToMany(mappedBy = "ugSessionsTable")
	private List<User_Groups> stUserGroups;
	
	public Sessions_Table() {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Sessions_Table.Sessions_Table() 0001");
		/*debug*/
	}

	/**
	 * Constructor using all fields except stRrn and stUserGroups
	 * @param userID user ID
	 * @param sessionID session ID. Equivalent to HttpServletRequest.getSession().getId()
	 * @param sessionToken hashed session ID
	 * @param lastRequest most recent request
	 * @param lastActive date and time of most recent activity
	 */
	public Sessions_Table(
			String userID, 
			String sessionID, 
			String sessionToken, 
			String lastRequest, 
			java.sql.Timestamp lastActive
			) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Sessions_Table.Sessions_Table() 0000");
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
	 * Return session ID. Equivalent to HttpServletRequest.getSession().getId()
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
	 * Set the date and time of the most recent activity to the given Timestamp
	 * @param stLastActive date and time of the most recent activity
	 */
	public void setStLastActive(java.sql.Timestamp stLastActive) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Sessions_Table.setStLastActive() 0000");
		/*debug*/
		this.stLastActive = stLastActive;
	}

	/**
	 * Set most recent request to the given String
	 * @param stLastRequest most recent request
	 */
	public void setStLastRequest(String stLastRequest) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Sessions_Table.setStLastRequest() 0000");
		/*debug*/
		this.stLastRequest = stLastRequest;
	}

	/**
	 * Set session token to the given String
	 * @param stSessionToken session token
	 */
	public void setStSessionToken(String stSessionToken) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Sessions_Table.setStSessionToken() 0000");
		/*debug*/
		this.stSessionToken = stSessionToken;
	}

	/**
	 * Set session ID to the given String.<p>
	 * Session ID is equivalent to HttpServletRequest.getSession().getId()
	 * @param stSesssionId session ID
	 */
	public void setStSesssionId(String stSesssionId) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Sessions_Table.setStSesssionId() 0000");
		/*debug*/
		this.stSesssionId = stSesssionId;
	}

	/**
	 * Set user ID to the given String
	 * @param stUserId user ID
	 */
	public void setStUserId(String stUserId) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Sessions_Table.setStUserId() 0000");
		/*debug*/
		this.stUserId = stUserId;
	}
}
