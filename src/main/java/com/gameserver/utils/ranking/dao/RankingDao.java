package com.gameserver.utils.ranking.dao;

import com.gameserver.utils.ranking.entity.CharacterRanking;

import java.util.List;

public interface RankingDao {
    List<CharacterRanking> getCharacterData();

    List<Integer> getRankingOrder(String type);
}
