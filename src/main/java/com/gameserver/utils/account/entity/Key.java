package com.gameserver.utils.account.entity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@DynamicUpdate
@Table(schema = "gameserver_utils", name = "key")
public class Key {

    @Id
    @Column(name="id")
    private int id;

    @Column(name="serial_number", nullable = false, unique = true)
    private String serialNumber;

    @Column(name="account_id")
    private Integer accountId;

    public Key() {
    }

    public Key(int id, String serialNumber, Integer accountId) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.accountId = accountId;
    }

    public int getId() {
        return id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
