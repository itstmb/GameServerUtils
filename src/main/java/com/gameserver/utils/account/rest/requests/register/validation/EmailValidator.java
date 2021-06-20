package com.gameserver.utils.account.rest.requests.register.validation;

import com.gameserver.utils.account.dao.AccountDao;
import com.gameserver.utils.account.rest.requests.common.validation.CommonChecks;
import com.gameserver.utils.account.rest.responses.AccountErrorException;
import com.gameserver.utils.account.service.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

public class EmailValidator implements ConstraintValidator<AccountRegisterConstraints.EmailConstraint, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class); // TODO - @Slf4j annotation instead

    private static final int EMAIL_MAX_LENGTH = 50;
    private static final String EMAIL_REGEX =
            "(?:[a-z0-9_-]+(?:\\.[a-z0-9_-]+)*|\"(?:[\\x01-\\" +
                    "x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b" +
                    "\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](" +
                    "?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0" +
                    "-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a" +
                    "-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\" +
                    "x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";

    @Autowired
    private CommonChecks commonChecks;

    @Autowired
    private AccountDao accountDao;

    @Override
    public void initialize(AccountRegisterConstraints.EmailConstraint constraintAnnotation) {}

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        LOGGER.debug("Validating email: [{}]", email);

        String message;

        if (commonChecks.isMissing(email)) {
            message = "Email field is empty";
        } else if (!isValidFormat(email)) {
            message = "Invalid email format";
        } else if (!isValidLength(email)) {
            message = MessageFormat.format("Email cannot exceed {0} characters",
                                            EMAIL_MAX_LENGTH);
        } else if (!isTaken(email)) {
            message = "Email already exists";
            throw new AccountErrorException(message, HttpStatus.CONFLICT);
        } else {
            return true;
        }

        throw new AccountErrorException(message, HttpStatus.FORBIDDEN);
    }

    private boolean isValidFormat(String email) {
        email = email.toLowerCase();
        return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email) &&
                email.matches(EMAIL_REGEX);
    }

    private boolean isValidLength(String email) {
        return email.length() < EMAIL_MAX_LENGTH;
    }

    private boolean isTaken(String email) {
        return accountDao.getByEmail(email) == null;
    }
}
