package com.yardi.ejb;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the YARDISEQ database table.
 * 
 */
@Entity
@Table(name="YARDISEQ", schema="DB2ADMIN")
@NamedQuery(name="Yardi_Seq.findAll", query="SELECT y FROM Yardi_Seq y")
public class Yardi_Seq implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SEQNAME")
	private String seqName;

	@Column(name="SEQVALUE")
	private long seqValue;

	public Yardi_Seq() {
	}

	public String getSeqName() {
		return this.seqName;
	}

	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	public long getSeqValue() {
		return this.seqValue;
	}

	public void setSeqValue(long seqValue) {
		this.seqValue = seqValue;
	}
}