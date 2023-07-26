package com.yardi.ejb;

import com.yardi.shared.QSECOFR.EditUserProfileRequest;

import jakarta.ejb.Local;

@Local
public interface EditUserProfileCTRL {
	/**
	 * Return the changed EditUserProfileRequest 
	 * 
	 * @return the current com.yardi.shared.QSECOFR.EditUserProfileRequest
	 */
	EditUserProfileRequest getEditUserProfileRequest();
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
	String[] getFeedback();
	/**
	 * An abstraction to handle the request from the web.<p>
	 * 
	 * The client only needs to call this method after injecting the web request and initializing the web request. 
	 * Based on the request, this method delegates to the appropriate method to complete the rest of the process.  
	 */
	void handleRequest();
	/**
	 * Directs the web request to initialize itself.<p>
	 * 
	 * There are two kinds of initialization, special and normal. Special initialization is performed for a find, delete or read request. 
	 * Normal initialization is performed for every request.
	 */
	void inzEditRequest();
	/**
	 * Release resources and remove the bean.<p>
	 * 
	 * Clients use this method to remove the bean and release the resources the stateful bean holds. This method calls 
	 * the remove method of com.yardi.ejb.UserProfileBean and com.yardi.ejb.UserGroups bean.  
	 */
	void removeBean();
	/**
	 * Inject the request from the web
	 * 
	 * @param editRequest POJO representation of the web request. 
	 */
	void setEditUserProfileRequest (EditUserProfileRequest editRequest);
}
