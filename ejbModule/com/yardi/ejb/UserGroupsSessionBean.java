package com.yardi.ejb;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.yardi.userServices.UserGroupsGraph;

/**
 * Session Bean implementation class UserGroupsSessionBean
 */
@Stateless
public class UserGroupsSessionBean implements UserGroupsSessionBeanRemote {
	@PersistenceContext(unitName="groupsMaster")
	private EntityManager em;

	public Vector<UserGroupsGraph> find(String userID) {
		System.out.println("com.yardi.ejb.UserGroupsSessionBean find() 0000");
		Vector<UserGroupsGraph> userGroupsVector = new Vector<UserGroupsGraph>();
		List userGroupsList;
    	TypedQuery<UserGroups> qry = em.createQuery(
    			  "SELECT g from UserGroups g " 
    			+ "WHERE g.ugUserId = :userID ",
    			UserGroups.class);
    	userGroupsList = qry
    			.setParameter("userID", userID)
    			.getResultList();

    	for (Object o : userGroupsList) {
    		UserGroups userGroup = (UserGroups) o;
    		userGroupsVector.add(new UserGroupsGraph(
    			userGroup.getUgUserId(), 
    			userGroup.getUgGroup(), 
    			userGroup.getUgRrn(), 
    			0, 
    			"", 
    			"", 
    			0)
    		);
    	}
    	
    	for (UserGroupsGraph graph : userGroupsVector) {
    		int groupType = graph.getUgGroup();
        	TypedQuery<GroupsMaster> q = em.createQuery(
      			  "SELECT g from GroupsMaster g " 
      			+ "WHERE g.gmType = :groupType ",
      			GroupsMaster.class);
        	GroupsMaster gm = q
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
    	
    	Collections.sort(userGroupsVector);
    	return userGroupsVector;
	}
	
    public UserGroupsSessionBean() {
    }
}
