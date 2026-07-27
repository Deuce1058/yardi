package com.yardi.ejb;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.yardi.ejb.model.Sessions_Table;
import com.yardi.ejb.model.User_Groups;
import com.yardi.ejb.model.User_Profile;
import com.yardi.shared.userServices.LoginInitialPage;
import com.yardi.shared.userServices.LoginUserGroupsGraph;
import com.yardi.shared.userServices.OperationResult;

/**
 * Immutable result object carrying the outcome of a user-groups lookup operation.
 *
 * <p><strong>Important note on entity mutability:</strong> Although this class is declared
 * {@code final} and its fields are {@code final}, the JPA entities it holds
 * ({@link User_Profile}, {@link User_Groups}, {@link Sessions_Table}) are intentionally
 * <em>mutable</em>. Callers depend on receiving managed entities whose state may be
 * lazily loaded or updated by the persistence context after this object is constructed.
 * Do <strong>not</strong> attempt to defensively copy or freeze these entities — doing so
 * would break JPA proxy behavior and cause {@code LazyInitializationException}s or stale
 * data. The immutability guarantee of this class covers only its own field references,
 * not the internal state of the entities those references point to.
 *
 * @see User_Profile
 * @see User_Groups
 * @see Sessions_Table
 */
public final class UserGroupsResult implements OperationResult {
	/**
	 * Raw feedback from the most recent operation. Formatted as {@code messageId=messageDescription},
	 * or a plain message if no delimiter is present.
	 */
	private final String feedback; 
	/**
	 * {@code true} if the operation completed successfully, {@code false} otherwise.
	 */
	private final boolean success; 
	/**
	 * The {@link User_Profile} entity embedded within {@link User_Groups}. Provided as a convenience
	 * reference for callers that need user profile data without navigating through {@link User_Groups}.
	 */
	private final User_Profile loginUserProfile;
	/**
	 * Vector that combines all columns from table USER_GROUPS and all columns from table GROUPS_MASTE. Used by the web to construct a dynamic list of groups the user belongs to, the 
	 * description of the group and a link the initial page of the group. User selects the initial page.   
	 */
	private final Vector<LoginUserGroupsGraph> userGroupsVector;
	/**
	 * A reference to the {@link User_Groups} entity.
	 */
	private final User_Groups user_Groups;
	/**
	 * The {@link Sessions_Table} entity embedded within {@link User_Groups}. Provided as a convenience
	 * reference for callers that need session data without navigating through {@link User_Groups}.
	 */
	private final Sessions_Table loginSessionTable;

	/**
	 * Constructs a result when user groups were successfully found.<p> 
	 * Builds {@link #userGroupsVector} from the provided list, resolves the embedded {@link Sessions_Table} and {@link User_Profile} entities, and 
	 * sets {@link #initialPageFeedback} based on whether the user belongs to multiple groups.
	 * 
	 * @param success general success or failure of the operation
	 * @param userGroupsList the list of groups that the user belongs to
	 */
	private UserGroupsResult(boolean success, List<User_Groups> userGroupsList) {
		System.out.println("com.yardi.ejb.UserGroupsResult.UserGroupsResult() 0004 ");
		this.success = success;
		userGroupsVector = buildUserGroupsVector(userGroupsList);
		user_Groups = userGroupsList.get(0);
		loginSessionTable = resolveSessionTable();
    	loginUserProfile = resolveUserProfile();
    	
    	if (userGroupsVector.size()>1) {
    		System.out.println("com.yardi.ejb.UserGroupsResult.UserGroupsResult() 0005 ");
        	feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000E;
    	} else {
    		System.out.println("com.yardi.ejb.UserGroupsResult.UserGroupsResult() 0007 ");
        	feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000;
    	}
	} 

	/**
	 * Constructs a UserGroupsResult when the user name was not found.<p> 	 
	 * {@link #userGroupsVector} is initialized to an empty vector and all entity references are set to {@code null}.
	 * 
	 * @param feedback the outcome of the operation
	 */
	private UserGroupsResult(String feedback) {
		System.out.println("com.yardi.ejb.UserGroupsResult.UserGroupsResult() 0000 ");
		this.feedback = feedback;
		this.success = false;
		this.loginUserProfile = null;
		this.userGroupsVector = new Vector<LoginUserGroupsGraph>();
		this.user_Groups=null;
		this.loginSessionTable=null;
	}

