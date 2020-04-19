package com.yardi.ejb;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the PP_PWD_POLICY database table.
 * 
 */
@Entity
@Table(name="PWD_POLICY", schema="DB2ADMIN")
@NamedQuery(name="Pwd_Policy.findAll", query="SELECT p FROM Pwd_Policy p")
public class Pwd_Policy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="PP_DAYS")
	private short ppDays;

	@Column(name="PP_NBR_UNIQUE")
	private short ppNbrUnique;

	@Column(name="PP_MAX_SIGNON_ATTEMPTS")
	private short ppMaxSignonAttempts;

	@Column(name="PP_PWD_MIN_LEN")
	private short ppPwdMinLen;
	
	@Transient private Boolean ppUpperRqd; 
	
	@Column(name = "PP_UPPER_RQD")
	private String pp_upper_rqd;
	
	@Transient private Boolean ppLowerRqd;
	
	@Column(name = "PP_LOWER_RQD")
	private String pp_lower_rqd;
	
	@Transient private Boolean ppNumberRqd; 

	@Column(name = "PP_NUMBER_RQD")
	private String pp_number_rqd;
	
	@Transient private Boolean ppSpecialRqd;
	
	@Column(name = "PP_SPECIAL_RQD")
	private String pp_special_rqd;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PP_RRN")
	private long ppRrn;

	public short getPpPwdMinLen() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy getPpPwdMinLen() 0000");
		//debug
		return ppPwdMinLen;
	}

	public void setPpPwdMinLen(short ppPwdMinLen) {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPpPwdMinLen() 0001");
		//debug
		this.ppPwdMinLen = ppPwdMinLen;
	}

	public Boolean getPpUpperRqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy getPpUpperRqd() 0002");
		//debug
		return ppUpperRqd;
	}

	void setPpUpperRqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPpUpperRqd() 0003");
		//debug
		ppUpperRqd = false;
		if (pp_upper_rqd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPpUpperRqd() 001F");
			//debug
			ppUpperRqd = true;
		}
		if (pp_upper_rqd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPpUpperRqd() 0020");
			//debug
			ppUpperRqd = false;
		}
	}

	public Boolean getPpLowerRqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy getPpLowerRqd() 0004");
		//debug
		return ppLowerRqd;
	}

	void setPpLowerRqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPpLowerRqd() 0005");
		//debug
		ppLowerRqd = false;
		if (pp_lower_rqd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPpLowerRqd() 0021");
			//debug
			ppLowerRqd = true;
		}
		if (pp_lower_rqd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPpLowerRqd() 0022");
			//debug
			ppLowerRqd = false;
		}
	}

	public Boolean getPpNumberRqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy getPpNumberRqd() 0006");
		//debug
		return ppNumberRqd;
	}

	void setPpNumberRqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPpNumberRqd() 0007");
		//debug
		ppNumberRqd = false;
		if (pp_number_rqd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPpNumberRqd() 0023");
			//debug
			ppNumberRqd = true;
		}
		if (pp_number_rqd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPpNumberRqd() 0024");
			//debug
			ppNumberRqd = false;
		}
	}

	public Boolean getPpSpecialRqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy getPpSpecialRqd() 0008");
		//debug
		return ppSpecialRqd;
	}

	void setPpSpecialRqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPpSpecialRqd() 0009");
		//debug
		ppSpecialRqd = false;
		if (pp_special_rqd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPpSpecialRqd() 0025");
			//debug
			ppSpecialRqd = true;
		}
		if (pp_special_rqd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPpSpecialRqd() 0026");
			//debug
			ppSpecialRqd = false;
		}
	}

	public Pwd_Policy() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy Pwd_Policy() 000A");
		//debug
	}

	public long getPpRrn() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy getPpRrn() 000B");
		//debug
		return this.ppRrn;
	}

	public void setPpRrn(long ppRrn) {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPpRrn() 000C");
		//debug
		this.ppRrn = ppRrn;
	}

	public short getPpDays() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy getPpDays() 000D");
		//debug
		return this.ppDays;
	}

	public void setPpDays(short ppDays) {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPpDays() 000E");
		//debug
		this.ppDays = ppDays;
	}

	public short getPpMaxSignonAttempts() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy getPpMaxSignonAttempts() 000F");
		//debug
		return this.ppMaxSignonAttempts;
	}

	public void setPpMaxSignonAttempts(short ppMaxSignonAttempts) {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPpMaxSignonAttempts() 0010");
		//debug
		this.ppMaxSignonAttempts = ppMaxSignonAttempts;
	}

	public short getPpNbrUnique() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy getPpNbrUnique() 0011");
		//debug
		return this.ppNbrUnique;
	}

	public void setPpNbrUnique(short ppNbrUnique) {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPpNbrUnique() 0012");
		//debug
		this.ppNbrUnique = ppNbrUnique;
	}

	@Override
	public String toString() {
		return "Pwd_Policy [ppDays=" + ppDays + ", ppNbrUnique=" + ppNbrUnique + ", ppMaxSignonAttempts=" + ppMaxSignonAttempts 
				+ ", ppPwdMinLen=" + ppPwdMinLen + ", pp_upper_rqd=" + pp_upper_rqd + ", pp_lower_rqd=" + pp_lower_rqd 
				+ ", pp_number_rqd=" + pp_number_rqd + ", pp_special_rqd=" + pp_special_rqd	+ ", ppUpperRqd=" + ppUpperRqd 
				+ ", ppLowerRqd=" + ppLowerRqd + ", ppNumberRqd=" + ppNumberRqd + ", ppSpecialRqd=" + ppSpecialRqd + ", ppRrn=" + ppRrn + "]";
	}

	public String getPp_upper_rqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy getPp_upper_rqd() 0013");
		//debug
		return pp_upper_rqd;
	}

	void setPp_upper_rqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPp_upper_rqd() 0014");
		//debug
		if (ppUpperRqd) {
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPp_upper_rqd() 0027");
			//debug
			pp_upper_rqd = "y";} 
		else {
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPp_upper_rqd() 0028");
			//debug
			pp_upper_rqd = "n";}
	}

	public void setPp_upper_rqd(String pp_upper_rqd) {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPp_upper_rqd() 0015");
		//debug
		this.pp_upper_rqd = pp_upper_rqd;
		setPpUpperRqd();
	}

	public String getPp_lower_rqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy getPp_lower_rqd() 0016");
		//debug
		return pp_lower_rqd;
	}

	public void setPp_lower_rqd(String pp_lower_rqd) {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPp_lower_rqd() 0017");
		//debug
		this.pp_lower_rqd = pp_lower_rqd;
		setPpLowerRqd();	
	}

	void setPp_lower_rqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPp_lower_rqd() 0018");
		//debug
		if (ppLowerRqd) 
			{
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPp_lower_rqd() 0029");
			//debug
			pp_lower_rqd = "y";} 
		else 
			{
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPp_lower_rqd() 002A");
			//debug
			pp_lower_rqd = "n";}
	}

	public String getPp_number_rqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy getPp_number_rqd() 0019");
		//debug
		return pp_number_rqd;
	}

	public void setPp_number_rqd(String pp_number_rqd) {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPp_number_rqd() 001A");
		//debug
		this.pp_number_rqd = pp_number_rqd;
		setPpNumberRqd();
	}

	void setPp_number_rqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPp_number_rqd() 001B");
		//debug
		if (ppNumberRqd) 
			{
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPp_number_rqd() 002B");
			//debug
			pp_number_rqd = "y";} 
		else
			{
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPp_number_rqd() 002C");
			//debug
			pp_number_rqd = "n";} 
	}

	public String getPp_special_rqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy getPp_special_rqd() 001C");
		//debug
		return pp_special_rqd;
	}

	public void setPp_special_rqd(String pp_special_rqd) {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPp_special_rqd() 001D");
		//debug
		this.pp_special_rqd = pp_special_rqd;
		setPpSpecialRqd();
	}

	void setPp_special_rqd() {
		//debug
		System.out.println("com.yardi.ejb.Pwd_Policy setPp_special_rqd() 001E");
		//debug
		if (ppSpecialRqd) 
			{
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPp_special_rqd() 002D");
			//debug
			pp_special_rqd = "y";} 
		else 
			{
			//debug
			System.out.println("com.yardi.ejb.Pwd_Policy setPp_special_rqd() 002E");
			//debug
			pp_special_rqd = "n";}
	}
}
