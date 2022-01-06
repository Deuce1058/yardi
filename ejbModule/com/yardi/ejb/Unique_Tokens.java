package com.yardi.ejb;

import java.io.Serializable;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


/**
 * The persistent class for the UNIQUE_TOKENS database table.
 * 
 */
@Entity
@Table(name="UNIQUE_TOKENS", schema="DB2ADMIN")
@NamedQuery(name="Unique_Tokens.findAll", query="SELECT u FROM Unique_Tokens u")
public class Unique_Tokens implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="UP1_USER_NAME")
	private String up1UserName;

	@Column(name="UP1_TOKEN")
	private String up1Token;

	@JsonFormat(
		shape = JsonFormat.Shape.STRING,
		pattern = "MM/dd/yyyy"
	)
	@Temporal(TemporalType.DATE)
	@Column(name="UP1_DATE_ADDED")
	private Date up1DateAdded;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="UP1_RRN")
	private long up1Rrn;

	public Unique_Tokens() {
		/*debug*/
		System.out.println("com.yardi.ejb.Unique_Tokens.Unique_Tokens() 0000");
		/*debug*/
	}

	public Unique_Tokens(String up1UserName, String up1Token, Date up1DateAdded) {
		/*debug*/
		System.out.println("com.yardi.ejb.Unique_Tokens.Unique_Tokens() 0001");
		/*debug*/
		this.up1UserName = up1UserName;
		this.up1Token = up1Token;
		this.up1DateAdded = up1DateAdded;
	}

	public long getUp1Rrn() {
		return this.up1Rrn;
	}

	public void setUp1Rrn(long up1Rrn) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUp1Rrn() 0000");
		/*debug*/
		this.up1Rrn = up1Rrn;
	}

	public Date getUp1DateAdded() {
		return this.up1DateAdded;
	}

	public void setUp1DateAdded(Date up1DateAdded) {
		/*debug*/
		System.out.println("com.yardi.ejb.setUp1DateAdded() 0000");
		/*debug*/
		this.up1DateAdded = up1DateAdded;
	}

	public String getUp1Token() {
		return this.up1Token;
	}

	public void setUp1Token(String up1Token) {
		/*debug*/
		System.out.println("com.yardi.setUp1Token() 0000");
		/*debug*/
		this.up1Token = up1Token;
	}

	public String getUp1UserName() {
		return this.up1UserName;
	}

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