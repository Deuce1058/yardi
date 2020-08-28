package com.yardi.ejb.model;

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

	@Column(name="PP_MAX_PWD_LEN")
	private Short ppMaxPwdLen;
	
	@Column(name="PP_MAX_REPEAT_CHAR")
	private Short ppMaxRepeatChar;

	@Column(name="PP_NBR_DIGITS")
	private Short ppNbrDigits;
	
	@Column(name="PP_NBR_UPPER")
	private Short ppNbrUpper;
	
	@Column(name="PP_NBR_LOWER")
	private Short ppNbrLower;
	
	@Column(name="PP_NBR_SPECIAL")
	private Short ppNbrSpecial;
	
	@Column(name="PP_CANT_CONTAIN_ID")
	private String pp_cant_contain_id;
	
	@Transient private boolean ppCantContainId; 

	@Column(name="PP_CANT_CONTAIN_PWD")
	private String pp_cant_contain_pwd;
	
	@Transient private boolean ppCantContainPwd;
		
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PP_RRN")
	private Long ppRrn;

	public Pwd_Policy() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy Pwd_Policy() 000A ");
		//debug
	}

	public Pwd_Policy(short ppDays, short ppNbrUnique, short ppMaxSignonAttempts, short ppPwdMinLen, 
			String pp_upper_rqd, String pp_lower_rqd, String pp_number_rqd,
			String pp_special_rqd, short ppMaxPwdLen, short ppMaxRepeatChar, short ppNbrDigits,
			short ppNbrUpper, short ppNbrLower, short ppNbrSpecial, String pp_cant_contain_id, String pp_cant_contain_pwd
			) {
		this.ppDays = ppDays;
		this.ppNbrUnique = ppNbrUnique;
		this.ppMaxSignonAttempts = ppMaxSignonAttempts;
		this.ppPwdMinLen = ppPwdMinLen;
		setPp_upper_rqd(pp_upper_rqd);
		setPp_lower_rqd(pp_lower_rqd);
		setPp_number_rqd(pp_number_rqd);
		setPp_special_rqd(pp_special_rqd);
		this.ppMaxPwdLen = ppMaxPwdLen;
		this.ppMaxRepeatChar = ppMaxRepeatChar;
		this.ppNbrDigits = ppNbrDigits;
		this.ppNbrUpper = ppNbrUpper;
		this.ppNbrLower = ppNbrLower;
		this.ppNbrSpecial = ppNbrSpecial;
		setPp_cant_contain_id(pp_cant_contain_id);
		setPp_cant_contain_pwd(pp_cant_contain_pwd);
	}
	
	public String getPp_cant_contain_id() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPp_cant_contain_id() 002F ");
		//debug
		return pp_cant_contain_id;
	}

	public String getPp_cant_contain_pwd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPp_cant_contain_pwd() 0037 ");
		//debug
		return pp_cant_contain_pwd;
	}

	public String getPp_lower_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPp_lower_rqd() 0016 ");
		//debug
		return pp_lower_rqd;
	}

	public String getPp_number_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPp_number_rqd() 0019 ");
		//debug
		return pp_number_rqd;
	}

	public String getPp_special_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPp_special_rqd() 001C ");
		//debug
		return pp_special_rqd;
	}

	public String getPp_upper_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPp_upper_rqd() 0013 ");
		//debug
		return pp_upper_rqd;
	}

	public short getPpDays() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpDays() 000D ");
		//debug
		return this.ppDays;
	}

	public Boolean getPpLowerRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpLowerRqd() 0004 ");
		//debug
		return ppLowerRqd;
	}

	public Short getPpMaxPwdLen() {
		return ppMaxPwdLen;
	}

	public Short getPpMaxRepeatChar() {
		return ppMaxRepeatChar;
	}

	public short getPpMaxSignonAttempts() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpMaxSignonAttempts() 000F ");
		//debug
		return this.ppMaxSignonAttempts;
	}

	public Short getPpNbrDigits() {
		return ppNbrDigits;
	}

	public Short getPpNbrLower() {
		return ppNbrLower;
	}

	public Short getPpNbrSpecial() {
		return ppNbrSpecial;
	}

	public short getPpNbrUnique() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpNbrUnique() 0011 ");
		//debug
		return this.ppNbrUnique;
	}

	public Short getPpNbrUpper() {
		return ppNbrUpper;
	}

	public Boolean getPpNumberRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpNumberRqd() 0006 ");
		//debug
		return ppNumberRqd;
	}

	public short getPpPwdMinLen() {
		//debug 
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpPwdMinLen() 0000 ");
		//debug
		return ppPwdMinLen;
	}

	public long getPpRrn() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpRrn() 000B ");
		//debug
		return this.ppRrn;
	}

	public Boolean getPpSpecialRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpSpecialRqd() 0008 ");
		//debug
		return ppSpecialRqd;
	}

	public Boolean getPpUpperRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy getPpUpperRqd() 0002 ");
		//debug
		return ppUpperRqd;
	}

	void setPp_cant_contain_id() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_id() 0030 ");
		//debug
		if (ppCantContainId) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_id() 0031 ");
			//debug
			pp_cant_contain_id = "y";
		} else {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_id() 0032 ");
			//debug
			pp_cant_contain_id = "n";
		}
	}

	public void setPp_cant_contain_id(String pp_cant_contain_id) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_id() 0033 ");
		//debug
		this.pp_cant_contain_id = pp_cant_contain_id;
		setPpCantContainId();	
	}

	void setPp_cant_contain_pwd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_pwd() 0038 ");
		//debug
		if (ppCantContainPwd) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_pwd() 0039 ");
			//debug
			pp_cant_contain_pwd = "y";
		} else {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_pwd() 003A ");
			//debug
			pp_cant_contain_pwd = "n";
		}
	}

	public void setPp_cant_contain_pwd(String pp_cant_contain_pwd) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_cant_contain_pwd() 003B ");
		//debug
		this.pp_cant_contain_pwd = pp_cant_contain_pwd;
		setPpCantContainPwd();	
	}

	void setPp_lower_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_lower_rqd() 0018 ");
		//debug
		if (ppLowerRqd) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_lower_rqd() 0029 ");
			//debug
			pp_lower_rqd = "y";
		} else {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_lower_rqd() 002A ");
			//debug
			pp_lower_rqd = "n";
		}
	}

	public void setPp_lower_rqd(String pp_lower_rqd) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_lower_rqd() 0017 ");
		//debug
		this.pp_lower_rqd = pp_lower_rqd;
		setPpLowerRqd();	
	}

	void setPp_number_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_number_rqd() 001B ");
		//debug
		if (ppNumberRqd) 
			{
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_number_rqd() 002B ");
			//debug
			pp_number_rqd = "y";} 
		else
			{
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_number_rqd() 002C ");
			//debug
			pp_number_rqd = "n";} 
	}

	public void setPp_number_rqd(String pp_number_rqd) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_number_rqd() 001A ");
		//debug
		this.pp_number_rqd = pp_number_rqd;
		setPpNumberRqd();
	}

	void setPp_special_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_special_rqd() 001E ");
		//debug
		if (ppSpecialRqd) 
			{
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_special_rqd() 002D ");
			//debug
			pp_special_rqd = "y";} 
		else 
			{
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_special_rqd() 002E ");
			//debug
			pp_special_rqd = "n";}
	}

	public void setPp_special_rqd(String pp_special_rqd) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_special_rqd() 001D ");
		//debug
		this.pp_special_rqd = pp_special_rqd;
		setPpSpecialRqd();
	}

	void setPp_upper_rqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_upper_rqd() 0014 ");
		//debug
		if (ppUpperRqd) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_upper_rqd() 0027 ");
			//debug
			pp_upper_rqd = "y";} 
		else {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_upper_rqd() 0028 ");
			//debug
			pp_upper_rqd = "n";}
	}

	public void setPp_upper_rqd(String pp_upper_rqd) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPp_upper_rqd() 0015 ");
		//debug
		this.pp_upper_rqd = pp_upper_rqd;
		setPpUpperRqd();
	}
	
	void setPpCantContainId() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpCantContainId() 0034 ");
		//debug
		ppCantContainId = false;
		if (pp_cant_contain_id.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpCantContainId() 0035 ");
			//debug
			ppCantContainId = true;
		}
		if (pp_cant_contain_id.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpCantContainId() 0036 ");
			//debug
			ppCantContainId = false;
		}
	}
	
	void setPpCantContainPwd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpCantContainPwd() 003C  ");
		//debug
		ppCantContainPwd = false;
		if (pp_cant_contain_pwd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpCantContainPwd() 003D ");
			//debug
			ppCantContainPwd = true;
		}
		if (pp_cant_contain_pwd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpCantContainPwd() 003E ");
			//debug
			ppCantContainPwd = false;
		}
	}

	public void setPpDays(short ppDays) {
		//debug 
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpDays() 000E ");
		//debug
		this.ppDays = ppDays;
	}

	void setPpLowerRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpLowerRqd() 0005 ");
		//debug
		ppLowerRqd = false;
		if (pp_lower_rqd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpLowerRqd() 0021 ");
			//debug
			ppLowerRqd = true;
		}
		if (pp_lower_rqd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpLowerRqd() 0022 ");
			//debug
			ppLowerRqd = false;
		}
	}

	public void setPpMaxPwdLen(short ppMaxPwdLen) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpMaxPwdLen() 0040 ");
		//debug
		this.ppMaxPwdLen = ppMaxPwdLen;
	}
	
	public void setPpMaxPwdLenNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpMaxPwdLenNull() 0041 ");
		//debug
		ppMaxPwdLen = null;
	}

	public void setPpMaxRepeatChar(Short ppMaxRepeatChar) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpMaxRepeatChar() 0042 ");
		//debug
		this.ppMaxRepeatChar = ppMaxRepeatChar;
	}

	public void setPpMaxRepeatCharNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpMaxRepeatCharNull() 0043 ");
		//debug
		ppMaxRepeatChar = null;
	}

	public void setPpMaxSignonAttempts(short ppMaxSignonAttempts) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpMaxSignonAttempts() 0010 ");
		//debug
		this.ppMaxSignonAttempts = ppMaxSignonAttempts;
	}

	public void setPpNbrDigits(short ppNbrDigits) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrDigits() 0044 ");
		//debug
		this.ppNbrDigits = ppNbrDigits;
	}
	
	public void setPpNbrDigitsNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrDigitsNull() 0045 ");
		//debug
		ppNbrDigits = null;
	}
	
	public void setPpNbrLower(Short ppNbrLower) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrLower() 0048 ");
		//debug
		this.ppNbrLower = ppNbrLower;
	}

	public void setPpNbrLowerNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrLowerNull() 0049 ");
		//debug
		ppNbrLower = null;
	}

	public void setPpNbrSpecial(Short ppNbrSpecial) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrSpecial() 004A ");
		//debug
		this.ppNbrSpecial = ppNbrSpecial;
	}

	public void setPpNbrSpecialNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrSpecialNull() 004B ");
		//debug
		ppNbrSpecial = null;
	}

	public void setPpNbrUnique(short ppNbrUnique) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrUnique() 0012 ");
		//debug
		this.ppNbrUnique = ppNbrUnique;
	}

	public void setPpNbrUpper(short ppNbrUpper) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrUpper() 0046 ");
		//debug
		this.ppNbrUpper = ppNbrUpper;
	}
	
	public void setPpNbrUpperNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNbrUpperNull() 0047 ");
		//debug
		ppNbrUpper = null;
	}

	void setPpNumberRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNumberRqd() 0007 ");
		//debug
		ppNumberRqd = false;
		if (pp_number_rqd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNumberRqd() 0023 ");
			//debug
			ppNumberRqd = true;
		}
		if (pp_number_rqd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpNumberRqd() 0024 ");
			//debug
			ppNumberRqd = false;
		}
	}

	public void setPpPwdMinLen(short ppPwdMinLen) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpPwdMinLen() 0001 ");
		//debug
		this.ppPwdMinLen = ppPwdMinLen;
	}
	
	public void setPpRrn(long ppRrn) {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpRrn() 000C ");
		//debug
		this.ppRrn = ppRrn;
	}
	
	public void setPpRrnNull() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpRrnNull() 003F ");
		//debug
		ppRrn = null;
	}

	void setPpSpecialRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpSpecialRqd() 0009 ");
		//debug
		ppSpecialRqd = false;
		if (pp_special_rqd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpSpecialRqd() 0025 ");
			//debug
			ppSpecialRqd = true;
		}
		if (pp_special_rqd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpSpecialRqd() 0026 ");
			//debug
			ppSpecialRqd = false;
		}
	}

	void setPpUpperRqd() {
		//debug
		System.out.println("com.yardi.ejb.model.Pwd_Policy setPpUpperRqd() 0003 ");
		//debug
		ppUpperRqd = false;
		if (pp_upper_rqd.equalsIgnoreCase("y")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpUpperRqd() 001F ");
			//debug
			ppUpperRqd = true;
		}
		if (pp_upper_rqd.equalsIgnoreCase("n")) {
			//debug
			System.out.println("com.yardi.ejb.model.Pwd_Policy setPpUpperRqd() 0020 ");
			//debug
			ppUpperRqd = false;
		}
	}
	
	public boolean isPpCantContainId() {
		return ppCantContainId;
	}

	public boolean isPpCantContainPwd() {
		return ppCantContainPwd;
	}

	@Override
	public String toString() {
		return "Pwd_Policy [ppDays=" + ppDays + ", ppNbrUnique=" + ppNbrUnique + ", ppMaxSignonAttempts=" + ppMaxSignonAttempts 
				+ ", ppPwdMinLen=" + ppPwdMinLen + ", pp_upper_rqd=" + pp_upper_rqd + ", pp_lower_rqd=" + pp_lower_rqd 
				+ ", pp_number_rqd=" + pp_number_rqd + ", pp_special_rqd=" + pp_special_rqd	+ ", ppUpperRqd=" + ppUpperRqd 
				+ ", ppLowerRqd=" + ppLowerRqd + ", ppNumberRqd=" + ppNumberRqd + ", ppSpecialRqd=" + ppSpecialRqd 
				+ ", ppPwdMaxLen=" + ppMaxPwdLen + ", ppMaxRepeatChar=" + ppMaxRepeatChar + ", ppNbrDigits=" + ppNbrDigits 
				+ ", ppNbrUpper=" + ppNbrUpper + ", ppNbrLower=" + ppNbrLower + ", ppNbrSpecial=" + ppNbrSpecial 
				+ ", ppCantContainID=" + ppCantContainId + ", ppCantContainPwd=" + ppCantContainPwd
				+ ", ppRrn=" + ppRrn 
				+ "]";
	}
}
