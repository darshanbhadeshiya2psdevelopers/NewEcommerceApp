package com.db.newecom.Response;

import com.db.newecom.Model.SizeFilterList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SizeFilterRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("sizes")
    private List<SizeFilterList> sizeFilterLists;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<SizeFilterList> getSizeFilterLists() {
        return sizeFilterLists;
    }

}
