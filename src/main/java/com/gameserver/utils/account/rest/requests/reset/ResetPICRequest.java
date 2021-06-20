package com.gameserver.utils.account.rest.requests.reset;

import com.gameserver.utils.account.rest.requests.register.validation.AccountRegisterConstraints.BirthdayConstraint;
import com.gameserver.utils.account.rest.requests.reset.validation.AccountResetConstraints.DiscordConstraint;
import com.gameserver.utils.account.rest.requests.reset.validation.AccountResetConstraints.PICConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResetPICRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResetPICRequest.class); // TODO - @Slf4j annotation instead

    @DiscordConstraint
    private final String discord;

    @BirthdayConstraint
    private final String birthday;

    @PICConstraint
    private final String newPIC;

    public ResetPICRequest(String discord, String birthday, String newPIC) {
        LOGGER.info("Received new request of type [{}] for account with discord [{}]",
                this.getClass().getSimpleName(), discord);
        this.discord = discord;
        this.birthday = birthday;
        this.newPIC = newPIC;
    }

    public String getDiscordId() {
        return discord;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getNewPIC() {
        return newPIC;
    }

    public String toString() {
        return "ResetPICRequest{" +
                "discordId='" + discord + '\'' +
                ", birthday='" + birthday + '\'' +
                ", PIC='" + newPIC + '\'' +
                '}';
    }

}
