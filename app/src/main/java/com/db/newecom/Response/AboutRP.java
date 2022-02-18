package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AboutRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("app_logo")
    private String app_logo;

    @SerializedName("app_author")
    private String app_author;

    @SerializedName("app_contact")
    private String app_contact;

    @SerializedName("app_email")
    private String app_email;

    @SerializedName("app_website")
    private String app_website;

    @SerializedName("app_description")
    private String app_description;

    @SerializedName("facebook")
    private String facebook;

    @SerializedName("twitter")
    private String twitter;

    @SerializedName("instagram")
    private String instagram;

    @SerializedName("youtube")
    private String youtube;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApp_logo() {
        return app_logo;
    }

    public void setApp_logo(String app_logo) {
        this.app_logo = app_logo;
    }

    public String getApp_author() {
        return app_author;
    }

    public void setApp_author(String app_author) {
        this.app_author = app_author;
    }

    public String getApp_contact() {
        return app_contact;
    }

    public void setApp_contact(String app_contact) {
        this.app_contact = app_contact;
    }

    public String getApp_email() {
        return app_email;
    }

    public void setApp_email(String app_email) {
        this.app_email = app_email;
    }

    public String getApp_website() {
        return app_website;
    }

    public void setApp_website(String app_website) {
        this.app_website = app_website;
    }

    public String getApp_description() {
        return app_description;
    }

    public void setApp_description(String app_description) {
        this.app_description = app_description;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }
}
