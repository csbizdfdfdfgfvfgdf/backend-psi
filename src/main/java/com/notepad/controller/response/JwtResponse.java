package com.notepad.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
* The JwtResponse to return token
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	@JsonProperty("token")
	private String jwttoken;
	private String refreshToken;
	private String uuid;

	public JwtResponse(String jwttoken, String refreshToken) {
		this.jwttoken = jwttoken;
		this.refreshToken = refreshToken;
	}

}