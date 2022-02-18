package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProSizeList implements Serializable {

    @SerializedName("product_size")
    private String product_size;

    public ProSizeList(String product_size) {
        this.product_size = product_size;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }
}
