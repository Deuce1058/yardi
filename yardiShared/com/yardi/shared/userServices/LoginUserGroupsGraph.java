package com.yardi.shared.userServices;

public class LoginUserGroupsGraph  implements Comparable<LoginUserGroupsGraph> {
	private String ugUserId;
	private int ugGroup;
	private long ugRrn;
	private int gmType;
	private String gmDescription;
	private String gmInitialPage;
	private long gmRrn;

	public LoginUserGroupsGraph() {
	}

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

	public boolean equals(Object o) {
		if (!(o instanceof LoginUserGroupsGraph)) 
			return false;
	
		LoginUserGroupsGraph g = (LoginUserGroupsGraph) o;
        return g.getUgGroup() == ugGroup;	
    }
	
	public int compareTo(LoginUserGroupsGraph g) {
		if (this == g) return 0;
		if (this.getUgGroup() < g.getUgGroup()) return -1;
		if (this.getUgGroup() > g.getUgGroup()) return 1;
		return 0;
	}
	
	public String getUgUserId() {
		return ugUserId;
	}

	public int getUgGroup() {
		return ugGroup;
	}

	public long getUgRrn() {
		return ugRrn;
	}

	public int getGmType() {
		return gmType;
	}

	public String getGmDescription() {
		return gmDescription;
	}

	public String getGmInitialPage() {
		return gmInitialPage;
	}

	public long getGmRrn() {
		return gmRrn;
	}

	public String toString() {
		return "LoginUserGroupsGraph [ugUserId=" + ugUserId + ", ugGroup=" + ugGroup + ", ugRrn=" + ugRrn + ", gmType="
				+ gmType + ", gmDescription=" + gmDescription + ", gmInitialPage=" + gmInitialPage + ", gmRrn=" + gmRrn
				+ "]";
	}
}
