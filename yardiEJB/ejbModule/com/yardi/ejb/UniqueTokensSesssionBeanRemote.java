package com.yardi.ejb;

import java.util.ArrayList;

import javax.ejb.Remote;
import javax.persistence.EntityManager;

@Remote
public interface UniqueTokensSesssionBeanRemote {
	UniqueToken find(long rrn);
	void persist(UniqueToken uniqueToken);
	void remove(UniqueToken uniqueToken);
	ArrayList<UniqueToken> findTokens(String userName);
}
