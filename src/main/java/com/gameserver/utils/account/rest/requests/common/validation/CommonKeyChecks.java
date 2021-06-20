package com.gameserver.utils.account.rest.requests.common.validation;

import com.gameserver.utils.account.dao.KeyDao;
import com.gameserver.utils.account.entity.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonKeyChecks {

    private static final String SERIAL_NUMBER_REGEX = "[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}";

    private final KeyDao keyDao;

    @Autowired
    public CommonKeyChecks(KeyDao keyDao) {
        this.keyDao = keyDao;
    }

    public boolean isInvalidFormat(String serialNumber) {
        return !serialNumber.matches(SERIAL_NUMBER_REGEX);
    }

    public boolean isNonexistentKey(String serialNumber) {
        return keyDao.getBySerialNumber(serialNumber) == null;
    }

    public boolean isTaken(String serialNumber) {
        Key key = keyDao.getBySerialNumber(serialNumber);
        return key.getAccountId() != null;
    }
}
