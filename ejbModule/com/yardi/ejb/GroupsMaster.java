package com.yardi.ejb;

import java.io.Serializable;
import java.util.Vector;

import javax.persistence.*;


/**
 * The persistent class for the GROUPS_MASTER database table.
 * 
 * Defining a relation between GroupsMaster and UserGroups see Example of a ManyToOne relationship annotations
 * https://en.wikibooks.org/wiki/Java_Persistence/ManyToOne#Example_of_a_ManyToOne_relationship_database
 * 
 * Syntax for JPQL join 
 * https://stackoverflow.com/questions/17698229/jpql-left-join-how-to-set-up-entity-relationship-in-entity-classes?rq=1
 * 
 * Inner join
 * http://www.thejavageek.com/2014/03/24/jpa-inner-joins/
 */
@Entity
@Table(name="GROUPS_MASTER")
@NamedQuery(name="GroupsMaster.findAll", query="SELECT g FROM GroupsMaster g")
public class GroupsMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="GM_TYPE")
	private int gmType;

	@Column(name="GM_DESCRIPTION")
	private String gmDescription;

	@Column(name="GM_INITIAL_PAGE")
	private String gmInitialPage;

	@GeneratedValue
	@Column(name="GM_RRN")
	private long gmRrn;

	@OneToMany(mappedBy = "masterGroup")
	private Vector<UserGroups> userGroups;
	
	
	
	
	public GroupsMaster() {
	}

	public int getGmType() {
		return this.gmType;
	}

	public void setGmType(int gmType) {
		this.gmType = gmType;
	}

	public String getGmDescription() {
		return this.gmDescription;
	}

	public void setGmDescription(String gmDescription) {
		this.gmDescription = gmDescription;
	}

	public String getGmInitialPage() {
		return this.gmInitialPage;
	}

	public void setGmInitialPage(String gmInitialPage) {
		this.gmInitialPage = gmInitialPage;
	}

	public long getGmRrn() {
		return this.gmRrn;
	}

	public void setGmRrn(long gmRrn) {
		this.gmRrn = gmRrn;
	}

}