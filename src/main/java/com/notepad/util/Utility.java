package com.notepad.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

	
	public static boolean isValidPassword(String password) {
		boolean isValid = false;
		
		if(password == null) {
			return isValid;
		}
		
		Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$");  
		Matcher m = p.matcher(password);  
		
		return m.matches();
	}
}
