package com.yardi.ejb;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * Entity implementation class for Entity: Permissions_Master.<p>
 * 
 * Database table PERMISSIONS_MASTER defines all possible permissions. Each permission has an associated description.
 * 
 * <pre><code>  Database table: PERMISSIONS_MASTER
 *  Schema:         DB2ADMIN</code></pre>
 */
@Entity
@Table(name="PERMISSIONS_MASTER", schema="DB2ADMIN")
@NamedQuery(name="Permissions_Master.findAll", query="SELECT p FROM Permissions_Master p")
public class Permissions_Master implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Column: PM_TYPE Permission type.<p>
	 * Primary key. This column can be joined to field <code>gpPermission</code> in the <code>Group_Permissions</code> entity.
	 */
	@Id
	@Column(name="PM_TYPE")
	private int pmType;

	/**
	 * Column: PM_DESCRIPTION Permission description.
	 */
	@Column(name="PM_DESCRIPTION")
	private String pmDescription;

	/**
	 * Column: PM_RRN Relative record number.<p>
	 * Primary key
	 */
	@GeneratedValue
	@Column(name="PM_RRN")
	private long pmRrn;

	public Permissions_Master() {
		/*debug*/
		System.out.println("com.yardi.ejb.Permissions_Master.Permissions_Master() 0000");
		/*debug*/
	}

	/**
	 * Return the permission type.<p>
	 * @return permission type
	 */
	public int getPmType() {
		return this.pmType;
	}

	/**
	 * Set the permission type.
	 * @param pmType the permission value to set.
	 */
	public void setPmType(int pmType) {
		/*debug*/
		System.out.println("com.yardi.ejb.setPmType() 0000");
		/*debug*/
		this.pmType = pmType;
	}

	/**
	 * Return the permission description.
	 * @return permission description
	 */
	public String getPmDescription() {
		return this.pmDescription;
	}

	/**
	 * Set permission description.
	 * @param pmDescription the description value to set
	 */
	public void setPmDescription(String pmDescription) {
		/*debug*/
		System.out.println("com.yardi.ejb.setPmDescription() 0000");
		/*debug*/
		this.pmDescription = pmDescription;
	}

	/**
	 * Return the relative record number
	 * @return relative record number
	 */
	public long getPmRrn() {
		return this.pmRrn;
	}

	/**
	 * Set relative record number.<p>
	 * @param pmRrn relative record number value to set
	 */
	public void setPmRrn(long pmRrn) {
		/*debug*/
		System.out.println("com.yardi.ejb.setPmRrn() 0000");
		/*debug*/
		this.pmRrn = pmRrn;
	}
}
