package com.db.newecom.Api;

import com.db.newecom.Response.AboutRP;
import com.db.newecom.Response.AddEditRemoveAddressRP;
import com.db.newecom.Response.AddEditRemoveBankRP;
import com.db.newecom.Response.AddToCartRP;
import com.db.newecom.Response.AddressListRP;
import com.db.newecom.Response.AppRP;
import com.db.newecom.Response.ApplyCouponRP;
import com.db.newecom.Response.BankDetailsRP;
import com.db.newecom.Response.BrandFilterRP;
import com.db.newecom.Response.CancelOrderProRP;
import com.db.newecom.Response.CartRP;
import com.db.newecom.Response.CategoryRP;
import com.db.newecom.Response.ChangeAddressRP;
import com.db.newecom.Response.CompareRP;
import com.db.newecom.Response.ContactRP;
import com.db.newecom.Response.CountryRP;
import com.db.newecom.Response.CouponsRP;
import com.db.newecom.Response.DataRP;
import com.db.newecom.Response.FaqRP;
import com.db.newecom.Response.FavouriteRP;
import com.db.newecom.Response.FilterRP;
import com.db.newecom.Response.GetAddressRP;
import com.db.newecom.Response.GetBankDetailRP;
import com.db.newecom.Response.HomeRP;
import com.db.newecom.Response.IsAddRP;
import com.db.newecom.Response.LoginRP;
import com.db.newecom.Response.MyOrderDetailRP;
import com.db.newecom.Response.MyOrderRP;
import com.db.newecom.Response.MyRatingReviewRP;
import com.db.newecom.Response.OfferRP;
import com.db.newecom.Response.OrderSummaryRP;
import com.db.newecom.Response.PaymentMethodRP;
import com.db.newecom.Response.PaymentSubmitRP;
import com.db.newecom.Response.PolicyRP;
import com.db.newecom.Response.PriceSelectionRP;
import com.db.newecom.Response.ProDetailRP;
import com.db.newecom.Response.ProRP;
import com.db.newecom.Response.ProReviewRP;
import com.db.newecom.Response.ProductRP;
import com.db.newecom.Response.RRSubmitRP;
import com.db.newecom.Response.RazorPayRP;
import com.db.newecom.Response.RegisterRP;
import com.db.newecom.Response.RemoveCartRP;
import com.db.newecom.Response.RemoveCouponRP;
import com.db.newecom.Response.SizeFilterRP;
import com.db.newecom.Response.SubCategoryRP;
import com.db.newecom.Response.UpdateCartRP;
import com.db.newecom.Response.UserProfileRP;
import com.db.newecom.Response.UserProfileUpdateRP;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    //get app data
    @POST("general_information")
    @FormUrlEncoded
    Call<AppRP> getAppData(@Field("status") String status);

    @POST("login")
    @FormUrlEncoded
    Call<LoginRP> getLogin(@Field("data") String data);

    @POST("register")
    @FormUrlEncoded
    Call<RegisterRP> getRegister(@Field("data") String data);

    @POST("forgot_password")
    @FormUrlEncoded
    Call<DataRP> getForgotPass(@Field("data") String data);

    @POST("home")
    @FormUrlEncoded
    Call<HomeRP> getHome(@Field("data") String data);

    @POST("faq")
    @FormUrlEncoded
    Call<FaqRP> getFaq(@Field("data") String data);

    @POST("about_us")
    @FormUrlEncoded
    Call<AboutRP> getAboutUs(@Field("data") String data);

    @POST("policy_data")
    @FormUrlEncoded
    Call<PolicyRP> getPolicy(@Field("data") String data);

    @POST("contact_subjects")
    @FormUrlEncoded
    Call<ContactRP> getContactSub(@Field("data") String data);

    @POST("contact_form")
    @FormUrlEncoded
    Call<DataRP> submitContact(@Field("data") String data);

    @POST("change_password")
    @FormUrlEncoded
    Call<DataRP> changePassword(@Field("data") String data);

    @POST("get_countries")
    @FormUrlEncoded
    Call<CountryRP> getCountry(@Field("data") String data);

    //add address
    @POST("addedit_address")
    @FormUrlEncoded
    Call<AddEditRemoveAddressRP> addEditAddress(@Field("data") String data);

    @POST("delete_address")
    @FormUrlEncoded
    Call<AddEditRemoveAddressRP> deleteAddress(@Field("data") String data);

    //get address
    @POST("single_address")
    @FormUrlEncoded
    Call<GetAddressRP> getAddress(@Field("data") String data);

    //Get address list
    @POST("get_addresses")
    @FormUrlEncoded
    Call<AddressListRP> getAddressList(@Field("data") String data);

    //Delete bank
    @POST("delete_bank_account")
    @FormUrlEncoded
    Call<AddEditRemoveBankRP> deleteBank(@Field("data") String data);

    //Submit bank detail
    @POST("addedit_bank_account")
    @FormUrlEncoded
    Call<AddEditRemoveBankRP> submitBankDetail(@Field("data") String data);

    //Get bank detail
    @POST("get_bank_details")
    @FormUrlEncoded
    Call<GetBankDetailRP> getBankDetail(@Field("data") String data);

    //Bank list
    @POST("get_bank_list")
    @FormUrlEncoded
    Call<BankDetailsRP> getBankList(@Field("data") String data);

    //get profile details
    @POST("profile")
    @FormUrlEncoded
    Call<UserProfileRP> getProfile(@Field("data") String data);

    //edit profile
    @POST("edit_profile")
    @Multipart
    Call<UserProfileUpdateRP> editProfile(@Part("data") RequestBody data, @Part MultipartBody.Part part);

    //Add to favourite
    @POST("wishlist")
    @FormUrlEncoded
    Call<FavouriteRP> addToFavourite(@Field("data") String data);

    //Favourite list
    @POST("my_wishlist")
    @FormUrlEncoded
    Call<ProductRP> getFavourite(@Field("data") String data);

    //remove product from wishlist
    @POST("remove_wishlist")
    @FormUrlEncoded
    Call<DataRP> removeWishlistPro(@Field("data") String data);

    //category list
    @POST("categories")
    @FormUrlEncoded
    Call<CategoryRP> getCategory(@Field("data") String data);

    //Sub category
    @POST("sub_categories")
    @FormUrlEncoded
    Call<SubCategoryRP> getSubCategory(@Field("data") String data);

    //Offer list
    @POST("offers")
    @FormUrlEncoded
    Call<OfferRP> getOffer(@Field("data") String data);

    //products by category and sub category
    @POST("productList_cat_sub")
    @FormUrlEncoded
    Call<ProductRP> getCatSubPro(@Field("data") String data);

    //products by banner
    @POST("products_by_banner")
    @FormUrlEncoded
    Call<ProductRP> getSliderPro(@Field("data") String data);

    //products by offer
    @POST("products_by_offer")
    @FormUrlEncoded
    Call<ProductRP> getOfferPro(@Field("data") String data);

    //products by today deal
    @POST("today_deal")
    @FormUrlEncoded
    Call<ProductRP> getTodayDealPro(@Field("data") String data);

    //brands product
    @POST("products_by_brand")
    @FormUrlEncoded
    Call<ProductRP> getBrandPro(@Field("data") String data);

    //products bt latest products
    @POST("get_latest_products")
    @FormUrlEncoded
    Call<ProductRP> getLatestPro(@Field("data") String data);

    //products by search
    @POST("search")
    @FormUrlEncoded
    Call<ProductRP> getSearchPro(@Field("data") String data);

    //Product detail
    @POST("single_product")
    @FormUrlEncoded
    Call<ProDetailRP> getProDetail(@Field("data") String data);

    //Add to cart
    @POST("cart_add_update")
    @FormUrlEncoded
    Call<AddToCartRP> addToCart(@Field("data") String data);

    //remove cart item
    @POST("cart_item_delete")
    @FormUrlEncoded
    Call<RemoveCartRP> removeCartItem(@Field("data") String data);

    //cart item update
    @POST("cart_add_update")
    @FormUrlEncoded
    Call<UpdateCartRP> getCartItemUpdate(@Field("data") String data);

    //Address Check
    @POST("is_address_avail")
    @FormUrlEncoded
    Call<IsAddRP> isAddress(@Field("data") String data);

    //Get my cart list
    @POST("my_cart")
    @FormUrlEncoded
    Call<CartRP> getCartList(@Field("data") String data);

    //Submit rating review
    @POST("product_review")
    @FormUrlEncoded
    Call<RRSubmitRP> submitRatingReview(@Field("data") String data);

    //all review
    @POST("users_review")
    @FormUrlEncoded
    Call<ProReviewRP> getProReviewDetail(@Field("data") String data);

    //Get My rating review
    @POST("my_review")
    @FormUrlEncoded
    Call<MyRatingReviewRP> getMyRatingReview(@Field("data") String data);

    //Delete My rating review
    @POST("delete_review")
    @FormUrlEncoded
    Call<DataRP> deleteMyReview(@Field("data") String data);

    //Filter list
    @POST("filter_list")
    @FormUrlEncoded
    Call<FilterRP> getFilterType(@Field("data") String data);

    //Price selection filter
    @POST("price_filter")
    @FormUrlEncoded
    Call<PriceSelectionRP> getPriceSelection(@Field("data") String data);

    //Brand filter
    @POST("brand_filter")
    @FormUrlEncoded
    Call<BrandFilterRP> getBrandFilter(@Field("data") String data);

    //Size filter
    @POST("size_filter")
    @FormUrlEncoded
    Call<SizeFilterRP> getSizeFilter(@Field("data") String data);

    //Filter product
    @POST("apply_filter")
    @FormUrlEncoded
    Call<ProRP> getFilterPro(@Field("data") String data);

    //Order summary
    @POST("order_summary")
    @FormUrlEncoded
    Call<OrderSummaryRP> getOrderDetail(@Field("data") String data);

    //Change address
    @POST("change_address")
    @FormUrlEncoded
    Call<ChangeAddressRP> getChangeAddress(@Field("data") String data);

    //Coupon list
    @POST("coupons")
    @FormUrlEncoded
    Call<CouponsRP> getCoupons(@Field("data") String data);

    //Apply coupon
    @POST("apply_coupon")
    @FormUrlEncoded
    Call<ApplyCouponRP> getApplyCouponsDetail(@Field("data") String data);

    //Remove coupon
    @POST("remove_coupon")
    @FormUrlEncoded
    Call<RemoveCouponRP> getRemoveCouponDetail(@Field("data") String data);

    //Get payment detail
    @POST("payment_details")
    @FormUrlEncoded
    Call<PaymentMethodRP> getPaymentMethod(@Field("data") String data);

    //My Orders
    @POST("my_order")
    @FormUrlEncoded
    Call<MyOrderRP> getMyOderList(@Field("data") String data);

    //My order detail
    @POST("order_detail")
    @FormUrlEncoded
    Call<MyOrderDetailRP> getMyOrderDetail(@Field("data") String data);

    //Cancel order and product
    @POST("order_or_product_cancel")
    @FormUrlEncoded
    Call<CancelOrderProRP> cancelOrderORProduct(@Field("data") String data);

    //Claim order and product
    @POST("claim_refund")
    @FormUrlEncoded
    Call<DataRP> claimOrderORProduct(@Field("data") String data);

    //submit payment
    @POST("payment")
    @FormUrlEncoded
    Call<PaymentSubmitRP> submitPayment(@Field("data") String data);

    //RazorPay response
    @POST("razorpay_order_id")
    @FormUrlEncoded
    Call<RazorPayRP> getRazorPayData(@Field("data") String data);

    //compare
    @POST("get_compare")
    @FormUrlEncoded
    Call<CompareRP> getCompareData(@Field("data") String data);

    //remove_pro_from_compare
    @POST("add_remove_compare")
    @FormUrlEncoded
    Call<DataRP> addRemoveCompare(@Field("data") String data);
}
