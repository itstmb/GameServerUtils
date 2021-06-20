package com.gameserver.utils.account.service;

import com.gameserver.utils.account.dao.AccountDiscordDao;
import com.gameserver.utils.account.entity.AccountDiscord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@EnableAsync
@EnableScheduling
public class AccountPermissionServiceImpl implements AccountPermissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountPermissionServiceImpl.class); // TODO - @Slf4j annotation instead

    private final AccountDiscordDao accountDiscordDao;
    final RestTemplate restTemplate;
    private final boolean isServiceEnabled;
    final String discordBotUri;

    private List<Integer> discordVerifiedAccounts;

    @Autowired

    public AccountPermissionServiceImpl(@Value("${gameserverutils.account.permission.enabled}") boolean isServiceEnabled,
                                        @Value("${discord.bot.ip}") String discordBotIP,
                                        @Value("${discord.bot.port}") int discordBotPort,
                                        @Value("${discord.bot.get.members}") String discordBotGetMembers,
                                        AccountDiscordDao accountDiscordDao,
                                        RestTemplate restTemplate) {
        this.isServiceEnabled = isServiceEnabled;
        this.discordBotUri = "http://" + discordBotIP + ":" + discordBotPort + "/" + discordBotGetMembers;
        this.discordVerifiedAccounts = Collections.emptyList();
        this.accountDiscordDao = accountDiscordDao;
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getDiscordVerifiedAccounts() {
        LOGGER.info("Received new request of type [GetDiscordVerifiedAccounts]");
        Map<String, Object> response = new HashMap<>();
        response.put("accounts", discordVerifiedAccounts);
        LOGGER.info("GetDiscordVerifiedAccounts responded with [{}] entries", ((List<Integer>) response.get("accounts")).size());
        return response;
    }

    @Async
    @Scheduled(fixedRateString = "${gameserverutils.account.permission.cycle}", initialDelayString = "${gameserverutils.account.permission.initial.delay}")
    public void updateServiceData() {
        if (isServiceEnabled) {
            LOGGER.trace("Updating discord verified accounts...");
            StopWatch sw = new StopWatch();
            sw.start();
            boolean result = updateDiscordVerifiedAccounts();
            sw.stop();
            if (result) {
                LOGGER.info("Discord Verified Accounts successfully updated (took {}ms)", sw.getTotalTimeMillis());
            }
        }
    }

    private boolean updateDiscordVerifiedAccounts() {
        HashSet<String> discordIds = getDiscordIds();
        List<AccountDiscord> accountDiscords = accountDiscordDao.getAll();

        // Make sure both bot and db are up and not empty to reset and update the verified accounts
        if (discordIds != null && discordIds.size() > 0
                && accountDiscords != null && accountDiscords.size() > 0) {
            this.discordVerifiedAccounts = new ArrayList<>();

            accountDiscords.forEach(accountDiscord -> {
                if (discordIds.contains(accountDiscord.getDiscordId())) {
                    Integer accountId = accountDiscord.getAccountId();
                    this.discordVerifiedAccounts.add(accountId);
                }
            });
            return true;
        } else {
            LOGGER.warn("Skipped update discord verified accounts since one of the fetch operations failed." +
                    " Size of returned objects: discordIds: {}, accountDiscords: {}",
                    discordIds == null ? null : discordIds.size(),
                    accountDiscords == null ? null : accountDiscords.size());
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private HashSet<String> getDiscordIds() {
        List<Long> response;
        try {
            response = restTemplate.postForObject(discordBotUri, null, List.class); // POST and not GET because this is how Beresheet's bot is implemented.
        } catch (Exception e) {
            LOGGER.error("Unable to receive discord IDs from the discord bot. discordBotUri was: {}", discordBotUri);
            return null;
        }

        return response == null ? null : convertToHashSet(response);
    }

    private HashSet<String> convertToHashSet(List<Long> items) {
        HashSet<String> result = new HashSet<>();
        items.forEach(discordId -> result.add(discordId.toString()));
        return result;
    }
}