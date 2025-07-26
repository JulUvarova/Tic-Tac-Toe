package com.school21.Tic_Tac_Toe.domain.service.user;

import com.school21.Tic_Tac_Toe.web.model.JwtRequest;
import com.school21.Tic_Tac_Toe.web.model.JwtResponse;
import com.school21.Tic_Tac_Toe.web.model.RefreshJwtRequest;
import com.school21.Tic_Tac_Toe.web.security.JwtAuthentication;

public interface AuthService {
    JwtResponse login(JwtRequest request);
    JwtResponse refreshAccessToken(RefreshJwtRequest request);
    JwtResponse refreshRefreshToken(RefreshJwtRequest request);
    JwtAuthentication getAuthentication(String token);
} 