package com.yardi.ejb;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * Entity implementation class for database table YARDISEQ.<p>
 * 
 * Database table YARDISEQ stores the next sequence value for tables which have a sequence column that is based on a table generator.  
 * 
 * <pre>Database table: YARDISEQ
 *Schema:         DB2ADMIN</pre>
 */
@Entity
@Table(name="YARDISEQ", schema="DB2ADMIN")
@NamedQuery(name="Yardi_Seq.findAll", query="SELECT y FROM Yardi_Seq y")
public class Yardi_Seq implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Column: SEQNAME sequence name<p>
	 * Primary key
	 */
	@Id
	@Column(name="SEQNAME")
	private String seqName;

	/**
	 * Column: SEQVALUE sequence value
	 */
	@Column(name="SEQVALUE")
	private long seqValue;

	public Yardi_Seq() {
		/*debug*/
		System.out.println("com.yardi.ejb.Yardi_Seq.Yardi_Seq() 0000");
		/*debug*/
	}

	/**
	 * Return the sequence name value
	 * @return sequence name
	 */
	public String getSeqName() {
		return this.seqName;
	}

	/**
	 * Return sequence value
	 * @return the next sequence
	 */
	public long getSeqValue() {
		return this.seqValue;
	}

	/**
	 * Set the sequence name
	 * @param seqName sequence name to set
	 */
	public void setSeqName(String seqName) {
		/*debug*/
		System.out.println("com.yardi.ejb.setSeqName() 0000");
		/*debug*/
		this.seqName = seqName;
	}

	/**
	 * Set sequence value
	 * @param seqValue sequence value to set
	 */
	public void setSeqValue(long seqValue) {
		/*debug*/
		System.out.println("com.yardi.ejb.setSeqValue() 0000");
		/*debug*/
		this.seqValue = seqValue;
	}
}