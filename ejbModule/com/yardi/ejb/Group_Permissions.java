package com.yardi.ejb;

import java.io.Serializable;
import javax.persistence.*;


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
	}

	public long getGpRrn() {
		return this.gpRrn;
	}

	public void setGpRrn(long gpRrn) {
		this.gpRrn = gpRrn;
	}

	public int getGpGroupType() {
		return this.gpGroupType;
	}

	public void setGpGroupType(int gpGroupType) {
		this.gpGroupType = gpGroupType;
	}

	public int getGpPermission() {
		return this.gpPermission;
	}

	public void setGpPermission(int gpPermission) {
		this.gpPermission = gpPermission;
	}
}