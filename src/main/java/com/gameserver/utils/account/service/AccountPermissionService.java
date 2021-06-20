package com.gameserver.utils.account.service;

import java.util.Map;

public interface AccountPermissionService {

    Map<String, Object> getDiscordVerifiedAccounts();

    void updateServiceData();
}
