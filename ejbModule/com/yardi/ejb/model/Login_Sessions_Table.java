package com.yardi.ejb.model;

/*
 * 2020 1104
 * added bidirectional OneToMany association with Login_User_Groups
 * removed association for Login_User_Profile
 */
import java.io.Serializable;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;


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
	
	@OneToMany(mappedBy = "ugSessionsTable")
	private List<Login_User_Groups> stUserGroups;
	
	public Login_Sessions_Table() {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Login_Sessions_Table.Login_Sessions_Table() 0001");
		/*debug*/
	}

	public Login_Sessions_Table(
			String userID, 
			String sessionID, 
			String sessionToken, 
			String lastRequest, 
			java.sql.Timestamp lastActive
			) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Login_Sessions_Table.Login_Sessions_Table() 0000");
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
		System.out.println("com.yardi.ejb.model.Login_Sessions_Table.setStLastActive() 0000");
		/*debug*/
		this.stLastActive = stLastActive;
	}

	public void setStLastRequest(String stLastRequest) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Login_Sessions_Table.setStLastRequest() 0000");
		/*debug*/
		this.stLastRequest = stLastRequest;
	}

	public void setStSessionToken(String stSessionToken) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Login_Sessions_Table.setStSessionToken() 0000");
		/*debug*/
		this.stSessionToken = stSessionToken;
	}

	public void setStSesssionId(String stSesssionId) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Login_Sessions_Table.setStSesssionId() 0000");
		/*debug*/
		this.stSesssionId = stSesssionId;
	}

	public void setStUserId(String stUserId) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Login_Sessions_Table.setStUserId() 0000");
		/*debug*/
		this.stUserId = stUserId;
	}
}
