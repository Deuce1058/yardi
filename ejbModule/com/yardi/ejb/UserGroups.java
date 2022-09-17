package com.yardi.ejb;

import java.util.Vector;

import jakarta.ejb.Local;

import com.yardi.ejb.model.Sessions_Table;
import com.yardi.ejb.model.User_Profile;
import com.yardi.shared.userServices.LoginInitialPage;
import com.yardi.shared.userServices.LoginUserGroupsGraph;

@Local
public interface UserGroups {
	Vector<LoginUserGroupsGraph> find(String userID);
	String getFeedback();
	String getInitialPage(String userName);
	Vector<LoginInitialPage> getInitialPageList();
	public Sessions_Table getLoginSessionTable();
	User_Profile getLoginUserProfile();
	Vector<LoginUserGroupsGraph> getUserGroups();
	void removeBean();  
}
