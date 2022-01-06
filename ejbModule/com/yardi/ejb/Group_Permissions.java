package com.yardi.ejb;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the GROUP_PERMISSIONS database table.
 * 
 */
@Entity
@Table(name="GROUP_PERMISSIONS", schema="DB2ADMIN")
@NamedQuery(name="Group_Permissions.findAll", query="SELECT g FROM Group_Permissions g")
public class Group_Permissions implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="GP_GROUP_TYPE")
	private int gpGroupType;

	@Column(name="GP_PERMISSION")
	private int gpPermission;

	@Id
	@GeneratedValue
	@Column(name="GP_RRN")
	private long gpRrn;

	public Group_Permissions() {
		/*debug*/
		System.out.println("com.yardi.ejb.Group_Permissions.Group_Permissions() 0000");
		/*debug*/
	}

	public long getGpRrn() {
		return this.gpRrn;
	}

	public void setGpRrn(long gpRrn) {
		/*debug*/
		System.out.println("com.yardi.ejb.setGpRrn() 0000");
		/*debug*/
		this.gpRrn = gpRrn;
	}

	public int getGpGroupType() {
		return this.gpGroupType;
	}

	public void setGpGroupType(int gpGroupType) {
		/*debug*/
		System.out.println("com.yardi.ejb.setGpGroupType() 0000");
		/*debug*/
		this.gpGroupType = gpGroupType;
	}

	public int getGpPermission() {
		return this.gpPermission;
	}

	public void setGpPermission(int gpPermission) {
		/*debug*/
		System.out.println("com.yardi.ejb.setGpPermission() 0000");
		/*debug*/
		this.gpPermission = gpPermission;
	}
}
