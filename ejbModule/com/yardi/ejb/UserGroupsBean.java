package com.yardi.ejb;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.yardi.ejb.model.Sessions_Table;
import com.yardi.ejb.model.User_Groups;
import com.yardi.ejb.model.User_Groups2;
import com.yardi.ejb.model.User_Profile;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.metamodel.EntityType;

/**
 * Session Bean implementation of methods for working with User_Groups entity.<p> Uses a transaction-scoped persistence context.
 */
@Stateless
public class UserGroupsBean implements UserGroups {
	@PersistenceContext(unitName="yardi", type=PersistenceContextType.TRANSACTION)
	private EntityManager em;

	/**
	 * Default constructor
	 */
	public UserGroupsBean() {
		System.out.println("com.yardi.ejb.UserGroupsBean UserGroupsBean() 0007 ");
    }
	
	/**
	 * Finds the groups that the given user belongs to.
	 * <p>
	 * Executes a native query joining USER_GROUPS, USER_PROFILE, SESSIONS_TABLE, and GROUPS_MASTER.
	 * USER_PROFILE and GROUPS_MASTER are inner joined, so the query returns results only when the
	 * user exists in both tables. SESSIONS_TABLE is outer joined because there may not be an active session.
	 * <p>
	 * A native query via {@link jakarta.persistence.EntityManager#createNativeQuery(String, Class) EntityManager}
	 * is required here. EclipseLink throws an exception when this query is executed as a
	 * {@link jakarta.persistence.TypedQuery TypedQuery}.
	 * 
	 * @param userID the user ID to search for.
	 * @return a {@link UserGroupsResult} containing the list of {@link User_Groups} entities
	 *         matching the given user ID, or a no-such-user result if none are found.<p>
	 *         
	 *         {@link UserGroupsResult#noSuchUserName()} if no rows are found, which typically
	 *         indicates the user does not exist in USER_PROFILE or USER_GROUPS.<p>
	 * 
	 *         {@link UserGroupsResult#foundUserGroups(List)} if rows are found.
	 */
	@Override
	public UserGroupsResult find(String userID) {
		System.out.println("com.yardi.ejb.UserGroupsBean find() 0000 ");
		List<User_Groups> userGroupsList;
		Query qry = em.createNativeQuery(
				"select t0.UG_USER_ID, t0.UG_GROUP, t0.UG_RRN "
						+ "from DB2ADMIN.USER_GROUPS t0 "
						+ "left outer join DB2ADMIN.SESSIONS_TABLE t3 on t3.ST_USER_ID = t0.UG_USER_ID "
						+ "join DB2ADMIN.USER_PROFILE t2              on t2.UP_USERID  = t0.UG_USER_ID "    
						+ "join DB2ADMIN.GROUPS_MASTER t1             on t1.GM_TYPE    = t0.UG_GROUP "
						+ "where t0.UG_USER_ID = ? "
						, User_Groups.class);
		userGroupsList = qry
				.setParameter(1, userID)
				.getResultList();

		if (userGroupsList.isEmpty()) {
			System.out.println("com.yardi.ejb.UserGroupsBean find() 000C ");
			return UserGroupsResult.noSuchUserName();
		}

		System.out.println("com.yardi.ejb.UserGroupsBean find() 0006 ");
		return UserGroupsResult.foundUserGroups(userGroupsList);
	}
	
	/**
	 * Find all User_Groups2 entities for the given userName.<p>
	 * 
	 * @param userName specifies which User_Groups2 entities to find.
	 * @return Vector containing User_Groups2 entities matching the given userName. Returns an empty Vector if the persistence context contains 
	 * no User_Groups2 entities matching the given userName and the USER_GROUPS database table has no rows matching userName.
	 */
	@Override
	public Vector<User_Groups2> find2(String userName) {
		System.out.println("com.yqrdi.ejb.UserGroupsBean.find2() 0013 ");
		isJoined();
		Vector<User_Groups2> userGroups = new Vector<User_Groups2>();
		TypedQuery<User_Groups2> qry = em.createQuery(
			  "SELECT g FROM User_Groups2 g "
			+ "WHERE g.ugUserId = :userName ", 
			  User_Groups2.class
		);
		userGroups = (Vector<User_Groups2>) qry.setParameter("userName", userName)
						.getResultList();

		if (userGroups.isEmpty()) {
			System.out.println("com.yqrdi.ejb.UserGroupsBean.find2() 0014 "
					+ "\n    "
					+ "User_Groups = [empty]"
					);
		} else {
			System.out.println("com.yardi.ejb.UserGroupsBean.find2() 0016 ");
			for (User_Groups2 group : userGroups) {
				System.out.println(
					  "    User_Groups = [" 
					+ "ugUserId ="
					+ group.getUgUserId()
					+ "ugGroup ="
					+ group.getUgGroup()
					+ "ugRrn ="
					+ group.getUgRrn()
					+ "]"
					);
				isManaged(group);
			}
		}
		return userGroups;
	}
	
	/**
	 * Test whether the given instance is an entity.<p>
	 * 
	 * @param clazz the instance to test. 
	 * @return boolean indicating whether the given instance is an entity.
	 */
	private boolean isEntity(Class<?> clazz) {
		System.out.println("com.yardi.ejb.UserGroupsBean isEntity() 001E ");
	    boolean foundEntity = false;
	    Set<EntityType<?>> entities = em.getMetamodel().getEntities();
	    
	    for(EntityType<?> entityType :entities) {
	        Class<?> entityClass = entityType.getJavaType();
	        
	        if(entityClass.equals(clazz)) {
	            foundEntity = true;
	        }
	    }
	    
		System.out.println("com.yardi.ejb.UserGroupsBean isEntity() 001F " + foundEntity);
	    return foundEntity;
	}

