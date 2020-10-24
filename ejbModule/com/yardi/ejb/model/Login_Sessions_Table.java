package com.yardi.ejb.model;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;


/**
 * The persistent class for the SESSIONS_TABLE database table.
 * 
 */
@Entity
@Table(name="SESSIONS_TABLE", schema="DB2ADMIN")
public class Login_Sessions_Table implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ST_USER_ID")
	private String stUserId;

	@Id
	@Column(name="ST_SESSSION_ID")
	private String stSesssionId;

	@Column(name="ST_SESSION_TOKEN")
	private String stSessionToken;

	@Column(name="ST_LAST_REQUEST")
	private String stLastRequest;

	@Column(name="ST_LAST_ACTIVE")
	private java.sql.Timestamp stLastActive;

	@TableGenerator(name="sessionsTableSeq",
			schema="DB2ADMIN",
			table="YARDISEQ",
			pkColumnName="SEQNAME",
			valueColumnName="SEQVALUE")
	@GeneratedValue(generator="sessionsTableSeq")
	@Column(name="ST_RRN")
	private long stRrn;

	@OneToOne(mappedBy = "ugSessionTable", fetch=FetchType.LAZY)
	private Login_User_Profile stUserProfile;

	public Login_Sessions_Table() {
	}

	public Login_Sessions_Table(
			String userID, 
			String sessionID, 
			String sessionToken, 
			String lastRequest, 
			java.sql.Timestamp lastActive
			) {
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
		this.stLastActive = stLastActive;
	}

	public void setStLastRequest(String stLastRequest) {
		this.stLastRequest = stLastRequest;
	}

	public void setStSessionToken(String stSessionToken) {
		this.stSessionToken = stSessionToken;
	}

	public void setStSesssionId(String stSesssionId) {
		this.stSesssionId = stSesssionId;
	}

	public void setStUserId(String stUserId) {
		this.stUserId = stUserId;
	}
}
