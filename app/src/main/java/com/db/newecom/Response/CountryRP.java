package com.db.newecom.Response;

import com.db.newecom.Model.Country;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CountryRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("user_name")
    private String user_name;

    @SerializedName("user_email")
    private String user_email;

    @SerializedName("user_phone")
    private String user_phone;

    @SerializedName("home_address_lbl")
    private String home_address_lbl;

    @SerializedName("office_address_lbl")
    private String office_address_lbl;

    @SerializedName("ECOMMERCE_APP")
    private List<Country> countryLists;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
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

    public String getHome_address_lbl() {
        return home_address_lbl;
    }

    public String getOffice_address_lbl() {
        return office_address_lbl;
    }

    public List<Country> getCountryLists() {
        return countryLists;
    }
}
