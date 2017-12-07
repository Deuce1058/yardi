package com.yardi.ejb;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: UserPermissions
 * Join of UserGroups and GroupPermissions
 *
 */


public class UserPermissions implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ugUserId;
	private int ugGroup;
	private int gpPermission;

	public UserPermissions() {
	}
   
	public UserPermissions(String ugUserId, int ugGroup, int gpPermission) {
		this.ugUserId = ugUserId;
		this.ugGroup = ugGroup;
		this.gpPermission = gpPermission;
	}

	public String getUgUserId() {
		return ugUserId;
	}
	public void setUgUserId(String ugUserId) {
		this.ugUserId = ugUserId;
	}
	public int getUgGroup() {
		return ugGroup;
	}
	public void setUgGroup(int ugGroup) {
		this.ugGroup = ugGroup;
	}
	public int getGpPermission() {
		return gpPermission;
	}
	public void setGpPermission(int gpPermission) {
		this.gpPermission = gpPermission;
	}
}
