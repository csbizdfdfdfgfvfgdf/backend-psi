package com.notepad.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.notepad.entity.enumeration.UserType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class CreateUserRequest {

    @ApiModelProperty(value = "The name of the user")
    @NotNull(message = "Please provide user name")
    @NotBlank(message = "Please provide user name")
    @Size(min = 5,max = 20, message = "User name min length is 5 and max length is 20")
    private String userName;

    @ApiModelProperty(value = "The email id of the user")
    @NotNull(message = "Please provide email")
    @NotBlank(message = "Please provide email")
    @Size(min = 8,max = 30, message = "Email min length is 5 and max length is 30")
    @Email(message = "Email is invalid")
    private String email;

   

    @JsonIgnore
    private UserType userType;

    @ApiModelProperty(value = "The password of the user")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Please provide password")
    @NotBlank(message = "Please provide password")
    @Size(min = 8,max = 20, message = "Password min length is 8 and max length is 20")
    private String password;

    @JsonIgnore
    private String uuid;
}
