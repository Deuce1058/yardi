package com.yardi.ejb;

import java.util.Vector;

import javax.ejb.Remote;

@Remote
public interface UniqueTokensSesssionBeanRemote {
	UniqueTokens find(long rrn);
	int persist(String userName, String token, java.util.Date dateAdded);
	int remove(long rrn);
	Vector<UniqueTokens> findTokens(String userName);
	String stringify();
}
