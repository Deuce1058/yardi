package com.yardi.ejb.model;
/*
 * 2020 1104
 * added attribute for Sessions_Table
 * added getter for Sessions_Table
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
 * Persistence class for tying together the following database tables: USER_PROFILE, USER_GROUPS, GROUPS_MASTER, and SESSIONS_TABLE.<p> 
 * 
 * Columns from these tables are used each time a user logs in. Only columns that are needed for login are used.<br>
 * Because database table UNIQUE_TOKENS is used only when the user is changing their password, it is not included here. 
 */
@Entity
@Table(name="USER_GROUPS", schema="DB2ADMIN")
public class User_Groups implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * User ID
	 * <pre style="font-family:consolas;">
	 * Database table: USER_GROUPS
	 * Schema:         DB2ADMIN
	 * Column:         UG_USER_ID
	 * </pre> 
	 */
	@Column(name="UG_USER_ID")
	private String ugUserId;

	/**
	 * Group ID
	 * <pre style="font-family:consolas;">
	 * Database table: USER_GROUPS
	 * Schema:         DB2ADMIN
	 * Column:         UG_GROUP
	 * </pre> 
	 */
	@Column(name="UG_GROUP")
	private int ugGroup;

	/**
	 * Primary key. Relative record number
	 * <pre style="font-family:consolas;">
	 * Database table: USER_GROUPS
	 * Schema:         DB2ADMIN
	 * Column:         UG_RRN
	 * </pre> 
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="UG_RRN")
	private long ugRrn;

	/**
	 * Reference to Groups_Master entity.
	 * <pre style="font-family:consolas;">
	 * Database tables: USER_GROUPS, GROUPS_MASTER
	 * Schema:          DB2ADMIN
	 * Join columns:    USER_GROUPS/UG_GROUP, GROUPS_MASTER/GM_TYPE  
	 * </pre> 
	 * 
	 * User_Groups entity has a bidirectional, many to one relationship with Groups_Master entity. Field <i>ugGroupsMaster</i> defines the owning side.
	 */
	@ManyToOne
	@JoinColumn(name = "UG_GROUP", referencedColumnName = "GM_TYPE",      nullable=false, updatable=false, insertable=false)
	private Groups_Master ugGroupsMaster;

	/**
	 * Reference to User_Profile entity.
	 * <pre style="font-family:consolas;">
	 * Database tables: USER_GROUPS, USER_PROFILE
	 * Schema:          DB2ADMIN
	 * Join columns:    USER_GROUPS/UG_USER_ID, USER_PROFILE/UP_USERID  
	 * </pre> 
	 * 
	 * User_Groups entity has a bidirectional, many to one relationship with User_Profile entity. Field <i>ugUserProfile</i> defines the owning side.
	 */
	@ManyToOne
	@JoinColumn(name = "UG_USER_ID", referencedColumnName = "UP_USERID",  nullable=false, updatable=false, insertable=false)
	private User_Profile ugUserProfile;
	
	/**
	 * Reference to Sessions_Table entity.
	 * <pre style="font-family:consolas;">
	 * Database tables: USER_GROUPS, SESSIONS_TABLE
	 * Schema:          DB2ADMIN
	 * Join columns:    USER_GROUPS/UG_USER_ID, SESSIONS_TABLE/ST_USER_ID  
	 * </pre> 
	 * 
	 * User_Groups entity has a bidirectional, many to one relationship with Sessions_Table entity. Field <i>ugSessionsTable</i> defines the owning side.
	 */
	@ManyToOne
	@JoinColumn(name = "UG_USER_ID", referencedColumnName = "ST_USER_ID", nullable=false, updatable=false, insertable=false)
	private Sessions_Table ugSessionsTable;
	
	/**
	 * Return a reference to User_Groups entity for convenience 
	 * @return reference to User_Groups entity
	 */
	public User_Groups getLoginUserGroups() {
		//debug
		System.out.println("com.yardi.ejb.Login_User_Groups getLoginUserGroups() 0000");
		//debug
		return this;
	}

	
	/**
	 * Return the group ID 
	 * @return group ID
	 */
	public int getUgGroup() {
		return this.ugGroup;
	}

	/** 
	 * Return a reference to the embeded Groups_Master entity for convenience  
	 * @return reference to the embeded Groups_Master entity
	 */
	public Groups_Master getUgGroupsMaster() {
		//debug
		System.out.println("com.yardi.ejb.Login_User_Groups getUgGroupsMaster() 0001");
		//debug
		return ugGroupsMaster;
	}

	/**
	 * Return the relative record number
	 * @return record number
	 */
	public long getUgRrn() {
		return this.ugRrn;
	}

	/**
	 * Return a reference to the embeded Sessions_Table entity for convenience  
	 * @return reference to the embeded Sessions_Table
	 */
	public Sessions_Table getUgSessionsTable() {
		/*debug*/
		System.out.println("com.yardi.ejb.Login_User_Groups getUgSessionTable() 0002 ");
		/*debug*/
		return ugSessionsTable;
	}

	/**
	 * Return the user ID
	 * @return user ID
	 */
	public String getUgUserId() {
		return this.ugUserId;
	}
	
	/**
	 * Return a reference to the embeded User_Profile entity for convenience  
	 * @return reference to the embeded User_Profile entity
	 */
	public User_Profile getUgUserProfile() {
		//debug
		System.out.println("com.yardi.ejb.Login_User_Groups getUgUserProfile() 0003");
		//debug
		return ugUserProfile;
	}

	
	@Override
	public String toString() {
		return "User_Groups [ugUserId=" + ugUserId + ", ugGroup=" + ugGroup + ", ugRrn=" + ugRrn + ","
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
