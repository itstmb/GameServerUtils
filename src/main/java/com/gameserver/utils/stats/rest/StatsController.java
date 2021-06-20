package com.gameserver.utils.stats.rest;

import com.gameserver.utils.stats.rest.responses.PlayerCountResponse;
import com.gameserver.utils.stats.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/stats")
public class StatsController {

    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @RequestMapping(value = "/playerCount", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    PlayerCountResponse playerCount() {
        return statsService.getPlayerCount();
    }
}
