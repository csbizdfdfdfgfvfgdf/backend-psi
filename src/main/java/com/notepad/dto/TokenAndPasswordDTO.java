package com.notepad.dto;

import java.io.Serializable;

public class TokenAndPasswordDTO implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;
	
	private String token;
	
	private String password;
	
	private String email;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "TokenAndPasswordDTO [token=" + token + ", password=" + password + ", email=" + email + "]";
	}

}
