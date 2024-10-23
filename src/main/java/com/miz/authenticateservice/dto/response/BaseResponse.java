package com.miz.authenticateservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.miz.authenticateservice.constant.ErrorCode;
import com.miz.authenticateservice.constant.SuccessCode;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse<T> {
    private boolean success;
    private T data;
    private Object error;
    private String message;
    private Date datetime;

    public BaseResponse(ErrorCode errorCode) {
        this.success = false;
        this.data = null;
        this.error = new ErrorPayload(errorCode);
        this.datetime = new Date();
        this.message = errorCode.getMessage();
    }

    public BaseResponse(T data, SuccessCode successCode) {
        this.success = true;
        this.data = data;
        this.error = null;
        this.datetime = new Date();
        this.message = successCode.getMessage();
    }

    public BaseResponse(int code, String errorMessage) {
        this.success = false;
        this.data = null;
        this.error = new ErrorPayload(code, errorMessage);
        this.datetime = new Date();
        this.message = errorMessage;
    }

    public BaseResponse(SuccessCode successCode) {
        this.success = true;
        this.data = null;
        this.error = null;
        this.datetime = new Date();
        this.message = successCode.getMessage();
    }


    public BaseResponse(boolean success, T data, Object error, String message) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.datetime = new Date();
        this.message = message;
    }
}
