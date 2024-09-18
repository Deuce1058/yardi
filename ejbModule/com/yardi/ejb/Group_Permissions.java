package com.yardi.ejb;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * Entity implementation class for database table GROUP_PERMISSIONS.<p>
 * 
 * Database table GROUP_PERMISSIONS defines what permissions a group has. Users are not granted permissions. Instead users are assigned to groups and groups 
 * are granted permissions.
 * 
 * <pre><code>  Database table: GROUP_PERMISSIONS
 *  Schema:         DB2ADMIN</code></pre>
 * 
 */
@Entity
@Table(name="GROUP_PERMISSIONS", schema="DB2ADMIN")
@NamedQuery(name="Group_Permissions.findAll", query="SELECT g FROM Group_Permissions g")
public class Group_Permissions implements Serializable {
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Column: GP_GROUP_TYPE Group type
	 */
	@Column(name="GP_GROUP_TYPE")
	private int gpGroupType;

	/**
	 * Column: GP_PERMISSION Group permission.<p> 
	 * This column can be joined to field <code>pmType</code> in the <code>Permissions_Master</code> entity. 
	 */
	@Column(name="GP_PERMISSION")
	private int gpPermission;

	/**
	 * Column: GP_RRN Relative record number<p>
	 * Primary key
	 */
	@Id
	@GeneratedValue
	@Column(name="GP_RRN")
	private long gpRrn;

	/**
	 * Default constructor
	 */
	public Group_Permissions() {
		/*debug*/
		System.out.println("com.yardi.ejb.Group_Permissions.Group_Permissions() 0000");
		/*debug*/
	}

	/**
	 * Return the relative record number
	 * @return relative record number
	 */
	public long getGpRrn() {
		return this.gpRrn;
	}

	/**
	 * Set the relative record number
	 * @param gpRrn the relative record number to set
	 */
	public void setGpRrn(long gpRrn) {
		/*debug*/
		System.out.println("com.yardi.ejb.setGpRrn() 0000");
		/*debug*/
		this.gpRrn = gpRrn;
	}

	/**
	 * Return the group type
	 * @return group type
	 */
	public int getGpGroupType() {
		return this.gpGroupType;
	}

	/**
	 * Set group type
	 * @param gpGroupType the group type to set
	 */
	public void setGpGroupType(int gpGroupType) {
		/*debug*/
		System.out.println("com.yardi.ejb.setGpGroupType() 0000");
		/*debug*/
		this.gpGroupType = gpGroupType;
	}

	/**
	 * Return the group permission
	 * @return group permission
	 */
	public int getGpPermission() {
		return this.gpPermission;
	}

	/**
	 * Set the group permission
	 * @param gpPermission the group permission to set
	 */
	public void setGpPermission(int gpPermission) {
		/*debug*/
		System.out.println("com.yardi.ejb.setGpPermission() 0000");
		/*debug*/
		this.gpPermission = gpPermission;
	}
}
