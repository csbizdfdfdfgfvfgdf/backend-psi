package com.notepad.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* The Utility provides utilities to the whole app
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public class Utility {

	/**
     * validates the password on defined criteria
     *
     * @param password string to validate
     * @return true if password matches the criteria, false otherwise
     */
	public static boolean isValidPassword(String password) {
		boolean isValid = false;
		
		if(password == null) {
			return isValid;
		}
		
		// pattern to match criteria 
		Pattern p = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$");  
		Matcher m = p.matcher(password);  
		
		return m.matches();
	}
}
