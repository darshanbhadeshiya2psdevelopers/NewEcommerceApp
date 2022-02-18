package com.db.newecom.Response;

import com.db.newecom.Model.BrandFilterList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BrandFilterRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("brand_list")
    private List<BrandFilterList> brandFilterLists;

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

    public List<BrandFilterList> getBrandFilterLists() {
        return brandFilterLists;
    }

    public void setBrandFilterLists(List<BrandFilterList> brandFilterLists) {
        this.brandFilterLists = brandFilterLists;
    }
}
