package com.db.newecom.Response;

import com.db.newecom.Model.FilterSortByList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FilterRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("brand_filter")
    private String brand_filter;

    @SerializedName("size_status")
    private String size_status;

    @SerializedName("sort_by")
    private List<FilterSortByList> filterSortByLists;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getBrand_filter() {
        return brand_filter;
    }

    public String getSize_status() {
        return size_status;
    }

    public List<FilterSortByList> getFilterSortByLists() {
        return filterSortByLists;
    }
}
