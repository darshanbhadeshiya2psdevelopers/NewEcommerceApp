package com.db.newecom.Response;

import com.db.newecom.Model.MyOrders;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserProfileRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("id")
    private String id;

    @SerializedName("user_name")
    private String user_name;

    @SerializedName("user_email")
    private String user_email;

    @SerializedName("user_phone")
    private String user_phone;

    @SerializedName("user_image")
    private String user_image;

    @SerializedName("address_name")
    private String address_name;

    @SerializedName("address_type")
    private String address_type;

    @SerializedName("address")
    private String address;

    @SerializedName("address_phone")
    private String address_phone;

    @SerializedName("address_count")
    private String address_count;

    @SerializedName("bank_count")
    private String bank_count;

    @SerializedName("bank_details")
    private String bank_details;

    @SerializedName("bank_name")
    private String bank_name;

    @SerializedName("account_no")
    private String account_no;

    @SerializedName("bank_holder_name")
    private String bank_holder_name;

    @SerializedName("bank_holder_phone")
    private String bank_holder_phone;

    @SerializedName("bank_holder_email")
    private String bank_holder_email;

    @SerializedName("bank_ifsc")
    private String bank_ifsc;

    @SerializedName("bank_type")
    private String bank_type;

    @SerializedName("cart_items")
    private String cart_items;

    @SerializedName("ECOMMERCE_APP")
    private List<MyOrders> myOrderLists;

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

    public String getId() {
        return id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getAddress_name() {
        return address_name;
    }

    public String getAddress_type() {
        return address_type;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress_phone() {
        return address_phone;
    }

    public String getAddress_count() {
        return address_count;
    }

    public String getBank_count() {
        return bank_count;
    }

    public String getBank_details() {
        return bank_details;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getAccount_no() {
        return account_no;
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

    public String getBank_ifsc() {
        return bank_ifsc;
    }

    public String getBank_type() {
        return bank_type;
    }

    public String getCart_items() {
        return cart_items;
    }

    public List<MyOrders> getMyOrderLists() {
        return myOrderLists;
    }
}
