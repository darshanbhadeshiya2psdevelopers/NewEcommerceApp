package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryList implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("category_name")
    private String category_name;

    @SerializedName("category_image")
    private String category_image;

    @SerializedName("category_image_thumb")
    private String category_image_thumb;

    @SerializedName("sub_cat_status")
    private String sub_cat_status;

    public CategoryList(String id, String category_name, String category_image, String category_image_thumb, String sub_cat_status) {
        this.id = id;
        this.category_name = category_name;
        this.category_image = category_image;
        this.category_image_thumb = category_image_thumb;
        this.sub_cat_status = sub_cat_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getCategory_image_thumb() {
        return category_image_thumb;
    }

    public void setCategory_image_thumb(String category_image_thumb) {
        this.category_image_thumb = category_image_thumb;
    }

    public String getSub_cat_status() {
        return sub_cat_status;
    }

    public void setSub_cat_status(String sub_cat_status) {
        this.sub_cat_status = sub_cat_status;
    }
}
