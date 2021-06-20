package com.gameserver.utils.ranking.service;

import com.gameserver.utils.ranking.rest.responses.RankingResponse;

public interface RankingService {
    RankingResponse getRanking(String type);

    RankingResponse getRankingIndex(String type, int startIndex, int endIndex, boolean discord);

    RankingResponse getCharacterRanking(String type, String name);
}
