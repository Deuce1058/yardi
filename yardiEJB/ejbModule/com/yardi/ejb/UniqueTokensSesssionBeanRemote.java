package com.yardi.ejb;

import java.util.ArrayList;

import javax.ejb.Remote;
import javax.persistence.EntityManager;

@Remote
public interface UniqueTokensSesssionBeanRemote {
	UniqueToken find(long rrn);
	int persist(String userName, String token, java.util.Date dateAdded);
	int remove(long rrn);
	ArrayList<UniqueToken> findTokens(String userName);
}
