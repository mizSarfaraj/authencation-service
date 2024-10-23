package com.miz.authenticateservice.config.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miz.authenticateservice.dto.response.BaseResponse;
import com.miz.authenticateservice.config.security.JwtService;
import com.miz.authenticateservice.constant.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CurrentUser currentUser;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final List<String> bypassedURIs = List.of("/v1/auth/signup", "/v1/auth/login", "/v1/auth/get-token");

    public JwtTokenFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            CurrentUser currentUser
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.currentUser = currentUser;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        String requestURI = request.getRequestURI();
        if (this.bypassedURIs.contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (Objects.isNull(authHeader) || !authHeader.startsWith(BEARER)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ObjectMapper mapper = new ObjectMapper();
            ErrorCode errorCode = Objects.isNull(authHeader)
                    ? ErrorCode.AUTHORIZATION_HEADER_IS_MISSING
                    : ErrorCode.BEARER_TOKEN_IS_MISSING;
            String errorJson = mapper.writeValueAsString(
                    new BaseResponse<>(errorCode)
            );
            response.getWriter().write(errorJson);
            return;
        }

        try {
            final String token = authHeader.substring(BEARER.length());
            if (jwtService.isTokenExpired(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                String errorJson = new ObjectMapper().writeValueAsString(
                        new BaseResponse<>(ErrorCode.EXPIRED_TOKEN)
                );
                response.getWriter().write(errorJson);
                return;
            }

            Claim claim = jwtService.verifyAccessToken(token);
            String email = claim.asString();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.nonNull(email) && Objects.isNull(authentication)) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

                currentUser.setEmail(userDetails.getUsername());
                currentUser.setAccessToken(token);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException jwtVerificationException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            String errorJson = new ObjectMapper().writeValueAsString(
                    new BaseResponse<>(ErrorCode.TOKEN_VERIFICATION_FAILED)
            );
            response.getWriter().write(errorJson);
        }
    }
}
