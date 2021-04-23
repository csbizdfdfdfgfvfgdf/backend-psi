package com.notepad.jwt.model;

import java.io.Serializable;

/**
* The JwtResponse to return token
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;

	public JwtResponse(String jwttoken) {
		this.jwttoken = jwttoken;
	}

	public String getToken() {
		return this.jwttoken;
	}
}