package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentSubmitRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("title")
    private String title;

    @SerializedName("thank_you_msg")
    private String thank_you_msg;

    @SerializedName("ord_confirm_msg")
    private String ord_confirm_msg;

    @SerializedName("order_unique_id")
    private String order_unique_id;

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

    public String getTitle() {
        return title;
    }

    public String getThank_you_msg() {
        return thank_you_msg;
    }

    public String getOrd_confirm_msg() {
        return ord_confirm_msg;
    }

    public String getOrder_unique_id() {
        return order_unique_id;
    }

}
