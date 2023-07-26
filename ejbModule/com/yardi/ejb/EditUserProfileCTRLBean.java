package com.yardi.ejb;

import java.util.Vector;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

import com.yardi.ejb.model.Full_Sessions_Table;
import com.yardi.ejb.model.Full_User_Profile;
import com.yardi.ejb.model.User_Groups2;
import com.yardi.shared.QSECOFR.EditUserProfileRequest;


/**
 * EditUserProfileCTRL is a controller bean used by com.yardi.QSECOFR.EditUserProfileService
 */
@Stateful @TransactionManagement(TransactionManagementType.BEAN)
public class EditUserProfileCTRLBean implements EditUserProfileCTRL {
	/**
	 * Injected reference to EJB com.yardi.ejb.UserProfileBean
	 */
	@EJB UserProfile userProfileBean; 
	/**
	 * Injected reference to EJB com.yardi.ejb.UniqueTokensBean	 
	 */
	@EJB UniqueTokens uniqueTokensBean;
	/**
	 * Injected reference to EJB com.yardi.ejb.UserGroupsBean
	 */
	@EJB UserGroups userGroupsBean;
	/**
	 * Injected reference to EJB com.yardi.ejb.SessionsTableBean
	 */
	@EJB SessionsTable sessionsTableBean;
	@Resource UserTransaction tx;
	/**
	 * POJO representation of the web request. Also used as part of the web response.
	 */
	private EditUserProfileRequest editRequest;
	/**
	 * Clients read this field to determine the status of the the most recent method call that provides feedback.
	 */
	private String[] feedback;
	
	public EditUserProfileCTRLBean() {
        /*debug*/
    	System.out.println("com.yardi.ejb.EditUserProfileCTRLBean() ");
        /*debug*/
    }
	
	/**
	 * Commit the transaction
	 * @param tx jakarta.transaction.UserTransaction
	 */
    private void commit(UserTransaction tx) {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.commit() 000A ");
		/*debug*/
		try {
			tx.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			/*debug*/
			System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.commit() 000B ");
			/*debug*/
			e.printStackTrace();
		}
	}

	/**
	 * Create a new User_Groups2 entity constructed from the fields in com.yardi.shared.QSECOFR.EditUserProfileRequest with the default user group.<p>
	 * Cause a new User_Groups2 entity to be persisted to the database. The actual persist is performed by com.yardi.ejb.UserGroupsBean.persist().
	 */
	private void createUserGroup() {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.createUserGroup() 001B ");
		/*debug*/
		txStatus();
		userGroupsBean.persist(new User_Groups2(editRequest.getFindUser(), com.yardi.shared.rentSurvey.YardiConstants.DEFAULT_USER_GROUP));
	}
	
	/**
	 *  Create a new Full_User_Profile entity constructed from the fields in com.yardi.shared.QSECOFR.EditUserProfileRequest. <p>
	 *  A new User_Groups2 entity is also created as part of this transaction to 
	 *  ensure referential integrity between the USER_PROFILE and USER_GROUPS database tables.<br><br>
	 *  
	 *  <strong>Feedback provided:</strong> <span style="font-family:consolas;">YRD0000</span> process completed normally.
	 */	
	private void createUserProfile() {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.createUserProfile() 0013 ");
		/*debug*/
		try {
			/*debug*/
			System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.createUserProfile() 0014 ");
			/*debug*/
			tx.begin();
			txStatus();
			persistFull_User_Profile(
					new Full_User_Profile(
							editRequest.getFindUser(),
							editRequest.getCurrentToken(),
							editRequest.getUpHomeMarket(),
							editRequest.getFirstName(), 
							editRequest.getLastName(), 
							editRequest.getAddress1(), 
							editRequest.getAddress2(), 
							editRequest.getCity(), 
							editRequest.getState(), 
							editRequest.getZip(), 
							editRequest.getZip4(), 
							editRequest.getPhone(), 
							editRequest.getFax(), 
							editRequest.getEmail(), 
							editRequest.getSsn(),
							editRequest.getBirthDate(),
							editRequest.getActiveYN(),
							editRequest.getPasswordExpirationDate(),
							" ",
							editRequest.getProfileDisabledDate(),	
							editRequest.getLastLoginDate(),
							editRequest.getPasswordAttempts(),
							/*a value is required for the sequence field because of the constructor but it will still be assigned by JPA*/
							0l 
					)
			);
			/* maintain referential integrity by inserting a USER_GROUPS row with the default value for UG_GROUP */
			createUserGroup();
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("="); 
			editRequest.setMsgID(feedback[0]);
			editRequest.setMsgDescription(feedback[1]);
			commit(tx);
			txStatus();
		} catch (NotSupportedException | SystemException e) {
			/*debug*/
			System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.createUserProfile() 0015 ");
			/*debug*/
			e.printStackTrace();
		}
	}
	

