package com.gameserver.utils.account.rest.requests.common.validation;

import org.springframework.stereotype.Component;

@Component
public class CommonChecks {

    public boolean isMissing(String field) {
        return field == null || field.isEmpty();
    }
}
