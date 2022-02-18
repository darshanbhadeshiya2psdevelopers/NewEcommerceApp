package com.db.newecom.Response;

import com.db.newecom.Model.Address;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AddressListRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("address_count")
    private String address_count;

    @SerializedName("ECOMMERCE_APP")
    private List<Address> addressItemLists;

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

    public String getAddress_count() {
        return address_count;
    }

    public void setAddress_count(String address_count) {
        this.address_count = address_count;
    }

    public List<Address> getAddressItemLists() {
        return addressItemLists;
    }

    public void setAddressItemLists(List<Address> addressItemLists) {
        this.addressItemLists = addressItemLists;
    }
}
