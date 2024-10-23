package com.miz.authenticateservice.dto.response;

import com.miz.authenticateservice.constant.ErrorCode;
import lombok.Data;

@Data
public class ErrorPayload {
    private int code;
    private String text;
    private Object details;

    public ErrorPayload(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public ErrorPayload(int code, String text, Object details) {
        this.code = code;
        this.text = text;
        this.details = details;
    }

    public ErrorPayload(ErrorCode errorCode) {
        this.code = errorCode.getCodeValue();
        this.text = errorCode.getMessage();
    }
}
