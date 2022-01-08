package com.yardi.ejb.model;
/*
 * 2020 1104
 * added attribute for Login_Sessions_Table
 * added getter for Login_Sessions_Table
 */
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
	
	@ManyToOne
	@JoinColumn(name = "UG_USER_ID", referencedColumnName = "ST_USER_ID", nullable=false, updatable=false, insertable=false)
	private Login_Sessions_Table ugSessionsTable;
	
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

	public Login_Sessions_Table getUgSessionsTable() {
		/*debug*/
		System.out.println("com.yardi.ejb.Login_User_Groups getUgSessionTable() 0002 ");
		/*debug*/
		return ugSessionsTable;
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
		return "Login_User_Groups [ugUserId=" + ugUserId + ", ugGroup=" + ugGroup + ", ugRrn=" + ugRrn + ","
				+ "\n    ugGroupsMaster.gmType=" 
				+ ugGroupsMaster.getGmType()
				+ ", ugGroupsMaster.gmDescription=" 
				+ ugGroupsMaster.getGmDescription()
				+ ", ugGroupsMaster.gmInitialPage="
				+ ugGroupsMaster.getGmInitialPage()
				+ ", ugGroupsMaster.gmRrn="
				+ ugGroupsMaster.getGmRrn()
				+ ",\n    ugUserProfile.upUserid="
				+ ugUserProfile.getUpUserid()
				+ ", ugUserProfile.uptoken="
				+ ugUserProfile.getUptoken()
				+ ", ugUserProfile.upPwdexpd="
				+ ugUserProfile.getUpPwdexpd()
				+ ", ugUserProfile.upPwdAttempts="
				+ ugUserProfile.getUpPwdAttempts()
				+ ", ugUserProfile.upDisabledDate="
				+ ugUserProfile.getUpDisabledDate()
				+ ", ugUserProfile.getUpActiveYn="
				+ ugUserProfile.getUpActiveYn()
				+ ",\n    ugSessionsTable.stUserId="
				+ ugSessionsTable.getStUserId()
				+ ", ugSessionsTable.stSessionId="
				+ ugSessionsTable.getStSessionId()
				+ ", ugSessionsTable.stSessionToken="
				+ ugSessionsTable.getStSessionToken()
				+ ", ugSessionsTable.stLastRequest="
				+ ugSessionsTable.getStLastRequest()
				+ ", ugSessionsTable.stLastActive="
				+ ugSessionsTable.getStLastActive()
				+ ", ugSessionsTable.stRrn="
				+ ugSessionsTable.getStRrn()
				+ "]";
	}
}
