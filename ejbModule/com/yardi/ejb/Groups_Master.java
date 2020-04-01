package com.yardi.ejb;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the GROUPS_MASTER database table.
 * 
 */
@Entity
@Table(name="GROUPS_MASTER", schema="DB2ADMIN")
@NamedQuery(name="Groups_Master.findAll", query="SELECT g FROM Groups_Master g")
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
	}

	public int getGmType() {
		return this.gmType;
	}

	public void setGmType(int gmType) {
		this.gmType = gmType;
	}

	public String getGmDescription() {
		return this.gmDescription;
	}

	public void setGmDescription(String gmDescription) {
		this.gmDescription = gmDescription;
	}

	public String getGmInitialPage() {
		return this.gmInitialPage;
	}

	public void setGmInitialPage(String gmInitialPage) {
		this.gmInitialPage = gmInitialPage;
	}

	public long getGmRrn() {
		return this.gmRrn;
	}

	public void setGmRrn(long gmRrn) {
		this.gmRrn = gmRrn;
	}

	public List<User_Groups> getGmUserGroups() {
		return gmUserGroups;
	}

	public void setGmUserGroups(List<User_Groups> gmUserGroups) {
		this.gmUserGroups = gmUserGroups;
	}

	@Override
	public String toString() {
		return "Groups_Master [gmType=" + gmType + ", gmDescription=" + gmDescription + ", gmInitialPage="
				+ gmInitialPage + ", gmRrn=" + gmRrn + ", gmUserGroups=" + gmUserGroups + "]";
	}
}
