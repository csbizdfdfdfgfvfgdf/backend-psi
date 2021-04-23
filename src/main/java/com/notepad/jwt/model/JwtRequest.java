package com.notepad.jwt.model;

import java.io.Serializable;

/**
* The JwtRequest that have username and password of a user
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public class JwtRequest implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;
	
	private String userName;
	private String password;
	
	//default constructor for JSON Parsing
	public JwtRequest()
	{
	}

	public JwtRequest(String userName, String password) {
		this.setUserName(userName);
		this.setPassword(password);
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}