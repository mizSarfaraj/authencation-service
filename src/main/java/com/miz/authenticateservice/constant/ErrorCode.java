package com.miz.authenticateservice.constant;

public enum ErrorCode {

    INTERNAL_SERVER_ERROR(100000, "Oops. Something went wrong"),
    AUTHORIZATION_HEADER_IS_MISSING(100001, "Authorization is missing"),
    BEARER_TOKEN_IS_MISSING(100002, "Bearer token is missing"),
    EXPIRED_TOKEN(100003, "Token has expired"),
    TOKEN_VERIFICATION_FAILED(100004, "Could not verify the token. Please, try again"),
    USER_NOT_FOUND(100005, "No user found with email id"),
    BAD_REQUEST(100006, "Bad Request! Please, check the request"),
    USER_ALREADY_EXISTS(100007, "User already exists with email id"),
    WRONG_EMAIL_PASSWORD(100008, "Incorrect email id or password."),
    ENDPOINT_DOES_NOT_EXISTS(100009, "The requested endpoint does not exists.");

    private Integer code;
    private String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCodeValue() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
