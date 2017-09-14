package com.yardi.ejb;

import javax.ejb.Remote;

@Remote
public interface UserProfileSessionBeanRemote {
	UserProfile find(String userName);
}