	/**
	 * The Full_User_Profile entity specified by com.yardi.shared.QSECOFR.EditUserProfileRequest will be deleted. <p>
	 * The corresponding rows in the UNIQUE_TOKENS, SESSIONS_TABLE and USER_GROUPS database tables are also removed to ensure referential integrity<br><br>
	 * 
	 * <strong>Feedback provided:</strong> <span style="font-family:consolas;">YRD0000</span> process completed normally.
	 */
	private void deleteUserProfile() {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.deleteUserProfile() 0016 ");
		/*debug*/
		try {
			tx.begin();
			txStatus();
			/* A find occurred previously in a transaction that has committed. Need to find again here */
			Full_User_Profile userProfile = findFullUserProfile(editRequest.getFindUser());
			removeUserProfile(userProfile);
			Vector<Unique_Tokens> userTokens = findUserTokens(editRequest.getFindUser());
			
			if (userTokens.size() > 0) {
				for (Unique_Tokens t : userTokens) {
					removeToken(t);
				}
			}
			
			Vector<User_Groups2> userGroups = findUserGroups(editRequest.getFindUser());
			
			for (User_Groups2 g : userGroups) {
				removeUserGroup2(g);
			}
			
			Vector<Full_Sessions_Table> userSessions = findUserSessions(editRequest.getFindUser());

			if (userSessions.size() > 0) {
				for (Full_Sessions_Table s : userSessions) {
					removeFullSessionsTable(s);
				}
			}			
			
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("="); 
			editRequest.setMsgID(feedback[0]);
			editRequest.setMsgDescription(feedback[1]);
			commit(tx);
			txStatus();
		} catch (NotSupportedException | SystemException e) {
			/*debug*/
			System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.deleteUserProfile() 0017 ");
			/*debug*/
			e.printStackTrace();
		}
	}	
	
	
	/**
	 * Find the Full_User_Profile entity for the given user ID.<p>
	 * 
	 * This method delegates to com.yardi.ejb.UserProfileBean.findFullUserProfile() to find entities matching the given user ID.
	 * 
	 * @param userId user ID to find
	 * @return Full_User_Profile entity that matched the given user ID. Returns null if the persistence context has no Full_User_Profile entity matching 
	 * the given user ID and the USER_PROFILE database table does not have a row matching the given user ID. 
	 */
	private Full_User_Profile findFullUserProfile(String userId) {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.findFullUserProfile() 0023 ");
		/*debug*/
		return userProfileBean.findFullUserProfile(userId);
	} 
	
	/**
	 * Find all groups the given user ID belongs to.<p>
	 * 
	 * This method delegates to com.yardi.ejb.UserGroupsBean.find2() to find entities matching the given user ID. 
	 * 
	 * @param userId specifies the User_Groups2 entities to find. 
	 * @return Vector containing every group the user belongs to. The returned vector is empty if the persistence context has no User_Groups2 entity 
	 * matching the given user ID and the USER_GROUPS database table has no rows matching the given user ID.
	 */
	private Vector<User_Groups2> findUserGroups(String userId) {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.findUserGroups() 0022 ");
		/*debug*/
		return userGroupsBean.find2(userId);
	}
	
