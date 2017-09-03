package com.yardi.ejb;

import javax.ejb.Remote;
import javax.persistence.EntityManager;

@Remote
public interface UniqueTokensSesssionBeanRemote {
	UniqueToken exists(long rrn);
	void beginTransaction ();
	void commitTransaction();
	void persist(UniqueToken uniqueToken);
	void remove(UniqueToken uniqueToken);
	EntityManager getEntityManager();
}
