package com.gameserver.utils.account.rest.requests.common.validation;

import com.gameserver.utils.account.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonUsernameChecks {

    public static final int USERNAME_MIN_LENGTH = 4;
    public static final int USERNAME_MAX_LENGTH = 12;

    private static final String USERNAME_REGEX = "[a-zA-Z0-9]*";

    private final AccountDao accountDao;

    @Autowired
    public CommonUsernameChecks(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public boolean isValidLength(String username) {
        return username.length() < USERNAME_MIN_LENGTH || username.length() > USERNAME_MAX_LENGTH;
    }

    public boolean isInvalidCharacters(String username) {
        return !username.matches(USERNAME_REGEX);
    }

    public boolean isExist(String username) {
        return accountDao.getByUsername(username) != null;
    }
}
