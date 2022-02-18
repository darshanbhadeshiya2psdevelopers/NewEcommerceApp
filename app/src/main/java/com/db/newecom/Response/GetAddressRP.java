package com.db.newecom.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetAddressRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("building_name")
    private String building_name;

    @SerializedName("road_area_colony")
    private String road_area_colony;

    @SerializedName("country")
    private String country;

    @SerializedName("city")
    private String city;

    @SerializedName("district")
    private String district;

    @SerializedName("state")
    private String state;

    @SerializedName("landmark")
    private String landmark;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("mobile_no")
    private String mobile_no;

    @SerializedName("alter_mobile_no")
    private String alter_mobile_no;

    @SerializedName("address_type")
    private String address_type;

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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public String getRoad_area_colony() {
        return road_area_colony;
    }

    public void setRoad_area_colony(String road_area_colony) {
        this.road_area_colony = road_area_colony;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAlter_mobile_no() {
        return alter_mobile_no;
    }

    public void setAlter_mobile_no(String alter_mobile_no) {
        this.alter_mobile_no = alter_mobile_no;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }
}
