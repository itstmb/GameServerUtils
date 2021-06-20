package com.gameserver.utils.account.rest.requests.register.validation;

import com.gameserver.utils.account.rest.requests.common.validation.CommonChecks;
import com.gameserver.utils.account.rest.requests.common.validation.CommonUsernameChecks;
import com.gameserver.utils.account.rest.responses.AccountErrorException;
import com.gameserver.utils.account.service.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

import static com.gameserver.utils.account.rest.requests.common.validation.CommonUsernameChecks.USERNAME_MAX_LENGTH;
import static com.gameserver.utils.account.rest.requests.common.validation.CommonUsernameChecks.USERNAME_MIN_LENGTH;

public class UsernameValidator implements ConstraintValidator<AccountRegisterConstraints.UsernameConstraint, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class); // TODO - @Slf4j annotation instead

    @Autowired
    private CommonChecks commonChecks;

    @Autowired
    private CommonUsernameChecks commonUsernameChecks;

    @Override
    public void initialize(AccountRegisterConstraints.UsernameConstraint constraintAnnotation) { }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        LOGGER.debug("Validating username: [{}]", username);
        String message;

        if (commonChecks.isMissing(username)) {
            message = "Username field is empty";
        } else if (commonUsernameChecks.isValidLength(username)) {
            message = MessageFormat.format("Username must be between {0} and {1} characters",
                                            USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH);
        } else if (commonUsernameChecks.isInvalidCharacters(username)) {
            message = "Username contains invalid characters";
        } else if (commonUsernameChecks.isExist(username)) {
            message = "Username already exists";
            throw new AccountErrorException(message, HttpStatus.CONFLICT);
        } else {
            return true;
        }

        throw new AccountErrorException(message, HttpStatus.FORBIDDEN);
    }
}
