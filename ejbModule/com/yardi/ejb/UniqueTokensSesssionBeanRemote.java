package com.yardi.ejb;

import java.util.ArrayList;
import java.util.Vector;

import javax.ejb.Remote;
import javax.persistence.EntityManager;

@Remote
public interface UniqueTokensSesssionBeanRemote {
	UniqueToken find(long rrn);
	int persist(String userName, String token, java.util.Date dateAdded);
	int remove(long rrn);
	Vector<UniqueToken> findTokens(String userName);
	String stringify();
}
