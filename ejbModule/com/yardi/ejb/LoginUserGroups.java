package com.yardi.ejb;

import java.util.Vector;

import javax.ejb.Remote;

import com.yardi.ejb.model.Login_Sessions_Table;
import com.yardi.ejb.model.Login_User_Profile;
import com.yardi.shared.userServices.LoginInitialPage;
import com.yardi.shared.userServices.LoginUserGroupsGraph;

@Remote
public interface LoginUserGroups {
	Vector<LoginUserGroupsGraph> find(String userID);
	String getFeedback();
	String getInitialPage(String userName);
	Vector<LoginInitialPage> getInitialPageList();
	public Login_Sessions_Table getLoginSessionTable();
	Login_User_Profile getLoginUserProfile();
	Vector<LoginUserGroupsGraph> getUserGroups();
	void removeBean();  
}
