package com.miz.authenticateservice.advice.exceptions;

import com.miz.authenticateservice.constant.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = -137493420921168255L;
    private ErrorCode errorCode;
    private String text;
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.text = errorCode.getMessage();
        this.errorCode = errorCode;
    }

    public CustomException(String message) {
        super(message);
        this.text = message;
    }
}
