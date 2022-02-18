package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RemoveCouponRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("coupon_id")
    private String coupon_id;

    @SerializedName("price")
    private String price;

    @SerializedName("payable_amt")
    private String payable_amt;

    @SerializedName("you_save_msg")
    private String you_save_msg;

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

    public String getCoupon_id() {
        return coupon_id;
    }

    public String getPrice() {
        return price;
    }

    public String getPayable_amt() {
        return payable_amt;
    }

    public String getYou_save_msg() {
        return you_save_msg;
    }

}