	/**
	 * Find the Full_User-Profile entity.<p>
	 * 
	 * The result of this find will determine whether the user can create, update, or delete. If the Full_User_Profile entity is found, update 
	 * and delete are allowed. Create is allowed if the Full_User_Profile entity is not found.<br><br>
	 * 
	 * If the Full_User_Profile entity was found then mapFullUserProfile() is called to map the Full_User_Profile entity to 
	 * com.yardi.shared.QSECOFR.EditUserProfileRequest which becomes part of the web response.<br><br>
	 * 
	 * <strong>Feedback provided:</strong>
	 * <pre>YRD0000 process completed normally
	 *YRD000D no such user name
	 * </pre> 
	 */
	private void findUserProfile() {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.finUserProfile() 0007 ");
		/*debug*/
		
		try {
			tx.begin();
			txStatus();
			Full_User_Profile userProfile = findFullUserProfile(editRequest.getFindUser());

			if (userProfile == null) {
				/*debug*/
				System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.finUserProfile() 000E ");
				/*debug*/
				feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD000D.split("="); 
				editRequest.setMsgID(feedback[0]);
				editRequest.setMsgDescription(feedback[1]);
				rollback(tx);
				txStatus();
				return;
			}
			
			/*debug*/
			System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.finUserProfile() 000F "
					+ "\n"
					+ "   userProfile=" 
					+ userProfile
					);
			/*debug*/
			mapFullUserProfile(userProfile); //map the Full_User_Profile entity to com.yardi.shared.QSECOFR.EditUserProfileRequest for the web response
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("="); 
			editRequest.setMsgID(feedback[0]);
			editRequest.setMsgDescription(feedback[1]);
			commit(tx);
			txStatus();
		} catch (NotSupportedException e) {
			/*debug*/
			System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.finUserProfile() 0008 ");
			/*debug*/
			e.printStackTrace();
		} catch (SystemException e) {
			/*debug*/
			System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.finUserProfile() 0009 ");
			/*debug*/
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Find all sessions for the given user.<p>
	 * 
	 * This method delegates to com.yardi.ejb.SessionsTableBean.findFull_Sessions_Table() to find Full_Sessions_Table entities matching the given user ID.
	 * 
	 * @param userId specifies the the Full_Sessions_Table entities to find.
	 * @return Vector containing all of the user's sessions. The returned vector is empty if the persistence context has no Full_Sessions_Table entity 
	 * matching the given user ID and SESSIONS_TABLE database table has no rows for the given user ID.
	 */
	private Vector<Full_Sessions_Table> findUserSessions(String userId) {
		return sessionsTableBean.findFull_Sessions_Table(userId);
	}

	/**
	 * Find all tokens for the given user ID.<p>
	 * 
	 * This method delegates to com.yardi.ejb.UniqueTokensBean.findTokens() to find Unique_Tokens entities matching the given user ID.
	 * 
	 * @param userId specifies the Unique_Tokens entities to find. 
	 * @return Vector containing all of the user's tokens. The returned Vector is empty if the persistence context has no Unique_Tokens entities  
	 * matching the given user ID and UNIQUE_TOKENS database table has no rows matching the given user ID. 
	 */
	private Vector<Unique_Tokens> findUserTokens(String userId) {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.findUserTokens() 0010 ");
		/*debug*/
		return uniqueTokensBean.findTokens(userId); 
	}	
	

	/**
	 * Return the changed EditUserProfileRequest.
	 * 
	 * @return the current com.yardi.shared.QSECOFR.EditUserProfileRequest
	 */
	public EditUserProfileRequest getEditUserProfileRequest() {
		return editRequest;		
	}
	
	/**
	 * Returns the current feedback.<p>
	 * 
	 * Each operation, create, read, update and delete, sets the feedback field to indicate status of the operation. Clients call this method to 
	 * determine the status of the most recent method called that provides feedback.<br><br>
	 *  
	 * Feedback has two parts, a message ID and a message description
	 * 
	 * @return String[] containing the current feedback 
	 */
	public String[] getFeedback() {
		return feedback;
	}
	
	/**
	 * An abstraction to handle the request from the web.<p>
	 * 
	 * The client only needs to call this method after injecting the web request and initializing the web request. 
	 * Based on the request, this method delegates to the appropriate method to complete the rest of the process.  
	 */
	public void handleRequest() {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.handleRequest() 0006 ");
		/*debug*/
    	if (editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_FIND)) {
    		findUserProfile();
    	}
    	
    	if (editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_ADD)) {
    		createUserProfile();
    	}
    	
