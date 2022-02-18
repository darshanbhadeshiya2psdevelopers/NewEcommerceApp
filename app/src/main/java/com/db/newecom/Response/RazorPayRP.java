package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RazorPayRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("razorpay_key_id")
    private String razorpay_key_id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String image;

    @SerializedName("order_id")
    private String order_id;

    @SerializedName("theme_color")
    private String theme_color;

    @SerializedName("currency")
    private String currency;

    @SerializedName("amount")
    private String amount;

    @SerializedName("email")
    private String email;

    @SerializedName("contact")
    private String contact;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public String getRazorpay_key_id() {
        return razorpay_key_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getTheme_color() {
        return theme_color;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAmount() {
        return amount;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

}
