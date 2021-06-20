package com.gameserver.utils.ranking.rest.responses;

import com.gameserver.utils.common.rest.responses.Response;

import java.util.Map;

public class RankingResponse extends Response {

    private Map data;

    public RankingResponse(int status, Map data) {
        super(status);
        this.data = data;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RankingResponse{" +
                "status=" + status +
                "data=" + data.toString() +
                ", timestamp=" + timeStamp +
                "}";
    }
}
