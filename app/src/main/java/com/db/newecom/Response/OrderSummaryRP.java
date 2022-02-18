package com.db.newecom.Response;

import com.db.newecom.Model.CartList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderSummaryRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("address_id")
    private String address_id;

    @SerializedName("cart_ids")
    private String cart_ids;

    @SerializedName("address")
    private String address;

    @SerializedName("name")
    private String name;

    @SerializedName("address_type")
    private String address_type;

    @SerializedName("mobile_no")
    private String mobile_no;

    @SerializedName("total_item")
    private String total_item;

    @SerializedName("cart_items")
    private String cart_items;

    @SerializedName("price")
    private String price;

    @SerializedName("delivery_charge")
    private String delivery_charge;

    @SerializedName("payable_amt")
    private String payable_amt;

    @SerializedName("you_save")
    private String you_save;

    @SerializedName("you_save_msg")
    private String you_save_msg;

    @SerializedName("is_applied_coupon")
    private boolean is_applied_coupon;

    @SerializedName("coupon_id")
    private String coupon_id;

    @SerializedName("order_summary")
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

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getCart_ids() {
        return cart_ids;
    }

    public void setCart_ids(String cart_ids) {
        this.cart_ids = cart_ids;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getTotal_item() {
        return total_item;
    }

    public void setTotal_item(String total_item) {
        this.total_item = total_item;
    }

    public String getCart_items() {
        return cart_items;
    }

    public void setCart_items(String cart_items) {
        this.cart_items = cart_items;
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

    public String getYou_save() {
        return you_save;
    }

    public void setYou_save(String you_save) {
        this.you_save = you_save;
    }

    public String getYou_save_msg() {
        return you_save_msg;
    }

    public void setYou_save_msg(String you_save_msg) {
        this.you_save_msg = you_save_msg;
    }

    public boolean isIs_applied_coupon() {
        return is_applied_coupon;
    }

    public void setIs_applied_coupon(boolean is_applied_coupon) {
        this.is_applied_coupon = is_applied_coupon;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public List<CartList> getCartLists() {
        return cartLists;
    }

    public void setCartLists(List<CartList> cartLists) {
        this.cartLists = cartLists;
    }
}
