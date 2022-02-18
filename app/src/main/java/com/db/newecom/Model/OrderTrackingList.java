package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderTrackingList implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("status_title")
    private String status_title;

    @SerializedName("status_desc")
    private String status_desc;

    @SerializedName("datetime")
    private String datetime;

    @SerializedName("is_status")
    private String is_status;

    @SerializedName("is_delivered")
    private String is_delivered;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus_title() {
        return status_title;
    }

    public void setStatus_title(String status_title) {
        this.status_title = status_title;
    }

    public String getStatus_desc() {
        return status_desc;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getIs_status() {
        return is_status;
    }

    public void setIs_status(String is_status) {
        this.is_status = is_status;
    }

    public String getIs_delivered() {
        return is_delivered;
    }

    public void setIs_delivered(String is_delivered) {
        this.is_delivered = is_delivered;
    }

}
