package com.yardi.ejb.model;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.*;

/**
 * Entity implementation class for Entity: Full_Sessions_Table.<br><br>
 * 
 * <pre>Database table: SESSIONS_TABLE
 *Schema: DB2ADMIN</pre>
 */
@Entity
@Table(name="SESSIONS_TABLE", schema="DB2ADMIN")
public class Full_Sessions_Table implements Serializable {
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
	 * Contains HttpServletRequest.getSession().getId()
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
	
	public Full_Sessions_Table() {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_Sessions_Table.Full_Sessions_Table() 0000 ");
		/*debug*/
	}

	/**
	 * Constructor using all fields.
	 * @param userID user ID
	 * @param sessionID the session ID. Equivalent to HttpServletRequest.getSession().getId() 
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

	public Timestamp getStLastActive() {
		return this.stLastActive;
	}

	public String getStLastRequest() {
		return this.stLastRequest;
	}

	public long getStRrn() {
		return this.stRrn;
	}

	public String getStSessionId() {
		return this.stSesssionId;
	}

	public String getStSessionToken() {
		return this.stSessionToken;
	}

	public String getStUserId() {
		return this.stUserId;
	}

	public void setStLastActive(java.sql.Timestamp stLastActive) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_Sessions_Table.setStLastActive() 0002 ");
		/*debug*/
		this.stLastActive = stLastActive;
	}

	public void setStLastRequest(String stLastRequest) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_Sessions_Table.setStLastRequest() 0003 ");
		/*debug*/
		this.stLastRequest = stLastRequest;
	}

	public void setStSessionToken(String stSessionToken) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_Sessions_Table.setStSessionToken() 0004 ");
		/*debug*/
		this.stSessionToken = stSessionToken;
	}

	public void setStSesssionId(String stSesssionId) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Full_Sessions_Table.setStSesssionId() 0005 ");
		/*debug*/
		this.stSesssionId = stSesssionId;
	}

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
