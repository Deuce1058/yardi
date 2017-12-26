package com.yardi.ejb;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the GROUPS_MASTER database table.
 * 
 */
@Entity
@Table(name="GROUPS_MASTER")
@NamedQuery(name="GroupsMaster.findAll", query="SELECT g FROM GroupsMaster g")
public class GroupsMaster implements Serializable {
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
	private List<UserGroups> gmUserGroups;

	public GroupsMaster() {
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

	public List<UserGroups> getGmUserGroups() {
		return gmUserGroups;
	}

	public void setGmUserGroups(List<UserGroups> gmUserGroups) {
		this.gmUserGroups = gmUserGroups;
	}

	@Override
	public String toString() {
		return "GroupsMaster [gmType=" + gmType + ", gmDescription=" + gmDescription + ", gmInitialPage="
				+ gmInitialPage + ", gmRrn=" + gmRrn + ", gmUserGroups=" + gmUserGroups + "]";
	}
}