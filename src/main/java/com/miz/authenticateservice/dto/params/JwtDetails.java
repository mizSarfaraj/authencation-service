package com.miz.authenticateservice.dto.params;

import lombok.Data;

@Data
public class JwtDetails {
    private Long userId;
    private String email;
}
