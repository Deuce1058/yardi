package com.yardi.ejb.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;


/**
 * The persistent class for the GROUPS_MASTER database table.
 * 
 */
@Entity
@Table(name="GROUPS_MASTER", schema="DB2ADMIN")
public class Groups_Master implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="GM_TYPE")
	private int gmType;

	@Column(name="GM_DESCRIPTION")
	private String gmDescription;

	@Column(name="GM_INITIAL_PAGE")
	private String gmInitialPage;

	@Column(name="GM_RRN")
	private long gmRrn;
	
	@OneToMany(mappedBy = "ugGroupsMaster")
	private List<User_Groups> gmUserGroups;

	public Groups_Master() {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Groups_Master.Login_Groups_Master() 0000");
		/*debug*/
	}

	public String getGmDescription() {
		return this.gmDescription;
	}

	public String getGmInitialPage() {
		return this.gmInitialPage;
	}

	public long getGmRrn() {
		return this.gmRrn;
	}

	public int getGmType() {
		return this.gmType;
	}

	public List<User_Groups> getGmUserGroups() {
		return gmUserGroups;
	}

	@Override
	public String toString() {
		return "Groups_Master [gmType=" + gmType + ", gmDescription=" + gmDescription + ", gmInitialPage="
				+ gmInitialPage + ", gmRrn=" + gmRrn + ", gmUserGroups=" + gmUserGroups + "]";
	}
}
