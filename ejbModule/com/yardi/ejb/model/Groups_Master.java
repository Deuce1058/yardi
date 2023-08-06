package com.yardi.ejb.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;


/**
 * Entity implementation class for Entity: Groups_Master.<br><br>
 * 
 * <pre>Database table: GROUPS_MASTER
 *Schema:         DB2ADMIN</pre>
 */
@Entity
@Table(name="GROUPS_MASTER", schema="DB2ADMIN")
public class Groups_Master implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Column: GM_TYPE
	 * Group type.
	 * Primary key
	 */
	@Id
	@Column(name="GM_TYPE")
	private int gmType;

	/**
	 * Column: GM_DESCRIPTION
	 * Group description 
	 */
	@Column(name="GM_DESCRIPTION")
	private String gmDescription;

	/**
	 * Column: GM_INITIAL_PAGE
	 * Group's initial page 
	 */
	@Column(name="GM_INITIAL_PAGE")
	private String gmInitialPage;

	/**
	 * Column: GM_RRN
	 * Relative record number
	 */
	@Column(name="GM_RRN")
	private long gmRrn;
	
	/**
	 * Reference to User_Groups entity.
	 * <pre>
	 * Database tables: USER_GROUPS
	 * Schema:          DB2ADMIN
	 * Mapped by:       ugGroupsMaster
	 * </pre> 
	 * 
	 * Groups_Master entity has a bidirectional, one to many relationship with User_Groups entity. Field <i>gmUserGroups</i> defines the inverse side.
	 */
	@OneToMany(mappedBy = "ugGroupsMaster")
	private List<User_Groups> gmUserGroups;

	public Groups_Master() {
		/*debug*/
		System.out.println("com.yardi.ejb.model.Groups_Master.Login_Groups_Master() 0000");
		/*debug*/
	}

	/**
	 * Return group description
	 * @return group description
	 */
	public String getGmDescription() {
		return this.gmDescription;
	}

	/**
	 * Return group's initial page
	 * @return initial page
	 */
	public String getGmInitialPage() {
		return this.gmInitialPage;
	}

	/**
	 * Return relative record number
	 * @return relative record number
	 */
	public long getGmRrn() {
		return this.gmRrn;
	}

	/**
	 * Return group type
	 * @return group type
	 */
	public int getGmType() {
		return this.gmType;
	}

	/**
	 * Return the list of User_Groups entities
	 * @return User_Groups entities
	 */
	public List<User_Groups> getGmUserGroups() {
		return gmUserGroups;
	}

	@Override
	public String toString() {
		return "Groups_Master [gmType=" + gmType + ", gmDescription=" + gmDescription + ", gmInitialPage="
				+ gmInitialPage + ", gmRrn=" + gmRrn + ", gmUserGroups=" + gmUserGroups + "]";
	}
}
