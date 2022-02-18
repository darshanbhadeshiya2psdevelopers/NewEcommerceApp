package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddEditRemoveAddressRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("address_id")
    private String address_id;

    @SerializedName("address")
    private String address;

    @SerializedName("name")
    private String name;

    @SerializedName("address_type")
    private String address_type;

    @SerializedName("mobile_no")
    private String mobile_no;

    @SerializedName("address_count")
    private String address_count;

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

    public String getAddress_id() {
        return address_id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getAddress_type() {
        return address_type;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getAddress_count() {
        return address_count;
    }
}
