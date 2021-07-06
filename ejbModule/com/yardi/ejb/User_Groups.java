package com.yardi.ejb;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the USER_GROUPS database table.
 * 
 */
@Entity
@Table(name="USER_GROUPS", schema="DB2ADMIN")
@NamedQuery(name="User_Groups.findAll", query="SELECT u FROM User_Groups u")
public class User_Groups implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="UG_USER_ID")
	private String ugUserId;

	@Column(name="UG_GROUP")
	private int ugGroup;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="UG_RRN")
	private long ugRrn;

	@ManyToOne
	@JoinColumn(name = "UG_GROUP", referencedColumnName = "GM_TYPE", nullable=false, updatable=false, insertable=false)
	private Groups_Master ugGroupsMaster;
	
	
	public User_Groups() {
		/*debug*/
		System.out.println("com.yardi.ejb.User_Groups.User_Groups() 0000");
		/*debug*/
	}

	public long getUgRrn() {
		return this.ugRrn;
	}

	public void setUgRrn(long ugRrn) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUgRrn() 0000");
		/*debug*/
		this.ugRrn = ugRrn;
	}

	public int getUgGroup() {
		return this.ugGroup;
	}

	public void setUgGroup(int ugGroup) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUgGroup() 0000");
		/*debug*/
		this.ugGroup = ugGroup;
	}

	public String getUgUserId() {
		return this.ugUserId;
	}

	public void setUgUserId(String ugUserId) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUgUserId() 0000");
		/*debug*/
		this.ugUserId = ugUserId;
	}

	public Groups_Master getUgGroupsMaster() {
		return ugGroupsMaster;
	}

	public void setUgGroupsMaster(Groups_Master ugGroupsMaster) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUgGroupsMaster() 0000");
		/*debug*/
		this.ugGroupsMaster = ugGroupsMaster;
	}

	@Override
	public String toString() {
		return "User_Groups [ugUserId=" + ugUserId + ", ugGroup=" + ugGroup + ", ugRrn=" + ugRrn + ", ugGroupsMaster="
				+ ugGroupsMaster + "]";
	}
}