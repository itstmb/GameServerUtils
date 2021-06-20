package com.gameserver.utils.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "gameserver_utils", name = "discord")
public class AccountDiscord {

    @Id
    @Column(name="account_id")
    private Integer accountId;

    @Column(name="discord_id", nullable = false, unique = true)
    private String discordId;

    public AccountDiscord() {
    }

    public AccountDiscord(Integer accountId, String discordId) {
        this.accountId = accountId;
        this.discordId = discordId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public String getDiscordId() {
        return discordId;
    }

    @Override
    public String toString() { // for debugging
        return "Discord{" +
                "accountId=" + accountId +
                ", discordId='" + discordId + '\'' +
                '}';
    }
}
