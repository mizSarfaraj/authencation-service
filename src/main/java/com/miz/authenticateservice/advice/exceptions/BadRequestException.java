package com.miz.authenticateservice.advice.exceptions;

import com.miz.authenticateservice.constant.ErrorCode;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = -1307493420921168255L;
    private ErrorCode errorCode;
    private String text;

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.text = errorCode.getMessage();
    }

    public BadRequestException(String message) {
        super(message);
        this.text = message;
    }
}
