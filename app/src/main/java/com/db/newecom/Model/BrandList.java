package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BrandList implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("brand_name")
    private String brand_name;

    @SerializedName("brand_image")
    private String brand_image;

    @SerializedName("brand_image_thumb")
    private String brand_image_thumb;

    public BrandList(String id, String brand_name, String brand_image, String brand_image_thumb) {
        this.id = id;
        this.brand_name = brand_name;
        this.brand_image = brand_image;
        this.brand_image_thumb = brand_image_thumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_image() {
        return brand_image;
    }

    public void setBrand_image(String brand_image) {
        this.brand_image = brand_image;
    }

    public String getBrand_image_thumb() {
        return brand_image_thumb;
    }

    public void setBrand_image_thumb(String brand_image_thumb) {
        this.brand_image_thumb = brand_image_thumb;
    }
}
