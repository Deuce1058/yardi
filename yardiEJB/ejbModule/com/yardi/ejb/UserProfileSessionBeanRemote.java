package com.yardi.ejb;

import javax.ejb.Remote;

@Remote
public interface UserProfileSessionBeanRemote {
	UserProfile userNameExists(String userName);
	void beginTransaction();
	void commitTransaction();
}
