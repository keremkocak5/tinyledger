package com.tiny.ledger.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    BALANCE_NEGATIVE("I0001", "Balance cannot be negative", HttpStatus.NOT_ACCEPTABLE),
    ACCOUNT_NOT_FOUND("I0002", "Account not found", HttpStatus.NOT_FOUND),
    DUPLICATE_UUID("I0003", "UUID already generated, please try again", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    ErrorCode(String errorCode, String errorMessage, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

}
