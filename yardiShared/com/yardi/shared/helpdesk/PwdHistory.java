package com.yardi.shared.helpdesk;


/**
 * History of the dates on which password was changed and the number of times the password was changed on that date.<p> 
 * JPQL Query uses this class to store a List of each date on which the user changed their password and the count of how many times the password changed on a given date. 
 * This information appears on the reset password page used by the help desk.   
 */
public class PwdHistory {
	
	/** 
	 * <span style="font-family:consolas;">Table:  UNIQUE_TOKENS</span><p>
	 * <span style="font-family:consolas;">Column: UP1_DATE_ADDED</span><p>
	 * Date token was added
	 */ 
	private java.sql.Date dateAdded;
	/**
	 * Number of tokens added on a given date 
	 */
	private long dateCount;  

	public PwdHistory() {
		System.out.println("com.yardi.shared.helpdesk.PwdHistory.PwdHistory() 0000");
	}
	
	/**
	 * Constructor using all fields
	 * @param dateAdded date token was added
	 * @param dateCount number of tokens added on a given date
	 */
	public PwdHistory(java.sql.Date dateAdded, long dateCount) {
		System.out.println("com.yardi.shared.helpdesk.PwdHistory.PwdHistory(Date, long) 0001");
		this.dateAdded = dateAdded; 
		this.dateCount = dateCount; 
	}

	/**
	 * Return date token was added
	 * @return date token was added
	 */
	public java.sql.Date getDateAdded() {
		return dateAdded;
	}

	/**
	 * Return number of times password was changed on a given date
	 * @return number of times password was changed on a given date
	 */
	public long getDateCount() {
		return dateCount;
	}

	/**
	 * Set date token was added to the given java.sql.Date
	 * @param dateAdded date token was added
	 */
	public void setDateAdded(java.sql.Date dateAdded) {
		System.out.println("com.yardi.shared.helpdesk.PwdHistory.setDateAdded() 0002");
		this.dateAdded = dateAdded;
	}

	/**
	 * Set number of times password was changed on a given date to the given long
	 * @param dateCount number of times password was changed on a given date
	 */
	public void setDateCount(long dateCount) {
		System.out.println("com.yardi.shared.helpdesk.PwdHistory.setDateCount() 0003");
		this.dateCount = dateCount;
	}	
}
