package com.gameserver.utils.account.rest.requests.register.validation;

import com.gameserver.utils.account.rest.requests.common.validation.CommonChecks;
import com.gameserver.utils.account.rest.requests.common.validation.CommonKeyChecks;
import com.gameserver.utils.account.rest.responses.AccountErrorException;
import com.gameserver.utils.account.service.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class KeyValidator implements ConstraintValidator<AccountRegisterConstraints.KeyConstraint, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class); // TODO - @Slf4j annotation instead

    @Autowired
    private CommonChecks commonChecks;

    @Autowired
    private CommonKeyChecks commonKeyChecks;

    @Override
    public void initialize(AccountRegisterConstraints.KeyConstraint constraintAnnotation) { }

    @Override
    public boolean isValid(String serialNumber, ConstraintValidatorContext context) {
        LOGGER.debug("Validating key: [{}]", serialNumber);

        String message;

        if (commonChecks.isMissing(serialNumber)) {
            message = "No key provided";
        } else if (commonKeyChecks.isInvalidFormat(serialNumber)) {
            message = "Invalid key format";
        } else if (commonKeyChecks.isNonexistentKey(serialNumber)) {
            message = "Invalid key";
        } else if (commonKeyChecks.isTaken(serialNumber)) {
            message = "Key has already been used";
        } else {
            return true;
        }

        throw new AccountErrorException(message, HttpStatus.UNAUTHORIZED);
    }
}
