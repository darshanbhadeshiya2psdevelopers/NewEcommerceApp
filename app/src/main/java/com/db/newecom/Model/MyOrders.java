package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyOrders implements Serializable {

    @SerializedName("order_id")
    private String order_id;

    @SerializedName("order_unique_id")
    private String order_unique_id;

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("product_title")
    private String product_title;

    @SerializedName("product_image")
    private String product_image;

    @SerializedName("order_status")
    private String order_status;

    @SerializedName("current_order_status")
    private String current_order_status;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_unique_id() {
        return order_unique_id;
    }

    public void setOrder_unique_id(String order_unique_id) {
        this.order_unique_id = order_unique_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getCurrent_order_status() {
        return current_order_status;
    }

    public void setCurrent_order_status(String current_order_status) {
        this.current_order_status = current_order_status;
    }
}
