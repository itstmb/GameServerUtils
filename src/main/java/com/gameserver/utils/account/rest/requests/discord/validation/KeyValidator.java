package com.gameserver.utils.account.rest.requests.discord.validation;

import com.gameserver.utils.account.dao.AccountDao;
import com.gameserver.utils.account.dao.AccountDiscordDao;
import com.gameserver.utils.account.dao.KeyDao;
import com.gameserver.utils.account.entity.Account;
import com.gameserver.utils.account.entity.Key;
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

public class KeyValidator implements ConstraintValidator<AccountDiscordConstraints.KeyConstraint, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class); // TODO - @Slf4j annotation instead

    @Autowired
    private CommonChecks commonChecks;

    @Autowired
    private CommonKeyChecks commonKeyChecks;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountDiscordDao accountDiscordDao;

    @Autowired
    private KeyDao keyDao;

    @Override
    public void initialize(AccountDiscordConstraints.KeyConstraint constraintAnnotation) { }

    @Override
    public boolean isValid(String key, ConstraintValidatorContext context) {
        LOGGER.debug("Validating key: [{}]", key);

        String message;

        if (commonChecks.isMissing(key)) {
            message = "No key provided";
        } else if (commonKeyChecks.isInvalidFormat(key)) {
            message = "Invalid key format";
        } else if (commonKeyChecks.isNonexistentKey(key) || !commonKeyChecks.isTaken(key)) {
            message = "Invalid key"; // using the same message to hide the key if existent but not used yet by any account
        } else if (isConnectedToDiscord(key)) {
            message = "Account already connected to another discord";
        } else {
            return true;
        }

        throw new AccountErrorException(message, HttpStatus.FORBIDDEN);
    }

    private boolean isConnectedToDiscord(String serialNumber) {
        Key key = keyDao.getBySerialNumber(serialNumber);
        Account account = accountDao.getById(key.getAccountId());
        return account != null && accountDiscordDao.getByAccountId(account.getId()) != null;
    }
}
