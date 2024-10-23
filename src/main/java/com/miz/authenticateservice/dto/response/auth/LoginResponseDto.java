package com.miz.authenticateservice.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponseDto {
    private String token;
    private String refreshToken;
}
