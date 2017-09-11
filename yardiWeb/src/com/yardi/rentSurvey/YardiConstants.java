package com.yardi.rentSurvey;

import java.util.regex.Pattern;

public class YardiConstants {
	public static final String YRD0000 = "YRD0000=Process completed normally";
	public static final String YRD0001 = "YRD0001=Invalid user name or password";
	public static final String YRD0002 = "YRD0002=Password expired";
	public static final String YRD0003 = "YRD0003=This account is disabled";
	public static final String YRD0004 = "YRD0004=This account is not active";
	public static final String YRD0005 = "YRD0005=Password is too short";
	public static final String YRD0006 = "YRD0006=Password must contain at least 1 upper case";
	public static final String YRD0007 = "YRD0007=Password must contain at least 1 lower case";
	public static final String YRD0008 = "YRD0008=Password must contain at least 1 number";
	public static final String YRD0009 = "YRD0009=Password must contain at least 1 special character";
	public static final String YRD000A = "YRD000A=Password matches a password that was previously used";
	public static final String YRD000B = "YRD000B=Password policy is missing";
	public static final String YRD000C = "YRD000C=Maximum signon attempts exceeded. The user profile has been disabled";
	public static final Pattern PATTERN_UPPER    = Pattern.compile(".*?\\p{Lu}");
	public static final Pattern PATTERN_LOWER    = Pattern.compile(".*?[\\p{L}&&[^\\p{Lu}]]");
	public static final Pattern PATTERN_NUMBER   = Pattern.compile(".*\\d.*");
	public static final Pattern PATTERN_SPECIAL1 = Pattern.compile(".*[~`!@#$%^&*=()--+_{}\\[\\]\\\\|;:'\"<>,./?].*");
	
	public void testPassword(String pwd) {
		// useful for testing password rules
		String special [] = {
				    "`", "~",  "!", "@", "#", "$",  "%", "^", "&", "*",
				    "(", ")",  "-", "_", "+", "=",  "[", "]", "{", "}",
				    "|", "\\", ";", ":", "'", "\"", "<", ">", ",", ".",
				    "/", "?"
		};
		
		String upper [] = {
				    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
				    "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
				    "U", "V", "W", "X", "Y", "Z"
		};
		
		String lower [] = {
				    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
				    "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
				    "u", "v", "w", "x", "y", "z"
		};
		
		String number [] = {
				    "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
		};
		
		String password [] = {
				    "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
		};
		
		for (int i = 0;i<password.length;i++) {
			
			if (PATTERN_SPECIAL1.matcher(password[i]).matches()) {
				System.out.println(password[i] + " matches");
			}
		}
	}
}
