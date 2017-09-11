package com.yardi.ejb;

import javax.ejb.Remote;

@Remote
public interface UserProfileSessionBeanRemote {
	UserProfile findUser(String userName);
	void beginTransaction();
	void commitTransaction();
}
