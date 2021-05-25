package com.notepad.serviceImpl;


import com.notepad.controller.request.TokenRefreshRequest;
import com.notepad.controller.response.JwtResponse;
import com.notepad.entity.RefreshToken;
import com.notepad.error.TokenRefreshException;
import com.notepad.jwt.config.JwtTokenUtil;
import com.notepad.repository.RefreshTokenRepository;
import com.notepad.repository.UserRepository;
import com.notepad.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;


@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtUtility;

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(String email) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findOneByEmail(email.toLowerCase().trim()).get());
        // refresh token expiry
        refreshToken.setExpiryDate(Instant.now().plusMillis(Long.valueOf(jwtUtility.getRefreshTokenExpiry()) ));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException();
        }
        return token;
    }

    /**
     * It will be deleted when user call refresh token to generate JWT and refresh token has been expired
     * It will delete that refresh token for user in DB
     * @param userId
     * @return count of deleted row
     */
    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    @Override
    public JwtResponse validateAndGenerateRefreshToken(TokenRefreshRequest request) {
        RefreshToken refreshToken = findByToken(request.getRefreshToken());
        if (refreshToken != null) {
            JwtResponse response = new JwtResponse();
            refreshToken = verifyExpiration(refreshToken);
            refreshToken.setExpiryDate(Instant.now().plusMillis(Long.valueOf(jwtUtility.getRefreshTokenExpiry()) ));
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshTokenRepository.save(refreshToken);
            response.setRefreshToken(refreshToken.getToken());
            response.setJwttoken(jwtUtility.generateTokenByUserName(refreshToken.getUser().getEmail()));
            return response;
        }
        throw new TokenRefreshException();
    }
}
