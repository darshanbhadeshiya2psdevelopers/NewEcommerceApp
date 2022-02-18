package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RemoveCartRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("cart_empty_msg")
    private String cart_empty_msg;

    @SerializedName("total_item")
    private String total_item;

    @SerializedName("price")
    private String price;

    @SerializedName("delivery_charge")
    private String delivery_charge;

    @SerializedName("payable_amt")
    private String payable_amt;

    @SerializedName("you_save_msg")
    private String you_save_msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCart_empty_msg() {
        return cart_empty_msg;
    }

    public void setCart_empty_msg(String cart_empty_msg) {
        this.cart_empty_msg = cart_empty_msg;
    }

    public String getTotal_item() {
        return total_item;
    }

    public void setTotal_item(String total_item) {
        this.total_item = total_item;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getPayable_amt() {
        return payable_amt;
    }

    public void setPayable_amt(String payable_amt) {
        this.payable_amt = payable_amt;
    }

    public String getYou_save_msg() {
        return you_save_msg;
    }

    public void setYou_save_msg(String you_save_msg) {
        this.you_save_msg = you_save_msg;
    }
}
