package com.yardi.ejb;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;


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
		/*debug*/
		System.out.println("com.yardi.ejb.Groups_Master.Groups_Master() 0000");
		/*debug*/
	}

	public int getGmType() {
		return this.gmType;
	}

	public void setGmType(int gmType) {
		/*debug*/
		System.out.println("com.yardi.ejb.Groups_Master.setGmType() 0000");
		/*debug*/
		this.gmType = gmType;
	}

	public String getGmDescription() {
		return this.gmDescription;
	}

	public void setGmDescription(String gmDescription) {
		/*debug*/
		System.out.println("com.yardi.setGmDescription() 0000");
		/*debug*/
		this.gmDescription = gmDescription;
	}

	public String getGmInitialPage() {
		return this.gmInitialPage;
	}

	public void setGmInitialPage(String gmInitialPage) {
		/*debug*/
		System.out.println("com.yardi.ejb.Groups_Master.setGmInitialPage() 0000");
		/*debug*/
		this.gmInitialPage = gmInitialPage;
	}

	public long getGmRrn() {
		return this.gmRrn;
	}

	public void setGmRrn(long gmRrn) {
		/*debug*/
		System.out.println("com.yardi.ejb.Groups_Master.setGmRrn() 0000");
		/*debug*/
		this.gmRrn = gmRrn;
	}

	public List<User_Groups> getGmUserGroups() {
		return gmUserGroups;
	}

	public void setGmUserGroups(List<User_Groups> gmUserGroups) {
		/*debug*/
		System.out.println("com.yardi.ejb.Groups_Master.setGmUserGroups() 0000");
		/*debug*/
		this.gmUserGroups = gmUserGroups;
	}

	@Override
	public String toString() {
		return "Groups_Master [gmType=" + gmType + ", gmDescription=" + gmDescription + ", gmInitialPage="
				+ gmInitialPage + ", gmRrn=" + gmRrn + ", gmUserGroups=" + gmUserGroups + "]";
	}
}
