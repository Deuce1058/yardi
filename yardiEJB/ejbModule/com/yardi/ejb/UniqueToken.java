package com.yardi.ejb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the UNIQUE_TOKENS database table.
 * 
 */
//@NamedQuery(name="UniqueToken.findAll", query="SELECT u FROM UniqueToken u")
@Entity
@Table(name="UNIQUE_TOKENS")
@NamedQuery(name="UniqueToken.findAll", query="SELECT u FROM UniqueToken u")
public class UniqueToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="UP1_USER_NAME")
	private String up1UserName;

	@Column(name="UP1_TOKEN")
	private String up1Token;

	@Temporal(TemporalType.DATE)
	@Column(name="UP1_DATE_ADDED")
	private Date up1DateAdded;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="UP1_RRN")
	private long up1Rrn;

	public UniqueToken() {
	}

	public UniqueToken(String up1UserName, String up1Token, Date up1DateAdded) {
		this.up1UserName = up1UserName;
		this.up1Token = up1Token;
		this.up1DateAdded = up1DateAdded;
	}

	public long getUp1Rrn() {
		return this.up1Rrn;
	}

	public void setUp1Rrn(long up1Rrn) {
		this.up1Rrn = up1Rrn;
	}

	public Date getUp1DateAdded() {
		return this.up1DateAdded;
	}

	public void setUp1DateAdded(Date up1DateAdded) {
		this.up1DateAdded = up1DateAdded;
	}

	public String getUp1Token() {
		return this.up1Token;
	}

	public void setUp1Token(String up1Token) {
		this.up1Token = up1Token;
	}

	public String getUp1UserName() {
		return this.up1UserName;
	}

	public void setUp1UserName(String up1UserName) {
		this.up1UserName = up1UserName;
	}

	@Override
	public String toString() {
		return "UniqueToken [up1UserName=" + up1UserName + ", up1Token="
				+ up1Token + ", up1DateAdded=" + up1DateAdded + ", up1Rrn="
				+ up1Rrn + "]";
	}
}