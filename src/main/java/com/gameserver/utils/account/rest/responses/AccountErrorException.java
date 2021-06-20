package com.gameserver.utils.account.rest.responses;

import org.springframework.http.HttpStatus;

public class AccountErrorException extends RuntimeException {

    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public AccountErrorException() {
    }

    public AccountErrorException(String message) {
        super(message);
    }

    public AccountErrorException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public AccountErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountErrorException(Throwable cause) {
        super(cause);
    }

    public AccountErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
