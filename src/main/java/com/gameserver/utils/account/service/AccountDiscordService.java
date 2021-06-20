package com.gameserver.utils.account.service;

import com.gameserver.utils.account.entity.Account;
import com.gameserver.utils.account.entity.AccountDiscord;
import com.gameserver.utils.account.rest.requests.discord.AccountDiscordConnectionRequest;
import com.gameserver.utils.account.rest.requests.discord.KeyDiscordConnectionRequest;
import com.gameserver.utils.account.rest.responses.AccountResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AccountDiscordService {
    AccountResponse connectDiscord(AccountDiscordConnectionRequest accountDiscordConnectionRequest);

    AccountResponse connectDiscord(KeyDiscordConnectionRequest keyDiscordConnectionRequest);

    AccountDiscord connectDiscord(Account account, String discordId);

    AccountDiscord getByAccount(int accountId);

    AccountResponse getCharacters(String discord);

    AccountResponse getDiscord(String characterName);

    ResponseEntity<Map<String, Object>> getUsername(String discordId);
}
