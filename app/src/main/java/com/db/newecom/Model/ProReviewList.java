package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProReviewList implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("user_name")
    private String user_name;

    @SerializedName("user_image")
    private String user_image;

    @SerializedName("user_rate")
    private String user_rate;

    @SerializedName("rate_desc")
    private String rate_desc;

    @SerializedName("rate_date")
    private String rate_date;

    public String getId() {
        return id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getUser_rate() {
        return user_rate;
    }

    public String getRate_desc() {
        return rate_desc;
    }

    public String getRate_date() {
        return rate_date;
    }
}
