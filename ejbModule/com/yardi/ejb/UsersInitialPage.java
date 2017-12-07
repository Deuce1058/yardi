package com.yardi.ejb;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: UsersInitialPage
 * Join USER_GROUPS and GROUPS_MASTER
 *
 */

public class UsersInitialPage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String userID;
	private int userGroup;
	private String groupDescription;
	private String initialPage;

	public UsersInitialPage() {
	}

	public UsersInitialPage(String userID, int userGroup, String groupDescription, String initialPage) {
		this.userID = userID;
		this.userGroup = userGroup;
		this.groupDescription = groupDescription;
		this.initialPage = initialPage;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(int userGroup) {
		this.userGroup = userGroup;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public String getInitialPage() {
		return initialPage;
	}

	public void setInitialPage(String initialPage) {
		this.initialPage = initialPage;
	}
}
