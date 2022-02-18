package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateCartRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("product_mrp")
    private String product_mrp;

    @SerializedName("product_sell_price")
    private String product_sell_price;

    @SerializedName("you_save")
    private String you_save;

    @SerializedName("you_save_per")
    private String you_save_per;

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

    @SerializedName("product_size")
    private String product_size;

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

    public String getProduct_mrp() {
        return product_mrp;
    }

    public void setProduct_mrp(String product_mrp) {
        this.product_mrp = product_mrp;
    }

    public String getProduct_sell_price() {
        return product_sell_price;
    }

    public void setProduct_sell_price(String product_sell_price) {
        this.product_sell_price = product_sell_price;
    }

    public String getYou_save() {
        return you_save;
    }

    public void setYou_save(String you_save) {
        this.you_save = you_save;
    }

    public String getYou_save_per() {
        return you_save_per;
    }

    public void setYou_save_per(String you_save_per) {
        this.you_save_per = you_save_per;
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

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }
}
