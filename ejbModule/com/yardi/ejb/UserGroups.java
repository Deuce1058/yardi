package com.yardi.ejb;

import java.util.Vector;

import jakarta.ejb.Local;

import com.yardi.shared.userServices.InitialPage;
import com.yardi.shared.userServices.UserGroupsGraph;

@Local
public interface UserGroups {
	Vector<UserGroupsGraph> find(String userID);
	String getFeedback();
	String getInitialPage(String userName);
	Vector<InitialPage> getInitialPageList();
	void removeBean();
}
