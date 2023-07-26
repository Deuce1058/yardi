package com.yardi.ejb;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.metamodel.EntityType;
import com.yardi.ejb.model.Sessions_Table;
import com.yardi.ejb.model.User_Groups;
import com.yardi.ejb.model.User_Groups2;
import com.yardi.ejb.model.User_Profile;
import com.yardi.shared.userServices.LoginInitialPage;
import com.yardi.shared.userServices.LoginUserGroupsGraph;

/**
 * Session Bean implementation of methods for working with User_Groups entity
 */
@Stateful
public class UserGroupsBean implements UserGroups {
	@PersistenceContext(unitName="yardi", type=PersistenceContextType.EXTENDED)
	private EntityManager em;
	/**
	 * The initial page of the description and the URL of the group the user belongs to.
	 */
	private String initialPage = "";
	/**
	 * Clients can read this field to determine the status of the most recent method call that provides feedback.
	 */
	private String feedback = "";
	/**
	 * Vector containing the initial page description and the URL for each group the user belongs to. This will be converted to JSON as part of the web response.
	 */
	private Vector<LoginInitialPage> initialPageList = new Vector<LoginInitialPage>();
	/**
	 * Contains the groups the user belongs to along with the description of the group and the group's initial page.
	 */
	private Vector<LoginUserGroupsGraph> userGroups;
	/**
	 * A reference to the User_Groups entity. This exists to allow easier access to embeded entities and other elements within the User_Groups entity 
	 */
	private User_Groups userGroupsEntity=null;
	/**
	 * A reference to the User_Profile entity embeded within User_Groups entity. This exists for the convenience of other components that may 
	 * need a reference to User_Profile.
	 */
	private User_Profile loginUserProfile;
	/**
	 * A reference to the Sessions_Table entity embeded within User_Groups entity. This exists for the convenience of other components that may 
	 * need a reference to Sessions_Table.
	 */
	private Sessions_Table loginSessionTable;

	public UserGroupsBean() {
		//debug
		System.out.println("com.yardi.ejb.UserGroupsBean UserGroupsBean() 0007 ");
		//debug
    }
	
