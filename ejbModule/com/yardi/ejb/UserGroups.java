package com.yardi.ejb;

import java.util.Vector;

import com.yardi.ejb.model.User_Groups2;

import jakarta.ejb.Local;

@Local
public interface UserGroups {
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
	UserGroupsResult find(String userID);
	/**
	 * Find all User_Groups2 entities for the given userName.<p>
	 * 
	 * @param userName specifies which User_Groups2 entities to find.
	 * @return Vector containing User_Groups2 entities matching the given userName. Returns an empty Vector if the persistence context contains 
	 * no User_Groups2 entities matching the given userName and the USER_GROUPS database table has no rows matching userName.
	 */
	Vector<User_Groups2> find2(String userName);
	/**
	 * Persist a User_Groups2 entity.<p>
	 * 
	 * @param group the entity to persist.
	 */ 
	void persist(User_Groups2 group);
	/**
	 * Remove the given User_Groups2 entity.<p>
	 * 
	 * @param group the entity to remove.
	 */
	void remove(User_Groups2 group);
}