	/**
	 * Test whether the entity manager is participating in a transaction.<p>
	 * @return boolean indicating whether the entity manager is joined to the current transaction.
	 */
	private boolean isJoined() {
  		System.out.println("com.yardi.ejb.UserGroupsBean isJoined() 0005 "
  				+ "\n"
  				+ "   isJoined="
  				+ em.isJoinedToTransaction()
  				);
		return em.isJoinedToTransaction();
	}

	/**
	 * Test whether Sessions_Table entity is managed.<p>
	 * @param sessionsTable the entity to test.
	 * @return boolean indicating whether the given Sessions_Table entity is being managed. Returns false if the given instance is null 
	 * or if the given instance is not an entity. 
	 */
	private boolean isManaged(Sessions_Table sessionsTable) {
  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0011 ");

  		if (sessionsTable==null) {
  	  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 001A "
  	  				+ "\n    "
	  				+ "em.contains(Sessions_Table)=false"
	  				);
	  		return false;
  		} 
  		
  		if (isEntity(sessionsTable.getClass())==false) {
  	  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0020 "
	  				+ "\n    "
	  				+ "em.contains(Sessions_Table)=false"
	  				);
	  		return false;
  		}

  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 001B "
  				+ "\n    "
  				+ "em.contains(Sessions_Table)="
  				+ em.contains(sessionsTable)
  				);
		return em.contains(sessionsTable);  			
	}
	
	/**
	 * Test whether the given User_Groups entity is managed.<p>
	 * @param userGroups the entity to test.
	 * @return boolean indicating whether the User_Groups entity is managed. Returns false if the given instance is null or if the given instance is not an entity.
	 */
	private boolean isManaged(User_Groups userGroups) {
  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0021 ");
		
  		if (userGroups==null) {
  	  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0022 "
  	  				+ "\n    "
	  				+ "Sem.contains(User_Groups)=false"
	  				);
	  		return false;
  		} 
  		
  		if (isEntity(userGroups.getClass())==false) {
  	  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0023 "
	  				+ "\n    "
	  				+ "em.contains(User_Groups)=false"
	  				);
	  		return false;
  		}

  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0004 "
  				+ "\n    "
  				+ "em.contains(User_Groups)="
  				+ em.contains(userGroups)
  				);
		return em.contains(userGroups);
	}
	
	/**
	 * Test whether the given User_Groups2 entity is managed.<p>
	 * @param userGroups2 the entity to test.
	 * @return boolean indicating whether the given instance is managed. Returns false if the given instance is null or if the given instance is not an entity.
	 */
	private boolean isManaged(User_Groups2 userGroups2) {
		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 001C ");
  		
		if (userGroups2==null) {
			System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 001D "
					+ "\n    "
	  				+ "em.contains(User_Groups2)=false"
					);
			return false;
		}
		
  		if (isEntity(userGroups2.getClass())==false) {
  	  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0024 "
	  				+ "\n    "
	  				+ "em.contains(User_Groups2)=false"
	  				);
	  		return false;
  		}

  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0015 "
  				+ "\n    "
  				+ "em.contains(User_Groups2)="
  				+ em.contains(userGroups2)
  				);
		return em.contains(userGroups2);
	}

	/**
	 * Test whether the User_Profile entity is managed.<p>
	 * @param userProfile the entity to test.
	 * @return boolean indicating whether the given instance is managed. Returns false if the given instance is null or if the given instance is not an entity.
	 */
	private boolean isManaged(User_Profile userProfile) {
  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0012 ");
  		
  		if (userProfile==null) {
  	  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0025 "
  	  				+ "\n    "
	  				+ "em.contains(User_Profile)=false"
	  				);
	  		return false;
  		} 
  		
  		if (isEntity(userProfile.getClass())==false) {
  	  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0026 "
	  				+ "\n    "
	  				+ "em.contains(User_Profile)=false"
	  				);
	  		return false;
  		}

  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0027 "
  				+ "\n"
  				+ "   em.contains(User_Profile)="
  				+ em.contains(userProfile)
  				);
		return em.contains(userProfile);
	}
	
	/**
	 * Persist a User_Groups2 entity.<p>
	 * 
	 * @param group the entity to persist.
	 */ 
	@Override
	public void persist(User_Groups2 group) {
		System.out.println("com.yardi.ejb.UserGroupsBean.persist() 0018 ");
		isJoined();
		em.persist(group);
		isManaged(group);
	}
	
	@PostConstruct
	private void postConstructCallback() {
		System.out.println("com.yardi.ejb.UserGroupsBean.postConstructCallback() 0010 "
				+ "\n    UserGroupsBean="
				+ this);
	}

	/**
	 * Remove the given User_Groups2 entity.<p>
	 * 
	 * @param group the entity to remove.
	 */
	@Override
	public void remove(User_Groups2 group) {
		System.out.println("com.yardi.ejb.UserGroupsBean.remove() 0017 ");
		isJoined();

		if (group!=null) {
			System.out.println("com.yardi.ejb.UserGroupsBean.remove() 0019 ");
			em.remove(group);
			isManaged(group);
		}
	}
}