	/**
	 * Find User_Groups entities by userID.<p>
	 * 
	 * Native query joins database tables: USER_GROUPS, SESSIONS_TABLE, USER_PROFILE and GROUPS_MASTER.<br><br>
	 * 
	 * Only the columns needed for login are used.<br><br>
	 * 
	 * A new com.yardi.shared.userServices.LoginUserGroupsGraph is constructed from all columns of USER_GROUPS database table and all columns of
	 * GROUPS_MASTER database table.<br><br>
	 * 
	 * The new com.yardi.shared.userServices.LoginUserGroupsGraph is added to a Vector of com.yardi.shared.userServices.LoginUserGroupsGraph.<br><br>
	 * 
	 * com.yardi.shared.userServices.LoginUserGroupsGraph implements Comparable so it can be sorted.<br><br>
	 * 
	 * Set the field <i>userGroupsEntity</i> to refer to the first User_Groups entity in query result list for convenience.<br> 
	 * This will allow easier access to elements embeded within the User_Groups entity.<br><br> 
	 * 
   	 * Set field <i>userGroups</i>. <i>userGroups</i> is a Vector of groups that the user belongs to along with a description and the path to the initial screen.<br>  
   	 * <i>userGroups</i> exists for the convenience of other components that might need this reference.<br><br>
   	 * 
  	 * Set field <i>loginSessionsTable</i>. <i>loginSessionsTable</i> is a reference to the Sessions_Table entity embeded within the User_Groups entity.<br>
   	 * It exists for the convenience of other components that may need this reference.<br><br>
   	 * 
  	 * Set field <i>loginUserProfile</i>. <i>loginUserProfile</i> is a reference to the User_Profile entity embebed within the User_Groups entity.<br>
   	 * It exists for the convenience of other components that may need this reference.<br>
   	 * <i>loginUserProfile</i> is null if the userId is not found in the USER_PROFILE database table.<br><br>
   	 * 
   	 * <strong>Feedback from this method:</strong>
   	 * <pre>YRD000D if the userId is not in the USER_PROFILE database table</pre><br><br>  
   	 * 
   	 * @param userID the user to find.
   	 * @return vector of groups that the user belongs to along with a description and the path to the initial screen for the group.
	 */
	public Vector<LoginUserGroupsGraph> find(String userID) {
    	//debug
		System.out.println("com.yardi.ejb.UserGroupsBean find() 0000 ");
		//debug
		Vector<LoginUserGroupsGraph> userGroupsVector = new Vector<LoginUserGroupsGraph>();
		List<User_Groups> userGroupsList;
		/*
		 * No typed query works no matter how its defined
		 * Must be native query with numbered parms
		 * Eclipselink does not seem to bind named parms in a native query
		 */
		Query qry = em.createNativeQuery(
				  "select t0.UG_USER_ID, t0.UG_GROUP, t0.UG_RRN "
			    + "from DB2ADMIN.USER_GROUPS t0 "
			    + "left outer join DB2ADMIN.SESSIONS_TABLE t3 on t3.ST_USER_ID = t0.UG_USER_ID  "
			    + "     and t0.UG_USER_ID = ? "
			    + "     and t3.ST_USER_ID = ? "
			    + "join DB2ADMIN.USER_PROFILE t2 on t0.UG_USER_ID = t2.UP_USERID "    
			    + "     and t0.UG_USER_ID = ? "
			    + "     and t2.UP_USERID = ? "
			    + "join DB2ADMIN.GROUPS_MASTER t1 on t0.UG_GROUP = t1.GM_TYPE " 
			      , User_Groups.class);
		userGroupsList = qry
				.setParameter(1, userID)
				.setParameter(2, userID)
				.setParameter(3, userID)
				.setParameter(4, userID)
				.getResultList();
	
		/*
		 * does not return sessions table row
		 * eclipselink insists on adding a where clause which probably causes the sessions table not to be selected
  		TypedQuery<User_Groups> qry = em.createQuery(
    			  "SELECT ug "
    			+ "from User_Groups ug "
    			+ "join ug.ugGroupsMaster gm "
    			+ "    on  ug.ugGroup = gm.gmType "
     			+ "join ug.ugUserProfile up "
    			+ "    on  ug.ugUserId = up.upUserid "
    			+ "    and ug.ugUserId = :userID "
    			+ "    and up.upUserid = :userID "
    			+ "left join ug.ugSessionsTable st "
    			+ "    on  ug.ugUserId = st.stUserId "
    			+ "    and ug.ugUserId = :userID "
    			+ "    and st.stUserId = :userID "
    			, User_Groups.class);
    	userGroupsList = qry
    			.setParameter("userID", userID)
    			.getResultList();
    	*/

    	if (userGroupsList.isEmpty()) {
        	//debug
    		System.out.println("com.yardi.ejb.UserGroupsBean find() 000C ");
    		//debug
    		loginUserProfile = null;
    		feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000D;
    		return userGroupsVector;
    	}
    	
		/* 
		 * Map each java.util.List element to a new com.yardi.shared.userServices.LoginUserGroupsGraph
		 * 
		 * The new com.yardi.shared.userServices.LoginUserGroupsGraph is added to a Vector of com.yardi.shared.userServices.LoginUserGroupsGraph
		 * 
		 * com.yardi.shared.userServices.LoginUserGroupsGraph implements Comparable so it can be sorted
		 * 
		 * Set the field userGroupsEntity to refer to the first User_Groups entity in thejava.util.List for convenience. 
		 * This will allow easier access to elements embeded within the User_Groups entity    
		 */
    	for (User_Groups userGroup : userGroupsList) {
    		userGroupsVector.add(new LoginUserGroupsGraph(
    			userGroup.getUgUserId(), 
    			userGroup.getUgGroup(), 
    			userGroup.getUgRrn(), 
    			userGroup.getUgGroupsMaster().getGmType(), 
    			userGroup.getUgGroupsMaster().getGmDescription(), 
    			userGroup.getUgGroupsMaster().getGmInitialPage(), 
    			userGroup.getUgGroupsMaster().getGmRrn())
    		);
    		
    		if(userGroupsEntity==null) {
    			//field userGroupsEntity refers to one User_Groups entity in the List. It exists for easier access to other embeded entities in User_Groups
    			userGroupsEntity = userGroup.getLoginUserGroups();
    		}

    		System.out.println("com.yardi.ejb.UserGroupsBean find() 0006 ");
    		isJoined();
    		isManaged(userGroup);
    	}
    	
    	//debug
		System.out.println("com.yardi.ejb.UserGroupsBean find() 000D ");
		//debug
    	Collections.sort(userGroupsVector);
    	/*
    	 * userGroupsVector is a Vector of groups that the user belongs to along with a description and the path to the initial screen for the group 
    	 * field userGroups refers to local Vector userGroupsVector. It exists for the convenience of other components that might need this reference
    	 */
    	setUserGroups(userGroupsVector);
    	/*
    	 * Field loginSessionsTable is a reference to the Sessions_Table entity embeded within the User_Groups entity.
    	 * It exists for the convenience of other components that may need this reference
    	 */
    	setLoginSessionTable();
    	/*
    	 * Field loginUserProfile is a reference to the User+Profile entity embebed within the User_Groups entity.
    	 * It exists for the convenience of other components that may need this reference  
    	 */
    	setLoginUserProfile();
    	return userGroupsVector;
	}
	
