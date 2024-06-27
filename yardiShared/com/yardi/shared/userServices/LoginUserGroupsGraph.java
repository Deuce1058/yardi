package com.yardi.shared.userServices;

/**
 * A container to hold every column resulting from the join of database tables USER_GROUPS and GROUPS_MASTER.  
 */
public class LoginUserGroupsGraph  implements Comparable<LoginUserGroupsGraph> {
	/**
	 * User ID from table USER_GROUPS
	 */
	private String ugUserId;
	/**
	 * Group type from table USER_GROUPS
	 */
	private int ugGroup;
	/**
	 * Relative record number from table USER_GROUPS
	 */
	private long ugRrn;
	/**
	 * Group type from table GROUPS_MASTER
	 */
	private int gmType;
	/**
	 * Group description from table GROUPS_MASTER
	 */
	private String gmDescription;
	/**
	 * Initial page for the group from table GROUPS_MASTER
	 */
	private String gmInitialPage;
	/**
	 * Relative record number from table GROUPS_MASTER
	 */
	private long gmRrn;

	public LoginUserGroupsGraph() {
	}

	/**
	 * Constructor using all fields
	 * @param ugUserId User ID from table USER_GROUPS
	 * @param ugGroup Group type from table USER_GROUPS
	 * @param ugRrn Relative record number from table USER_GROUPS
	 * @param gmType Group type from table GROUPS_MASTER
	 * @param gmDescription Group description from table GROUPS_MASTER
	 * @param gmInitialPage Initial page for the group from table GROUPS_MASTER
	 * @param gmRrn Relative record number from table GROUPS_MASTER
	 */
	public LoginUserGroupsGraph(String ugUserId, int ugGroup, long ugRrn, int gmType, String gmDescription,
			String gmInitialPage, long gmRrn) {
		this.ugUserId = ugUserId;
		this.ugGroup = ugGroup;
		this.ugRrn = ugRrn;
		this.gmType = gmType;
		this.gmDescription = gmDescription;
		this.gmInitialPage = gmInitialPage;
		this.gmRrn = gmRrn;
	}

	/**
	 * {@link Comparable#compareTo(Object) Implements compareTo(T o) in java.lang.Comparable&lt;T&gt;}
	 */
	public int compareTo(LoginUserGroupsGraph g) {
		if (this == g) return 0;
		if (this.getUgGroup() < g.getUgGroup()) return -1;
		if (this.getUgGroup() > g.getUgGroup()) return 1;
		return 0;
	}
	
	/**
	 * Determines whether the given object is equal to this LoginUserGroupsGraph.<p> 
	 * The given object is considered equal to this this LoginUserGroupsGraph if the given object is an instance of this LoginUserGroupsGraph and the user group 
	 * value of the given object is equal to the value of this user group
	 * 
	 * @param o The object to be tested
	 * @return boolean indicating whether the given object is equal to this LoginUserGroupsGraph 
	 */
	public boolean equals(Object o) {
		if (!(o instanceof LoginUserGroupsGraph)) 
			return false;
	
		LoginUserGroupsGraph g = (LoginUserGroupsGraph) o;
        return g.getUgGroup() == ugGroup;	
    }
	
	/**
	 * Return the group's description
	 * @return group's description 
	 */
	public String getGmDescription() {
		return gmDescription;
	}

	/**
	 * Return the group's initial page
	 * @return group's initial page
	 */
	public String getGmInitialPage() {
		return gmInitialPage;
	}

	/**
	 * Return the relative record number from database table GROUPS_MASTER
	 * @return relative record number from database table GROUPS_MASTER
	 */
	public long getGmRrn() {
		return gmRrn;
	}

	/**
	 * Return the group type
	 * @return group type
	 */
	public int getGmType() {
		return gmType;
	}

	/**
	 * Return the group number
	 * @return group number
	 */
	public int getUgGroup() {
		return ugGroup;
	}

	/**
	 * Return the relative record number from database table USER_GROUPS
	 * @return relative record number from database table USER_GROUPS
	 */
	public long getUgRrn() {
		return ugRrn;
	}
	
	/**
	 * Return the user id from database table USER_GROUPS
	 * @return user id from database table USER_GROUPS
	 */
	public String getUgUserId() {
		return ugUserId;
	}

	public String toString() {
		return "LoginUserGroupsGraph [ugUserId=" + ugUserId + ", ugGroup=" + ugGroup + ", ugRrn=" + ugRrn + ", gmType="
				+ gmType + ", gmDescription=" + gmDescription + ", gmInitialPage=" + gmInitialPage + ", gmRrn=" + gmRrn
				+ "]";
	}
}