	/**
	 * Construct the Vector of initial pages for the groups the user belongs to.<p>
	 * 
	 * <i>views/selectGroup.html</i> uses the initialPageList. For each group the user belongs to, the Vector contains a short description for a button,
	 * a label for a button and URL of the group's initial page.<br><br> 
	 * 
	 * @return a {@link Vector} of {@link LoginInitialPage} records, one per group the user belongs to
	 */
	private Vector<LoginInitialPage> buildInitialPageList() {
		System.out.println("com.yardi.ejb.UserGroupsResult.buildInitialPageList() 0008 ");
		Vector<LoginInitialPage> initialPageList = new Vector<LoginInitialPage>();

		for (LoginUserGroupsGraph g : userGroupsVector) {
			//getGmDescription returns a string containing the short description for the button and a label for the button
			//getGmInitialPage() returns the url value for url= attribute
			initialPageList.add(new LoginInitialPage(g.getGmDescription(),
				g.getGmInitialPage()));
		}
		
		System.out.println("com.yardi.ejb.UserGroupsResult.buildInitialPageList() 000C "
				+ "\n    "
				+ "initialPageList="
				+ initialPageList
				);
		return initialPageList;
	}

	/**
	 * For each {@link User_Groups} entity in the list construct a vector containing the group id, group type, group description and initial page for the group.
	 * The web will use this vector to dynamically build a list of the group id and group description. The user will select a link in this list to request the initial page
	 * @param userGroupsList the list of {@link User_Groups} entities to map
	 * @return sorted vector containing all columns from {@link User_Groups} and {@link com.yardi.ejb.model.Groups_Master Groups_Master}
	 */
	private Vector<LoginUserGroupsGraph> buildUserGroupsVector(List<User_Groups> userGroupsList) {
		System.out.println("com.yardi.ejb.UserGroupsResult.buildUserGroupsVector() 0011 ");
		Vector<LoginUserGroupsGraph> userGroupsVector = new Vector<LoginUserGroupsGraph>();
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
    	}
    	
