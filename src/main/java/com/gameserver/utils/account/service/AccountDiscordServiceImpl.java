package com.gameserver.utils.account.service;

import com.gameserver.utils.account.dao.AccountDao;
import com.gameserver.utils.account.dao.AccountDiscordDao;
import com.gameserver.utils.account.dao.KeyDao;
import com.gameserver.utils.account.entity.Account;
import com.gameserver.utils.account.entity.AccountDiscord;
import com.gameserver.utils.account.entity.CCharacter;
import com.gameserver.utils.account.entity.Key;
import com.gameserver.utils.account.rest.requests.discord.AccountDiscordConnectionRequest;
import com.gameserver.utils.account.rest.requests.discord.KeyDiscordConnectionRequest;
import com.gameserver.utils.account.rest.responses.AccountResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@EnableCaching
public class AccountDiscordServiceImpl implements AccountDiscordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDiscordService.class); // TODO - @Slf4j annotation instead

    private final AccountDao accountDao;
    private final KeyDao keyDao;
    private final AccountDiscordDao accountDiscordDao;

    @Autowired
    public AccountDiscordServiceImpl(AccountDao accountDao, KeyDao keyDao, AccountDiscordDao accountDiscordDao) {
        this.accountDao = accountDao;
        this.keyDao = keyDao;
        this.accountDiscordDao = accountDiscordDao;
    }

    public AccountResponse connectDiscord(AccountDiscordConnectionRequest accountDiscordConnectionRequest) {
        LOGGER.debug("Connecting account [{}] with discord [{}]", accountDiscordConnectionRequest.getUsername(),
                accountDiscordConnectionRequest.getDiscordId());

        Account account = accountDao.getByUsername(accountDiscordConnectionRequest.getUsername());
        AccountDiscord accountDiscord = connectDiscord(account, accountDiscordConnectionRequest.getDiscordId());

        AccountResponse accountResponse = new AccountResponse(HttpStatus.OK.value(), "Successfully connected discord to the account");
        LOGGER.info("Account [{}] connected successfully with discord [{}], response was: {}",
                accountDiscord.getAccountId(), accountDiscord.getDiscordId(), accountResponse.toString());
        return accountResponse;
    }

    public AccountResponse connectDiscord(KeyDiscordConnectionRequest keyDiscordConnectionRequest) {
        LOGGER.debug("Connecting key [{}] with discord [{}]", keyDiscordConnectionRequest.getKey(),
                keyDiscordConnectionRequest.getDiscordId());

        Key key = keyDao.getBySerialNumber(keyDiscordConnectionRequest.getKey());
        Account account = accountDao.getById(key.getAccountId());
        AccountDiscord accountDiscord = connectDiscord(account, keyDiscordConnectionRequest.getDiscordId());

        AccountResponse accountResponse = new AccountResponse(HttpStatus.OK.value(), "Successfully connected discord to the account");
        LOGGER.info("Account [{}] connected successfully with discord [{}], response was: {}",
                accountDiscord.getAccountId(), accountDiscord.getDiscordId(), accountResponse.toString());
        return accountResponse;
    }

    public AccountDiscord connectDiscord(Account account, String discordId) {
        AccountDiscord accountDiscord = new AccountDiscord(account.getId(), discordId);
        accountDiscordDao.save(accountDiscord);
        return accountDiscord;
    }

    public AccountDiscord getByAccount(int accountId) {
        return accountDiscordDao.getByAccountId(accountId);
    }

    @Override
    public AccountResponse getCharacters(String discordId) { // Not cached because characters can be added/removed
        AccountDiscord accountDiscord = accountDiscordDao.getByDiscordId(discordId);
        if (accountDiscord == null) {
            return new AccountResponse(HttpStatus.OK.value(), "Discord not found");
        }

        List<CCharacter> characters = accountDao.getCharactersByAccountId(accountDiscord.getAccountId());
        if (characters == null) {
            return new AccountResponse(HttpStatus.OK.value(), "Account does not have any characters");
        }

        List<String> characterNames = new ArrayList<>();
        characters.forEach(cCharacter -> characterNames.add(cCharacter.getName()));

        AccountResponse accountResponse = new AccountResponse(HttpStatus.OK.value(), characterNames);
        LOGGER.info("GetCharacters request for discord [{}] succeeded, response was: {}", discordId, accountResponse.toString());

        return accountResponse;
    }

    @Override
    @Cacheable("characterToDiscord") // Cached since discord for characters doesn't change
    public AccountResponse getDiscord(String characterName) {
        Account account = accountDao.getByCharacterName(characterName);
        if (account == null) {
            return new AccountResponse(HttpStatus.OK.value(), "Character not found");
        }
        AccountDiscord accountDiscord = accountDiscordDao.getByAccountId(account.getId());
        if (accountDiscord == null) {
            return new AccountResponse(HttpStatus.OK.value(), "Account does not have a connected discord");
        }

        AccountResponse accountResponse = new AccountResponse(HttpStatus.OK.value(), accountDiscord.getDiscordId());
        LOGGER.info("GetDiscord request for character [{}] succeeded, response was: {}", characterName, accountResponse.toString());

        return accountResponse;
    }

    @Override
    @Cacheable("discordToAccount")
    public ResponseEntity<Map<String, Object>> getUsername(String discordId) {
        AccountDiscord accountDiscord = accountDiscordDao.getByDiscordId(discordId);
        if (accountDiscord == null) {
            LOGGER.info("GetAccountNameByDiscord request for discord {} succeeded, no matching account found", discordId);
            return ResponseEntity.noContent().build();
        }

        int accountId = accountDiscord.getAccountId();
        String accountName = accountDao.getById(accountId).getUsername();
        Map<String, Object> response = new HashMap<>();
        response.put("name", accountName);
        LOGGER.info("GetAccountNameByDiscord request for discord {} succeeded, account is {} ", discordId, accountName);

        return ResponseEntity.ok(response);
    }
}
