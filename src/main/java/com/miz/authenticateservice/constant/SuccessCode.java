package com.miz.authenticateservice.constant;

public enum SuccessCode {
    DATA_SAVED_SUCCESSFULLY(500000, "Data Saved Successfully"),
    USER_CREATED_SUCCESSFULLY(500001, "User created successfully"),
    LOGGED_IN_SUCCESSFULLY(500002, "User logged in successfully"),
    DATA_FETCH_SUCCESSFULLY(500003, "Data fetched Successfully");

    private Integer code;
    private String message;

    SuccessCode(Integer code, String message) {
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
