package com.db.newecom.Response;

import com.db.newecom.Model.CouponsList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CouponsRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("ECOMMERCE_APP")
    private List<CouponsList> couponsLists;

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

    public List<CouponsList> getCouponsLists() {
        return couponsLists;
    }

    public void setCouponsLists(List<CouponsList> couponsLists) {
        this.couponsLists = couponsLists;
    }

}
