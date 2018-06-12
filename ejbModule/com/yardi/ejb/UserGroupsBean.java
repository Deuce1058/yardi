package com.yardi.ejb;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.yardi.userServices.InitialPage;
import com.yardi.userServices.UserGroupsGraph;

/**
 * Session Bean implementation class UserGroupsBean
 */
@Stateless
public class UserGroupsBean implements UserGroups {
	@PersistenceContext(unitName="yardi")
	private EntityManager em;
	private String initialPage = "";
	private String feedback = "";
	private Vector<InitialPage> initialPageList = new Vector<InitialPage>();
	private Vector<UserGroupsGraph> userGroups;

	public Vector<UserGroupsGraph> find(String userID) {
		System.out.println("com.yardi.ejb.UserGroupsBean find() 0000");
		Vector<UserGroupsGraph> userGroupsVector = new Vector<UserGroupsGraph>();
		List userGroupsList;
    	TypedQuery<User_Groups> qry = em.createQuery(
    			  "SELECT g from User_Groups g " 
    			+ "WHERE g.ugUserId = :userID AND "
    			+ "g.ugGroupsMaster.gmType = g.ugGroup ",
    			User_Groups.class);
    	userGroupsList = qry
    			.setParameter("userID", userID)
    			.getResultList();

    	for (Object o : userGroupsList) {
    		User_Groups userGroup = (User_Groups) o;
    		userGroupsVector.add(new UserGroupsGraph(
    			userGroup.getUgUserId(), 
    			userGroup.getUgGroup(), 
    			userGroup.getUgRrn(), 
    			userGroup.getUgGroupsMaster().getGmType(), 
    			userGroup.getUgGroupsMaster().getGmDescription(), 
    			userGroup.getUgGroupsMaster().getGmInitialPage(), 
    			userGroup.getUgGroupsMaster().getGmRrn())
    		);
    	}
    	
    	/*
    	for (UserGroupsGraph graph : userGroupsVector) {
    		int groupType = graph.getUgGroup();
        	TypedQuery<Groups_Master> q = em.createQuery(
      			  "SELECT g from Groups_Master g " 
      			+ "WHERE g.gmType = :groupType ",
      			Groups_Master.class);
        	Groups_Master gm = q
      			.setParameter("groupType", groupType)
      			.getSingleResult();
        	
        	for (int n=0; n<userGroupsVector.size(); n++) {
        		UserGroupsGraph v = userGroupsVector.get(n);
        		if (v.getUgGroup() == gm.getGmType()) {
        			v.setGmType(gm.getGmType());
        			v.setGmDescription(gm.getGmDescription());
        			v.setGmInitialPage(gm.getGmInitialPage());
        			v.setGmRrn(gm.getGmRrn());
        			n = userGroupsVector.size() + 1;
        		}
        	}
    	}
    	*/
    	Collections.sort(userGroupsVector);
    	return userGroupsVector;
	}
	
    public String getInitialPage(String userName) {
    	feedback = com.yardi.rentSurvey.YardiConstants.YRD0000;
    	userGroups = find(userName);
		initialPage = userGroups.get(0).getGmInitialPage(); //GM_INITIAL_PAGE from GROUPS_MASTER
		//debug
		System.out.println("com.yardi.ejb UserGroupsBean getInitialPage() 0001" 
				+ "\n"
				+ "   initialPage="
				+ initialPage
				);
		System.out.println("com.yardi.ejb UserGroupsBean getInitialPage() 0002");
		for (UserGroupsGraph u : userGroups) {
			System.out.println(
				  "\n"
				+ "   UserGroupsGraph=" 
				+ u.toString()
				);
		}
		//debug

		if (userGroups.size()>1) {
			// user is in multiple groups. Set ST_LAST_REQUEST to the html select group page. User picks the initial page
			feedback = com.yardi.rentSurvey.YardiConstants.YRD000E;
			initialPage = com.yardi.rentSurvey.YardiConstants.USER_SELECT_GROUP_PAGE;
			//debug
			System.out.println("com.yardi.ejb UserGroupsBean getInitialPage() 0003" 
					+ "\n"
					+ "   initialPage="
					+ initialPage
					);
			//debug
		}
		
		return initialPage;
	}

	public String getFeedback() {
		return feedback;
	}

	public Vector<InitialPage> getInitialPageList() {
		setInitialPageList();
		return initialPageList;
	}

	private void setInitialPageList() {
		this.initialPageList = new Vector<InitialPage>();

		for (UserGroupsGraph g : userGroups) {
			//getGmDescription returns a string containing the short description for the button and a label for the button
			//getGmInitialPage() returns the url value for url= attribute
			initialPageList.add(new InitialPage(g.getGmDescription(),
				g.getGmInitialPage()));
		}
	}

	public UserGroupsBean() {
    }
}
