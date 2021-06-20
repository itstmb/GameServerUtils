package com.gameserver.utils.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "game_world", name = "character")
public class CCharacter {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "account_id")
    private int accountId;

    @Column(name = "name")
    private String name;

    public CCharacter() {
    }

    public int getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }
}
