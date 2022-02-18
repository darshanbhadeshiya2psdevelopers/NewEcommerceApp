package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProColorList implements Serializable {

    @SerializedName("color_id")
    private String color_id;

    @SerializedName("color_code")
    private String color_code;

    @SerializedName("color_name")
    private String color_name;

    @SerializedName("color_image")
    private String color_image;

    @SerializedName("product_name")
    private String product_name;

    public ProColorList(String color_id, String color_code, String color_name, String color_image, String product_name) {
        this.color_id = color_id;
        this.color_code = color_code;
        this.color_name = color_name;
        this.color_image = color_image;
        this.product_name = product_name;
    }

    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getColor_image() {
        return color_image;
    }

    public void setColor_image(String color_image) {
        this.color_image = color_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
