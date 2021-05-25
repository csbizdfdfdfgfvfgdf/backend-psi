package com.notepad.service;


import com.notepad.controller.request.TokenRefreshRequest;
import com.notepad.controller.response.JwtResponse;
import com.notepad.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken findByToken(String token);

    RefreshToken createRefreshToken(String userName);

    RefreshToken verifyExpiration(RefreshToken token);

    int deleteByUserId(Long userId);

    JwtResponse validateAndGenerateRefreshToken(TokenRefreshRequest request);
}
