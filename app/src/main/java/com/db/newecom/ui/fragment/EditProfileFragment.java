package com.db.newecom.ui.fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.DataRP;
import com.db.newecom.Response.UserProfileRP;
import com.db.newecom.Response.UserProfileUpdateRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Method method;
    private ProgressDialog progressDialog;
    private LinearLayout progressBar;
    private RelativeLayout empty_layout;
    private LinearLayout edit_profile_layout, change_pass_layout;
    private TextView change_pass_txt;
    private Button save_btn, cancel_btn, change_btn;
    private EditText et_old_pass, et_new_pass, et_cnew_pass;
    private String old_pass, new_pass, cnew_pass, imageProfile;
    private CircleImageView user_img;
    private ImageView edit_profileimg_btn;
    private EditText et_editprofile_name, et_editprofile_email, et_editprofile_mobile;
    private boolean isProfile = false, isRemove = false;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        GlobalBus.getBus().register(this);
        method = new Method(getActivity());
        progressDialog = new ProgressDialog(getActivity(), R.style.ProgressDialogStyle);

        empty_layout = view.findViewById(R.id.empty_layout);
        progressBar = view.findViewById(R.id.ll_progress_edit_profile);
        edit_profile_layout = view.findViewById(R.id.edit_profile_layout);
        change_pass_layout = view.findViewById(R.id.change_pass_layout);
        change_pass_txt = view.findViewById(R.id.change_pass_txt);
        save_btn = view.findViewById(R.id.save_btn);
        et_old_pass = view.findViewById(R.id.et_old_pass);
        et_new_pass = view.findViewById(R.id.et_new_pass);
        et_cnew_pass = view.findViewById(R.id.et_cnew_pass);
        cancel_btn = view.findViewById(R.id.cancel_btn);
        change_btn = view.findViewById(R.id.change_btn);

        user_img = view.findViewById(R.id.user_img);
        edit_profileimg_btn = view.findViewById(R.id.edit_profileimg_btn);
        et_editprofile_name = view.findViewById(R.id.et_editprofile_name);
        et_editprofile_email = view.findViewById(R.id.et_editprofile_email);
        et_editprofile_mobile = view.findViewById(R.id.et_editprofile_mobile);

        et_editprofile_email.setEnabled(false);

        if (method.getLoginType().equals("facebook") || method.getLoginType().equals("google")) {
            change_pass_txt.setVisibility(View.GONE);
            edit_profileimg_btn.setVisibility(View.GONE);
        }

        edit_profileimg_btn.setOnClickListener(v -> {
            BottomSheetDialogFragment fragment = new ProfileImagePicker();
            fragment.show(getActivity().getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
        });

        save_btn.setOnClickListener(v -> checkEditProfileValidation());

        change_pass_txt.setOnClickListener(v -> {
            edit_profile_layout.setVisibility(View.GONE);
            change_pass_layout.setVisibility(View.VISIBLE);
        });

        if (method.isNetworkAvailable(getActivity())) {

            if (method.isLogin())
                profile(method.userId());
            else {
                progressBar.setVisibility(View.GONE);
                edit_profile_layout.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
                method.alertBox(getResources().getString(R.string.no_internet_connection));
            }
        }
        else {
            method.alertBox(getResources().getString(R.string.no_internet_connection));
            edit_profile_layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            empty_layout.setVisibility(View.VISIBLE);
        }

        change_btn.setOnClickListener(v -> {
            if (method.isNetworkAvailable(getActivity())) {
                checkpassvalidations();
            }
            else {
                method.alertBox(getResources().getString(R.string.no_internet_connection));
                progressBar.setVisibility(View.GONE);
                empty_layout.setVisibility(View.VISIBLE);
            }
        });

        cancel_btn.setOnClickListener(v -> {
            edit_profile_layout.setVisibility(View.VISIBLE);
            change_pass_layout.setVisibility(View.GONE);
        });

        return view;
    }

    @Subscribe
    public void getData(Events.ProImage proImage) {
        isProfile = proImage.isIs_profile();
        isRemove = proImage.isIs_remove();
        if (proImage.isIs_profile()) {
            imageProfile = proImage.getImage_path();
            Uri uri = Uri.fromFile(new File(imageProfile));
            Glide.with(getActivity().getApplicationContext()).load(uri)
                    .placeholder(R.drawable.default_profile)
                    .into(user_img);
        }
        if (proImage.isIs_remove()) {
            Glide.with(getActivity().getApplicationContext()).load(R.drawable.default_profile)
                    .placeholder(R.drawable.default_profile)
                    .into(user_img);
        }
    }
    
    private boolean isValidMobile(String mobile) {
        if(!Pattern.matches("[a-zA-Z]+", mobile)) {
            return mobile.length() > 6 && mobile.length() <= 13;
        }
        return false;
    }

    private void checkEditProfileValidation() {

        String name = et_editprofile_name.getText().toString();
        String phoneNo = et_editprofile_mobile.getText().toString();

        if (name.equals("") || name.isEmpty()) {
            et_editprofile_name.requestFocus();
            et_editprofile_name.setError(getResources().getString(R.string.please_enter_name));
        } else if (phoneNo.equals("") || phoneNo.isEmpty()) {
            et_editprofile_mobile.requestFocus();
            et_editprofile_mobile.setError(getResources().getString(R.string.please_enter_phone));
        } else if (!isValidMobile(phoneNo)) {
            et_editprofile_mobile.requestFocus();
            et_editprofile_mobile.setError(getResources().getString(R.string.please_enter_phone));
        } else {
            if (method.isNetworkAvailable(getActivity())) {

                et_editprofile_name.clearFocus();
                et_editprofile_mobile.clearFocus();
                profileUpdate(method.userId(), name, phoneNo, imageProfile);

            } else {
                method.alertBox(getResources().getString(R.string.no_internet_connection));
            }
        }

    }

    private void profile(String userId) {

        if (getActivity() != null){

            progressBar.setVisibility(View.VISIBLE);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<UserProfileRP> call = apiService.getProfile(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<UserProfileRP>() {
                @Override
                public void onResponse(Call<UserProfileRP> call, Response<UserProfileRP> response) {

                    if (getActivity() != null){

                        UserProfileRP userProfileRP = response.body();
                        assert userProfileRP != null;

                        try {

                            if(userProfileRP.getStatus().equals("1")){

                                if(userProfileRP.getSuccess().equals("1")){

                                    progressBar.setVisibility(View.GONE);
                                    empty_layout.setVisibility(View.GONE);
                                    imageProfile = userProfileRP.getUser_image();

                                    Glide.with(getActivity().getApplicationContext()).load(userProfileRP.getUser_image())
                                            .placeholder(R.drawable.default_profile).into(user_img);

                                    et_editprofile_name.setText(userProfileRP.getUser_name());
                                    et_editprofile_email.setText(userProfileRP.getUser_email());
                                    et_editprofile_mobile.setText(userProfileRP.getUser_phone());

                                }
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    empty_layout.setVisibility(View.VISIBLE);
                                    edit_profile_layout.setVisibility(View.GONE);
                                    method.alertBox(userProfileRP.getMsg());
                                }

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                empty_layout.setVisibility(View.VISIBLE);
                                edit_profile_layout.setVisibility(View.GONE);
                                method.alertBox(userProfileRP.getMessage());
                            }

                        }
                        catch (Exception e){
                            Log.d("exception_error", e.toString());
                            progressBar.setVisibility(View.GONE);
                            empty_layout.setVisibility(View.VISIBLE);
                            edit_profile_layout.setVisibility(View.GONE);
                            method.alertBox(userProfileRP.getMessage());
                        }

                    }
                }

                @Override
                public void onFailure(Call<UserProfileRP> call, Throwable t) {
                    Log.e("fail_api", t.toString());
                    empty_layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });
        }

    }

    private void profileUpdate(String userId, String name, String phoneNo, String imageProfile) {

        if (getActivity() != null) {

            progressDialog.show();
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);

            MultipartBody.Part body = null;

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("id", userId);
            jsObj.addProperty("user_name", name);
            jsObj.addProperty("user_phone", phoneNo);
            jsObj.addProperty("is_remove", isRemove);
            if (isProfile) {
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(imageProfile));
                // MultipartBody.Part is used to send also the actual file name
                body = MultipartBody.Part.createFormData("user_image", new File(imageProfile).getName(), requestFile);
            }
            // add another part within the multipart request
            RequestBody requestBody_data =
                    RequestBody.create(MediaType.parse("multipart/form-data"), API.toBase64(jsObj.toString()));
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<UserProfileUpdateRP> call = apiService.editProfile(requestBody_data, body);
            call.enqueue(new Callback<UserProfileUpdateRP>() {
                @Override
                public void onResponse(@NotNull Call<UserProfileUpdateRP> call, @NotNull Response<UserProfileUpdateRP> response) {

                    if (getActivity() != null) {
                        try {
                            UserProfileUpdateRP userProfileUpdateRP = response.body();
                            assert userProfileUpdateRP != null;

                            if (userProfileUpdateRP.getStatus().equals("1")) {

                                if (userProfileUpdateRP.getSuccess().equals("1")) {
                                    Toast.makeText(getActivity(), userProfileUpdateRP.getMsg(), Toast.LENGTH_SHORT).show();
                                    Events.Login loginNotify = new Events.Login(userProfileUpdateRP.getUser_name(), userProfileUpdateRP.getUser_email(), userProfileUpdateRP.getUser_image());
                                    GlobalBus.getBus().post(loginNotify);
                                    Events.ProfileUpdate profileUpdate = new Events.ProfileUpdate("");
                                    GlobalBus.getBus().post(profileUpdate);
                                } else {
                                    method.alertBox(userProfileUpdateRP.getMsg());
                                }

                            } else {
                                method.alertBox(userProfileUpdateRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d("exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(@NotNull Call<UserProfileUpdateRP> call, @NotNull Throwable t) {
                    // Log error here since request failed
                    Log.e("fail_api", t.toString());
                    progressDialog.dismiss();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });

        }

    }

    private void checkpassvalidations() {

        old_pass = et_old_pass.getText().toString();
        new_pass = et_new_pass.getText().toString();
        cnew_pass = et_cnew_pass.getText().toString();

        et_old_pass.setError(null);
        et_new_pass.setError(null);
        et_cnew_pass.setError(null);

        if (old_pass.equals("") || old_pass.isEmpty()) {
            et_old_pass.requestFocus();
            et_old_pass.setError(getActivity().getResources().getString(R.string.please_enter_old_password));
        } else if (new_pass.equals("") || new_pass.isEmpty()) {
            et_new_pass.requestFocus();
            et_new_pass.setError(getActivity().getResources().getString(R.string.please_enter_new_password));
        } else if (cnew_pass.equals("") || cnew_pass.isEmpty()) {
            et_cnew_pass.requestFocus();
            et_cnew_pass.setError(getActivity().getResources().getString(R.string.please_enter_new_password));
        } else if (!new_pass.equals(cnew_pass)) {
            method.alertBox("Password and confirm password does not match");
        } else {
            if (method.isNetworkAvailable(getActivity())) {

                et_old_pass.clearFocus();
                et_new_pass.clearFocus();
                et_cnew_pass.clearFocus();

                changePassword(method.userId(), old_pass, new_pass);

            } else {
                method.alertBox(getResources().getString(R.string.no_internet_connection));
            }
        }
    }

    private void changePassword(String userId, String old_pass, String new_pass) {

        if (getActivity() != null) {

            progressDialog.show();
            progressDialog.setMessage(getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);

            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(getActivity()));
            jsObj.addProperty("user_id", userId);
            jsObj.addProperty("old_password", old_pass);
            jsObj.addProperty("new_password", new_pass);
            ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
            Call<DataRP> call = apiService.changePassword(API.toBase64(jsObj.toString()));
            call.enqueue(new Callback<DataRP>() {
                @Override
                public void onResponse(Call<DataRP> call, Response<DataRP> response) {

                    if (getActivity() != null) {
                        try {
                            DataRP dataRP = response.body();
                            assert dataRP != null;

                            if (dataRP.getSuccess().equals("1")) {
                                et_old_pass.setText("");
                                et_new_pass.setText("");
                                et_cnew_pass.setText("");
                            }
                            method.alertBox(dataRP.getMsg());

                            if (dataRP.getStatus().equals("1")) {

                            } else {
                                method.alertBox(dataRP.getMessage());
                            }

                        } catch (Exception e) {
                            Log.d("Exception_error", e.toString());
                            method.alertBox(getResources().getString(R.string.failed_try_again));
                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<DataRP> call, Throwable t) {
                    Log.e("fail_api", t.toString());
                    progressDialog.dismiss();
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }
            });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }
}