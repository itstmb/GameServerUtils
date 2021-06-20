package com.gameserver.utils.account.rest.requests.register.validation;

import com.gameserver.utils.account.rest.requests.common.validation.CommonChecks;
import com.gameserver.utils.account.rest.responses.AccountErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

public class PasswordValidator implements ConstraintValidator<AccountRegisterConstraints.PasswordConstraint, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordValidator.class); // TODO - @Slf4j annotation instead

    private static final int PASSWORD_MIN_LENGTH = 6;
    private static final int PASSWORD_MAX_LENGTH = 12;
    private static final String PASSWORD_REGEX = "[a-zA-Z0-9!@#$%^&*()~=+]*";

    @Autowired
    private CommonChecks commonChecks;

    @Override
    public void initialize(AccountRegisterConstraints.PasswordConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        LOGGER.debug("Validating password");
        String message;

        if (commonChecks.isMissing(password)) {
            message = "Password field is empty";
        } else if (!isValidLength(password)) {
            message = MessageFormat.format("Password must be between {0} and {1} characters",
                    PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH);
        } else if (!isValidCharacters(password)) {
            message = "Password contains invalid characters";
        } else {
            return true;
        }

        throw new AccountErrorException(message, HttpStatus.FORBIDDEN);
    }

    private boolean isValidLength(String username) {
        return username.length() >= PASSWORD_MIN_LENGTH && username.length() <= PASSWORD_MAX_LENGTH;
    }

    private boolean isValidCharacters(String username) {
        return username.matches(PASSWORD_REGEX);
    }
}
