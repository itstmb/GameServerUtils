package com.gameserver.utils.stats.rest.responses;

import com.gameserver.utils.common.rest.responses.Response;

public class PlayerCountResponse extends Response {

    private final String serverStatus;
    private final int playerCount;

    public PlayerCountResponse(int status, String serverStatus, int playerCount) {
        super(status);
        this.serverStatus = serverStatus;
        this.playerCount = playerCount;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public String getServerStatus() {
        return serverStatus;
    }

    @Override
    public String toString() {
        return "PlayerCountResponse{" +
                "status=" + status +
                ", timestamp=" + timeStamp +
                ", serverStatus=" + serverStatus +
                ", playerCount=" + playerCount +
                "}";
    }
}
