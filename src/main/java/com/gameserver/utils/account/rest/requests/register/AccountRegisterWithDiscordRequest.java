package com.gameserver.utils.account.rest.requests.register;

import com.gameserver.utils.account.rest.requests.discord.validation.AccountDiscordConstraints.DiscordConstraint;

import java.util.Map;

public class AccountRegisterWithDiscordRequest extends AccountRegisterRequest {

    @DiscordConstraint
    private final String discord;

    public AccountRegisterWithDiscordRequest(Map<String, Object> account, String discord) {
        super(account);
        this.discord = discord;
    }

    public String getDiscord() {
        return discord;
    }

    @Override
    public String toString() {
        return "AccountRegisterWithDiscordRequest{" +
                super.toString() +
                ", discord='" + discord + '\'' +
                '}';
    }
}
