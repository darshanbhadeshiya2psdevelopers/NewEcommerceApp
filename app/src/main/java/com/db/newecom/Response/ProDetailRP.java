package com.db.newecom.Response;

import com.db.newecom.Model.ProColorList;
import com.db.newecom.Model.ProImageList;
import com.db.newecom.Model.ProReviewList;
import com.db.newecom.Model.ProSizeList;
import com.db.newecom.Model.ProductList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProDetailRP implements Serializable {

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

    @SerializedName("is_favorite")
    private String is_favorite;

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

    @SerializedName("brand_id")
    private String brand_id;

    @SerializedName("product_title")
    private String product_title;

    @SerializedName("product_desc")
    private String product_desc;

    @SerializedName("product_image")
    private String product_image;

    @SerializedName("product_image_square")
    private String product_image_square;

    @SerializedName("product_image_portrait")
    private String product_image_portrait;

    @SerializedName("product_mrp")
    private String product_mrp;

    @SerializedName("product_sell_price")
    private String product_sell_price;

    @SerializedName("you_save")
    private String you_save;

    @SerializedName("you_save_per")
    private String you_save_per;

    @SerializedName("is_color")
    private String is_color;

    @SerializedName("color_id")
    private String color_id;

    @SerializedName("color_code")
    private String color_code;

    @SerializedName("is_size")
    private String is_size;

    @SerializedName("total_rate")
    private String total_rate;

    @SerializedName("rate_avg")
    private String rate_avg;

    @SerializedName("product_features")
    private String product_features;

    @SerializedName("size_chart")
    private String size_chart;

    @SerializedName("share_link")
    private String share_link;

    @SerializedName("product_qty")
    private String product_qty;

    @SerializedName("product_status")
    private String product_status;

    @SerializedName("product_status_lbl")
    private String product_status_lbl;

    @SerializedName("over_all_rate")
    private String over_all_rate;

    @SerializedName("five_rate_per")
    private String five_rate_per;

    @SerializedName("five_rate_count")
    private String five_rate_count;

    @SerializedName("four_rate_per")
    private String four_rate_per;

    @SerializedName("four_rate_count")
    private String four_rate_count;

    @SerializedName("three_rate_per")
    private String three_rate_per;

    @SerializedName("three_rate_count")
    private String three_rate_count;

    @SerializedName("two_rate_per")
    private String two_rate_per;

    @SerializedName("two_rate_count")
    private String two_rate_count;

    @SerializedName("one_rate_per")
    private String one_rate_per;

    @SerializedName("one_rate_count")
    private String one_rate_count;

    @SerializedName("cart_items")
    private String cart_items;

    @SerializedName("product_images")
    private List<ProImageList> proImageLists;

    @SerializedName("color_arr")
    private List<ProColorList> proColorLists;

    @SerializedName("product_sizes")
    private List<ProSizeList> proSizeLists;

    @SerializedName("reviews")
    private List<ProReviewList> proReviewLists;

    @SerializedName("related_products")
    private List<ProductList> productLists;

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

    public String getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getProduct_image_square() {
        return product_image_square;
    }

    public void setProduct_image_square(String product_image_square) {
        this.product_image_square = product_image_square;
    }

    public String getProduct_image_portrait() {
        return product_image_portrait;
    }

    public void setProduct_image_portrait(String product_image_portrait) {
        this.product_image_portrait = product_image_portrait;
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

    public String getIs_color() {
        return is_color;
    }

    public void setIs_color(String is_color) {
        this.is_color = is_color;
    }

    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public String getIs_size() {
        return is_size;
    }

    public void setIs_size(String is_size) {
        this.is_size = is_size;
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

    public String getProduct_features() {
        return product_features;
    }

    public void setProduct_features(String product_features) {
        this.product_features = product_features;
    }

    public String getCart_items() {
        return cart_items;
    }

    public void setCart_items(String cart_items) {
        this.cart_items = cart_items;
    }

    public String getSize_chart() {
        return size_chart;
    }

    public void setSize_chart(String size_chart) {
        this.size_chart = size_chart;
    }

    public String getShare_link() {
        return share_link;
    }

    public void setShare_link(String share_link) {
        this.share_link = share_link;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
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

    public String getOver_all_rate() {
        return over_all_rate;
    }

    public void setOver_all_rate(String over_all_rate) {
        this.over_all_rate = over_all_rate;
    }

    public String getFive_rate_per() {
        return five_rate_per;
    }

    public void setFive_rate_per(String five_rate_per) {
        this.five_rate_per = five_rate_per;
    }

    public String getFive_rate_count() {
        return five_rate_count;
    }

    public void setFive_rate_count(String five_rate_count) {
        this.five_rate_count = five_rate_count;
    }

    public String getFour_rate_per() {
        return four_rate_per;
    }

    public void setFour_rate_per(String four_rate_per) {
        this.four_rate_per = four_rate_per;
    }

    public String getFour_rate_count() {
        return four_rate_count;
    }

    public void setFour_rate_count(String four_rate_count) {
        this.four_rate_count = four_rate_count;
    }

    public String getThree_rate_per() {
        return three_rate_per;
    }

    public void setThree_rate_per(String three_rate_per) {
        this.three_rate_per = three_rate_per;
    }

    public String getThree_rate_count() {
        return three_rate_count;
    }

    public void setThree_rate_count(String three_rate_count) {
        this.three_rate_count = three_rate_count;
    }

    public String getTwo_rate_per() {
        return two_rate_per;
    }

    public void setTwo_rate_per(String two_rate_per) {
        this.two_rate_per = two_rate_per;
    }

    public String getTwo_rate_count() {
        return two_rate_count;
    }

    public void setTwo_rate_count(String two_rate_count) {
        this.two_rate_count = two_rate_count;
    }

    public String getOne_rate_per() {
        return one_rate_per;
    }

    public void setOne_rate_per(String one_rate_per) {
        this.one_rate_per = one_rate_per;
    }

    public String getOne_rate_count() {
        return one_rate_count;
    }

    public void setOne_rate_count(String one_rate_count) {
        this.one_rate_count = one_rate_count;
    }

    public List<ProImageList> getProImageLists() {
        return proImageLists;
    }

    public void setProImageLists(List<ProImageList> proImageLists) {
        this.proImageLists = proImageLists;
    }

    public List<ProColorList> getProColorLists() {
        return proColorLists;
    }

    public void setProColorLists(List<ProColorList> proColorLists) {
        this.proColorLists = proColorLists;
    }

    public List<ProSizeList> getProSizeLists() {
        return proSizeLists;
    }

    public void setProSizeLists(List<ProSizeList> proSizeLists) {
        this.proSizeLists = proSizeLists;
    }

    public List<ProReviewList> getProReviewLists() {
        return proReviewLists;
    }

    public void setProReviewLists(List<ProReviewList> proReviewLists) {
        this.proReviewLists = proReviewLists;
    }

    public List<ProductList> getProductLists() {
        return productLists;
    }

    public void setProductLists(List<ProductList> productLists) {
        this.productLists = productLists;
    }
}
