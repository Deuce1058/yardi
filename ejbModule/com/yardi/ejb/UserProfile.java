package com.yardi.ejb;

import jakarta.ejb.Local;

import com.yardi.ejb.model.Full_User_Profile;
import com.yardi.ejb.model.User_Profile;

/**
 * Template for classes that work with User_Profie entity
 */
@Local
public interface UserProfile {
    /**
     * Authenticate the user.
     * <p>
     * <strong>Initial conditions:</strong>
     * <ul>
     *   <li>There is a reference to Password_Policy entity</li>
     *   <li>There is a reference to User_Profile entity</li>
     *   <li>The User_Profile entity is not disabled</li>
     *   <li>The User_Proile entity is active</li>
     * </ul><br>
	 *
     * If these conditions are satisfied then com.yardi.shared.userServices.PasswordAuthentication.athenticate() checks that the hash of the 
     * plain text password matches the hashed password stored in User_Profile entity.<br><br><br>
     * 
     * 
     * <strong>If the password is invalid:</strong>
     * <ul>
     *   <li>Number of password attempts is incremented</li> 
     *   <li>Set <i>upPwdAttempts</i> in User_Profile entity</li>
     *   <li>If the number of password attempts equals <i>ppMaxSignonAttempts</i> defined in Password_Policy entity then disable the User_Profile entity 
     *       by setting <i>upDisabledDate</i></li>
     * </ul>
     * <br>
     * <strong>If the password is valid:</strong>
     * <div style="display:flex; flex-direction: row">
     *   <div>
     *     <pre>    <u>If the password needs to be changed:</u></pre>
     *     <ul>
     *       <li>set <i>upPwdAttempts</i> in User_Profile entity to zero to give the user credit for successfully authenticating</li>
     *       <li>set feedback to <span style="font-family:consolas;">YRD0002</span></li>
     *       <li>return false to indicate authentication has not yet succeeded</li>
     *     </ul>
     *   </div>
     *   <div>
     *     <pre>    <u>If the user is not in the process of changing their password:</u></pre>
     *     <ul>
     *       <li>call the <i>loginSuccess()</i> method</li>
     *       <li>return the boolean value from com.yardi.shared.userServices.PasswordAuthentication.authenticate()</li>
     *     </ul>    
     *   </div>
     * </div>
     * <br><br>  
     * <strong>Feedback provided by this method:</strong> 
     * <pre>
     * YRD0000 normal completion
     * YRD0001 the injected User_Profile entity is null (indicates an invalid user name)
     * YRD0002 the password has expired
     * YRD0003 the User_Profile entity is disabled (user cant login). Password must be reset to login
     * YRD0004 the User_Profile entity is inactive. Administrator must clear the inactive flag to login
     * YRD000B password policy is missing
     * YRD000C maximum signon attempts exceeded. The User_Profile entity is disabled
     * YRD000F invalid password
     * </pre>
     * 
     * 
     * @param userName  user's name
     * @param password  password in plain text
     * @param userIsChangingPassword boolean indicating whether user is in the process of changing their password
     * @return boolean indicating whether authentication was successful 
     */
	boolean authenticate(String userName, String password, boolean userIsChangingPassword);	
	/**
	 * Change the hashed password stored in the User_Profile entity.<p>
	 * 
	 * <strong>Steps to change the user's token:</strong>
	 * <ul>
	 *   <li>Hash the new password</li>
	 *   <li>Calculate the new password expiration date based on today + the password life in days as defined by <i>ppDays</i> in the Pwd_Policy entity</li>
	 *   <li>Set the hashed new password in the User_Profile entity</li>
	 *   <li>Set the password expiration date in the User_Profile entity</li>
	 * </ul>  
	 * 
	 * @param newPassword the new plain text password 
	 */
	void changeUserToken(final char [] newPassword);
    /**
     * Return the User_Profile entity specified by <i>userName</i>.<p> 
     * 
     * Returns null if the User_Profile entity is not in the persistence context and USER_PROFILE database table has no row matching userName.<p>
     * 
	 * Clients use this method to obtain a User_Profile entity. This method is not called during login.<br><br> 
     * 
     * Note that the returned entity contains only the columns from USER_PROFILE that are needed for login.
     * 
     * @param userName the entity to find.
     * @return User_Profile entity that matches <i>userName</i>.
     */
	User_Profile find(String userName);
    /**
	 * Return the Full_User_Profile entity specified by <i>userID</i>.<p>
	 * 
	 * Returns null if the Full_User_Profile entity is not in the persistence context and the USER_PROFILE database table has no row matching userID.<br><br>
	 * 
	 * Clients use this method to obtain a Full_User_Profile entity. This method is not called during login.<br><Br> 
	 * 
	 * Note that Full_User_Profile entity contains all columns from USER_PROFILE database table.
	 * 
	 * @param userID the Full_User_Profile to find
	 * @return Full_User_Profile that matches <i>userID</i>
	 */
	Full_User_Profile findFullUserProfile(String userID);
	/**
	 * Return the status of the most recent method call that provides feedback.<p>
	 * Clients call <i>getFeedback()</i> to determine the status of the most recent method call that provides feedback.
	 * @return feedback from the most recent method call that provides feedback.
	 */
    String getFeedback();
    /**
     * Return the class's reference to the User_Profile entity stored in the <i>userProfile</i> field
     * @return reference to the User_Profile entity
     */
	User_Profile getUserProfile();
	/**
     * Update the user profile to reflect successful login.<p> 
     * 
     * Password attempts is set to zero, disabled date is set to null, last login date is today.<br><br>
     * 
     * Merges the state in the <i>userProfile</i> field into the persistence context.
     */
	void loginSuccess();
    /**
     * Merge the given Full_User_Profile state into the persistence context.
     * 
     * @param userProfile entity containing the state to be merged
     * @return the managed Full_User_Profile that the state was merged to
     */
	Full_User_Profile merge(Full_User_Profile userProfile);
	/**
     * Persist a Full_User_Profile 
     * 
     * @param userProfile the Full_User_Profile to persist.
     */
	void persist(Full_User_Profile userProfile);
	/**
	 * Remove the given Full_User_Profile entity.<p>
	 *  
	 * @param userProfile the entity to remove.
	 */
	void remove(Full_User_Profile userProfile);
	/**
	 *  Stateful session bean remove method. Called by clients to release resources used by com.yardi.ejb.UserProfileBean.
	 */
	void removeBean();
	/**
	 * Update the number of invalid password attempts since the last successful login in the User_Profile entity.<p>
	 * 
	 * The state stored in the <i>userProfile</i> field is merged into the persistence context.
	 * @param pwdAttempts the value to set
	 */
	void setUpPwdAttempts(short pwdAttempts);
	/**
	 * Inject the given User_Profile entity.<p>
	 * 
	 * During login clients inject the User_Profile entity because a reference has been obtained prior to this point and the class should use that reference
	 * 
	 * @param userProfile the User_Profile entity to inject. 
	 */
    void setUserProfile(User_Profile userProfile);
}
