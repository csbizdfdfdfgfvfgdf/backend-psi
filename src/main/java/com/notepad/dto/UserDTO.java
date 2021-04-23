package com.notepad.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.notepad.entity.enumeration.UserType;

import io.swagger.annotations.ApiModelProperty;

/**
* The UserDTO to store info of a user
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The unique id of a user")
	private Long userId;
	@ApiModelProperty(value = "The name of the user")
	private String userName;
	@ApiModelProperty(value = "The email id of the user")
	private String email;
	@ApiModelProperty(value = "The phone number of the user")
	private String phone;
	@ApiModelProperty(value = "The type of the user")
	private UserType userType;
	
	@ApiModelProperty(value = "The password of the user")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	@ApiModelProperty(value = "The password of the user")
	private String confirmPassword;
	
	@ApiModelProperty(value = "The created date of the user")
	private LocalDateTime created;
	@ApiModelProperty(value = "The updated date of the user")
	private LocalDateTime updated;
	
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", userName=" + userName + ", email=" + email + ", phone=" + phone
				+ ", userType=" + userType + ", password=" + password + ", confirmPassword=" + confirmPassword
				+ ", created=" + created + ", updated=" + updated + "]";
	}
	
}
