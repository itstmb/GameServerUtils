package com.gameserver.utils.ranking.service;

import com.gameserver.utils.account.service.AccountDiscordService;
import com.gameserver.utils.ranking.entity.CharacterData;
import com.gameserver.utils.ranking.entity.CharacterRanking;
import com.gameserver.utils.ranking.rest.responses.RankingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.math.NumberUtils.min;

@Service
public class RankingServiceImpl implements RankingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RankingServiceImpl.class); // TODO - @Slf4j annotation instead

    private final RankingDataService rankingDataService;
    private final AccountDiscordService accountDiscordService;

    @Autowired
    public RankingServiceImpl(RankingDataService rankingDataService, AccountDiscordService accountDiscordService) {
        this.rankingDataService = rankingDataService;
        this.accountDiscordService = accountDiscordService;
    }

    @Override
    public RankingResponse getRanking(String type) {
        LOGGER.info("Received new request of type [GetRankingRequest] for type [{}]", type);
        Map<Integer, CharacterRanking> result = buildRankingTable(rankingDataService.getRankingTable(type), 1);
        LOGGER.info("GetRankingRequest responded with [{}] entries", result.size());
        return new RankingResponse(200, result);
    }

    @Override
    public RankingResponse getRankingIndex(String type, int startIndex, int endIndex, boolean discord) {
        LOGGER.info("Received new request of type [GetRankingIndexRequest] for type [{}], indexes [{}-{}]", type, startIndex, endIndex);
        List<Integer> rankingOrder = rankingDataService.getRankingTable(type);
        if (startIndex > rankingOrder.size() || startIndex > endIndex) {
            LOGGER.info("GetRankingIndexRequest startIndex [{}] was higher than rankingOrder size [{}] or endIndex [{}]", startIndex, rankingOrder.size(), endIndex);
            return new RankingResponse(200, null);
        }
        int iterationEndIndex = min(endIndex, rankingOrder.size());
        List<Integer> charList = rankingOrder.subList(startIndex-1, iterationEndIndex);
        Map result = discord ? buildRankingTableMetadata(charList, startIndex) : buildRankingTable(charList, startIndex);
        LOGGER.info("GetRankingIndexRequest responded with [{}] entries", result.size());
        return new RankingResponse(200, result);
    }

    @Override
    public RankingResponse getCharacterRanking(String type, String name) {
        LOGGER.info("Received new request of type [GetCharacterRankingRequest] for type [{}], name [{}] ", type, name);
        List<Integer> rankingOrder = rankingDataService.getRankingTable(type);
        Integer characterId = rankingDataService.getCharacterId(name);
        if (characterId == null) {
            LOGGER.info("GetCharacterRankingRequest couldn't find a character with name [{}]", name);
            return new RankingResponse(200, null);
        }

        int characterRank = rankingOrder.indexOf(characterId);
        if (characterRank == -1) {
            LOGGER.info("GetCharacterRankingRequest couldn't find character [{}] in rankingOrder of type [{}]", name, type);
            return new RankingResponse(200, null);
        }
        Map<Integer, CharacterRanking> result = buildRankingTable(rankingOrder.subList(characterRank, characterRank+1),
                                                                  characterRank+1);
        LOGGER.info("GetCharacterRankingRequest responded with [{}] entries", result.size());
        return new RankingResponse(200, result);
    }

    private Map<Integer, CharacterRanking> buildRankingTable(List<Integer> rankingTable, int startIndex) {
        Map<Integer, CharacterRanking> result = new HashMap<>();
        for (Integer characterId : rankingTable) {
            result.put(startIndex++, rankingDataService.getCharacterRanking(characterId));
        }
        return result;
    }

    private Map<Integer, CharacterData> buildRankingTableMetadata(List<Integer> rankingTable, int startIndex) {
        Map<Integer, CharacterData> result = new HashMap<>();
        CharacterRanking characterRanking;
        for (Integer characterId : rankingTable) {
            characterRanking = rankingDataService.getCharacterRanking(characterId);
            result.put(startIndex++, new CharacterData(characterRanking, (String)accountDiscordService.getDiscord(characterRanking.getName()).getData()));
        }
        return result;
    }
}
