package com.notepad.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
* The TokenAndPasswordDTO to store token info linked to user email
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public class TokenAndPasswordDTO implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The token to verify email")
	@NotNull(message = "Please provide token")
	@NotBlank(message = "Please provide token")
	@Size(min = 8,max = 100, message = "Email min length is 8 and max length is 100")
	private String token;

	@ApiModelProperty(value = "The new password of the user")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull(message = "Please provide password")
	@NotBlank(message = "Please provide password")
	@Size(min = 8,max = 50, message = "Password min length is 8 and max length is 50")
	private String password;

	@ApiModelProperty(value = "The email id of the user")
	@NotNull(message = "Please provide email")
	@NotBlank(message = "Please provide email")
	@Size(min = 8,max = 50, message = "Email min length is 5 and max length is 50")
	@Email(message = "Email is invalid")
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
