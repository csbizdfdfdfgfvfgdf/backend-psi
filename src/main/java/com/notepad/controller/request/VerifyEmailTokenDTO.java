package com.notepad.controller.request;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


public class VerifyEmailTokenDTO implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The token to verify email")
	@NotNull(message = "Please provide token")
	@NotBlank(message = "Please provide token")
	@Size(min = 8,max = 100, message = "Email min length is 8 and max length is 100")
	private String token;

	@ApiModelProperty(value = "The email id of the user")
	@NotNull(message = "Please provide email")
	@NotBlank(message = "Please provide email")
	@Size(min = 8,max = 50, message = "Email min length is 5 and max length is 50")
	@Email(message = "Email is invalid")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
