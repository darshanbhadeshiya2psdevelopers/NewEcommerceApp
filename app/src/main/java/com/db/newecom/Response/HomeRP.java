package com.db.newecom.Response;

import com.db.newecom.Model.BrandList;
import com.db.newecom.Model.CategoryList;
import com.db.newecom.Model.HomeCatSubProList;
import com.db.newecom.Model.MyOrders;
import com.db.newecom.Model.OfferList;
import com.db.newecom.Model.ProductList;
import com.db.newecom.Model.SliderList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HomeRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("banners")
    private List<SliderList> sliderLists;

    @SerializedName("categories")
    private List<CategoryList> categoryLists;

    @SerializedName("my_order")
    private List<MyOrders> myOrderLists;

    @SerializedName("brands")
    private List<BrandList> brandLists;

    @SerializedName("todays_deals")
    private List<ProductList> todayLists;

    @SerializedName("latest_products")
    private List<ProductList> latestLists;

    @SerializedName("top_rated_products")
    private List<ProductList> topRatedLists;

    @SerializedName("offers")
    private List<OfferList> offerLists;

    @SerializedName("recent_view")
    private List<ProductList> recentViewList;

    @SerializedName("home_category")
    private List<HomeCatSubProList> homeCatSubProLists;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<SliderList> getSliderLists() {
        return sliderLists;
    }

    public List<CategoryList> getCategoryLists() {
        return categoryLists;
    }

    public List<MyOrders> getMyOrderLists() {
        return myOrderLists;
    }

    public List<BrandList> getBrandLists() {
        return brandLists;
    }

    public List<ProductList> getTodayLists() {
        return todayLists;
    }

    public List<ProductList> getLatestLists() {
        return latestLists;
    }

    public List<ProductList> getTopRatedLists() {
        return topRatedLists;
    }

    public List<OfferList> getOfferLists() {
        return offerLists;
    }

    public List<ProductList> getRecentViewList() {
        return recentViewList;
    }

    public List<HomeCatSubProList> getHomeCatSubProLists() {
        return homeCatSubProLists;
    }
}
