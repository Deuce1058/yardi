package com.yardi.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the SESSIONS_TABLE database table.
 * 
 */
@Entity
@Table(name="SESSIONS_TABLE")
@NamedQuery(name="Sessions_Table.findAll", query="SELECT s FROM Sessions_Table s")
public class Sessions_Table implements Serializable {
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
	private Timestamp stLastActive;

	@GeneratedValue
	@Column(name="ST_RRN")
	private long stRrn;

	public Sessions_Table() {
	}

	public String getStSesssionId() {
		return this.stSesssionId;
	}

	public void setStSesssionId(String stSesssionId) {
		this.stSesssionId = stSesssionId;
	}

	public Timestamp getStLastActive() {
		return this.stLastActive;
	}

	public void setStLastActive(Timestamp stLastActive) {
		this.stLastActive = stLastActive;
	}

	public String getStLastRequest() {
		return this.stLastRequest;
	}

	public void setStLastRequest(String stLastRequest) {
		this.stLastRequest = stLastRequest;
	}

	public long getStRrn() {
		return this.stRrn;
	}

	public void setStRrn(long stRrn) {
		this.stRrn = stRrn;
	}

	public String getStSessionToken() {
		return this.stSessionToken;
	}

	public void setStSessionToken(String stSessionToken) {
		this.stSessionToken = stSessionToken;
	}

	public String getStUserId() {
		return this.stUserId;
	}

	public void setStUserId(String stUserId) {
		this.stUserId = stUserId;
	}
}