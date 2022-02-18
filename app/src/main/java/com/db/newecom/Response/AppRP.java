package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

//    @SerializedName("app_developed_by")
//    private String app_developed_by;

    @SerializedName("app_currency_code")
    private String app_currency_code;

    @SerializedName("cart_items")
    private String cart_items ;

//    @SerializedName("privacy_policy")
//    private String privacy_policy ;
//
//    @SerializedName("user_name")
//    private String user_name ;
//
//    @SerializedName("user_email")
//    private String user_email ;
//
//    @SerializedName("user_image")
//    private String user_image ;
//
//    @SerializedName("app_update_status")
//    private boolean app_update_status ;
//
//    @SerializedName("app_new_version")
//    private int app_new_version ;
//
//    @SerializedName("app_update_desc")
//    private String app_update_desc ;
//
//    @SerializedName("app_redirect_url")
//    private String app_redirect_url ;
//
//    @SerializedName("cancel_update_status")
//    private boolean cancel_update_status ;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

//    public String getApp_developed_by() {
//        return app_developed_by;
//    }

    public String getApp_currency_code() {
        return app_currency_code;
    }

    public String getCart_items() {
        return cart_items;
    }

//    public String getPrivacy_policy() {
//        return privacy_policy;
//    }
//
//    public String getUser_name() {
//        return user_name;
//    }
//
//    public String getUser_email() {
//        return user_email;
//    }
//
//    public String getUser_image() {
//        return user_image;
//    }
//
//    public boolean isApp_update_status() {
//        return app_update_status;
//    }
//
//    public int getApp_new_version() {
//        return app_new_version;
//    }
//
//    public String getApp_update_desc() {
//        return app_update_desc;
//    }
//
//    public String getApp_redirect_url() {
//        return app_redirect_url;
//    }
//
//    public boolean isCancel_update_status() {
//        return cancel_update_status;
//    }
}
