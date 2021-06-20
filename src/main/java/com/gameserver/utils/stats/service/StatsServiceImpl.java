package com.gameserver.utils.stats.service;

import com.gameserver.utils.stats.rest.responses.PlayerCountResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

@Service
public class StatsServiceImpl implements StatsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsServiceImpl.class); // TODO - @Slf4j annotation instead

    final String gameserverRestUri;
    final RestTemplate restTemplate;

    @Autowired
    public StatsServiceImpl(@Value("${gameserver.restapi.ip}") String gameserverIP,
                            @Value("${gameserver.restapi.port}") int gameserverPort,
                            RestTemplate restTemplate) {
        this.gameserverRestUri = "http://" + gameserverIP + ":" + gameserverPort + "/";
        this.restTemplate = restTemplate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PlayerCountResponse getPlayerCount() {
        LOGGER.info("Received new request of type [GetPlayerCountRequest]");
        try {
            LinkedHashMap<String, Integer> result = restTemplate.getForObject(gameserverRestUri + "/stats/player_count", LinkedHashMap.class);
            PlayerCountResponse playerCountResponse = new PlayerCountResponse(HttpStatus.OK.value(),"online", result != null ? result.get("characters") : 0);
            LOGGER.info("GetPlayerCountRequest response was: [{}]", result);
            return playerCountResponse;
        } catch (Exception ex) {
            LOGGER.info("Could not receive a response from the Game Server RestAPI");
            return new PlayerCountResponse(HttpStatus.OK.value(),"offline", 0);
        }
    }
}
