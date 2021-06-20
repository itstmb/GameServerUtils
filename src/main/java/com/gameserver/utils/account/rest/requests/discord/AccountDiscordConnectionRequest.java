package com.gameserver.utils.account.rest.requests.discord;

import com.gameserver.utils.account.rest.requests.discord.validation.AccountDiscordConstraints.DiscordConstraint;
import com.gameserver.utils.account.rest.requests.discord.validation.AccountDiscordConstraints.UsernameConstraint;
import com.gameserver.utils.account.service.AccountServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountDiscordConnectionRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class); // TODO - @Slf4j annotation instead

    @UsernameConstraint
    private final String username;

    @DiscordConstraint
    private final String discord;

    public AccountDiscordConnectionRequest(String username, String discordId) {
        LOGGER.info("Received new AccountDiscordConnection request for account with username: [{}] and discord id: [{}]",
                username, discordId);

        this.username = username;
        this.discord = discordId;
    }

    public String getUsername() {
        return username;
    }

    public String getDiscordId() {
        return discord;
    }

    @Override
    public String toString() {
        return "AccountDiscordConnectionRequest{" +
                "username='" + username + '\'' +
                ", discordId='" + discord + '\'' +
                '}';
    }
}
