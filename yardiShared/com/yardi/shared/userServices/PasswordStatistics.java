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
	/**
	 * Number of upper case characters found 
	 */
	private int pwdNbrUpper;
	/**
	 * Number of lower case characters found
	 */
	private int pwdNbrLower;
	/**
	 * Number of special characters found
	 */
	private int pwdNbrSpecial;
	/**
	 * Number of digits found 
	 */
	private int pwdNbrNbr;
	/**
	 * Length of the password
	 */
	private int pwdLength;
	/**
	 * Number of repeated characters that were found 
	 */
	private int pwdNbrRepeatedChar; 
	/**
	 * Stores the password to be examined
	 */
	private char[] password;
	/**
	 * Indicated whether the password contains upper case characters
	 */
	boolean hasUpper   = false;
	/**
	 * Indicates whether the password contains lower case characters 
	 */
	boolean hasLower   = false;
	/**
	 * Indicates whether the password contains digits
	 */
	boolean hasDigit   = false;
	/**
	 * Indicates whether the password contains special characters
	 */
	boolean hasSpecial = false;
	
	/**
	 * Default constructor
	 */
	public PasswordStatistics() {
	}
	
	/**
	 * Constructor to initialize the password to be scanned. Calls <code>scanPassword()</code> to perform the analysis.
	 * @param password the password to be scanned
	 */
	public PasswordStatistics(char[] password) {
		this.password = password;
		scanPassword();
	}

	/**
	 * Return the password length
	 * @return password length
	 */
	public int getPwdLength() {
		return pwdLength;
	}
	
	/**
	 * Return the number of lower case characters found in the password
	 * @return number of lower case characters found
	 */
	public int getPwdNbrLower() {
		return pwdNbrLower;
	}

	/**
	 * Return number of digits found in the password
	 * @return number of digits found
	 */
	public int getPwdNbrNbr() {
		return pwdNbrNbr;
	}

	/**
	 * Return number of repeated characters found in the password
	 * @return number of repeated characters found
	 */
	public int getPwdNbrRepeatedChar() {
		return pwdNbrRepeatedChar;
	}

	/**
	 * Return number of special characters found in the password
	 * @return number of special characters found
	 */
	public int getPwdNbrSpecial() {
		return pwdNbrSpecial;
	}

	/**
	 * Return number of upper case characters found in the password
	 * @return number of upper case characters found
	 */
	public int getPwdNbrUpper() {
		return pwdNbrUpper;
	}

	/**
	 * Scan the password and compile statistics
	 */
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

	/**
	 * Debug
	 */
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
	
	/**
	 * Debug
	 */
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

	/**
	 * Debug
	 */
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
