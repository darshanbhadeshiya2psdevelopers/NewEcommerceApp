package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bank implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("bank_holder_name")
    private String bank_holder_name;

    @SerializedName("bank_holder_phone")
    private String bank_holder_phone;

    @SerializedName("bank_holder_email")
    private String bank_holder_email;

    @SerializedName("account_no")
    private String account_no;

    @SerializedName("account_type")
    private String account_type;

    @SerializedName("bank_ifsc")
    private String bank_ifsc;

    @SerializedName("bank_name")
    private String bank_name;

    @SerializedName("is_default")
    private String is_default;

    public String getId() {
        return id;
    }

    public String getBank_holder_name() {
        return bank_holder_name;
    }

    public String getBank_holder_phone() {
        return bank_holder_phone;
    }

    public String getBank_holder_email() {
        return bank_holder_email;
    }

    public String getAccount_no() {
        return account_no;
    }

    public String getAccount_type() {
        return account_type;
    }

    public String getBank_ifsc() {
        return bank_ifsc;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getIs_default() {
        return is_default;
    }
}
