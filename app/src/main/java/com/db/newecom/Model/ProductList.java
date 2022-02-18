package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductList implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("sub_category_id")
    private String sub_category_id;

    @SerializedName("category_name")
    private String category_name;

    @SerializedName("sub_category_name")
    private String sub_category_name;

    @SerializedName("total_views")
    private String total_views;

    @SerializedName("brand_id")
    private String brand_id;

    @SerializedName("product_title")
    private String product_title;

    @SerializedName("product_desc")
    private String product_desc;

    @SerializedName("product_image")
    private String product_image;

    @SerializedName("product_image_portrait")
    private String product_image_portrait;

    @SerializedName("product_image_square")
    private String product_image_square;

    @SerializedName("product_mrp")
    private String product_mrp;

    @SerializedName("product_sell_price")
    private String product_sell_price;

    @SerializedName("you_save")
    private String you_save;

    @SerializedName("you_save_per")
    private String you_save_per;

    @SerializedName("total_rate")
    private String total_rate;

    @SerializedName("rate_avg")
    private String rate_avg;

    @SerializedName("product_status")
    private String product_status;

    @SerializedName("product_status_lbl")
    private String product_status_lbl;

    @SerializedName("product_qty")
    private String product_qty;

    @SerializedName("product_default_size")
    private String product_default_size;

    @SerializedName("is_favorite")
    private String is_favorite;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_image_portrait() {
        return product_image_portrait;
    }

    public void setProduct_image_portrait(String product_image_portrait) {
        this.product_image_portrait = product_image_portrait;
    }

    public String getProduct_image_square() {
        return product_image_square;
    }

    public void setProduct_image_square(String product_image_square) {
        this.product_image_square = product_image_square;
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

    public String getTotal_rate() {
        return total_rate;
    }

    public void setTotal_rate(String total_rate) {
        this.total_rate = total_rate;
    }

    public String getRate_avg() {
        return rate_avg;
    }

    public void setRate_avg(String rate_avg) {
        this.rate_avg = rate_avg;
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getTotal_views() {
        return total_views;
    }

    public void setTotal_views(String total_views) {
        this.total_views = total_views;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public void setProduct_status_lbl(String product_status_lbl) {
        this.product_status_lbl = product_status_lbl;
    }

    public String getProduct_default_size() {
        return product_default_size;
    }

    public void setProduct_default_size(String product_default_size) {
        this.product_default_size = product_default_size;
    }

    public String getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }
}
