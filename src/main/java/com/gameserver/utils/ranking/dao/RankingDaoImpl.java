package com.gameserver.utils.ranking.dao;

import com.gameserver.utils.ranking.entity.CharacterRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.gameserver.utils.ranking.RankingConfig.*;

@Service
public class RankingDaoImpl implements RankingDao {

    private final JdbcTemplate jdbcTemplate;
    private final Map<String, String> rankingQueries;

    @Autowired
    public RankingDaoImpl(JdbcTemplate jdbcTemplate, Map<String, String> rankingQueries) {
        this.jdbcTemplate = jdbcTemplate;
        this.rankingQueries = rankingQueries;
    }

    public List<CharacterRanking> getCharacterData() {
        // TODO: Exception handling, DB down/unreachable
        return jdbcTemplate.query(SELECT_DATA_QUERY, new BeanPropertyRowMapper<>(CharacterRanking.class));
    }

    public List<Integer> getRankingOrder(String type) {
        // TODO: Exception handling, DB down/unreachable
        return jdbcTemplate.queryForList(rankingQueries.get(type), Integer.class);
    }
}
