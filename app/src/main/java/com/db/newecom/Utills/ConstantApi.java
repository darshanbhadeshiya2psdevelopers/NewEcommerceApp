package com.db.newecom.Utills;

import android.os.Environment;

import com.db.newecom.Model.ProImageList;

import java.util.ArrayList;
import java.util.List;

public class ConstantApi {

    private ConstantApi() {
        throw new UnsupportedOperationException();
    }

    //app currency code
    public static String currency = "";

    //storage folder path
    public static String appStorage = Environment.getExternalStorageDirectory() + "/ECommerce/" + "Invoice/";

    public static String exceptionError = "exception_error";
    public static String failApi = "fail_api";

    public static String pre_min = "";
    public static String pre_max = "";
    public static String pre_min_temp = "";//temporary price min
    public static String pre_max_temp = "";//temporary price max

    public static String brand_ids = "";
    public static String brand_ids_temp = "";//temporary brand ids

    public static String sizes = "";
    public static String sizes_temp = "";//temporary size list

    public static List<ProImageList> proImageLists = new ArrayList<>();

}
