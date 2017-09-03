package com.yardi.ejb;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PP_PWD_POLICY database table.
 * 
 */
@Entity
@Table(name="PP_PWD_POLICY")
@NamedQuery(name="PpPwdPolicy.findAll", query="SELECT p FROM PpPwdPolicy p")
public class PpPwdPolicy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="PP_DAYS")
	private short ppDays;

	@Column(name="PP_NBR_UNIQUE")
	private short ppNbrUnique;

	@Column(name="PP_MAX_SIGNON_ATTEMPTS")
	private short ppMaxSignonAttempts;

	@Column(name="PP_PWD_MIN_LEN")
	private short ppPwdMinLen;
	
	@Column(name="PP_UPPER_RQD")
	private String ppUpperRqd; 
	
	@Column(name="PP_LOWER_RQD")
	private String ppLowerRqd; 
	
	@Column(name="PP_NUMBER_RQD")
	private String ppNumberRqd; 
	
	@Column(name="PP_SPECIAL_RQD")
	private String ppSpecialRqd; 

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PP_RRN")
	private long ppRrn;

	public short getPpPwdMinLen() {
		return ppPwdMinLen;
	}

	public void setPpPwdMinLen(short ppPwdMinLen) {
		this.ppPwdMinLen = ppPwdMinLen;
	}

	public String getPpUpperRqd() {
		return ppUpperRqd;
	}

	public void setPpUpperRqd(String ppUpperRqd) {
		this.ppUpperRqd = ppUpperRqd;
	}

	public String getPpLowerRqd() {
		return ppLowerRqd;
	}

	public void setPpLowerRqd(String ppLowerRqd) {
		this.ppLowerRqd = ppLowerRqd;
	}

	public String getPpNumberRqd() {
		return ppNumberRqd;
	}

	public void setPpNumberRqd(String ppNumberRqd) {
		this.ppNumberRqd = ppNumberRqd;
	}

	public String getPpSpecialRqd() {
		return ppSpecialRqd;
	}

	public void setPpSpecialRqd(String ppSpecialRqd) {
		this.ppSpecialRqd = ppSpecialRqd;
	}

	public PpPwdPolicy() {
	}

	public long getPpRrn() {
		return this.ppRrn;
	}

	public void setPpRrn(long ppRrn) {
		this.ppRrn = ppRrn;
	}

	public short getPpDays() {
		return this.ppDays;
	}

	public void setPpDays(short ppDays) {
		this.ppDays = ppDays;
	}

	public short getPpMaxSignonAttempts() {
		return this.ppMaxSignonAttempts;
	}

	public void setPpMaxSignonAttempts(short ppMaxSignonAttempts) {
		this.ppMaxSignonAttempts = ppMaxSignonAttempts;
	}

	public short getPpNbrUnique() {
		return this.ppNbrUnique;
	}

	public void setPpNbrUnique(short ppNbrUnique) {
		this.ppNbrUnique = ppNbrUnique;
	}

}