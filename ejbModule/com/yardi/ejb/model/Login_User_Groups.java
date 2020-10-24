package com.yardi.ejb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * <p>Persistence class for tying together USER_PROFILE, USER_GROUPS, GROUPS_MASTER, and SESSIONS_TABLE. 
 * Columns from these tables are used each time a user logs in.</p>
 * <br>
 * Because UNIQUE_TOKENS is used only when the user is changing their password, it is not included here 
 * 
 * @author Jim
 */
@Entity
@Table(name="USER_GROUPS", schema="DB2ADMIN")
public class Login_User_Groups implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="UG_USER_ID")
	private String ugUserId;

	@Column(name="UG_GROUP")
	private int ugGroup;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="UG_RRN")
	private long ugRrn;

	@ManyToOne
	@JoinColumn(name = "UG_GROUP", referencedColumnName = "GM_TYPE",      nullable=false, updatable=false, insertable=false)
	private Login_Groups_Master ugGroupsMaster;

	@ManyToOne
	@JoinColumn(name = "UG_USER_ID", referencedColumnName = "UP_USERID",  nullable=false, updatable=false, insertable=false)
	private Login_User_Profile ugUserProfile;

	public Login_User_Groups getLoginUserGroups() {
		//debug
		System.out.println("com.yardi.ejb.Login_User_Groups getLoginUserGroups() 0000");
		//debug
		return this;
	}

	public int getUgGroup() {
		return this.ugGroup;
	}

	public Login_Groups_Master getUgGroupsMaster() {
		//debug
		System.out.println("com.yardi.ejb.Login_User_Groups getUgGroupsMaster() 0001");
		//debug
		return ugGroupsMaster;
	}

	public long getUgRrn() {
		return this.ugRrn;
	}

	public String getUgUserId() {
		return this.ugUserId;
	}

	public Login_User_Profile getUgUserProfile() {
		//debug
		System.out.println("com.yardi.ejb.Login_User_Groups getUgUserProfile() 0003");
		//debug
		return ugUserProfile;
	}
	
	@Override
	public String toString() {
		return "Login_User_Groups [ugUserId=" + ugUserId + ", ugGroup=" + ugGroup + ", ugRrn=" + ugRrn + ", ugGroupsMaster="
				+ ugGroupsMaster + "]";
	}
}
