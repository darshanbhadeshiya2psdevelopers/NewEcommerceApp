package com.db.newecom.Response;

import com.db.newecom.Model.MyOrders;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MyOrderRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("ECOMMERCE_APP")
    private List<MyOrders> myOrderLists;

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

    public List<MyOrders> getMyOrderLists() {
        return myOrderLists;
    }

    public void setMyOrderLists(List<MyOrders> myOrderLists) {
        this.myOrderLists = myOrderLists;
    }

}
