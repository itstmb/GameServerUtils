package com.gameserver.utils.account.dao;

import com.gameserver.utils.account.entity.Key;

public interface KeyDao {

    void save(Key key) throws RuntimeException;

    Key getBySerialNumber(String serialNumber);
}
