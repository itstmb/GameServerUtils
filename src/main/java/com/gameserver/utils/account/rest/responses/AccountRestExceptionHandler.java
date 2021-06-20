package com.gameserver.utils.account.rest.responses;

import com.gameserver.utils.account.service.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;

@ControllerAdvice
public class AccountRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class); // TODO - @Slf4j annotation instead

    @ExceptionHandler
    public ResponseEntity<AccountErrorResponse> handleException(ValidationException exc) {

        AccountErrorException cause = (AccountErrorException) exc.getCause();
        if (cause == null) {
            cause = new AccountErrorException("Unknown Validation Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        AccountErrorResponse error = new AccountErrorResponse(cause.getHttpStatus().value(),
                cause.getMessage());

        LOGGER.info("Account request denied due to validation exception, response was: {}", error.toString());
        return new ResponseEntity<>(error, cause.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<AccountErrorResponse> handleException(AccountErrorException exc) {
        AccountErrorResponse error = new AccountErrorResponse(exc.getHttpStatus().value(),
                exc.getMessage());

        LOGGER.info("Account request denied due to account error exception, response was: {}", error.toString());
        return new ResponseEntity<>(error, exc.getHttpStatus());
    }

    @ExceptionHandler
    public HttpEntity<?> handleException(JSONException exc) {
        HttpStatus httpStatus = exc.getHttpStatus();
        return new ResponseEntity<>(exc.getResponseMessage(), httpStatus);
    }



    @ExceptionHandler
    public ResponseEntity<AccountErrorResponse> handleException(Exception exc) {

        AccountErrorResponse error = new AccountErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error");

        LOGGER.warn("Unhandled exception has reached the handler: " + exc.toString());
        LOGGER.info("Account request denied due to an unhandled error exception, response was: {}", error.toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
