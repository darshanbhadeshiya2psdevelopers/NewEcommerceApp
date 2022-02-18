package com.db.newecom.Response;

import com.db.newecom.Model.CartList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CartRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("address_count")
    private String address_count;

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

    @SerializedName("ECOMMERCE_APP")
    private List<CartList> cartLists;

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

    public String getAddress_count() {
        return address_count;
    }

    public void setAddress_count(String address_count) {
        this.address_count = address_count;
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

    public List<CartList> getCartLists() {
        return cartLists;
    }

    public void setCartLists(List<CartList> cartLists) {
        this.cartLists = cartLists;
    }
}
