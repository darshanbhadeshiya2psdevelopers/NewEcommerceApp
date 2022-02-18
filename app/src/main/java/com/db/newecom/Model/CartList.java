package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartList implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("product_status")
    private String product_status;

    @SerializedName("product_status_lbl")
    private String product_status_lbl;

    @SerializedName("product_cart_qty")
    private String product_cart_qty;

    @SerializedName("product_qty")
    private String product_qty;

    @SerializedName("product_size")
    private String product_size;

    @SerializedName("product_title")
    private String product_title;

    @SerializedName("product_image")
    private String product_image;

    @SerializedName("product_mrp")
    private String product_mrp;

    @SerializedName("product_sell_price")
    private String product_sell_price;

    @SerializedName("you_save")
    private String you_save;

    @SerializedName("you_save_per")
    private String you_save_per;

    @SerializedName("max_unit_buy")
    private String max_unit_buy;

    @SerializedName("original_sell_price")
    private String original_sell_price;

    @SerializedName("original_price")
    private String original_price;

    public CartList(String id, String product_id, String user_id, String product_status, String product_status_lbl, String product_cart_qty, String product_qty, String product_size, String product_title, String product_image, String product_mrp, String product_sell_price, String you_save, String you_save_per, String max_unit_buy, String original_price, String original_sell_price) {
        this.id = id;
        this.product_id = product_id;
        this.user_id = user_id;
        this.product_status = product_status;
        this.product_status_lbl = product_status_lbl;
        this.product_cart_qty = product_cart_qty;
        this.product_qty = product_qty;
        this.product_size = product_size;
        this.product_title = product_title;
        this.product_image = product_image;
        this.product_mrp = product_mrp;
        this.product_sell_price = product_sell_price;
        this.you_save = you_save;
        this.you_save_per = you_save_per;
        this.max_unit_buy = max_unit_buy;
        this.original_sell_price = original_sell_price;
        this.original_price = original_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_status() {
        return product_status;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }

    public String getProduct_status_lbl() {
        return product_status_lbl;
    }

    public void setProduct_status_lbl(String product_status_lbl) {
        this.product_status_lbl = product_status_lbl;
    }

    public String getProduct_cart_qty() {
        return product_cart_qty;
    }

    public void setProduct_cart_qty(String product_cart_qty) {
        this.product_cart_qty = product_cart_qty;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
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

    public String getMax_unit_buy() {
        return max_unit_buy;
    }

    public void setMax_unit_buy(String max_unit_buy) {
        this.max_unit_buy = max_unit_buy;
    }

    public String getOriginal_sell_price() {
        return original_sell_price;
    }

    public void setOriginal_sell_price(String original_sell_price) {
        this.original_sell_price = original_sell_price;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }
}
