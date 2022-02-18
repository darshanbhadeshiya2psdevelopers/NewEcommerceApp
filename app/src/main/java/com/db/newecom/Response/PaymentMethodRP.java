package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentMethodRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("cod_status")
    private String cod_status;

    @SerializedName("paypal_status")
    private String paypal_status;

    @SerializedName("stripe_status")
    private String stripe_status;

    @SerializedName("razorpay_status")
    private String razorpay_status;

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

    public String getCod_status() {
        return cod_status;
    }

    public String getPaypal_status() {
        return paypal_status;
    }

    public String getStripe_status() {
        return stripe_status;
    }

    public String getRazorpay_status() {
        return razorpay_status;
    }

}
