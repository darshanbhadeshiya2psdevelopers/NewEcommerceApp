package com.db.newecom.Response;

import com.db.newecom.Model.CompareProList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CompareRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("total_products")
    private String total_products;

    @SerializedName("ECOMMERCE_APP")
    private List<CompareProList> productLists;

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

    public String getTotal_products() {
        return total_products;
    }

    public void setTotal_products(String total_products) {
        this.total_products = total_products;
    }

    public List<CompareProList> getProductLists() {
        return productLists;
    }

    public void setProductLists(List<CompareProList> productLists) {
        this.productLists = productLists;
    }
}