    	Collections.sort(userGroupsVector);
		System.out.println("com.yardi.ejb.UserGroupsResult.buildUserGroupsVector() 0006 "
				+ "\n    "
				+ "userGroupsVector="
				+ userGroupsVector
				);
    	return userGroupsVector;
	}

	/**
	 * Return the raw feedback string
	 * @return raw feedback string 
	 */
	@Override
	public String  getFeedback() { 
		return feedback; 
	}
	
	/**
	 * Returns the initial page URL for the user's group.<p> If the user belongs to multiple groups,
	 * returns {@link com.yardi.shared.rentSurvey.YardiConstants#USER_SELECT_GROUP_PAGE} and the
	 * user will select the initial page from from a list. *
     * 
     * @param userName specifies the user whose initial page is returned.
     * @return the initial page URL, or the group selection page URL if the user belongs to multiple groups
     */
	public String getInitialPage(String userName) {
		String initialPage = userGroupsVector.get(0).getGmInitialPage(); //GM_INITIAL_PAGE from GROUPS_MASTER
		System.out.println("com.yardi.ejb.UserGroupsResult.getInitialPage() 0001 " 
				+ "\n"
				+ "   initialPage="
				+ initialPage
				);
		System.out.println("com.yardi.ejb.UserGroupsResult.getInitialPage() 0002 ");
		for (LoginUserGroupsGraph u : userGroupsVector) {
			System.out.println(
				  "\n"
				+ "   UserGroupsGraph=" 
				+ u.toString()
				);
		}

		if (userGroupsVector.size()>1) {
			// user is in multiple groups. Set ST_LAST_REQUEST to the html select group page. User picks the initial page
			initialPage = com.yardi.shared.rentSurvey.YardiConstants.USER_SELECT_GROUP_PAGE;
			System.out.println("com.yardi.ejb.UserGroupsResult.getInitialPage() 0003 " 
					+ "\n"
					+ "   initialPage="
					+ initialPage
					);
		}
		
		return initialPage;
	}

	
	/**
	 * When user belongs to multiple groups returns the data needed to render {@code views/selectGroup.html}.<br>
	 * Each element contains the button description, button label, and initial page URL for one group. 
	 * 
	 * @return a {@link Vector} of {@link LoginInitialPage} records, one per group the user belongs to
	 */
	public Vector<LoginInitialPage> getInitialPageList() {
		return buildInitialPageList();
	}
	
	/**
	 * Returns the {@link Sessions_Table} entity embedded within {@link User_Groups}
	 * @return the {@link Sessions_Table} entity embedded within {@link User_Groups}
	 */
	public Sessions_Table getLoginSessionTable() {
		return loginSessionTable;
	} 

	/**
	 * Returns the {@link User_Profile} entity embedded within {@link User_Groups}
	 * @return the {@link User_Profile} entity embedded within {@link User_Groups}
	 */
	public User_Profile getLoginUserProfile() {
		return loginUserProfile;
	}

	
	/**
	 * Return the message description which is the String on the right side of the delimiter of the raw feedback String. Return the entire raw feedback String of no delimiter is present  
	 * @return message description
	 */
	@Override
	public String getMsgDescription() {
		int i = feedback.indexOf("=");

		if (i<0) {
			return feedback;
		}

		return feedback.substring(i+1); 
	}

	/**
	 * Return the message ID which is the string on the left side of the delimiter of the raw feedback String. Return the entire raw feedback String of no delimiter is present.
	 * @return message ID
	 */
	@Override
	public String getMsgid() {
		int i = feedback.indexOf("=");

		if (i<0) {
			return feedback;
		}

		return feedback.substring(0, i); 
	}

	/**
	 * Return an immutable view of the groups the user belongs to.  
	 * 
	 * @implNote
	 * Caller needs to convert List to Vector&lt;LoginUserGroupsGraph&gt vector = new Vector&lt&gt(result.getUserGroupsVector());
	 * @return
	 */
	public List<LoginUserGroupsGraph> getUserGroupsVector() {
		return Collections.unmodifiableList(userGroupsVector);
	}

	/**
	 * Return the success flag which indicates whether the operation succeeded or failed
	 * @return {@code true} if the operation completed successfully, {@code false} otherwise
	 */
	@Override
	public boolean isSuccess() { 
		return success; 
	}

	/**
	 * Retrieves the {@link Sessions_Table} entity embedded within {@link #user_Groups}.
	 *
	 * @return the embedded {@link Sessions_Table}, or {@code null} if not present
	 */
	private Sessions_Table resolveSessionTable() {
		System.out.println("com.yardi.ejb.UserGroupsResult.resolveSessionTable() 000E ");
		
		if (user_Groups==null) {
			System.out.println("com.yardi.ejb.UserGroupsResult.resolveSessionTable() 000B ");
		}
		
		if (user_Groups.getUgSessionsTable()==null) {
			System.out.println("com.yardi.ejb.UserGroupsResult.resolveSessionTable() 000A ");
		}
		
		return user_Groups.getUgSessionsTable();
	}

	/**
	 * Retrieves the {@link User_Profile} entity embedded within {@link #user_Groups}.
	 *
	 * @return the embedded {@link User_Profile}, or {@code null} if not present
	 */
	private User_Profile resolveUserProfile() {
		System.out.println("com.yardi.ejb.UserGroupsResult.resolveUserProfile() 000F ");
		
		if (user_Groups.getUgUserProfile()==null) {
			System.out.println("com.yardi.ejb.UserGroupsResult.resolveUserProfile() 0009 ");
		}
		
		return user_Groups.getUgUserProfile();
	}
	
	@Override
	public String toString() {
		return "UserGroupsResult [feedback=" + feedback + ", success=" + success + ", loginUserProfile="
				+ loginUserProfile + ", user_Groups=" + user_Groups + ", loginSessionTable=" + loginSessionTable + "]";
	}

	/**
	 * Factory method for a successful result.<p> 
	 * Use when the user groups lookup finds one or more groups for the user.
	 *
	 * @param userGroupsList the list of {@link User_Groups} entities returned by the lookup operationS
	 * @return a successful {@link UserGroupsResult} with {@link #feedback} set to
	 * {@link com.yardi.shared.rentSurvey.YardiConstants#YRD0000}
	 */
	public static UserGroupsResult foundUserGroups(List<User_Groups> userGroupsList) {
		System.out.println("com.yardi.ejb.UserGroupsResult.foundUserGroups() 000D ");
		return new UserGroupsResult(true, userGroupsList);
	}

	/**
	 * Factory method for a failed result. Use when the user name was not found.
	 *
	 * @return a failed {@link UserGroupsResult} with {@link #feedback} set to
	 * {@link com.yardi.shared.rentSurvey.YardiConstants#YRD000D}
	 */
	public static UserGroupsResult noSuchUserName() { 
		System.out.println("com.yardi.ejb.UserGroupsResult.noSuchUserName() 0010 ");
		return new UserGroupsResult(com.yardi.shared.rentSurvey.YardiConstants.YRD000D);
	}
}
