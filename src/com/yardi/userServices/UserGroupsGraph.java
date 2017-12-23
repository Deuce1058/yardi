package com.yardi.userServices;

public class UserGroupsGraph  implements Comparable<UserGroupsGraph> {
	private String ugUserId;
	private int ugGroup;
	private long ugRrn;
	private int gmType;
	private String gmDescription;
	private String gmInitialPage;
	private long gmRrn;

	public UserGroupsGraph() {
	}

	public UserGroupsGraph(String ugUserId, int ugGroup, long ugRrn, int gmType, String gmDescription,
			String gmInitialPage, long gmRrn) {
		this.ugUserId = ugUserId;
		this.ugGroup = ugGroup;
		this.ugRrn = ugRrn;
		this.gmType = gmType;
		this.gmDescription = gmDescription;
		this.gmInitialPage = gmInitialPage;
		this.gmRrn = gmRrn;
	}

	public boolean equals(Object o) {
		if (!(o instanceof UserGroupsGraph)) 
			return false;
	
		UserGroupsGraph g = (UserGroupsGraph) o;
        return g.getUgGroup() == ugGroup;	
    }
	
	public int compareTo(UserGroupsGraph g) {
		if (this == g) return 0;
		if (this.getUgGroup() < g.getUgGroup()) return -1;
		if (this.getUgGroup() > g.getUgGroup()) return 1;
		return 0;
	}
	
	public String getUgUserId() {
		return ugUserId;
	}

	public void setUgUserId(String ugUserId) {
		this.ugUserId = ugUserId;
	}

	public int getUgGroup() {
		return ugGroup;
	}

	public void setUgGroup(int ugGroup) {
		this.ugGroup = ugGroup;
	}

	public long getUgRrn() {
		return ugRrn;
	}

	public void setUgRrn(long ugRrn) {
		this.ugRrn = ugRrn;
	}

	public int getGmType() {
		return gmType;
	}

	public void setGmType(int gmType) {
		this.gmType = gmType;
	}

	public String getGmDescription() {
		return gmDescription;
	}

	public void setGmDescription(String gmDescription) {
		this.gmDescription = gmDescription;
	}

	public String getGmInitialPage() {
		return gmInitialPage;
	}

	public void setGmInitialPage(String gmInitialPage) {
		this.gmInitialPage = gmInitialPage;
	}

	public long getGmRrn() {
		return gmRrn;
	}

	public void setGmRrn(long gmRrn) {
		this.gmRrn = gmRrn;
	}

	public String toString() {
		return "UserGroupsGraph [ugUserId=" + ugUserId + ", ugGroup=" + ugGroup + ", ugRrn=" + ugRrn + ", gmType="
				+ gmType + ", gmDescription=" + gmDescription + ", gmInitialPage=" + gmInitialPage + ", gmRrn=" + gmRrn
				+ "]";
	}
}
