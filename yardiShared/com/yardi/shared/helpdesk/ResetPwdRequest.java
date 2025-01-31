package com.yardi.shared.helpdesk;

import java.sql.Timestamp;
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
     * Message description
     */
    private String msgDescription;
    /**
     * Message ID
     */
    private String msgID; 	
	/**
	 * New password
	 */
    private String newPassword;
	/** 
     * History of the dates on which password was changed and the number of times the password was changed on that date 
     */ 
    private List <PwdHistory> pwdHistory;
    /** 
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_ACTIVE_YN</span><p>
     * User profile active flag. Y for active. N for inactive. User cant login unless this column is Y. Only an admin can set this column to Y.
     */ 
    private String upActiveYn;
    /**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_DISABLED_DATE</span><p>
     *  Date and time when the user profile became disabled due to too many invalid password attempts since the last successful login.
     */
    private Timestamp upDisabledDate;
    /**
     * A String containing the formatted disabled date for display purposes
     */
    private String upDisabledDateString;
    /**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_LAST_LOGIN_DATE</span><p>
     * Date and time of last successful login.
     */
    private Timestamp upLastLoginDate; 
    /**
     * A String containing the formatted last login date for display purposes
     */
    private String upLastLoginDateString; 
    /**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_PWD_ATTEMPTS</span><p>
     * Number of invalid password attempts since the last successful login.
    */ 
    private short upPwdAttempts;
    /**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p> 
     * <span style="font-family:consolas;">Column: UP_PWDEXPD</span><p>
     * Password expiration date. The date on which the password must be changed.
     */
    private Timestamp upPwdexpd;
    /**
     * A string containing the formatted password expiration date for display purposes
     */
    private String upPwdexpdString;    
    /**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_TEMP_PWD</span><p>
     * Hashed temporary password assigned by help desk
     */
    private String upTempPwd;
    /**
	 * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column: UP_USERID</span><p>
	 * User ID
	 */
    private String upUserid;

    /**
     * Default constructor
     */
    public ResetPwdRequest() {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.ResetPwdRequest() 0000");
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
	 * Return disabled timestamp
	 * @return disabled timestamp
	 */
	public Timestamp getUpDisabledDate() {
		return upDisabledDate;
	}


	/**
     * Return the formatted disabled date for display purposes
     * @return formatted disabled date for display purposes
     */
	public String getUpDisabledDateString() {
		return upDisabledDateString;
	}
    
    /**
	 * Return last login timestamp
	 * @return last login timestamp
	 */
	public Timestamp getUpLastLoginDate() {
		return upLastLoginDate;
	}
	
	
	/**
	 * Get formatted last login date for display purposes
	 * @return formatted last login date for display purposes
	 */
	public String getUpLastLoginDateString() {
		return upLastLoginDateString;
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
	 * Return password expiration timestamp
	 * @return password expiration timestamp
	 */
	public Timestamp getUpPwdexpd() {
		return upPwdexpd;
	}
	
	/**
	 * Return the formatted password expiration date for display purposes
	 * @return formatted password expiration date for display purposes
	 */
	public String getUpPwdexpdString() {
		return upPwdexpdString;
	}
	
	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_TEMP_PWD</span><p>
     * Return hashed temporary password assigned by help desk
     * @return hashed temporary password assigned by help desk
     */
    public String getUpTempPwd() {
		return upTempPwd;
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
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setAction() 0001");
		this.action = action;
	}
	
	/**
	 * Set message description to the given String
	 * @param msgDescription message description 
	 */
	public void setMsgDescription(String msgDescription) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setMsgDescription() 0002");
		this.msgDescription = msgDescription;
	}
	
	/**
	 * Set message ID to the given String
	 * @param msgID the message ID to set
	 */
	public void setMsgID(String msgID) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setMsgID() 0003");
		this.msgID = msgID;
	}
	
	/**
	 * Set new password to the given String
	 * @param newPassword new password
	 */
	public void setNewPassword(String newPassword) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setNewPassword() 0004");
		this.newPassword = newPassword;
	}

	/**
     * Set history of the dates on which password was changed and the number of times the password was changed on that date 
     * @param pwdHistory history of dates on which password was changed and number of times password was changed on that date
     */
	public void setPwdHistory(List<PwdHistory> pwdHistory) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setPwdHistory() 0005");
		this.pwdHistory = pwdHistory;
	}
	
	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_ACTIVE_YN</span><p>
	 * Set user profile active flag
	 * @param upActiveYn user profile active flag 
	 */
	public void setUpActiveYn(String upActiveYn) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setUpActiveYn() 0006");
		this.upActiveYn = upActiveYn;
	}
	
	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p> 
     * <span style="font-family:consolas;">Column: UP_DISABLED_DATE</span><p>
	 * Set date and time on which the user profile was disabled to the given Timestamp
	 * @param upDisabledDate date and time on which the user profile was disabled 
	 */
	public void setUpDisabledDate(java.sql.Timestamp upDisabledDate) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setUpDisabledDate() 0007");
		this.upDisabledDate = upDisabledDate;
	}
	
	/**
	 * Set formatted disabled date for display purposes to the given String 
	 * @param upDisabledDateString disabled date for display purposes
	 */
	public void setUpDisabledDateString(String upDisabledDateString) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setUpDisabledDateString() 000D ");
		this.upDisabledDateString = upDisabledDateString;
	}
	
	/**'
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_LAST_LOGIN_DATE</span><p>
	 * Set date and time of the most recent successful login to the given Timestamp
	 * @param upLastLoginDate date and time of the most recent successful login
	 */
	public void setUpLastLoginDate(java.sql.Timestamp upLastLoginDate) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setUpLastLoginDate() 0008");
		this.upLastLoginDate = upLastLoginDate;
	}
	
	/**
	 * Set formatted last login date for display purposes to the given String 
	 * @param upLastLoginDateString last login date for display purposes
	 */
	public void setUpLastLoginDateString(String upLastLoginDateString) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setUpLastLoginDateString() 000E ");
		this.upLastLoginDateString = upLastLoginDateString;
	}

	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_PWD_ATTEMPTS</span><p>
	 * Set number of failed password attempts since the most recent successful login to the given short
	 * @param upPwdAttempts number of failed password attempts
	 */
	public void setUpPwdAttempts(short upPwdAttempts) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setUpPwdAttempts() 0009");
		this.upPwdAttempts = upPwdAttempts;
	}

	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p> 
     * <span style="font-family:consolas;">Column: UP_PWDEXPD</span><p>
	 * Set date and time on which the password expires to the given Timestamp 
	 * @param upPwdexpd date and time on which the password expires
	 */
	public void setUpPwdexpd(java.sql.Timestamp upPwdexpd) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setUpPwdexpd() 000A");
		this.upPwdexpd = upPwdexpd;
	}

	/**
	 * Set formatted password expiration date for display purposes to the given String
	 * @param upPwdexpdString password expiration date for display purposes
	 */
	public void setUpPwdexpdString(String upPwdexpdString) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setUpPwdexpdString() 000F ");
		this.upPwdexpdString = upPwdexpdString;
	}

	/**
     * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
     * <span style="font-family:consolas;">Column: UP_TEMP_PWD</span><p>
     * Set temporary password to the given String
     * @param upTempPwd temporary password
     */
	public void setUpTempPwd(String upTempPwd) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setUpTempPwd() 0010 ");
		this.upTempPwd = upTempPwd;
	}
	
	/**
	 * <span style="font-family:consolas;">Table:  USER_PROFILE</span><p>
	 * <span style="font-family:consolas;">Column: UP_USERID</span><p>
	 * Set user ID to the given String
	 * @param upUserid user ID
	 */
	public void setUpUserid(String upUserid) {
    	System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.setUpUserid() 000B");
		this.upUserid = upUserid;
	}
	
	@Override
	public String toString() {
		System.out.println("com.yardi.shared.helpdesk.ResetPwdRequest.toString() 000C ");
		return "ResetPwdRequest [action=" + action + ", msgDescription=" + msgDescription + ", msgID=" + msgID
				+ ", newPassword=" + newPassword + ", pwdHistory=" + pwdHistory + ", upActiveYn=" + upActiveYn
				+ ", upDisabledDateString=" + upDisabledDateString + ", upLastLoginDateString=" + upLastLoginDateString
				+ ", upPwdexpdString=" + upPwdexpdString + ", upDisabledDate=" + upDisabledDate + ", upLastLoginDate="
				+ upLastLoginDate + ", upPwdexpd=" + upPwdexpd + ", upPwdAttempts=" + upPwdAttempts + ", upTempPwd="
				+ upTempPwd + ", upUserid=" + upUserid + "]";
	}
}
