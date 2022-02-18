package com.db.newecom.Response;

import com.db.newecom.Model.Faq;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FaqRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("ECOMMERCE_APP")
    private List<Faq> faqLists;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Faq> getFaqLists() {
        return faqLists;
    }
}
