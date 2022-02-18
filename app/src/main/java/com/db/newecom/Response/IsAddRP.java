package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IsAddRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("is_address_avail")
    private String is_address_avail;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIs_address_avail() {
        return is_address_avail;
    }

    public void setIs_address_avail(String is_address_avail) {
        this.is_address_avail = is_address_avail;
    }
}
