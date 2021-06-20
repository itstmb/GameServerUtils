package com.gameserver.utils.account.rest.requests.register;

import com.gameserver.utils.account.rest.requests.discord.validation.AccountDiscordConstraints.DiscordConstraint;

import java.util.Map;

import static com.gameserver.utils.account.rest.requests.register.validation.AccountRegisterConstraints.KeyConstraint;

public class AccountRegisterWithDiscordAndKeyRequest extends AccountRegisterRequest {

    @DiscordConstraint
    private final String discord;

    @KeyConstraint
    private final String key;

    public AccountRegisterWithDiscordAndKeyRequest(Map<String, Object> account, String discord, String key) {
        super(account);
        this.discord = discord;
        this.key = key;
    }

    public String getDiscord() {
        return discord;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "AccountRegisterWithDiscordRequest{" +
                super.toString() +
                ", discord='" + discord + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
