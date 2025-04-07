package com.tiny.ledger.exception;

import com.tiny.ledger.enums.ErrorCode;
import lombok.Getter;

@Getter
public class TinyLedgerRuntimeException extends RuntimeException {

    private final ErrorCode errorCode;

    public TinyLedgerRuntimeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
