package com.yardi.ejb;

import jakarta.ejb.Local;

import com.yardi.shared.userServices.LoginRequest;
import com.yardi.shared.userServices.LoginResponse;

/**
 * Services for authentication and user profile management<p>
 * 1. Authentication
 * 2. Change password
 * 3. Password policy
 */
@Local
public interface UserServices {
	/**
	 * Authenticate the user.<p> 
	 * 
	 * <strong>The following feedback is provided:</strong><br>
	 * <pre>
	 * YRD0000 normal completion
	 * YRD0001 Invalid user name or password
	 * YRD0002 the password has expired
	 * YRD0003 the User_Profile entity is disabled (user cant login). Password must be reset to login
	 * YRD0004 the User_Profile entity is inactive. Administrator must clear the inactive flag to login
	 * YRD000B password policy is missing
	 * YRD000C maximum signon attempts exceeded. The User_Profile entity is disabled
	 * YRD000E User belongs to multiple groups
	 * YRD000F invalid password
	 * </pre>
	 * 
	 * @return boolean indicating whether authentication was successful or unsuccessful
	 */
	boolean authenticate();
	/**
	 * Support for changing password either on demand or when the current password has expired.<p>
	 * Before the user is able to change their password, they must first authenticate using their current credentials.<p>
	 * 
	 * <strong>The following feedback is provided:</strong><br>
	 * <pre>
	 * YRD0000 normal completion
	 * YRD0001 Invalid user name or password
	 * YRD0002 the password has expired
	 * YRD0003 the User_Profile entity is disabled (user cant login). Password must be reset to login
	 * YRD0004 the User_Profile entity is inactive. Administrator must clear the inactive flag to login
	 * YRD0005 Password must be at least %n characters long
	 * YRD0006 Password must contain at least 1 upper case
	 * YRD0007 Password must contain at least 1 lower case
	 * YRD0008 Password must contain at least 1 number
	 * YRD0009 Password must contain at least 1 special character
     * YRD000A Password matches a password that was previously used
	 * YRD000B Password policy is missing
	 * YRD000C maximum signon attempts exceeded. The User_Profile entity is disabled
	 * YRD000E User belongs to multiple groups
	 * YRD000F invalid password
	 * YRD0010 New password must not contain current password
	 * YRD0011 New password must not contain user name in any case
	 * YRD0015 Password must not be longer than %n characters
	 * YRD0016 Password contains more than %n repeated characters
	 * YRD0017 Password must contain at least %n numbers
	 * YRD0018 Password must contain at least %n upper case characters
	 * YRD0019 Password must contain at least %n lower case characters
	 * YRD001A Password must contain at least %n special characters
	 * </pre>
	 * 
	 * @return boolean indicating whether the change password process was successful 
	 */
	boolean chgPwd();
	/**
	 * Returns the status of the most recent method call that provides feedback.<p>
	 * Clients call <i>getFeedback()</i> to determine the status of the most recent method call that provides feedback.
	 * @return feedback from the most recent method call that provides feedback.
	 */
	String getFeedback();
    /**
     * Returns the URL of the page for the group that the user belongs to.<p>
     * @return the user's initial page.
     */
	String getInitialPage();
	/**
	 * Returns the POJO representation of the web request to login.<p> The login request contains the user's credentials, message ID, message description, 
	 * string indicating whether user is changing their password, boolean indicating whether user is changing their password and the session ID which is 
	 * equivalent to HttpServletRequest.getSession().getId().
	 * @return POJO representation of the web request to login.
	 */
	LoginRequest getLoginRequest();
	/**
	 * Returns the POJO representation of the response to the web request to login.<p>
	 * The login response contains the user's credentials, message ID and message description. Field <i>loginResponse</i> is converted to JSON when
	 * responding to the web request.
	 * @return POJO representation of the response to the web request to login.
	 */
	LoginResponse getLoginResponse();
	/**
	 * Returns the session ID which is equivalent to HttpServletRequest.getSession().getId().
	 * @return the value of field <i>sessionID</i>
	 */
	String getSessionID();
	/**
	 * Stateful session bean remove method.
	 */
	void remove();
	/** 
	 * Inject the request from the web to login.
	 * @param loginRequest POJO representation of the web request to login
	 */
	void setLoginRequest(LoginRequest loginRequest);
	/** 
	 * Set field <i>sessionID</i>.<p>
	 * 
	 * @param sessionID the value to set. Equivalent to HttpServletRequest.getSession().getId().
	 */
	void setSessionID(String sessionID);
}