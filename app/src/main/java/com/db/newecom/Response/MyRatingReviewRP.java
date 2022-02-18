package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MyRatingReviewRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("ECOMMERCE_APP")
    private List<RatingReviewRP> MyReviewsLists;

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

    public List<RatingReviewRP> getMyReviewsLists() {
        return MyReviewsLists;
    }

    public void setMyReviewsLists(List<RatingReviewRP> myReviewsLists) {
        MyReviewsLists = myReviewsLists;
    }
}
