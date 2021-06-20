package com.gameserver.utils.account.rest.responses;

import com.gameserver.utils.common.rest.responses.Response;

public class AccountResponse extends Response {

    private Object data;

    public AccountResponse(int status, Object data) {
        super(status);
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toString() {
        return "AccountResponse{" +
                "status=" + status +
                ", data='" + data + '\'' +
                ", timestamp=" + timeStamp +
                "}";
    }
}
