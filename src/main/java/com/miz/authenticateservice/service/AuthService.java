package com.miz.authenticateservice.service;

import com.auth0.jwt.interfaces.Claim;
import com.miz.authenticateservice.advice.exceptions.BadRequestException;
import com.miz.authenticateservice.advice.exceptions.CustomException;
import com.miz.authenticateservice.config.security.JwtService;
import com.miz.authenticateservice.constant.ErrorCode;
import com.miz.authenticateservice.dto.request.auth.LoginRequestDto;
import com.miz.authenticateservice.dto.request.auth.SignUpRequestDto;
import com.miz.authenticateservice.dto.response.auth.LoginResponseDto;
import com.miz.authenticateservice.entity.Users;
import com.miz.authenticateservice.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsersRepository usersRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public void signup(SignUpRequestDto signUpRequestDto) {
        if (Objects.isNull(signUpRequestDto.getEmail()) || Objects.isNull(signUpRequestDto.getPassword())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }
        Optional<Users> optionalUsers = usersRepository.findByEmail(signUpRequestDto.getEmail());
        if (optionalUsers.isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }
        Users users = Users.builder()
                .createdAt(new Date())
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword().trim()))
                .isActive(true)
                .build();
        usersRepository.save(users);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        if (Objects.isNull(loginRequestDto.getEmail()) || Objects.isNull(loginRequestDto.getPassword())) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        Users users = usersRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), users.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_EMAIL_PASSWORD);
        }

        String token = jwtService.generateAccessToken(loginRequestDto.getEmail());
        String refreshToken = jwtService.generateRefreshToken(loginRequestDto.getEmail());

        return LoginResponseDto.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    public String getNewToken(String refreshToken) {
        if (jwtService.isTokenExpired(refreshToken)) {
            throw new CustomException("Token expired");
        }
        Claim claim = jwtService.verifyRefreshToken(refreshToken);
        return jwtService.generateAccessToken(claim.asString());
    }

}
