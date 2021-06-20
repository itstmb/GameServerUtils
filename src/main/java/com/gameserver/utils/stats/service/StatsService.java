package com.gameserver.utils.stats.service;

import com.gameserver.utils.stats.rest.responses.PlayerCountResponse;

public interface StatsService {
    PlayerCountResponse getPlayerCount();
}
