package com.yardi.ejb.model;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

import com.ibm.db2.cmx.annotation.Column;
import com.ibm.db2.cmx.annotation.Id;
import com.ibm.db2.cmx.annotation.Table;

import jakarta.persistence.Entity;

/**
 * Entity for checking whether the USER_PROFILE table contains the given primary key<p>
 *
 * <pre>Database table: USER_PROFILE
 * Schema: DB2ADMIN</pre>
 */
@Entity
@Table(name="USER_PROFILE", schema="DB2ADMIN")
public class User_Profile_Existence_Check implements Serializable {
	/**
	 * Serial version ID   
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Column: UP_USERID User ID primary key 
	 */
	@Id
	@Column(name="UP_USERID")
	private String upUserid;

	/**
	 * Default constructor
	 */
	public User_Profile_Existence_Check() {
	}   

	/**
	 * Constructor using all fields
	 * @param upUserid user ID
	 */
	public User_Profile_Existence_Check(String upUserid) {
		this.upUserid = upUserid;
	}   

	/**'
	 * Return the user ID
	 * @return User ID
	 */
	public String getUpUserid() {
		return upUserid;
	}

	/**
	 * Set user ID to the given String
	 * @param upUserid user ID
	 */
	public void setUpUserid(String upUserid) {
		this.upUserid = upUserid;
	}   
}
