package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddEditRemoveBankRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("bank_count")
    private String bank_count;

    @SerializedName("bank_details")
    private String bank_details;

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

    public String getBank_count() {
        return bank_count;
    }

    public String getBank_details() {
        return bank_details;
    }
}
