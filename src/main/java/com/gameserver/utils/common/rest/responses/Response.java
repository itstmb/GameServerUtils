package com.gameserver.utils.common.rest.responses;

public abstract class Response {

    protected int status;
    protected long timeStamp;

    public Response(int status) {
        this.status = status;
        this.timeStamp = System.currentTimeMillis();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
