package com.yardi.ejb.model;
/*
 * 2020 1104
 * added attribute for Sessions_Table
 * added getter for Sessions_Table
 */
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity implementation class for Entity: User_Groups2.<br><br>
 * 
 * <pre>
 * Database table: USER_GROUPS
 * Schema: DB2ADMIN
 * </pre>
 */
@Entity
@Table(name="USER_GROUPS", schema="DB2ADMIN")
public class User_Groups2 implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Column: UG_USER_ID 
	 */
	@Column(name="UG_USER_ID")
	private String ugUserId;

	/**
	 * Column: UG_GROUP 
	 */
	@Column(name="UG_GROUP")
	private int ugGroup;

	/**
	 * Column: UG_RRN<p>
	 * 
	 * ID Column<br>
	 * Generated value strategy IDENTITY.  <span style="font-family:times;">DB2</span> generates the sequence.
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="UG_RRN")
	private long ugRrn;

	public User_Groups2() {
		/*debug*/
		System.out.println("com.yardi.ejb.model.User_Groups2() ");
		/*debug*/
	}

	/**
	 * Constructor that uses all fields.
	 * @param ugUserId user name.
	 * @param ugGroup group number.
	 */
	public User_Groups2(String ugUserId, int ugGroup) {
		this.ugUserId = ugUserId;
		this.ugGroup = ugGroup;
	}

	public int getUgGroup() {
		return this.ugGroup;
	}

	public long getUgRrn() {
		return this.ugRrn;
	}

	public String getUgUserId() {
		return this.ugUserId;
	}

	public void setUgGroup(int ugGroup) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.setUgGroup() 0000 ");
		/*debug*/
		this.ugGroup = ugGroup;
	}

	public void setUgRrn(long ugRrn) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.setUgRrn() 0001 ");
		/*debug*/
		this.ugRrn = ugRrn;
	}

	public void setUgUserId(String ugUserId) {
		/*debug*/
		System.out.println("com.yardi.ejb.model.setUgUserId() 0002 ");
		/*debug*/
		this.ugUserId = ugUserId;
	}
	
	@Override
	public String toString() {
		return "User_Groups2 [ugUserId=" + ugUserId + ", ugGroup=" + ugGroup + ", ugRrn=" + ugRrn + "]";
	}
}
