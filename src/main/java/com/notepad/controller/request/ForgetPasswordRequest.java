package com.notepad.controller.request;

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
public class ForgetPasswordRequest {

    @ApiModelProperty(value = "The email id of the user")
    @NotNull(message = "Please provide email")
    @NotBlank(message = "Please provide email")
    @Size(min = 8,max = 50, message = "Email min length is 5 and max length is 30")
    @Email(message = "Email is invalid")
    private String email;
}
