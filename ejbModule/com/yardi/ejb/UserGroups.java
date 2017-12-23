package com.yardi.ejb;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the USER_GROUPS database table.
 * 
 */
@Entity
@Table(name="USER_GROUPS")
@NamedQuery(name="UserGroups.findAll", query="SELECT u FROM UserGroups u")
public class UserGroups implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="UG_USER_ID")
	private String ugUserId;

	@Column(name="UG_GROUP")
	private int ugGroup;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="UG_RRN")
	private long ugRrn;

	public UserGroups() {
	}

	public long getUgRrn() {
		return this.ugRrn;
	}

	public void setUgRrn(long ugRrn) {
		this.ugRrn = ugRrn;
	}

	public int getUgGroup() {
		return this.ugGroup;
	}

	public void setUgGroup(int ugGroup) {
		this.ugGroup = ugGroup;
	}

	public String getUgUserId() {
		return this.ugUserId;
	}

	public void setUgUserId(String ugUserId) {
		this.ugUserId = ugUserId;
	}
}