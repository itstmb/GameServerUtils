package com.gameserver.utils.ranking.rest;

import com.gameserver.utils.ranking.rest.responses.RankingResponse;
import com.gameserver.utils.ranking.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping(value = "/api/ranking")
public class RankingController {

    final RankingService rankingService;

    @Autowired
    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @RequestMapping(value = "/{type}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    RankingResponse getRanking(@PathVariable String type) {
        return rankingService.getRanking(type);
    }

    @RequestMapping(value = "/{type}/{index}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    RankingResponse getRanking(@PathVariable String type,
                               @PathVariable("index") @Positive Integer index) {
        return rankingService.getRankingIndex(type, index, index, false);
    }

    @RequestMapping(value = "/{type}/{startIndex}/{endIndex}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    RankingResponse getRanking(@PathVariable("type") String type,
                               @PathVariable("startIndex") @Positive Integer startIndex,
                               @PathVariable("endIndex") @Positive Integer endIndex,
                               @RequestParam(required = false) boolean discord) {
        return rankingService.getRankingIndex(type, startIndex, endIndex, discord);
    }

    @RequestMapping(value = "{type}/name/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    RankingResponse getCharacterRanking(@PathVariable("type") String type,
                                        @PathVariable("name") String name) {
        return rankingService.getCharacterRanking(type, name.toLowerCase());
    }
}