	/**
	 * Find all User_Groups2 entities for the given userName.<p>
	 * 
	 * @param userName specifies which User_Groups2 entities to find.
	 * @return Vector containing User_Groups2 entities matching the given userName. Returns an empty Vector if the persistence context contains 
	 * no User_Groups2 entities matching the given userName and the USER_GROUPS database table has no rows matching userName.
	 */
	public Vector<User_Groups2> find2(String userName) {
		/*debug*/
		System.out.println("com.yqrdi.ejb.UserGroupsBean.find2() 0013 ");
		/*debug*/
		isJoined();
		Vector<User_Groups2> userGroups = new Vector<User_Groups2>();
		TypedQuery<User_Groups2> qry = em.createQuery(
			  "SELECT g FROM User_Groups2 g "
			+ "WHERE g.ugUserId = :userName ", 
			  User_Groups2.class
		);
		userGroups = (Vector<User_Groups2>) qry.setParameter("userName", userName)
						.getResultList();
		/*debug*/
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
		/*debug*/
		}
		return userGroups;
	}
	
	/**
	 * Clients use this method to determine the status of the most recent method call that provides feedback.<p>
	 * @return the status of the most recent method call that provides feedback.
	 */
    public String getFeedback() {
		return feedback;
	}

    /**
     * Returns the URL of the page for the group the user belongs to.<p>
     * 
     * If the user belongs to multiple groups the field <i>initialPage</i> is set to <i>views/selectGroup.html</i>. The initial page is selected 
     * by the user from a list of initial page names and descriptions representing each group they belong to.<br><br> 
     * 
     * <strong>The following feedback is provided:</strong>
     * <pre>YRD0000 the process completed normally
     *YRD000E If the user is in multiple groups</pre>
     * 
     * @param userName specifies the user whose initial page is returned.
     * @return the user's initial page.
     */
	public String getInitialPage(String userName) {
    	feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
		initialPage = userGroups.get(0).getGmInitialPage(); //GM_INITIAL_PAGE from GROUPS_MASTER
		//debug
		System.out.println("com.yardi.ejb.UserGroupsBean 0001 getInitialPage()  " 
				+ "\n"
				+ "   initialPage="
				+ initialPage
				);
		System.out.println("com.yardi.ejb.UserGroupsBean 0002 getInitialPage()  ");
		for (LoginUserGroupsGraph u : userGroups) {
			System.out.println(
				  "\n"
				+ "   UserGroupsGraph=" 
				+ u.toString()
				);
		}
		//debug

		if (userGroups.size()>1) {
			// user is in multiple groups. Set ST_LAST_REQUEST to the html select group page. User picks the initial page
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000E;
			initialPage = com.yardi.shared.rentSurvey.YardiConstants.USER_SELECT_GROUP_PAGE;
			//debug
			System.out.println("com.yardi.ejb.UserGroupsBean getInitialPage() 0003 " 
					+ "\n"
					+ "   initialPage="
					+ initialPage
					);
			//debug
		}
		
		return initialPage;
	}

	/**
	 * Return a Vector containing the description and URL of each group the user belongs to.<p> 
	 * 
	 * Clients call getInitialPageList() to get the data for <i>views/selectGroup.html</i> when the user belongs to multiple groups.<br><br>
	 * 
	 * @return Vector containing the short description for a button, a label for a button and a URL.
	 */
	public Vector<LoginInitialPage> getInitialPageList() {
		setInitialPageList();
		return initialPageList;
	}

	/**
	 * Return a reference to the field <i>loginSessionTable</i>.<p>
	 * 
	 * <i>loginSessionTable</i> is a reference to the Sessions_Table entity embeded within the User_Groups entity.<br>
	 * It exists for the convenience of other components that may need this reference.<br>
	 * 
	 * @return a reference to the Sessions_Table entity embeded within the User_Groups entity. 
	 */
	public Sessions_Table getLoginSessionTable() {
		return loginSessionTable;
	}

	/**
	 * Return a reference to the field <i>loginUserProfile</i>.<p>
	 * 
	 * <i>loginUserProfile</i> is a reference to the User_Profile entity embebed within the User_Groups entity.<br>
	 * It exists for the convenience of other components that may need this reference.<br>
	 * 
	 * @return a reference to the User_Profile entity embebed within the User_Groups entity. 
	 */
	public User_Profile getLoginUserProfile() {
		return loginUserProfile;
	}

	/**
	 * Returns a reference to the field <i>userGroups</i>.<p>
	 * 
	 * <i>userGroups</i> contains every column from database tables USER_GROUPS and GROUPS_MASTER.<br> 
	 * 
	 * @return reference to the field <i>userGroups</i>.
	 */
	@Override
	public Vector<LoginUserGroupsGraph> getUserGroups() {
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
  	  		System.out.println(
  	  				  "com.yardi.ejb.UserGroupsBean isManaged() 001A "
  	  				+ "\n"
	  				+ "   em.contains(Sessions_Table)=false"
	  				);
	  		return false;
  		} 
  		
  		if (isEntity(sessionsTable.getClass())==false) {
  	  		System.out.println(
	  				  "com.yardi.ejb.UserGroupsBean isManaged() 0020 "
	  				+ "\n"
	  				+ "   em.contains(Sessions_Table)=false"
	  				);
	  		return false;
  		}

  		System.out.println(
  				  "com.yardi.ejb.UserGroupsBean isManaged() 001B "
  				+ "\n"
  				+ "    em.contains(Sessions_Table)="
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
  	  		System.out.println(
  	  				  "com.yardi.ejb.UserGroupsBean isManaged() 0022 "
  	  				+ "\n"
	  				+ "   em.contains(User_Groups)=false"
	  				);
	  		return false;
  		} 
  		
  		if (isEntity(userGroups.getClass())==false) {
  	  		System.out.println(
	  				  "com.yardi.ejb.UserGroupsBean isManaged() 0023 "
	  				+ "\n"
	  				+ "   em.contains(User_Groups)=false"
	  				);
	  		return false;
  		}

  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0004 "
  				+ "\n"
  				+ "   em.contains(User_Groups)="
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
			System.out.println(
					  "com.yardi.ejb.UserGroupsBean isManaged() 001D "
					+ "\n"
	  				+ "   em.contains(User_Groups2)=false"
					);
			return false;
		}
		
  		if (isEntity(userGroups2.getClass())==false) {
  	  		System.out.println(
	  				  "com.yardi.ejb.UserGroupsBean isManaged() 0024 "
	  				+ "\n"
	  				+ "   em.contains(User_Groups2)=false"
	  				);
	  		return false;
  		}

  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0015 "
  				+ "\n"
  				+ "   em.contains(User_Groups2)="
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
  	  		System.out.println(
  	  				  "com.yardi.ejb.UserGroupsBean isManaged() 0025 "
  	  				+ "\n"
	  				+ "   em.contains(User_Profile)=false"
	  				);
	  		return false;
  		} 
  		
  		if (isEntity(userProfile.getClass())==false) {
  	  		System.out.println(
	  				  "com.yardi.ejb.UserGroupsBean isManaged() 0026 "
	  				+ "\n"
	  				+ "   em.contains(User_Profile)=false"
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
	public void persist(User_Groups2 group) {
		/*debug*/
		System.out.println("com.yardi.ejb.UserGroupsBean.persist() 0018 ");
		/*debug*/
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
	public void remove(User_Groups2 group) {
		/*debug*/
		System.out.println("com.yardi.ejb.UserGroupsBean.remove() 0017 ");
		/*debug*/
		isJoined();

		if (group!=null) {
			/*debug*/
			System.out.println("com.yardi.ejb.UserGroupsBean.remove() 0019 ");
			/*debug*/
			em.remove(group);
			isManaged(group);
		}
	}
	
	/**
	 * Remove the stateful session bean and release resources it holds. 	
	 */
	@Override
	@Remove
	public void removeBean() {
		System.out.println("com.yardi.ejb.UserGroupsBean removeBean() 0008 ");
	}
	
	/**
	 * Construct the Vector of initial pages for the groups the user belongs to.<p>
	 * 
	 * <i>views/selectGroup.html</i> uses the initialPageList. For each group the user belongs to, the Vector contains a short description for a button,
	 * a label for a button and URL of the group's initial page.<br><br> 
	 * 
	 * Field <i>initialPageList</i> exists for the convenience of other components that may need a reference to the initial page list.
	 */
	private void setInitialPageList() {
		initialPageList = new Vector<LoginInitialPage>();

		for (LoginUserGroupsGraph g : userGroups) {
			//getGmDescription returns a string containing the short description for the button and a label for the button
			//getGmInitialPage() returns the url value for url= attribute
			initialPageList.add(new LoginInitialPage(g.getGmDescription(),
				g.getGmInitialPage()));
		}
	}
	
	/**
	 * Field <i>loginSessionsTable</i> is a reference to the Sessions_Table entity embeded within the User_Groups entity.<p>
	 * It exists for the convenience of other components that may need this reference.
	 */
	private void setLoginSessionTable() {
		/*debug*/
		System.out.println("com.yardi.ejb.UserGroupsBean.setLoginSessionTable() 000E ");
		
		if (userGroupsEntity==null) {
			System.out.println("com.yardi.ejb.UserGroupsBean.setLoginSessionTable() 000B ");
		}
		if (userGroupsEntity.getUgSessionsTable()==null) {
			System.out.println("com.yardi.ejb.UserGroupsBean.setLoginSessionTable() 000A ");
		}
		/*debug*/
		loginSessionTable = userGroupsEntity.getUgSessionsTable();
		isJoined();
		isManaged(loginSessionTable);
	}

	/**
	 * Field <i>loginUserProfile</i> is a reference to the User_Profile entity embebed within the User_Groups entity.<p>
	 * It exists for the convenience of other components that may need this reference.  
	 */
	private void setLoginUserProfile() {
		/*debug*/
		System.out.println("com.yardi.ejb.UserGroupsBean.setLoginUserProfile() 000F ");
		
		if (userGroupsEntity.getUgUserProfile()==null) {
			System.out.println("com.yardi.ejb.UserGroupsBean.setLoginUserProfile() 0009 ");
		}
		/*debug*/
		loginUserProfile = userGroupsEntity.getUgUserProfile();
		isJoined();
		isManaged(loginUserProfile);
	}
	
	/**
	 * Set a Vector containing every column from USER_GROUPS database table and every column from GROUPS_MASTER database table.<p> 
	 * There is one element in this Vector for each group the user belongs to.<br>
	 * Field userGroups exists for the convenience of other components that might need this reference.<br>
	 * 
	 * @param userGroups Vector containing every column from USER_GROUPS database table and every column from GROUPS_MASTER database table.
	 */
	private void setUserGroups(Vector<LoginUserGroupsGraph> userGroups) {
		this.userGroups = userGroups;
	}
}
