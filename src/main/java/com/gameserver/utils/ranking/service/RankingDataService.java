package com.gameserver.utils.ranking.service;

import com.gameserver.utils.ranking.entity.CharacterRanking;

import java.util.List;

public interface RankingDataService {
    void updateRanking();

    CharacterRanking getCharacterRanking(Integer id);

    Integer getCharacterId(String name);

    List<Integer> getRankingTable(String type);
}
