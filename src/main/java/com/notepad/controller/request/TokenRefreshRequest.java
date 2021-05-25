package com.notepad.controller.request;

import javax.validation.constraints.NotBlank;

public class TokenRefreshRequest {
  @NotBlank(message = "Please provide refresh token")
  private String refreshToken;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
