package com.gameserver.utils.account.rest.requests.register;

import java.util.Map;

import static com.gameserver.utils.account.rest.requests.register.validation.AccountRegisterConstraints.KeyConstraint;

public class AccountRegisterWithKeyRequest extends AccountRegisterRequest {

    @KeyConstraint
    private final String key;

    public AccountRegisterWithKeyRequest(Map<String, Object> account, String key) {
        super(account);
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "AccountRegisterWithKeyRequest{" +
                super.toString() +
                ", key='" + key + '\'' +
                '}';
    }
}
