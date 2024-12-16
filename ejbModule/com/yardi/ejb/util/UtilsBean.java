package com.yardi.ejb.util;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

import java.util.Set;

import jakarta.annotation.PostConstruct;

/**
 * Utility methods 
 */
@Stateless
public class UtilsBean implements Utils {

    /**
     * Default constructor. 
     */
    public UtilsBean() {
    	System.out.println("com.yardi.ejb.util.UtilsBean.UtilsBean() 0000 ");
    }

	/**
	 * Test whether the instance is an entity.
	 * 
	 * @param clazz the instance to test. 
	 * @param em EntityManager
	 * @return boolean indicating whether the given instance is an entity.
	 */
	public boolean isEntity(Class<?> clazz, EntityManager em) {
		System.out.println("com.yardi.ejb.util.UtilsBean.isEntity() 000A ");
		
		if (clazz==null) {
			System.out.println("com.yardi.ejb.util.UtilsBean.isEntity() clazz is null 000B ");
			return false;
		}
		
		if (em==null) {
			System.out.println("com.yardi.ejb.util.UtilsBean.isEntity() em is null 000C ");
			return false;
		}
	
	    boolean foundEntity = false;
	    Set<EntityType<?>> entities = em.getMetamodel().getEntities();
	    
	    for(EntityType<?> entityType :entities) {
	        Class<?> entityClass = entityType.getJavaType();
	        
	        if(entityClass.equals(clazz)) {
	            foundEntity = true;
	        }
	    }
	    
		System.out.println("com.yardi.ejb.util.UtilsBean.isEntity() 0010 " + foundEntity);
	    return foundEntity;
	}
	
	/**
	 * Test whether the EntityManager is joined to a transaction. 
	 * @return boolean indicating whether the EntityManager is joined to the current transaction.
	 */
	public boolean isJoined(EntityManager em) {

    	if (em==null) {
			System.out.println("com.yardi.ejb.util.UtilsBean.isJoined() em is null 000D ");
			return false;
		}
	
  		System.out.println("com.yardi.ejb.util.UtilsBean.isJoined() 0005 "
  				+ "\n"
  				+ "   isJoined="
  				+ em.isJoinedToTransaction()
  				);
		return em.isJoinedToTransaction();
	}
	
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
	public <T> boolean isManaged(EntityManager em, T entity) {
        System.out.println("com.yardi.ejb.util.UtilsBean.isManaged() 0006 ");
        
        if (entity == null) {
            System.out.println("com.yardi.ejb.util.UtilsBean.isManaged() 0007 entity is null.");
            return false;
        }

    	if (em==null) {
			System.out.println("com.yardi.ejb.util.UtilsBean.isManaged() em is null 000E ");
			return false;
		}
	
        // Ensure the class of the entity is a JPA entity
        if (isEntity(entity.getClass(), em)==false) {
            System.out.println("com.yardi.ejb.util.UtilsBean.isManaged() 0008 " + entity.getClass().getName() + " is not an entity.");
            return false;
        }

		System.out.println("com.yardi.ejb.util.UtilsBean.isManaged() 0009 "
				+ "\n "
				+ "   em.contains("
				+ entity.getClass().getName()
				+ ")="
				+ em.contains(entity)
				);
        return em.contains(entity);
    }
    
    /** 
     * post construct call back 
     */
    @PostConstruct
    private void postConstructCallback() {
    	System.out.println("com.yardi.ejb.util.UtilsBean.postConstructCallback() 0001 ");
    }
    
    /**
	 * Log the transaction status
	 */
	public void txStatus(UserTransaction tx) {
    	System.out.println("com.yardi.ejb.util.UtilsBean.txStatus() 0002 ");

    	if (tx==null) {
			System.out.println("com.yardi.ejb.util.UtilsBean.txStatus() tx is null 000F ");
			return;
		}
	
		String status = null;
		
		try {
			switch(tx.getStatus()) {
			case jakarta.transaction.Status.STATUS_ACTIVE:
				status = "active";
				break;
			case jakarta.transaction.Status.STATUS_COMMITTED:
				status = "committed";
				break;
			case jakarta.transaction.Status.STATUS_COMMITTING:
				status = "committing";
				break;
			case jakarta.transaction.Status.STATUS_MARKED_ROLLBACK:
				status = "marked rollback";
				break;
			case jakarta.transaction.Status.STATUS_NO_TRANSACTION:
				status = "no transaction";
				break;
			case jakarta.transaction.Status.STATUS_PREPARED:
				status = "prepared";
				break;
			case jakarta.transaction.Status.STATUS_PREPARING:
				status = "prepairing";
				break;
			case jakarta.transaction.Status.STATUS_ROLLEDBACK:
				status = "rolled back";
				break;
			case jakarta.transaction.Status.STATUS_ROLLING_BACK:
				status = "rolling back";
				break;
			case jakarta.transaction.Status.STATUS_UNKNOWN:
				status = "unknown";
				break;
			default:
				status = "undefined";
			}
		} catch (SystemException e) {
			System.out.println("com.yardi.ejb.util.UtilsBean.txStatus() SystemException 0003 ");
			e.printStackTrace();
		}
		
		System.out.println("com.yardi.ejb.util.UtilsBean.txStatus() 0004 "
  				+ "\n"
  				+ "   tx status="
  				+ status
  				);
	}
}