package com.gameserver.utils.account.rest.requests.common.validation;

import com.gameserver.utils.account.dao.AccountDiscordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonDiscordChecks {

    private static final String DISCORD_ID_REGEX = "[1-9][0-9]{16,17}"; // Discord User IDs can be 17 or 18 characters long

    private final AccountDiscordDao accountDiscordDao;

    @Autowired
    public CommonDiscordChecks(AccountDiscordDao accountDiscordDao) {
        this.accountDiscordDao = accountDiscordDao;
    }

    public boolean isValidFormat(String discordId) {
        return !discordId.matches(DISCORD_ID_REGEX);
    }

    public boolean isConnected(String discordId) {
        return accountDiscordDao.getByDiscordId(discordId) != null;
    }
}
