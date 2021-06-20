package com.gameserver.utils.account.rest.requests.reset.validation;

import com.gameserver.utils.account.rest.requests.common.validation.CommonChecks;
import com.gameserver.utils.account.rest.responses.AccountErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

public class PICValidator implements ConstraintValidator<AccountResetConstraints.PICConstraint, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PICValidator.class); // TODO - @Slf4j annotation instead
    private static final int PIC_MIN_LENGTH = 6;
    private static final int PIC_MAX_LENGTH = 16;
    private static final String PIC_REGEX = "[a-zA-Z0-9]*";

    @Autowired
    private CommonChecks commonChecks;

    public void initialize(AccountResetConstraints.PICConstraint constraint) {
    }

    public boolean isValid(String pic, ConstraintValidatorContext context) {
        LOGGER.debug("Validating PIC");
        String message;

        if (commonChecks.isMissing(pic)) {
            message = "PIC field is empty";
        } else if (!isValidLength(pic)) {
            message = MessageFormat.format("PIC must be between {0} and {1} characters",
                    PIC_MIN_LENGTH, PIC_MAX_LENGTH);
        } else if (!isValidCharacters(pic)) {
            message = "PIC contains invalid characters";
        } else {
            return true;
        }

        throw new AccountErrorException(message, HttpStatus.FORBIDDEN);
    }

    private boolean isValidLength(String pic) {
        return pic.length() >= PIC_MIN_LENGTH && pic.length() <= PIC_MAX_LENGTH;
    }

    private boolean isValidCharacters(String pic) {
        return pic.matches(PIC_REGEX);
    }
}
