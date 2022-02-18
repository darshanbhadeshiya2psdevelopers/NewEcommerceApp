package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProImageList implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("product_image")
    private String product_image;

    @SerializedName("product_image_thumb")
    private String product_image_thumb;

    @SerializedName("product_image_portrait")
    private String product_image_portrait;

    public ProImageList(String id, String product_image, String product_image_thumb, String product_image_portrait) {
        this.id = id;
        this.product_image = product_image;
        this.product_image_thumb = product_image_thumb;
        this.product_image_portrait = product_image_portrait;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_image_thumb() {
        return product_image_thumb;
    }

    public String getProduct_image_portrait() {
        return product_image_portrait;
    }

    public void setProduct_image_portrait(String product_image_portrait) {
        this.product_image_portrait = product_image_portrait;
    }

    public void setProduct_image_thumb(String product_image_thumb) {
        this.product_image_thumb = product_image_thumb;
    }
}
