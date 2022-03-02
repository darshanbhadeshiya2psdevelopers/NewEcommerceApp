package com.db.newecom.Response;

import com.db.newecom.Model.MyOrders;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ReturnOrderRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("ECOMMERCE_APP")
    private List<MyOrders> myOrderLists;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public List<MyOrders> getMyOrderLists() {
        return myOrderLists;
    }

}