    	if (editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_DELETE)) {
    		deleteUserProfile();
    	}
    	if (editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_UPDATE)) {
    		updateUserProfile();
    	}
    }
	
	/**
	 * Directs the web request to initialize itself.<p>
	 * 
	 * There are two kinds of initialization, special and normal. Special initialization is performed for a find, delete or read request. 
	 * Normal initialization is performed for every request.
	 */
	public void inzEditRequest() {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.inzEditRequest() 0003 ");
		/*debug*/
		if (   editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_FIND)
				|| editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_DELETE)
				|| editRequest.getAction().equals(com.yardi.shared.rentSurvey.YardiConstants.EDIT_USER_PROFILE_REQUEST_ACTION_REMOVE)) {
				/*
				 * The web page is giving us more than we need. We just need a user name for find and delete so
				 * clear out the other fields. Also, for find and delete, the other fields we dont need will have 
				 * garbage left over from the previous request.    
				 */
				System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.inzEditRequest() 0004 ");
				editRequest.specialInzsr();
			}
		
		editRequest.inzEditRequest();
	}

	/**
     * Map com.yardi.ejb.model.Full_User_Profile entity to com.yardi.shared.QSECOFR.EditUserProfileRequest fields. This will become part of the web response.<p>
     * 
     * <strong>Feedback provided:</strong> <span style="font-family:consolas;">YRD0000</span> process completed normally.
     * 
     * @param userProfile the Full_User_Profile entity to map 
     */
	private void mapFullUserProfile(Full_User_Profile userProfile) {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.mapFullUserProfile() 0011 ");
		/*debug*/
		feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("="); 
		editRequest.setMsgID         (feedback[0]);
		editRequest.setMsgDescription(feedback[1]);
		editRequest.setFirstName     (userProfile.getUpFirstName());
		editRequest.setLastName      (userProfile.getUpLastName());
		editRequest.setAddress1      (userProfile.getUpAddress1());

		if (userProfile.getUpAddress2()==null) {
			editRequest.setAddress2  ("");
		} else {
			editRequest.setAddress2  (userProfile.getUpAddress2());
		}

		editRequest.setCity          (userProfile.getUpCity());
		editRequest.setState         (userProfile.getUpState());
		editRequest.setZip           (userProfile.getUpZip());

		if (userProfile.getUpZip4()==null) {
			editRequest.setZip4      ("");
		} else {
			editRequest.setZip4      (userProfile.getUpZip4());
		}

		editRequest.setPhone         (userProfile.getUpPhone());

		if (userProfile.getUpFax()==null) {
			editRequest.setFax       ("");
		} else {
			editRequest.setFax       (userProfile.getUpFax());
		} 

		if (userProfile.getUpEmail()==null) {
			editRequest.setEmail     ("");
		} else {
			editRequest.setEmail     (userProfile.getUpEmail());
		}

		editRequest.setSsn           (userProfile.getUpssn());
		editRequest.setDob           (editRequest.stringify(userProfile        .getUpdob()));
		editRequest.setHomeMarket    (Short.toString(userProfile.getUpHomeMarket()));
		editRequest.setActiveYN      (userProfile.getUpActiveYn());
		editRequest.setPwdExpDate    (editRequest.stringify(userProfile        .getUpPwdexpd()));
		String dateTime[] = new String[2];

		if (userProfile.getUpDisabledDate()==null) {
			editRequest.setDisabledDate("");
			editRequest.setDisabledTime("");
		} else {
			dateTime = editRequest.stringify(userProfile      .getUpDisabledDate());
			editRequest.setDisabledDate(dateTime[0]);
			editRequest.setDisabledTime(dateTime[1]);
		}

		editRequest.setPwdAttempts   (Short.toString(userProfile.getUpPwdAttempts()));
		editRequest.setCurrentToken  (userProfile.getUptoken());
		dateTime = editRequest.stringify(userProfile        .getUpLastLoginDate());
		editRequest.setLastLogin     (dateTime[0]);
		editRequest.setLastLoginTime (dateTime[1]);
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.mapFullUserProfile() 0012 "
				+ "\n"
				+ "   editRequest=" + editRequest);
		/*debug*/
	}

    /**
	 * Merge the given Full_User_Profile entity into the persistence context.<p>
	 * 
	 * This method delegates to com.yardi.ejb.UserProfileBean.merge() to perform the merge.
	 *  
	 * @param profile the state to merge
     * @return managed instance that the given Full_User_Profile was merged to. 
	 */
	private Full_User_Profile mergeFullUserProfile(Full_User_Profile profile) {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.mergeFullUserProfile() 001E ");
		/*debug*/
		return userProfileBean.merge(profile);
	}

	/**
	 * Persist a new Full_User_Profile entity.<p>
	 * 
	 * This method delegates to com.yardi.ejb.UserProfileBean.persist() to persist the given Full_User_Profile entity.
	 * 
	 * @param profile the entity to persist
	 */
	private void persistFull_User_Profile(Full_User_Profile profile) {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.persistFull_User_Profile() 001F ");
		/*debug*/
		userProfileBean.persist(profile);
	}

	@PostConstruct
	private void postConstructCallback() {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.postConstructCallback() 0000 ");
		/*debug*/
	}
	
	/**
	 * Release resources and remove the bean.<p>
	 * 
	 * Clients use this method to remove the bean and release the resources the stateful bean holds. This method calls 
	 * the remove method of com.yardi.ejb.UserProfileBean and com.yardi.ejb.UserGroups bean.  
	 */
	@Remove
	public void removeBean() {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.removeBean() 0005 ");
		/*debug*/
		userProfileBean.removeBean();
		userGroupsBean.removeBean();
	}
    	
    /**
	 * Remove the given Full_Sessions_Table entity.<p>
	 * 
	 * This method delegates to com.yardi.ejb.SessionsTableBean.remove() to remove the given Full_Sessions_Table entity.
	 * 
	 * @param session the entity to remove
	 */
	private void removeFullSessionsTable(Full_Sessions_Table session) {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.removeFullSessionsTable() 0019 ");
		/*debug*/
		sessionsTableBean.remove(session);
	}

	/**
	 * Remove the given Unique_Tokens entity.<p>
	 * 
	 * This method delegates to com.yardi.ejb.UniqueTokensBean.remove() to remove the given Unique_Tokens entity.
	 * 
	 * @param token the Unique_Tokens entity to remove 
	 */
	private void removeToken(Unique_Tokens token) {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.removeToken() 0018 ");
		/*debug*/
		uniqueTokensBean.remove(token.getUp1Rrn());	
	}
	
	/**
	 * Remove the given User_Groups2 entity.<p>
	 * 
	 * This method delegates to com.yardi.ejb.UserGroupsBean.remove() to remove the given User_Groups2 entity.
	 * 
	 * @param userGroup the User_Groups2 entity to remove
	 */
	private void removeUserGroup2(User_Groups2 userGroup) {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.removeUserGroup2() 001A ");
		/*debug*/
		userGroupsBean.remove(userGroup);
	}
	
	/**
	 * Remove the given Full_User_Profile entity.<p>
	 * 
	 * This method delegates to com.yardi.ejb.UserProfieBean.remove() to remove the given Full_User_Profile entity.
	 * 
	 * @param profile the entity to remove
	 */
	private void removeUserProfile(Full_User_Profile profile) {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.removeUserProfile() 0024 ");
		/*debug*/
		userProfileBean.remove(profile);
	}
	
	/**
	 * Roll back the transaction
	 * @param tx jakarta.transaction.UserTransaction
	 */
	private void rollback(UserTransaction tx) {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.rollback() 000C ");
		/*debug*/
		try {
			tx.rollback();
		} catch (IllegalStateException | SecurityException | SystemException e) {
			/*debug*/
			System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.rollback() 000D ");
			/*debug*/
			e.printStackTrace();
		}
	}
	
	/**
	 * Inject the request from the web
	 * 
	 * @param editRequest POJO representation of the web request. 
	 */
	public void setEditUserProfileRequest (EditUserProfileRequest editRequest) {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.setEditUserProfileRequest() 0001 ");
		/*debug*/
		this.editRequest = editRequest;
		/*debug*/
        System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.setEditUserProfileRequest() 0002 "
            	+ "\n"
            	+ "   editRequest=" + this.editRequest);
		/*debug*/
	}
	
	/**
	 * Log the transaction status
	 */
	private void txStatus() {
		String status = null;
		
		try {
			switch(tx.getStatus()) {
			case jakarta.transaction.Status.STATUS_ACTIVE:
				status = "active";
				break;
			case jakarta.transaction.Status.STATUS_COMMITTED:
				status = "committed";
				break;
			case jakarta.transaction.Status.STATUS_COMMITTING:
				status = "committing";
				break;
			case jakarta.transaction.Status.STATUS_MARKED_ROLLBACK:
				status = "marked rollback";
				break;
			case jakarta.transaction.Status.STATUS_NO_TRANSACTION:
				status = "no transaction";
				break;
			case jakarta.transaction.Status.STATUS_PREPARED:
				status = "prepared";
				break;
			case jakarta.transaction.Status.STATUS_PREPARING:
				status = "prepairing";
				break;
			case jakarta.transaction.Status.STATUS_ROLLEDBACK:
				status = "rolled back";
				break;
			case jakarta.transaction.Status.STATUS_ROLLING_BACK:
				status = "rolling back";
				break;
			case jakarta.transaction.Status.STATUS_UNKNOWN:
				status = "unknown";
				break;
			default:
				status = "undefined";
			}
		} catch (SystemException e) {
			System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.txStatus() SystemException 0025 ");
			e.printStackTrace();
		}
		
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.txStatus() 0026 "
  				+ "\n"
  				+ "   tx status="
  				+ status
  				);
	}
	
	/**
	 * Update all columns in the USER_PROFILE database table using the fields from the web request.<p>
	 * 
	 * <strong>Feedback provided:</strong> <span style="font-family:consolas;">YRD0000</span> process completed normally.
	 */
	private void updateUserProfile() {
		/*debug*/
		System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.updateUserProfile() 001C ");
		/*debug*/
		try {
			tx.begin();
			txStatus();
			/*debug*/
			System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.updateUserProfile() 0020 ");
			/*debug*/
			/* A find occurred previously in another transaction that has committed. Need to find again here */
			Full_User_Profile userProfile = findFullUserProfile(editRequest.getFindUser());
			mergeFullUserProfile(
					new Full_User_Profile(
							editRequest.getFindUser(),
							editRequest.getCurrentToken(),
							editRequest.getUpHomeMarket(),
							editRequest.getFirstName(), 
							editRequest.getLastName(), 
							editRequest.getAddress1(), 
							editRequest.getAddress2(), 
							editRequest.getCity(), 
							editRequest.getState(), 
							editRequest.getZip(), 
							editRequest.getZip4(), 
							editRequest.getPhone(), 
							editRequest.getFax(), 
							editRequest.getEmail(), 
							editRequest.getSsn(),
							editRequest.getBirthDate(),
							editRequest.getActiveYN(),
							editRequest.getPasswordExpirationDate(),
							"",
							editRequest.getProfileDisabledDate(),
							editRequest.getLastLoginDate(),
							editRequest.getPasswordAttempts(), 
							/* do not change the sequence column from the found Full_User_Profile */
							userProfile.getUprrn()
					)
			);
			feedback = com.yardi.shared.rentSurvey.YardiConstants.YRD0000.split("="); 
			editRequest.setMsgID(feedback[0]);
			editRequest.setMsgDescription(feedback[1]);
			/*debug*/
			System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.updateUserProfile() 001D ");
			/*debug*/
			commit(tx);
			txStatus();
		} catch (NotSupportedException | SystemException e) {
			/*debug*/
			System.out.println("com.yardi.ejb.EditUserProfileCTRLBean.updateUserProfile() exception 0021 ");
			/*debug*/
			e.printStackTrace();
		}
	}
}
