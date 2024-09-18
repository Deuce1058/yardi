package com.yardi.ejb;

import java.io.Serializable;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


/**
 * Entity implementation class for database table UNIQUE_TOKENS<p>
 * 
 * Database table UNIQUE_TOKENS is used to prevent password reuse. When a user changes their password, the current password is saved 
 * in UNIQUE_TOKENS database table before the password is changed. The number of saved passwords is defined in password policy.
 * 
 * <pre><code>  Database table: UNIQUE_TOKENS
 *  Schema:         DB2ADMIN</code></pre>
 */
@Entity
@Table(name="UNIQUE_TOKENS", schema="DB2ADMIN")
@NamedQuery(name="Unique_Tokens.findAll", query="SELECT u FROM Unique_Tokens u")
public class Unique_Tokens implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Column: UP1_USER_NAME user name.
	 */
	@Column(name="UP1_USER_NAME")
	private String up1UserName;

	/**
	 * Column: UP1_TOKEN a hashed password
	 */
	@Column(name="UP1_TOKEN")
	private String up1Token;
	
	/**
	 * Column: UP1_DATE_ADDED date password was saved
	 */
	@JsonFormat(
		shape = JsonFormat.Shape.STRING,
		pattern = "MM/dd/yyyy"
	)
	@Temporal(TemporalType.DATE)
	@Column(name="UP1_DATE_ADDED")
	private Date up1DateAdded;

	/**
	 * Column: UP1_RRN relative record number<p>
	 * Primary key
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="UP1_RRN")
	private long up1Rrn;

	public Unique_Tokens() {
		/*debug*/
		System.out.println("com.yardi.ejb.Unique_Tokens.Unique_Tokens() 0000");
		/*debug*/
	}

	/**
	 * Construct a Unique_Tokens entity using all fields except relative record number.<p>
	 * 
	 * @param up1UserName user name
	 * @param up1Token a hashed password
	 * @param up1DateAdded date password was saved
	 */
	public Unique_Tokens(String up1UserName, String up1Token, Date up1DateAdded) {
		/*debug*/
		System.out.println("com.yardi.ejb.Unique_Tokens.Unique_Tokens() 0001");
		/*debug*/
		this.up1UserName = up1UserName;
		this.up1Token = up1Token;
		this.up1DateAdded = up1DateAdded;
	}

	public Date getUp1DateAdded() {
		return this.up1DateAdded;
	}

	/**
	 * Return the relative record number
	 * @return relative record number
	 */
	public long getUp1Rrn() {
		return this.up1Rrn;
	}

	/**
	 * Return the hashed password
	 * @return the hashed password
	 */
	public String getUp1Token() {
		return this.up1Token;
	}

	/**
	 * Return the user name
	 * @return user name
	 */
	public String getUp1UserName() {
		return this.up1UserName;
	}

	/**
	 * Set date password was saved
	 * @param up1DateAdded password saved date value to set
	 */
	public void setUp1DateAdded(Date up1DateAdded) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUp1DateAdded() 0000");
		/*debug*/
		this.up1DateAdded = up1DateAdded;
	}

	/**
	 * Set relative record number
	 * @param up1Rrn relative record number value to set
	 */
	public void setUp1Rrn(long up1Rrn) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUp1Rrn() 0000");
		/*debug*/
		this.up1Rrn = up1Rrn;
	}

	/**
	 * Set the hashed password
	 * @param up1Token hashed password value to set
	 */
	public void setUp1Token(String up1Token) {
		/*debug*/
		System.out.println("com.yardi.setUp1Token() 0000");
		/*debug*/
		this.up1Token = up1Token;
	}

	/**
	 * Set user name
	 * @param up1UserName user name value to set
	 */
	public void setUp1UserName(String up1UserName) {
		/*debug*/
		System.out.println("com.yardi.setUp1UserName() 0000");
		/*debug*/
		this.up1UserName = up1UserName;
	}

	@Override
	public String toString() {
		return "Unique_Tokens [up1UserName=" + up1UserName + ", up1Token="
				+ up1Token + ", up1DateAdded=" + up1DateAdded + ", up1Rrn="
				+ up1Rrn + "]";
	}
}