package com.db.newecom.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.Model.Country;
import com.db.newecom.R;
import com.db.newecom.Response.AddEditRemoveAddressRP;
import com.db.newecom.Response.CountryRP;
import com.db.newecom.Response.GetAddressRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_Address_Activity extends AppCompatActivity {

    private boolean isEdit;
    private Method method;
    private ProgressDialog progressDialog;
    private EditText et_name_address, et_email_address, et_phoneNo_address, et_alter_phoneNo_address,
            et_houseName_address, et_roadName_address, et_landmark_address, et_state_address, et_district_address,
            et_city_address, et_pinCode_address;
    private Spinner spinner;
    private RadioGroup radioGroup;
    private List<Country> countryLists;
    private Button save_btn;
    private String selectCountry, addressId;
    private String addressType, type, typeOrder, product_id, product_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        type = getIntent().getStringExtra("type");
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        addressId = getIntent().getStringExtra("address_id");

        if (type.equals("check_address")){
            typeOrder = getIntent().getStringExtra("type_order");
            product_id = getIntent().getStringExtra("product_id");
            product_size = getIntent().getStringExtra("product_size");
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (isEdit) {
                getSupportActionBar().setTitle(R.string.edit_address);
            }
        }

        method = new Method(this);
        countryLists = new ArrayList<>();
        selectCountry = getResources().getString(R.string.select_country);
        progressDialog = new ProgressDialog(this);

        et_name_address = findViewById(R.id.et_name_address);
        et_email_address = findViewById(R.id.et_email_address);
        et_phoneNo_address = findViewById(R.id.et_phoneNo_address);
        et_alter_phoneNo_address = findViewById(R.id.et_alter_phoneNo_address);
        et_houseName_address = findViewById(R.id.et_houseName_address);
        et_roadName_address = findViewById(R.id.et_roadName_address);
        et_landmark_address = findViewById(R.id.et_landmark_address);
        et_state_address = findViewById(R.id.et_state_address);
        et_district_address = findViewById(R.id.et_district_address);
        et_city_address = findViewById(R.id.et_city_address);
        et_pinCode_address = findViewById(R.id.et_pinCode_address);
        spinner = findViewById(R.id.spinner_add_address);
        radioGroup = findViewById(R.id.radioGroup_address);
        save_btn = findViewById(R.id.save_btn);

        if (method.isNetworkAvailable(this)) {
            if (method.isLogin()) {
                getCountry(method.userId());
            } else {
                getCountry("");
            }
        } else {
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

    }

    private void getCountry(String userId) {
        countryLists.clear();

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("user_id", userId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<CountryRP> call = apiService.getCountry(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<CountryRP>() {
            @Override
            public void onResponse(@NotNull Call<CountryRP> call, @NotNull Response<CountryRP> response) {

                try {
                    CountryRP countryRP = response.body();
                    assert countryRP != null;

                    if (countryRP.getStatus().equals("1")) {

                        et_name_address.setText(countryRP.getUser_name());
                        et_email_address.setText(countryRP.getUser_email());
                        et_phoneNo_address.setText(countryRP.getUser_phone());

                        RadioButton rbHome = (RadioButton) radioGroup.getChildAt(0);
                        RadioButton rbOffice = (RadioButton) radioGroup.getChildAt(1);
                        rbHome.setText(countryRP.getHome_address_lbl());
                        rbOffice.setText(countryRP.getOffice_address_lbl());

                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @SuppressLint("ResourceType")
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                RadioButton rb = group.findViewById(checkedId);
                                if (null != rb && checkedId > -1) {
                                    if (rb.getText().equals(countryRP.getHome_address_lbl())) {
                                        addressType = "1";
                                    } else {
                                        addressType = "2";
                                    }
                                }
                            }
                        });

                        if (isEdit) {
                            getAddress(addressId);
                        }

                        countryLists.add(new Country(selectCountry));
                        countryLists.addAll(countryRP.getCountryLists());

                        List<String> strings = new ArrayList<>();
                        for (int i = 0; i < countryLists.size(); i++) {
                            strings.add(countryLists.get(i).getCountry_name());
                        }

                        // Creating adapter for spinner_cat
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Add_Address_Activity.this, android.R.layout.simple_spinner_item, strings);
                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // attaching data adapter to spinner_cat
                        spinner.setAdapter(dataAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i == 0) {
                                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.dark_grey));
                                } else {
                                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                                }
                                selectCountry = countryLists.get(i).getCountry_name();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        save_btn.setOnClickListener(view -> submit());

                    }
                    else {
                        method.alertBox(countryRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<CountryRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail_api", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    private void getAddress(String addressId) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("address_id", addressId);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<GetAddressRP> call = apiService.getAddress(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<GetAddressRP>() {
            @Override
            public void onResponse(@NotNull Call<GetAddressRP> call, @NotNull Response<GetAddressRP> response) {

                try {

                    GetAddressRP getAddressRP = response.body();

                    assert getAddressRP != null;

                    if (getAddressRP.getStatus().equals("1")) {

                        et_pinCode_address.setText(getAddressRP.getPincode());
                        et_houseName_address.setText(getAddressRP.getBuilding_name());
                        et_roadName_address.setText(getAddressRP.getRoad_area_colony());
                        et_city_address.setText(getAddressRP.getCity());
                        et_district_address.setText(getAddressRP.getDistrict());
                        et_state_address.setText(getAddressRP.getState());
                        et_landmark_address.setText(getAddressRP.getLandmark());
                        et_name_address.setText(getAddressRP.getName());
                        et_email_address.setText(getAddressRP.getEmail());
                        et_phoneNo_address.setText(getAddressRP.getMobile_no());
                        et_alter_phoneNo_address.setText(getAddressRP.getAlter_mobile_no());

                        selectCountry = getAddressRP.getCountry();
                        int countryPosition = 0;

                        for (int i = 0; i < countryLists.size(); i++) {
                            if(countryLists.get(i).getCountry_name().equals(selectCountry))
                                countryPosition = i;
                        }

                        spinner.setSelection(countryPosition);

                        addressType = getAddressRP.getAddress_type();
                        if (addressType.equals("1")) {
                            radioGroup.check(radioGroup.getChildAt(0).getId());
                        } else {
                            radioGroup.check(radioGroup.getChildAt(1).getId());
                        }

                    } else {
                        method.alertBox(getAddressRP.getMessage());
                    }


                } catch (Exception e) {
                    Log.d("Exception_error", e.toString());
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<GetAddressRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                progressDialog.dismiss();
                Log.e("Fail_api", t.toString());
            }
        });

    }

    private void submit() {

        String pinCode = et_pinCode_address.getText().toString();
        String houseName = et_houseName_address.getText().toString();
        String roadName = et_roadName_address.getText().toString();
        String city = et_city_address.getText().toString();
        String state = et_state_address.getText().toString();
        String district = et_district_address.getText().toString();
        String landmark = et_landmark_address.getText().toString();
        String name = et_name_address.getText().toString();
        String email = et_email_address.getText().toString();
        String phoneNo = et_phoneNo_address.getText().toString();
        String alterNatePhoneNo = et_alter_phoneNo_address.getText().toString();

        if (name.equals("") || name.isEmpty()) {
            et_name_address.requestFocus();
            et_name_address.setError(getResources().getString(R.string.please_enter_name));
        } else if (!isValidMail(email) || email.isEmpty()) {
            et_email_address.requestFocus();
            et_email_address.setError(getResources().getString(R.string.please_enter_email));
        } else if (phoneNo.equals("") || phoneNo.isEmpty()) {
            et_phoneNo_address.requestFocus();
            et_phoneNo_address.setError(getResources().getString(R.string.please_enter_phone));
        } else if (houseName.equals("") || houseName.isEmpty()) {
            et_houseName_address.requestFocus();
            et_houseName_address.setError(getResources().getString(R.string.please_enter_details));
        } else if (roadName.equals("") || roadName.isEmpty()) {
            et_roadName_address.requestFocus();
            et_roadName_address.setError(getResources().getString(R.string.please_enter_details));
        } else if (selectCountry == null || selectCountry.equals("") || selectCountry.equals(getResources().getString(R.string.select_country))) {
            method.alertBox(getResources().getString(R.string.please_select_country));
        } else if (state.equals("") || state.isEmpty()) {
            et_state_address.requestFocus();
            et_state_address.setError(getResources().getString(R.string.please_enter_state));
        } else if (city.equals("") || city.isEmpty()) {
            et_city_address.requestFocus();
            et_city_address.setError(getResources().getString(R.string.please_enter_city));
        } else if (pinCode.equals("") || pinCode.isEmpty()) {
            et_pinCode_address.requestFocus();
            et_pinCode_address.setError(getResources().getString(R.string.please_enter_pinCode));
        } else if (addressType == null || addressType.equals("") || addressType.isEmpty()) {
            method.alertBox(getResources().getString(R.string.please_select_address_type));
        } else {

            et_pinCode_address.clearFocus();
            et_houseName_address.clearFocus();
            et_roadName_address.clearFocus();
            et_city_address.clearFocus();
            et_state_address.clearFocus();
            et_district_address.clearFocus();
            et_landmark_address.clearFocus();
            et_name_address.clearFocus();
            et_email_address.clearFocus();
            et_phoneNo_address.clearFocus();
            et_alter_phoneNo_address.clearFocus();

            if (method.isNetworkAvailable(this)) {
                if (method.isLogin()) {
                    addAddress(name, pinCode, houseName, roadName, city, state, district, landmark, phoneNo, alterNatePhoneNo, email, isEdit);
                } else {
                    method.alertBox(getResources().getString(R.string.you_have_not_login));
                }
            } else {
                method.alertBox(getResources().getString(R.string.no_internet_connection));
            }
        }
    }

    private void addAddress(String name, String pinCode, String houseName, String roadName, String city, String state, String district, String landmark, String phoneNo, String alterNatePhoneNo, String email, boolean isedit) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(this));
        jsObj.addProperty("user_id", method.userId());
        jsObj.addProperty("pincode", pinCode);
        jsObj.addProperty("building_name", houseName);
        jsObj.addProperty("road_area_colony", roadName);
        jsObj.addProperty("city", city);
        jsObj.addProperty("district", district);
        jsObj.addProperty("state", state);
        jsObj.addProperty("landmark", landmark);
        jsObj.addProperty("name", name);
        jsObj.addProperty("mobile_no", phoneNo);
        jsObj.addProperty("alter_mobile_no", alterNatePhoneNo);
        jsObj.addProperty("email", email);
        jsObj.addProperty("country", selectCountry);
        jsObj.addProperty("address_type", addressType);
        if (isedit) {
            jsObj.addProperty("id", addressId);
            jsObj.addProperty("type", "edit");
        }
        else
            jsObj.addProperty("type", "add");

        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<AddEditRemoveAddressRP> call = apiService.addEditAddress(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<AddEditRemoveAddressRP>() {
            @Override
            public void onResponse(@NotNull Call<AddEditRemoveAddressRP> call, @NotNull Response<AddEditRemoveAddressRP> response) {

                try {

                    AddEditRemoveAddressRP addEditRemoveAddressRp = response.body();

                    assert addEditRemoveAddressRp != null;

                    if (addEditRemoveAddressRp.getStatus().equals("1")){
                        if (addEditRemoveAddressRp.getSuccess().equals("1")) {

                            radioGroup.clearCheck();
                            addressType = "";
                            selectCountry = getResources().getString(R.string.select_country);

                            /**
                             * - User choose cart option in profile section and add or edit address then update address MyAddressFragment and profileFragment
                             * - Add or edit address using My Address Fragment then update data
                             */
                            Events.EventMYAddress eventMYAddress = new Events.EventMYAddress(addEditRemoveAddressRp.getAddress(), addEditRemoveAddressRp.getAddress_count(), "addAddress");
                            GlobalBus.getBus().post(eventMYAddress);

                            Events.OnBackData onBackData = new Events.OnBackData("");
                            GlobalBus.getBus().post(onBackData);

                            Toast.makeText(Add_Address_Activity.this, addEditRemoveAddressRp.getMsg(), Toast.LENGTH_SHORT).show();

                            onBackPressed();

                            //check_address this tag used to the check address available or not (detail fragment and cart activity)
                            if (type.equals("check_address")) {

                                startActivity(new Intent(Add_Address_Activity.this, OrderSummaryActivity.class)
                                        .putExtra("type", typeOrder)
                                        .putExtra("product_id", product_id)
                                        .putExtra("product_size", product_size)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();

                            } else {

                                // add_my_address -> MyAddressFragment
                                // edit_my_add -> change MyAddressAdapter
                                if (type.equals("add_my_address") || type.equals("edit_my_add")) {
                                    onBackPressed();
                                }

                                //add_change_address -> change address activity add address
                                //edit_change_add -> change change address adapter
                                if (type.equals("add_change_address") || type.equals("edit_change_add")) {
                                    if (addEditRemoveAddressRp.getSuccess().equals("1")) {

                                        Events.UpdateDeliveryAddress updateDeliveryAddress = new Events.UpdateDeliveryAddress(addressId, addEditRemoveAddressRp.getAddress(),
                                                addEditRemoveAddressRp.getName(), addEditRemoveAddressRp.getMobile_no(), addEditRemoveAddressRp.getAddress_type());
                                        GlobalBus.getBus().post(updateDeliveryAddress);

                                        onBackPressed();

                                    }

                                }
                            }

                        } else {
                            method.alertBox(addEditRemoveAddressRp.getMsg());
                        }
                    }
                    else {
                        method.alertBox(addEditRemoveAddressRp.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("Exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<AddEditRemoveAddressRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("Fail_api", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });

    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
            onBackPressed();
        else
            return super.onOptionsItemSelected(item);

        return true;
    }
}