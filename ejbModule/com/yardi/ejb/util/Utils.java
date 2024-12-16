package com.yardi.ejb.util;

import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;

public interface Utils {

	/**
	 * Test whether the instance is an entity.
	 * 
	 * @param clazz the instance to test. 
	 * @param em EntityManager
	 * @return boolean indicating whether the given instance is an entity.
	 */
	boolean isEntity(Class<?> clazz, EntityManager em);

	/**
	 * Test whether the EntityManager is joined to a transaction. 
	 * @return boolean indicating whether the EntityManager is joined to the current transaction.
	 */
	boolean isJoined(EntityManager em);

	/**
	 * Test whether the persistence context contains the given entity.<p>
	 * 
	 * If <i>entity</i> is null return false.<p>
	 * 
	 * If <i>entity</i> is not an entity return false
	 * @param <T> generic
	 * @param em the current EntityManager
	 * @param entity class to test
	 * @return boolean indicating whether the given class is an entity
	 */
	<T> boolean isManaged(EntityManager em, T entity);

	/**
	 * Log the transaction status
	 */
	void txStatus(UserTransaction tx);

}