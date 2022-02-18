package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OfferList implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("offer_title")
    private String offer_title;

    @SerializedName("offer_image")
    private String offer_image;

    @SerializedName("offer_image_thumb")
    private String offer_image_thumb;

    public OfferList(String id, String offer_title, String offer_image, String offer_image_thumb) {
        this.id = id;
        this.offer_title = offer_title;
        this.offer_image = offer_image;
        this.offer_image_thumb = offer_image_thumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOffer_title() {
        return offer_title;
    }

    public void setOffer_title(String offer_title) {
        this.offer_title = offer_title;
    }

    public String getOffer_image() {
        return offer_image;
    }

    public void setOffer_image(String offer_image) {
        this.offer_image = offer_image;
    }

    public String getOffer_image_thumb() {
        return offer_image_thumb;
    }

    public void setOffer_image_thumb(String offer_image_thumb) {
        this.offer_image_thumb = offer_image_thumb;
    }
}
