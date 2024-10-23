package com.miz.authenticateservice.controller;

import com.miz.authenticateservice.constant.SuccessCode;
import com.miz.authenticateservice.dto.request.auth.LoginRequestDto;
import com.miz.authenticateservice.dto.request.auth.SignUpRequestDto;
import com.miz.authenticateservice.dto.response.BaseResponse;
import com.miz.authenticateservice.dto.response.auth.LoginResponseDto;
import com.miz.authenticateservice.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<SignUpRequestDto>> signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signup(signUpRequestDto);
        return new ResponseEntity<>(new BaseResponse<>(SuccessCode.USER_CREATED_SUCCESSFULLY), HttpStatus.OK);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        return new ResponseEntity<>(new BaseResponse<>(loginResponseDto, SuccessCode.LOGGED_IN_SUCCESSFULLY), HttpStatus.OK);
    }

    @GetMapping("/get-token")
    public ResponseEntity<BaseResponse<String>> refreshToken(@RequestParam String refreshToken) {
        String token = authService.getNewToken(refreshToken);
        return new ResponseEntity<>(new BaseResponse<>(token, SuccessCode.LOGGED_IN_SUCCESSFULLY), HttpStatus.OK);
    }
}
