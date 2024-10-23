package com.miz.authenticateservice.advice.exceptions;

import com.miz.authenticateservice.constant.ErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = -1307493420921168L;
    private ErrorCode errorCode;
    private String text;

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public UnauthorizedException(String message) {
        super(message);
        this.text = message;
    }
}
