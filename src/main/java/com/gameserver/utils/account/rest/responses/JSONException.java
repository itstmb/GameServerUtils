package com.gameserver.utils.account.rest.responses;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class JSONException extends RuntimeException {

    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public JSONException() {
    }

    public JSONException(String message) {
        super(message);
    }

    public JSONException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public JSONException(String message, Throwable cause) {
        super(message, cause);
    }

    public JSONException(Throwable cause) {
        super(cause);
    }

    public JSONException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, Object> getResponseMessage() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", this.getMessage());
        return response;
    }
}
