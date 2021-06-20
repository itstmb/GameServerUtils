package com.gameserver.utils.account.rest.requests.reset;

import com.gameserver.utils.account.rest.requests.reset.validation.AccountResetConstraints.*;
import com.gameserver.utils.account.rest.requests.register.validation.AccountRegisterConstraints.PasswordConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResetPasswordRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetPasswordRequest.class); // TODO - @Slf4j annotation instead

    @UsernameConstraint
    private final String username;

    @DiscordConstraint
    private final String discord;

    @PasswordConstraint
    private final String newPassword;

    public ResetPasswordRequest(String username, String discord, String newPassword) {
        LOGGER.info("Received new request of type [{}] for account with username [{}]",
                this.getClass().getSimpleName(), username);
        this.username = username;
        this.discord = discord;
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getDiscordId() {
        return discord;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String toString() {
        return "ResetPasswordRequest{" +
            "username='" + username + '\'' +
            ", discordId='" + discord + '\'' +
            ", password='" + newPassword + '\'' +
            '}';
    }
}
