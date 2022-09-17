package com.yardi.ejb;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.persistence.Query;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import com.yardi.ejb.model.Sessions_Table;
import com.yardi.ejb.model.User_Groups;
import com.yardi.ejb.model.User_Profile;
import com.yardi.shared.userServices.LoginInitialPage;
import com.yardi.shared.userServices.LoginUserGroupsGraph;

/**
 * Session Bean implementation class UserGroupsBean
 */
@Stateful
public class UserGroupsBean implements UserGroups {
	@PersistenceContext(unitName="yardi", type=PersistenceContextType.EXTENDED)
	private EntityManager em;
	private String initialPage = "";
	private String feedback = "";
	private Vector<LoginInitialPage> initialPageList = new Vector<LoginInitialPage>();
	private Vector<LoginUserGroupsGraph> userGroups;
	private User_Groups loginUserGroups=null;
	private User_Profile loginUserProfile;
	private Sessions_Table loginSessionTable;

	public UserGroupsBean() {
		//debug
		System.out.println("com.yardi.ejb.UserGroupsBean UserGroupsBean() 0007 ");
		//debug
    }
	
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
    		
    		if(loginUserGroups==null) {
    			loginUserGroups = userGroup.getLoginUserGroups();
    		}

    		System.out.println("com.yardi.ejb.UserGroupsBean find() 0006 ");
    		isJoined();
    		isManaged(userGroup);
    	}
    	
    	//debug
		System.out.println("com.yardi.ejb.UserGroupsBean find() 000D ");
		//debug
    	Collections.sort(userGroupsVector);
    	setUserGroups(userGroupsVector);
    	setLoginSessionTable();
    	setLoginUserProfile();
    	return userGroupsVector;
	}

	public String getFeedback() {
		return feedback;
	}

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

	public Vector<LoginInitialPage> getInitialPageList() {
		setInitialPageList();
		return initialPageList;
	}

	public Sessions_Table getLoginSessionTable() {
		return loginSessionTable;
	}

	public User_Profile getLoginUserProfile() {
		return loginUserProfile;
	}

	@Override
	public Vector<LoginUserGroupsGraph> getUserGroups() {
		return userGroups;
	}

	private boolean isJoined() {
  		System.out.println("com.yardi.ejb.UserGroupsBean isJoined() 0005 "
  				+ "\n"
  				+ "   isJoined="
  				+ em.isJoinedToTransaction()
  				);
		return em.isJoinedToTransaction();
	}

	private boolean isManaged(Sessions_Table loginSessionsTable) {
  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0011 ");
  		
  		if (!(loginSessionsTable==null)) {
  	  		System.out.println(
  	  				  "\n"
  	  				+ "   em.contains(loginSessionsTable)="
  	  				+ em.contains(loginSessionsTable)
  	  				);
  			return em.contains(loginSessionsTable);  			
  		} 
  		else {
  	  		System.out.println(
  	  				  "\n"
  	  				+ "   em.contains(loginSessionsTable)=false"
  	  				);
  	  		return false;
  		}
	}

	private boolean isManaged(User_Groups loginUserGroups) {
  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0004 "
  				+ "\n"
  				+ "   em.contains(loginUserGroups)="
  				+ em.contains(loginUserGroups)
  				);
		return em.contains(loginUserGroups);
	}

	private boolean isManaged(User_Profile loginUserProfile) {
  		System.out.println("com.yardi.ejb.UserGroupsBean isManaged() 0012 "
  				+ "\n"
  				+ "   em.contains(loginUserProfile)="
  				+ em.contains(loginUserProfile)
  				);
		return em.contains(loginUserProfile);
	}

	@PostConstruct
	private void postConstructCallback() {
		System.out.println("com.yardi.ejb.UserGroupsBean.postConstructCallback() 0010 "
				+ "\n    UserGroupsBean="
				+ this);
	}
	
	@Override
	@Remove
	public void removeBean() {
		System.out.println("com.yardi.ejb.UserGroupsBean removeBean() 0008 ");
	}
	
	private void setInitialPageList() {
		initialPageList = new Vector<LoginInitialPage>();

		for (LoginUserGroupsGraph g : userGroups) {
			//getGmDescription returns a string containing the short description for the button and a label for the button
			//getGmInitialPage() returns the url value for url= attribute
			initialPageList.add(new LoginInitialPage(g.getGmDescription(),
				g.getGmInitialPage()));
		}
	}
	
	private void setLoginSessionTable() {
		/*debug*/
		System.out.println("com.yardi.ejb.UserGroupsBean.setLoginSessionTable() 000E ");
		
		if (loginUserGroups==null) {
			System.out.println("com.yardi.ejb.UserGroupsBean.setLoginSessionTable() 000B ");
		}
		if (loginUserGroups.getUgSessionsTable()==null) {
			System.out.println("com.yardi.ejb.UserGroupsBean.setLoginSessionTable() 000A ");
		}
		/*debug*/
		loginSessionTable = loginUserGroups.getUgSessionsTable();
		isJoined();
		isManaged(loginSessionTable);
	}

	private void setLoginUserProfile() {
		/*debug*/
		System.out.println("com.yardi.ejb.UserGroupsBean.setLoginUserProfile() 000F ");
		
		if (loginUserGroups.getUgUserProfile()==null) {
			System.out.println("com.yardi.ejb.UserGroupsBean.setLoginUserProfile() 0009 ");
		}
		/*debug*/
		loginUserProfile = loginUserGroups.getUgUserProfile();
		isJoined();
		isManaged(loginUserProfile);
	}
	
	private void setUserGroups(Vector<LoginUserGroupsGraph> userGroups) {
		this.userGroups = userGroups;
	}
}
