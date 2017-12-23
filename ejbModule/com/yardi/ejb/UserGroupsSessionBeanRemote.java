package com.yardi.ejb;

import java.util.Vector;

import javax.ejb.Remote;

import com.yardi.userServices.UserGroupsGraph;

@Remote
public interface UserGroupsSessionBeanRemote {
	Vector<UserGroupsGraph> find(String userID);
}
