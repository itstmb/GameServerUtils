package com.gameserver.utils.account.rest.responses;

import com.gameserver.utils.common.rest.responses.Response;

public class AccountErrorResponse extends Response {

    private String message;

    public AccountErrorResponse(int status, String message) {
        super(status);
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String toString() { // for debugging
        return "AccountErrorResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", timestamp=" + timeStamp +
                "}";
    }
}
