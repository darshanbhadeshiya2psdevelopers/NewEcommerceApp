package com.db.newecom.Response;

import com.db.newecom.Model.Bank;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BankDetailsRP implements Serializable {

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

    @SerializedName("ECOMMERCE_APP")
    private List<Bank> bankDetailLists;

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

    public List<Bank> getBankDetailLists() {
        return bankDetailLists;
    }
}
