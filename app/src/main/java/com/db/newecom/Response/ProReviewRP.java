package com.db.newecom.Response;

import com.db.newecom.Model.ProReviewList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProReviewRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("over_all_rate")
    private String over_all_rate;

    @SerializedName("five_rate_per")
    private String five_rate_per;

    @SerializedName("five_rate_count")
    private String five_rate_count;

    @SerializedName("four_rate_per")
    private String four_rate_per;

    @SerializedName("four_rate_count")
    private String four_rate_count;

    @SerializedName("three_rate_per")
    private String three_rate_per;

    @SerializedName("three_rate_count")
    private String three_rate_count;

    @SerializedName("two_rate_per")
    private String two_rate_per;

    @SerializedName("two_rate_count")
    private String two_rate_count;

    @SerializedName("one_rate_per")
    private String one_rate_per;

    @SerializedName("one_rate_count")
    private String one_rate_count;

    @SerializedName("reviews")
    private List<ProReviewList> proReviewLists;

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

    public String getOver_all_rate() {
        return over_all_rate;
    }

    public void setOver_all_rate(String over_all_rate) {
        this.over_all_rate = over_all_rate;
    }

    public String getFive_rate_per() {
        return five_rate_per;
    }

    public void setFive_rate_per(String five_rate_per) {
        this.five_rate_per = five_rate_per;
    }

    public String getFive_rate_count() {
        return five_rate_count;
    }

    public void setFive_rate_count(String five_rate_count) {
        this.five_rate_count = five_rate_count;
    }

    public String getFour_rate_per() {
        return four_rate_per;
    }

    public void setFour_rate_per(String four_rate_per) {
        this.four_rate_per = four_rate_per;
    }

    public String getFour_rate_count() {
        return four_rate_count;
    }

    public void setFour_rate_count(String four_rate_count) {
        this.four_rate_count = four_rate_count;
    }

    public String getThree_rate_per() {
        return three_rate_per;
    }

    public void setThree_rate_per(String three_rate_per) {
        this.three_rate_per = three_rate_per;
    }

    public String getThree_rate_count() {
        return three_rate_count;
    }

    public void setThree_rate_count(String three_rate_count) {
        this.three_rate_count = three_rate_count;
    }

    public String getTwo_rate_per() {
        return two_rate_per;
    }

    public void setTwo_rate_per(String two_rate_per) {
        this.two_rate_per = two_rate_per;
    }

    public String getTwo_rate_count() {
        return two_rate_count;
    }

    public void setTwo_rate_count(String two_rate_count) {
        this.two_rate_count = two_rate_count;
    }

    public String getOne_rate_per() {
        return one_rate_per;
    }

    public void setOne_rate_per(String one_rate_per) {
        this.one_rate_per = one_rate_per;
    }

    public String getOne_rate_count() {
        return one_rate_count;
    }

    public void setOne_rate_count(String one_rate_count) {
        this.one_rate_count = one_rate_count;
    }

    public List<ProReviewList> getProReviewLists() {
        return proReviewLists;
    }

    public void setProReviewLists(List<ProReviewList> proReviewLists) {
        this.proReviewLists = proReviewLists;
    }

}
