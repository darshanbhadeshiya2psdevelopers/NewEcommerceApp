package com.db.newecom.Utills;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.text.Html;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.db.newecom.R;
import androidx.appcompat.app.AlertDialog;

import com.db.newecom.Response.PaymentSubmitRP;
import com.db.newecom.ui.activity.MainActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class Method {

    private Activity activity;
    public static boolean loginBack = false;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private final String myPreference = "ecommerceapp";
    public String prefLogin = "prefLogin";
    public String loginType = "loginType";
    public String profileId = "profileId";
    public String userEmail = "userEmail";
    public String userName = "userName";

    public static boolean goToLogin() {
        return loginBack;
    }

    public static void login(boolean isValue) {
        loginBack = isValue;
    }

    public Method(Activity activity) {

        this.activity = activity;
        pref = activity.getSharedPreferences(myPreference, 0); // 0 - for private mode
        editor = pref.edit();

    }

    public boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void alertBox(String message) {

        if (!activity.isFinishing()) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity, R.style.DialogTitleTextStyle);
            builder.setMessage(Html.fromHtml(message));
            builder.setCancelable(false);
            builder.setPositiveButton(activity.getResources().getString(R.string.ok),
                    (arg0, arg1) -> {

                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    //user login or not
    public boolean isLogin() {
        return pref.getBoolean(prefLogin, false);
    }

    //get login type
    public String getLoginType() {
        return pref.getString(loginType, null);
    }

    //get user id
    public String userId() {
        return pref.getString(profileId, null);
    }

    //get user name
    public String userName() {
        return pref.getString(userName, null);
    }

    //get screen width
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    //check number or note
    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //order conform dialog
    public void conformDialog(PaymentSubmitRP paymentSubmitRP) {

        if (!activity.isFinishing()) {
            Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_order_conform);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            Button button_shop = dialog.findViewById(R.id.button_shop_now_doc);
            Button button_myOrder = dialog.findViewById(R.id.button_myOrder_doc);
            TextView textView_title = dialog.findViewById(R.id.textView_title_doc);
            TextView textView_thankYou = dialog.findViewById(R.id.textView_thankYou_msg_doc);
            TextView textView_msg = dialog.findViewById(R.id.textView_msg_doc);
            TextView textView_orderId = dialog.findViewById(R.id.textView_orderId_doc);

            textView_title.setText(paymentSubmitRP.getTitle());
            textView_thankYou.setText(paymentSubmitRP.getThank_you_msg());
            textView_msg.setText(paymentSubmitRP.getOrd_confirm_msg());
            textView_orderId.setText(paymentSubmitRP.getOrder_unique_id());

            dialog.show();

            button_shop.setOnClickListener(v -> {
                dialog.dismiss();
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finishAffinity();
            });

            button_myOrder.setOnClickListener(v -> {
                dialog.dismiss();
                activity.startActivity(new Intent(activity, MainActivity.class)
                        .putExtra("type", "my_order"));
                activity.finishAffinity();
            });
        }

    }

    @SuppressLint("HardwareIds")
    public String getDeviceId() {
        String deviceId;
        try {
            deviceId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            deviceId = "NotFound";
        }
        return deviceId;
    }

}
