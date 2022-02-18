package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubCategoryList implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("sub_category_name")
    private String sub_category_name;

    @SerializedName("sub_category_image")
    private String sub_category_image;

    @SerializedName("sub_category_image_thumb")
    private String sub_category_image_thumb;

    @SerializedName("category_id")
    private String category_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getSub_category_image() {
        return sub_category_image;
    }

    public void setSub_category_image(String sub_category_image) {
        this.sub_category_image = sub_category_image;
    }

    public String getSub_category_image_thumb() {
        return sub_category_image_thumb;
    }

    public void setSub_category_image_thumb(String sub_category_image_thumb) {
        this.sub_category_image_thumb = sub_category_image_thumb;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
