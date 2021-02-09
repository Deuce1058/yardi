package com.yardi.shared.userServices;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Several password rules rely on scanning the password as a <b>char []</b> to determine whether the password contains 
 * upper case, lower case, etc. <br><br>
 * Rather than duplicating that scan logic for each rule, this class performs a single scan on the password and accumulates 
 * statistics that can be checked to determine whether a password complies with password policy.
 *   
 * @author Jim
 *
 */
public class PasswordStatistics {
	private int pwdNbrUpper;
	private int pwdNbrLower;
	private int pwdNbrSpecial;
	private int pwdNbrNbr;
	private int pwdLength;
	private int pwdNbrRepeatedChar; 
	private char[] password;
	boolean hasUpper   = false;
	boolean hasLower   = false;
	boolean hasDigit   = false;
	boolean hasSpecial = false;
	
	public PasswordStatistics() {
	}
	
	public PasswordStatistics(char[] password) {
		this.password = password;
		scanPassword();
	}

	private void scanPassword() {
		pwdLength = password.length;
		HashMap<Character, Integer> checkedChar = new HashMap<Character, Integer>();
		hasUpper   = false;
		hasLower   = false;
		hasDigit   = false;
		hasSpecial = false;
		
		for (char p : password) {
			if (Character.isLowerCase(p)) {
				pwdNbrLower++;
				hasLower = true;
			} else {
				if (Character.isUpperCase(p)) {
					pwdNbrUpper++;
					hasUpper = true;
				} else {
					if (Character.isDigit(p)) {
						pwdNbrNbr++;
						hasDigit = true;
					} else { //It wasnt upper, wasnt lower, wasnt a digit, so we'll count it as special
						pwdNbrSpecial++;
						hasSpecial = true;
					}
				}
			}
			Integer occurrence = checkedChar.get((Object)p);
			
			if (occurrence == null) {
				checkedChar.put(p, 1);
			} else {
				occurrence++;
				checkedChar.replace(p, occurrence);
			}
		}
		Iterator<Entry<Character, Integer>> it = checkedChar.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry<Character, Integer> e = (Map.Entry<Character, Integer>)it.next();
			Integer val = e.getValue();
			int v = val.intValue();
			if (v > 1) {
				pwdNbrRepeatedChar += v;
			}
		}
	}
	
	public int getPwdNbrUpper() {
		return pwdNbrUpper;
	}

	public int getPwdNbrLower() {
		return pwdNbrLower;
	}

	public int getPwdNbrSpecial() {
		return pwdNbrSpecial;
	}

	public int getPwdNbrNbr() {
		return pwdNbrNbr;
	}

	public int getPwdLength() {
		return pwdLength;
	}

	public int getPwdNbrRepeatedChar() {
		return pwdNbrRepeatedChar;
	}

	@Override
	public String toString() {
		return "com.yardi.userServices PasswordStatistics toString() 0000"
				+ "\n"
				+ "             hasUpper="
				+ hasUpper
				+ "\n"
				+ "             haslower="
				+ hasLower
				+ "\n"
				+ "             hasDigit="
				+ hasDigit
				+ "\n"
				+ "           hasSpecial="
				+ hasSpecial
				+ "\n"
				+ "          pwdNbrUpper=" 
				+ pwdNbrUpper
				+ "\n"
				+ "          pwdNbrLower=" 
				+ pwdNbrLower
				+ "\n"
				+ "        pwdNbrSpecial="
				+ pwdNbrSpecial 
				+ "\n"
				+ "            pwdNbrNbr=" 
				+ pwdNbrNbr
				+ "\n"
				+ "            pwdLength=" 
				+ pwdLength
				+ "\n"
				+ "   pwdNbrRepeatedChar="
				+ pwdNbrRepeatedChar 
				+ "\n"
				+ "             password=" 
				+ Arrays.toString(password);
	}

	public String toString1() {
		return "com.yardi.userServices PasswordStatistics toString() 0001"
				+ "\n"
				+ "   password="
				+ Arrays.toString(password)
				+ "\n"
				+ "   pwdNbrRepeatedchar="
				+ pwdNbrRepeatedChar
				+ "\n"
				+ "          pwdNbrUpper=" 
				+ pwdNbrUpper
				+ "\n"
				+ "          pwdNbrLower=" 
				+ pwdNbrLower
				+ "\n"
				+ "            pwdNbrNbr=" 
				+ pwdNbrNbr
				+ "\n"
				+ "        pwdNbrSpecial="
				+ pwdNbrSpecial 
				+ "\n"
				+ "             hasUpper="
				+ hasUpper
				+ "\n"
				+ "             haslower="
				+ hasLower
				+ "\n"
				+ "             hasDigit="
				+ hasDigit
				+ "\n"
				+ "           hasSpecial="
				+ hasSpecial;
	}
	
	public String toString2() {
		return "password="
				+ Arrays.toString(password)
				+ "\n"
				+ "     pwdNbrUpper=" 
				+ pwdNbrUpper
				+ "\n"
				+ "     pwdNbrLower=" 
				+ pwdNbrLower
				+ "\n"
				+ "   pwdNbrSpecial="
				+ pwdNbrSpecial 
				+ "\n"
				+ "       pwdNbrNbr=" 
				+ pwdNbrNbr;
	}

	public String toString3() {
		return "password="
				+ Arrays.toString(password)
				+ "\n"
				+ "            pwdLength=" 
				+ pwdLength
				+ "\n"
				+ "   pwdNbrRepeatedChar="
				+ pwdNbrRepeatedChar; 
	}
}
