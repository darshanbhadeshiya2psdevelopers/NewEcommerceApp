package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SliderList implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("banner_title")
    private String banner_title;

    @SerializedName("banner_image")
    private String banner_image;

    public String getId() {
        return id;
    }

    public String getBanner_title() {
        return banner_title;
    }

    public String getBanner_image() {
        return banner_image;
    }
}
