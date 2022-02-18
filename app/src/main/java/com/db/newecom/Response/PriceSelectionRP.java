package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PriceSelectionRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("price_min")
    private String price_min;

    @SerializedName("price_max")
    private String price_max;

    @SerializedName("pre_price_min")
    private String pre_price_min;

    @SerializedName("pre_price_max")
    private String pre_price_max;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getPrice_min() {
        return price_min;
    }

    public String getPrice_max() {
        return price_max;
    }

    public String getPre_price_min() {
        return pre_price_min;
    }

    public String getPre_price_max() {
        return pre_price_max;
    }

}
