package com.db.newecom.Utills;

//import com.db.ecommerceapp.Model.MyOrders;

import com.db.newecom.Model.MyOrders;

import java.util.List;

public class Events {

    private Events() {
        throw new UnsupportedOperationException();
    }

    //Event used to send message from login notify.
    public static class Login {
        private String name, email, userImage;

        public Login(String name, String email, String userImage) {
            this.name = name;
            this.email = email;
            this.userImage = userImage;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getUserImage() {
            return userImage;
        }
    }

    //Event used to update cart
    public static class UpdateCart {

        private String total_item, price, delivery_charge, payable_amt, you_save_msg, msg;

        public UpdateCart(String total_item, String price, String delivery_charge, String payable_amt, String you_save_msg, String msg) {
            this.total_item = total_item;
            this.price = price;
            this.delivery_charge = delivery_charge;
            this.payable_amt = payable_amt;
            this.you_save_msg = you_save_msg;
            this.msg = msg;
        }

        public String getTotal_item() {
            return total_item;
        }

        public String getPrice() {
            return price;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public String getPayable_amt() {
            return payable_amt;
        }

        public String getYou_save_msg() {
            return you_save_msg;
        }

        public String getMsg() {
            return msg;
        }
    }

    //Event used to back activity
    public static class OnBackData {
        private String back;

        public OnBackData(String back) {
            this.back = back;
        }

        public String getLogin() {
            return back;
        }
    }

    //Event used to apply coupon code amount order summery
    public static class CouponCodeAmount {

        private String coupon_id, price, payable_amt, you_save_msg;

        public CouponCodeAmount(String coupon_id, String price, String payable_amt, String you_save_msg) {
            this.coupon_id = coupon_id;
            this.price = price;
            this.payable_amt = payable_amt;
            this.you_save_msg = you_save_msg;
        }

        public String getCoupon_id() {
            return coupon_id;
        }

        public String getPrice() {
            return price;
        }

        public String getPayable_amt() {
            return payable_amt;
        }

        public String getYou_save_msg() {
            return you_save_msg;
        }

    }

    //Event used to update cart
    public static class UpdateCartOrderSum {

        private String total_item, price, delivery_charge, payable_amt, you_save_msg;

        public UpdateCartOrderSum(String total_item, String price, String delivery_charge, String payable_amt, String you_save_msg) {
            this.total_item = total_item;
            this.price = price;
            this.delivery_charge = delivery_charge;
            this.payable_amt = payable_amt;
            this.you_save_msg = you_save_msg;
        }

        public String getTotal_item() {
            return total_item;
        }

        public String getPrice() {
            return price;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public String getPayable_amt() {
            return payable_amt;
        }

        public String getYou_save_msg() {
            return you_save_msg;
        }
    }

    //Event used to update address order summery
    public static class UpdateDeliveryAddress {

        private String addressId, address, name, mobileNo, addressType;

        public UpdateDeliveryAddress(String addressId, String address, String name, String mobileNo, String addressType) {
            this.addressId = addressId;
            this.address = address;
            this.name = name;
            this.mobileNo = mobileNo;
            this.addressType = addressType;
        }

        public String getAddressId() {
            return addressId;
        }

        public String getAddress() {
            return address;
        }

        public String getName() {
            return name;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public String getAddressType() {
            return addressType;
        }
    }

    //Event used to item count cart
    public static class CartItem {
        private String cart_item;

        public CartItem(String cart_item) {
            this.cart_item = cart_item;
        }

        public String getCart_item() {
            return cart_item;
        }
    }

    //Event used to my order update rating
    public static class RatingUpdate {

        private int rate;
        private String rate_msg;

        public RatingUpdate(int rate, String rate_msg) {
            this.rate = rate;
            this.rate_msg = rate_msg;
        }

        public int getRate() {
            return rate;
        }

        public String getRate_msg() {
            return rate_msg;
        }

    }

    //Event used to update address list
    public static class EventMYAddress {

        private String address, addressCount, type;

        public EventMYAddress(String address, String addressCount, String type) {
            this.address = address;
            this.addressCount = addressCount;
            this.type = type;
        }

        public String getAddress() {
            return address;
        }

        public String getAddressCount() {
            return addressCount;
        }

        public String getType() {
            return type;
        }
    }

    //Event used to update address list
    public static class EventBankDetail {

        private String bankCount, bankDetails, type;

        public EventBankDetail(String bankCount, String bankDetails, String type) {
            this.bankCount = bankCount;
            this.bankDetails = bankDetails;
            this.type = type;
        }

        public String getBankCount() {
            return bankCount;
        }

        public String getBankDetails() {
            return bankDetails;
        }

        public String getType() {
            return type;
        }
    }

    //Event used to update status my order list
    public static class CancelOrder {

        private String productId, orderUniqueId, type;
        private List<MyOrders> myOrderLists;

        public CancelOrder(String productId, String orderUniqueId, String type, List<MyOrders> myOrderLists) {
            this.productId = productId;
            this.orderUniqueId = orderUniqueId;
            this.type = type;
            this.myOrderLists = myOrderLists;
        }

        public String getProductId() {
            return productId;
        }

        public String getOrderUniqueId() {
            return orderUniqueId;
        }

        public String getType() {
            return type;
        }

        public List<MyOrders> getMyOrderLists() {
            return myOrderLists;
        }
    }

    //Event used to update status my order list
    public static class ClaimOrder {

        private String productId, orderUniqueId;

        public ClaimOrder(String productId, String orderUniqueId) {
            this.productId = productId;
            this.orderUniqueId = orderUniqueId;
        }

        public String getProductId() {
            return productId;
        }

        public String getOrderUniqueId() {
            return orderUniqueId;
        }
    }

    //Event used to filter call
    public static class Filter {

        private String id, brand_ids, size, sortBy, pre_min, pre_max;

        public Filter(String id, String brand_ids, String size, String sortBy, String pre_min, String pre_max) {
            this.id = id;
            this.brand_ids = brand_ids;
            this.size = size;
            this.sortBy = sortBy;
            this.pre_min = pre_min;
            this.pre_max = pre_max;
        }

        public String getId() {
            return id;
        }

        public String getBrand_ids() {
            return brand_ids;
        }

        public String getSize() {
            return size;
        }

        public String getSortBy() {
            return sortBy;
        }

        public String getPre_min() {
            return pre_min;
        }

        public String getPre_max() {
            return pre_max;
        }
    }

    //Event used to update profile
    public static class ProfileUpdate {

        private String string;

        public ProfileUpdate(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }
    }

    //Event used to update status drawer selection
    public static class GoToHome {

        private String string;

        public GoToHome(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }
    }

    //Event used to update remove and update image
    public static class ProImage {

        private String string, image_path;
        private boolean is_profile, is_remove;

        public ProImage(String string, String image_path, boolean is_profile, boolean is_remove) {
            this.string = string;
            this.image_path = image_path;
            this.is_profile = is_profile;
            this.is_remove = is_remove;
        }

        public String getString() {
            return string;
        }

        public String getImage_path() {
            return image_path;
        }

        public boolean isIs_profile() {
            return is_profile;
        }

        public boolean isIs_remove() {
            return is_remove;
        }
    }

}
