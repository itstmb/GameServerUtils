package com.gameserver.utils.account.dao;

import com.gameserver.utils.account.entity.AccountDiscord;

import java.util.List;

public interface AccountDiscordDao {

    void save(AccountDiscord accountDiscord) throws RuntimeException;

    AccountDiscord getByAccountId(int accountId);

    AccountDiscord getByDiscordId(String discordId);

    List<AccountDiscord> getAll();
}
