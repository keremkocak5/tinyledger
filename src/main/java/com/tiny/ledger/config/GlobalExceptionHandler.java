package com.tiny.ledger.config;

import com.tiny.ledger.exception.TinyLedgerRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {TinyLedgerRuntimeException.class})
    protected ProblemDetail handleException(TinyLedgerRuntimeException e) {
        log.warn("TinyLedgerRuntimeException {}",  e.getErrorCode().getErrorMessage());
        return ProblemDetail.forStatusAndDetail(e.getErrorCode().getHttpStatus(), e.getErrorCode().getErrorMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    protected ProblemDetail handleException(Exception e) {
        log.error("Internal Server Error!", e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

}
