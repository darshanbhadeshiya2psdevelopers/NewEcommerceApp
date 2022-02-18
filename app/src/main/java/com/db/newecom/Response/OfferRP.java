package com.db.newecom.Response;

import com.db.newecom.Model.OfferList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OfferRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("ECOMMERCE_APP")
    private List<OfferList> offerLists;

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

    public List<OfferList> getOfferLists() {
        return offerLists;
    }

    public void setOfferLists(List<OfferList> offerLists) {
        this.offerLists = offerLists;
    }
}
