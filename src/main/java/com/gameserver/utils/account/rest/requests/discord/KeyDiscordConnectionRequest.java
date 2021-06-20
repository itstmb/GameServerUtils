package com.gameserver.utils.account.rest.requests.discord;

import com.gameserver.utils.account.rest.requests.discord.validation.AccountDiscordConstraints.DiscordConstraint;
import com.gameserver.utils.account.rest.requests.discord.validation.AccountDiscordConstraints.KeyConstraint;
import com.gameserver.utils.account.service.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyDiscordConnectionRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class); // TODO - @Slf4j annotation instead

    @KeyConstraint
    private final String key;

    @DiscordConstraint
    private final String discord;

    public KeyDiscordConnectionRequest(String key, String discordId) {
        LOGGER.info("Received new KeyDiscordConnection request for key serial number: [{}] and discord id: [{}]",
                key, discordId);

        this.key = key;
        this.discord = discordId;
    }

    public String getKey() {
        return key;
    }

    public String getDiscordId() {
        return discord;
    }

    @Override
    public String toString() {
        return "KeyDiscordConnectionRequest{" +
                "key='" + key + '\'' +
                ", discordId='" + discord + '\'' +
                '}';
    }
}
