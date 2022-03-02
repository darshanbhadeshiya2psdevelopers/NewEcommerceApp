package com.db.newecom.Response;

import com.db.newecom.Model.MyOrders;
import com.db.newecom.Model.OrderTrackingList;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MyOrderDetailRP implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("success")
    private String success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("order_id")
    private String order_id;

    @SerializedName("order_unique_id")
    private String order_unique_id;

    @SerializedName("order_date")
    private String order_date;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("address")
    private String address;

    @SerializedName("mobile_no")
    private String mobile_no;

    @SerializedName("product_title")
    private String product_title;

    @SerializedName("product_image")
    private String product_image;

    @SerializedName("product_qty")
    private String product_qty;

    @SerializedName("product_price")
    private String product_price;

    @SerializedName("total_price")
    private String total_price;

    @SerializedName("selling_price")
    private String selling_price;

    @SerializedName("discount_amt")
    private String discount_amt;

    @SerializedName("delivery_charge")
    private String delivery_charge;

    @SerializedName("payable_amt")
    private String payable_amt;

    @SerializedName("payment_mode")
    private String payment_mode;

    @SerializedName("payment_id")
    private String payment_id;

    @SerializedName("cancel_order_amt")
    private String cancel_order_amt;

    @SerializedName("opd_price")
    private String opd_price;

    @SerializedName("opd_discount")
    private String opd_discount;

    @SerializedName("opd_delivery")
    private String opd_delivery;

    @SerializedName("opd_amountPayable")
    private String opd_amountPayable;

    @SerializedName("current_order_status")
    private String current_order_status;

    @SerializedName("cancel_product")
    private String cancel_product;

    @SerializedName("is_claim")
    private String is_claim;

    @SerializedName("is_order_claim")
    private String is_order_claim;

    @SerializedName("download_invoice")
    private String download_invoice;

    @SerializedName("my_rating")
    private String my_rating;

    @SerializedName("product_size")
    private String product_size;

    @SerializedName("product_color")
    private String product_color;

    @SerializedName("reason")
    private String reason;

    @SerializedName("refund_status")
    private String refund_status;

    @SerializedName("order_status")
    private List<OrderTrackingList> orderTrackingLists;

    @SerializedName("delivery_datetime")
    private String delivery_datetime;

    @SerializedName("order_other_items_status")
    private String order_other_items_status;

    @SerializedName("order_other_items")
    private List<MyOrders> myOrderLists;

    @SerializedName("bank_id")
    private String bank_id;

    @SerializedName("bank_status")
    private String bank_status;

    @SerializedName("cart_items")
    private String cart_items;

    @SerializedName("is_return_button")
    private String is_return_button;

    @SerializedName("return_pay")
    private String return_pay;

    @SerializedName("return_reason")
    private String return_reason;

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

    public String getProduct_id() {
        return product_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getOrder_unique_id() {
        return order_unique_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getProduct_title() {
        return product_title;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public String getProduct_price() {
        return product_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public String getDiscount_amt() {
        return discount_amt;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public String getPayable_amt() {
        return payable_amt;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public String getCancel_order_amt() {
        return cancel_order_amt;
    }

    public String getOpd_price() {
        return opd_price;
    }

    public String getOpd_discount() {
        return opd_discount;
    }

    public String getOpd_delivery() {
        return opd_delivery;
    }

    public String getOpd_amountPayable() {
        return opd_amountPayable;
    }

    public String getCurrent_order_status() {
        return current_order_status;
    }

    public String getCancel_product() {
        return cancel_product;
    }

    public String getIs_claim() {
        return is_claim;
    }

    public String getIs_order_claim() {
        return is_order_claim;
    }

    public String getDownload_invoice() {
        return download_invoice;
    }

    public String getMy_rating() {
        return my_rating;
    }

    public String getProduct_size() {
        return product_size;
    }

    public String getProduct_color() {
        return product_color;
    }

    public String getReason() {
        return reason;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public List<OrderTrackingList> getOrderTrackingLists() {
        return orderTrackingLists;
    }

    public String getDelivery_datetime() {
        return delivery_datetime;
    }

    public String getOrder_other_items_status() {
        return order_other_items_status;
    }

    public List<MyOrders> getMyOrderLists() {
        return myOrderLists;
    }

    public String getBank_id() {
        return bank_id;
    }

    public String getBank_status() {
        return bank_status;
    }

    public String getCart_items() {
        return cart_items;
    }

    public String getIs_return_button() {
        return is_return_button;
    }

    public String getReturn_pay() {
        return return_pay;
    }

    public String getReturn_reason() {
        return return_reason;
    }
}
