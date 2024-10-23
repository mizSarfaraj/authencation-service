package com.miz.authenticateservice.advice;

import com.miz.authenticateservice.advice.exceptions.BadRequestException;
import com.miz.authenticateservice.advice.exceptions.CustomException;
import com.miz.authenticateservice.advice.exceptions.UnauthorizedException;
import com.miz.authenticateservice.constant.ErrorCode;
import com.miz.authenticateservice.dto.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Objects;

@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";

    private <T extends Exception> void printDetailedMessage(T e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (!element.getClassName().startsWith("java.") &&
                    !element.getClassName().startsWith("javax.") &&
                    !element.getClassName().startsWith("org.springframework.")) {
                final String ERR_TYPE_STRING = String.format("Error type : %s %s %s", ANSI_RED, e.getClass().getCanonicalName(), ANSI_RESET);
                final String CLASS_STRING = String.format("Exception in class : %s %s %s", ANSI_GREEN, element.getClassName(), ANSI_RESET);
                final String LINE_NO_STRING = String.format("at line number : %s %s %s", ANSI_BLUE, element.getLineNumber(), ANSI_RESET);
                final String METHOD_STRING = String.format("method : %s %s %s", ANSI_YELLOW, element.getMethodName(), ANSI_RESET);
                final String MSG_STRING = String.format("with message : %s %s %s", ANSI_RED, e.getMessage(), ANSI_RESET);
                log.error("\n\n\t{} \n\t{} \n\t{} \n\t{} \n\t{}\n\n", ERR_TYPE_STRING, CLASS_STRING, LINE_NO_STRING, METHOD_STRING, MSG_STRING);
                break;
            }
        }
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<BaseResponse<?>> handleException(BadRequestException exception) {
        printDetailedMessage(exception);
        BaseResponse<?> baseResponse = Objects.isNull(exception.getErrorCode())
                ? new BaseResponse<>(400, exception.getText())
                : new BaseResponse<>(exception.getErrorCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<BaseResponse<?>> handleException(UnauthorizedException exception) {
        printDetailedMessage(exception);
        BaseResponse<?> baseResponse = Objects.isNull(exception.getErrorCode())
                ? new BaseResponse<>(4001, exception.getText())
                : new BaseResponse<>(exception.getErrorCode());
        return new ResponseEntity<>(baseResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<BaseResponse<?>> handleException(CustomException exception) {
        printDetailedMessage(exception);
        BaseResponse<?> baseResponse = Objects.isNull(exception.getErrorCode())
                ? new BaseResponse<>(99999, exception.getText())
                : new BaseResponse<>(exception.getErrorCode());
        return new ResponseEntity<>(baseResponse, exception.getHttpStatus());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<BaseResponse<?>> handleException(NoResourceFoundException e) {
        printDetailedMessage(e);
        BaseResponse<?> baseResponse = new BaseResponse<>(ErrorCode.ENDPOINT_DOES_NOT_EXISTS);
        return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<BaseResponse<?>> handleException(Exception e) {
        printDetailedMessage(e);
        BaseResponse<?> baseResponse = new BaseResponse<>(500, e.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
