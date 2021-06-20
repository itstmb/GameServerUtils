package com.gameserver.utils.ranking.service;

import com.gameserver.utils.ranking.dao.RankingDao;
import com.gameserver.utils.ranking.entity.CharacterRanking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@EnableScheduling
public class RankingDataServiceImpl implements RankingDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RankingDataServiceImpl.class); // TODO - @Slf4j annotation instead

    private final RankingDao rankingDao;
    private final HashMap<Integer, CharacterRanking> charactersData;
    private final HashMap<String, Integer> charactersNameToId;
    private final Map<String,List<Integer>> rankings;
    private final List<String> rankingTypes;

    @Autowired
    public RankingDataServiceImpl(RankingDao rankingDao, List<String> rankingTypes) {
        this.rankingDao = rankingDao;
        this.charactersData = new HashMap<>();
        this.charactersNameToId = new HashMap<>();
        this.rankings = new HashMap<>();
        this.rankingTypes = rankingTypes;
    }

    @Scheduled(fixedRateString = "${gameserverutils.ranking.cycle}")
    public void updateRanking() {
        LOGGER.trace("Updating ranking tables...");
        StopWatch sw = new StopWatch();
        sw.start();

        updateCharacterData();
        for (String type : rankingTypes) {
            updateRankingOrderTable(type);
        }

        sw.stop();
        LOGGER.info("Ranking tables successfully updated (took {}ms)", sw.getTotalTimeMillis());
    }

    private void updateCharacterData() {
        List<CharacterRanking> charactersData = rankingDao.getCharacterData();
        for (CharacterRanking characterData : charactersData) {
            this.charactersData.put(characterData.getId(), characterData);
            this.charactersNameToId.put(characterData.getName().toLowerCase(), characterData.getId());
        }
    }

    private void updateRankingOrderTable(String type) {
        this.rankings.put(type, rankingDao.getRankingOrder(type));
    }

    public CharacterRanking getCharacterRanking(Integer id) {
        return this.charactersData.get(id);
    }

    public Integer getCharacterId(String name) {
        return this.charactersNameToId.get(name);
    }

    public List<Integer> getRankingTable(String type) {
        return this.rankings.get(type);
    }
}
