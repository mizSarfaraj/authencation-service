package com.miz.authenticateservice.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.miz.authenticateservice.advice.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class JwtService {
    private final String accessTokenSecret;
    private final String accessTokenExpiration;
    private final String refreshTokenSecret;
    private final String refreshTokenExpiration;

    public JwtService(
            @Value("${authentication.security.token.secret}") String accessTokenSecret,
            @Value("${authentication.security.token.expiration}") String accessTokenExpiration,
            @Value("${authentication.security.refresh-token.secret}") String refreshTokenSecret,
            @Value("${authentication.security.refresh-token.expiration}") String refreshTokenExpiration
    ) {
        this.accessTokenSecret = accessTokenSecret;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenSecret = refreshTokenSecret;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    private Algorithm getJwtAlgorithm(String secretKey) {
        return Algorithm.HMAC256(secretKey);
    }

    private String createToken(
            String email, String expiration, Algorithm algorithm
    ) {
        long exp = Long.parseLong(expiration);
        return JWT.create()
                .withIssuer("system")
                .withSubject(email)
                .withClaim("user", email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + exp))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis() + 1000L))
                .sign(algorithm);
    }

    public String generateAccessToken(String email) {
        return this.createToken(email, this.accessTokenExpiration, getJwtAlgorithm(this.accessTokenSecret));
    }

    public String generateRefreshToken(String email) {
        return this.createToken(email, this.refreshTokenExpiration, getJwtAlgorithm(this.refreshTokenSecret));
    }

    private JWTVerifier getJwtVerifier(Algorithm algorithm) {
        return JWT.require(algorithm)
                .withIssuer("system")
                .build();
    }

    public Claim verifyAccessToken(String token) {
        JWTVerifier verifier = getJwtVerifier(getJwtAlgorithm(this.accessTokenSecret));
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("user");
        } catch (JWTVerificationException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    public Claim verifyRefreshToken(String refreshToken) {
        JWTVerifier verifier = getJwtVerifier(getJwtAlgorithm(this.refreshTokenSecret));
        try {
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            return decodedJWT.getClaim("user");
        } catch (JWTVerificationException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date expirationDate = jwt.getExpiresAt();
            if (Objects.nonNull(expirationDate) && expirationDate.before(new Date())) {
                return true;
            }
        } catch (JWTDecodeException e) {
            log.error("token expiration exceptions : {}", e);
        }
        return false;
    }
}
