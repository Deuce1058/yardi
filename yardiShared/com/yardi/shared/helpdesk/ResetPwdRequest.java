package com.yardi.shared.helpdesk;

import java.util.List;

/**
 * A request to reset the user's password.<p>
 * The container holds details about the status of the user's account for the help desk to review on the password reset page. 
 * This container also holds a history of the dates on which password was changed and the number of times the password was changed on that date. 
 */
public class ResetPwdRequest {
	/**
	 * Action to perform: find or resetPwd
	 */
	private String action;
    /**
     * Message ID
     */
    private String msgID;
    /**
     * Message description
     */
    private String msgDescription; 
	
	/**
	 * New password
	 */
    private String newPassword;
	/**
	 * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column: UP_USERID</span><p>
	 * User ID
	 */
    private String upUserid;
    /** 
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_ACTIVE_YN</span><p>
     * User profile active flag. Y for active. N for inactive. User cant login unless this column is Y. Only an admin can set this column to Y.
     */ 
    private String upActiveYn;
    /**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p> 
     * <span style="font-family:consolas;">Column: UP_DISABLED_DATE</span><p>
     * Date and time when the user profile became disabled due to too many invalid password attempts since the last successful login.
     */ 
    private java.sql.Timestamp upDisabledDate; 
    /**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p> 
     * <span style="font-family:consolas;">Column: UP_PWDEXPD</span><p>
     * Password expiration date. The date on which the password must be changed.
     */ 
    private java.sql.Timestamp upPwdexpd; 
    /**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_LAST_LOGIN_DATE</span><p>
     * Date and time of last successful login.
     */ 
    private java.sql.Timestamp upLastLoginDate; 
    /**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_PWD_ATTEMPTS</span><p>
     * Number of invalid password attempts since the last successful login.
    */ 
    private short upPwdAttempts; 
    
    /** 
     * History of the dates on which password was changed and the number of times the password was changed on that date 
     */ 
    private List <PwdHistory> pwdHistory;      
    
    /**
     * Default constructor
     */
    public ResetPwdRequest() {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest ResetPwdRequest() 0000");
    }

    /**
     * Return action to perform: find or resetPwd
     * @return action to perform
     */
	public String getAction() {
		return action;
	}

	/**
	 * Return message description
	 * @return message description
	 */
	public String getMsgDescription() {
		return msgDescription;
	}
    
    /**
	 * Return message ID 
	 * @return message ID
	 */
	public String getMsgID() {
		return msgID;
	}
	
	/**
	 * Return new password 
	 * @return new password
	 */
	public String getNewPassword() {
		return newPassword;
	}
	
	/**
     * Return history of the dates on which password was changed and the number of times the password was changed on that date 
     * @return history of dates on which password was changed and number of times password was changed on that date
     */
    public List<PwdHistory> getPwdHistory() {
		return pwdHistory;
	}
	
	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_ACTIVE_YN</span><p>
	 * Return user profile active flag
	 * @return user profile active flag
	 */
	public String getUpActiveYn() {
		return upActiveYn;
	}
	
	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p> 
     * <span style="font-family:consolas;">Column: UP_DISABLED_DATE</span><p>
	 * Return date and time on which the user profile was disabled due to too many failed login attempts since the last successful login 
	 * @return date and time on which the user profile was disabled
	 */
	public java.sql.Timestamp getUpDisabledDate() {
		return upDisabledDate;
	}
	
	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_LAST_LOGIN_DATE</span><p>
	 * Return date and time of the most recent successful login
	 * @return date and time of the most recent successful login
	 */
	public java.sql.Timestamp getUpLastLoginDate() {
		return upLastLoginDate;
	}
	
	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_PWD_ATTEMPTS</span><p>
	 * Return number of failed password attempts since the most recent successful login
	 * @return number of failed password attempts
	 */
	public short getUpPwdAttempts() {
		return upPwdAttempts;
	}
	
	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p> 
     * <span style="font-family:consolas;">Column: UP_PWDEXPD</span><p>
	 * Return date and time on which the password expires
	 * @return date and time on which the password expires
	 */
	public java.sql.Timestamp getUpPwdexpd() {
		return upPwdexpd;
	}
	
	/**
	 * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column: UP_USERID</span><p>
	 * Return user ID
	 * @return user ID
	 */
	public String getUpUserid() {
		return upUserid;
	}
	
	/**
	 * Set action to perform to the given String
	 * @param action to perform: find or resetPwd
	 */
	public void setAction(String action) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest setAction() 0001");
		this.action = action;
	}
	
	/**
	 * Set message description to the given String
	 * @param msgDescription message description 
	 */
	public void setMsgDescription(String msgDescription) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest setMsgDescription() 0002");
		this.msgDescription = msgDescription;
	}
	
	/**
	 * Set message ID to the given String
	 * @param msgID the message ID to set
	 */
	public void setMsgID(String msgID) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest setMsgID() 0003");
		this.msgID = msgID;
	}
	
	/**
	 * Set new password to the given String
	 * @param newPassword new password
	 */
	public void setNewPassword(String newPassword) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest setNewPassword() 0004");
		this.newPassword = newPassword;
	}
	
	/**
     * Set history of the dates on which password was changed and the number of times the password was changed on that date 
     * @param pwdHistory history of dates on which password was changed and number of times password was changed on that date
     */
	public void setPwdHistory(List<PwdHistory> pwdHistory) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest setPwdHistory() 0005");
		this.pwdHistory = pwdHistory;
	}
	
	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_ACTIVE_YN</span><p>
	 * Set user profile active flag
	 * @param upActiveYn user profile active flag 
	 */
	public void setUpActiveYn(String upActiveYn) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest setUpActiveYn() 0006");
		this.upActiveYn = upActiveYn;
	}
	
	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p> 
     * <span style="font-family:consolas;">Column: UP_DISABLED_DATE</span><p>
	 * Set date and time on which the user profile was disabled to the given Timestamp
	 * @param upDisabledDate date and time on which the user profile was disabled 
	 */
	public void setUpDisabledDate(java.sql.Timestamp upDisabledDate) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest setUpDisabledDate() 0007");
		this.upDisabledDate = upDisabledDate;
	}
	
	/**'
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_LAST_LOGIN_DATE</span><p>
	 * Set date and time of the most recent successful login to the given Timestamp
	 * @param upLastLoginDate date and time of the most recent successful login
	 */
	public void setUpLastLoginDate(java.sql.Timestamp upLastLoginDate) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest setUpLastLoginDate() 0008");
		this.upLastLoginDate = upLastLoginDate;
	}
	
	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_PWD_ATTEMPTS</span><p>
	 * Set number of failed password attempts since the most recent successful login to the given short
	 * @param upPwdAttempts number of failed password attempts
	 */
	public void setUpPwdAttempts(short upPwdAttempts) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest setUpPwdAttempts() 0009");
		this.upPwdAttempts = upPwdAttempts;
	}
	
	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p> 
     * <span style="font-family:consolas;">Column: UP_PWDEXPD</span><p>
	 * Set date and time on which the password expires to the given Timestamp 
	 * @param upPwdexpd date and time on which the password expires
	 */
	public void setUpPwdexpd(java.sql.Timestamp upPwdexpd) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest setUpPwdexpd() 000A");
		this.upPwdexpd = upPwdexpd;
	}
	
	/**
	 * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column: UP_USERID</span><p>
	 * Set user ID to the given String
	 * @param upUserid user ID
	 */
	public void setUpUserid(String upUserid) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest setUpUserid() 000B");
		this.upUserid = upUserid;
	}
}
