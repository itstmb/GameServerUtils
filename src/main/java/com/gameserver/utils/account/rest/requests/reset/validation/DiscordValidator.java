package com.gameserver.utils.account.rest.requests.reset.validation;

import com.gameserver.utils.account.dao.AccountDao;
import com.gameserver.utils.account.rest.requests.common.validation.CommonChecks;
import com.gameserver.utils.account.rest.requests.common.validation.CommonDiscordChecks;
import com.gameserver.utils.account.rest.responses.AccountErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DiscordValidator implements ConstraintValidator<AccountResetConstraints.DiscordConstraint, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordValidator.class); // TODO - @Slf4j annotation instead

    @Autowired
    private CommonChecks commonChecks;

    @Autowired
    private CommonDiscordChecks commonDiscordChecks;

    @Autowired
    private AccountDao accountDao;

    @Override
    public void initialize(AccountResetConstraints.DiscordConstraint constraintAnnotation) { }

    @Override
    public boolean isValid(String discordId, ConstraintValidatorContext constraintValidatorContext) {
        LOGGER.debug("Validating discord: [{}]", discordId);
        String message;

        if (commonChecks.isMissing(discordId)) {
            message = "No discord provided";
        } else if (commonDiscordChecks.isValidFormat(discordId)) {
            message = "Invalid discord format";
        } else if (!commonDiscordChecks.isConnected(discordId)) {
            message = "Discord is not connected to an account";
        } else {
            return true;
        }

        throw new AccountErrorException(message, HttpStatus.FORBIDDEN);
    }
}
