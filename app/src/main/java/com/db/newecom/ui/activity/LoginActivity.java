package com.db.newecom.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.db.newecom.Api.ApiClient;
import com.db.newecom.Api.ApiInterface;
import com.db.newecom.R;
import com.db.newecom.Response.DataRP;
import com.db.newecom.Response.LoginRP;
import com.db.newecom.Response.RegisterRP;
import com.db.newecom.Utills.API;
import com.db.newecom.Utills.Events;
import com.db.newecom.Utills.GlobalBus;
import com.db.newecom.Utills.Method;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ImageView close_btn, google_btn, facebook_btn;
    private EditText et_email, et_password, et_fpass_email;
    private Button login_btn, fpass_btn;
    private TextView signup_txt, forgetpass_txt, back_to_login;
    private LinearLayout login_ll, forgetpass_ll;
    private Method method;
    private String email, password, femail;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 007;
    private CallbackManager callbackManager;
    public static String myPreference = "mypref";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        close_btn = findViewById(R.id.close_btn);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_fpass_email = findViewById(R.id.et_fpass_email);
        login_btn = findViewById(R.id.login_btn);
        fpass_btn = findViewById(R.id.fpass_btn);
        google_btn = findViewById(R.id.google_btn);
        facebook_btn = findViewById(R.id.facebook_btn);
        signup_txt = findViewById(R.id.signup_txt);
        forgetpass_txt = findViewById(R.id.forgetpass_txt);
        back_to_login = findViewById(R.id.back_to_login);
        login_ll = findViewById(R.id.login_ll);
        forgetpass_ll = findViewById(R.id.forgetpass_ll);

        pref = getSharedPreferences(myPreference, 0);
        editor = pref.edit();

        progressDialog = new ProgressDialog(LoginActivity.this);
        method = new Method(LoginActivity.this);

        close_btn.setOnClickListener(view -> onBackPressed());
        signup_txt.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });

        login_btn.setOnClickListener(view -> {

            email = et_email.getText().toString();
            password = et_password.getText().toString();

            et_email.clearFocus();
            et_password.clearFocus();
            
            login();

        });

        fpass_btn.setOnClickListener(view -> {

            femail = et_fpass_email.getText().toString();
            et_fpass_email.setError(null);
            et_fpass_email.clearFocus();

            if (femail.equals("")) {
                et_fpass_email.requestFocus();
                et_fpass_email.setError(getResources().getString(R.string.please_enter_email));
            }
            else if (!isValidMail(femail)){
                et_fpass_email.requestFocus();
                et_fpass_email.setError(getResources().getString(R.string.please_enter_valid_email));
            }
            else {
                if (method.isNetworkAvailable(this)) {
                    forgetPassword(femail);
                } else {
                    method.alertBox(getResources().getString(R.string.no_internet_connection));
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        google_btn.setOnClickListener(view -> gogglelogin());

        callbackManager = CallbackManager.Factory.create();

        facebook_btn.setOnClickListener(view -> {
            if (view == facebook_btn) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fbUser(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d("data_app", "");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("error_fb", error.toString());
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        forgetpass_txt.setOnClickListener(view -> {
            login_ll.setVisibility(View.GONE);
            forgetpass_ll.setVisibility(View.VISIBLE);
        });

        back_to_login.setOnClickListener(view -> {
            login_ll.setVisibility(View.VISIBLE);
            forgetpass_ll.setVisibility(View.GONE);
        });

    }

    private void gogglelogin() {

        if (method.isNetworkAvailable(this)) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            method.alertBox(getResources().getString(R.string.no_internet_connection));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            assert account != null;

            String photo = "";

            if (account.getPhotoUrl() != null)
                photo = account.getPhotoUrl().toString();

            registerSocialNetwork(account.getId(), account.getDisplayName(), account.getEmail(), "google", photo);

        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
        }
    }

    private void fbUser(LoginResult loginResult) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
            try {
                String id = object.getString("id");
                String name = object.getString("name");
                String email = object.getString("email");
                String photo_url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                registerSocialNetwork(id, name, email, "facebook", photo_url);
            } catch (JSONException e) {
                try {
                    String id = object.getString("id");
                    String name = object.getString("name");
                    String photo_url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    registerSocialNetwork(id, name, "", "facebook", photo_url);
                } catch (JSONException e1) {

                    try {
                        String id = object.getString("id");
                        String name = object.getString("name");
                        String email = object.getString("email");
                        registerSocialNetwork(id, name, email, "facebook", "");
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.type(large)"); // Parameters that we ask for facebook
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void login() {

        et_email.setError(null);
        et_password.setError(null);

        if (email.isEmpty()) {
            et_email.requestFocus();
            et_email.setError(getResources().getString(R.string.please_enter_email));
        }else if (!isValidMail(email)){
            et_email.requestFocus();
            et_email.setError(getResources().getString(R.string.please_enter_valid_email));
        }else if (password.isEmpty()) {
            et_password.requestFocus();
            et_password.setError(getResources().getString(R.string.please_enter_password));
        } else {
            et_email.clearFocus();
            et_password.clearFocus();

            if (method.isNetworkAvailable(LoginActivity.this)) {
                login(email, password, "Normal");
            } else {
                method.alertBox(getResources().getString(R.string.no_internet_connection));
            }

        }

    }

    private void login(String email, String password, String normal) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsonObject = (JsonObject) new Gson().toJsonTree(new API(LoginActivity.this));
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<LoginRP> call = apiService.getLogin(API.toBase64(jsonObject.toString()));
        call.enqueue(new Callback<LoginRP>() {
            @Override
            public void onResponse(Call<LoginRP> call, Response<LoginRP> response) {

                try {
                    LoginRP loginRP = response.body();

                    assert loginRP != null;
                    if (loginRP.getSuccess().equals("1")){

                        method.editor.putBoolean(method.prefLogin, true);
                        method.editor.putString(method.profileId, loginRP.getUser_id());
                        method.editor.putString(method.userEmail, email);
                        method.editor.putString(method.loginType, normal);
                        method.editor.commit();

                        et_email.setText("");
                        et_password.setText("");

                        Toast.makeText(LoginActivity.this, "Login Successful...", Toast.LENGTH_SHORT).show();

                        if (Method.goToLogin()) {
                            Events.Login loginNotify = new Events.Login(loginRP.getName(), loginRP.getEmail(), loginRP.getUser_image());
                            GlobalBus.getBus().post(loginNotify);
                            Method.login(false);
                            onBackPressed();
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finishAffinity();
                        }

                    } else {
                        method.alertBox(loginRP.getMsg());
                    }

                }
                catch (Exception e){
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<LoginRP> call, Throwable t) {

                Log.e("fail_api", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));

            }
        });
    }

    @SuppressLint("HardwareIds")
    public void registerSocialNetwork(String id, String sendName, String sendEmail, final String type, String photo_url) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API(LoginActivity.this));
        jsObj.addProperty("name", sendName);
        jsObj.addProperty("email", sendEmail);
        jsObj.addProperty("auth_id", id);
        jsObj.addProperty("device_id", method.getDeviceId());
        jsObj.addProperty("type", type);
        jsObj.addProperty("register_platform", "android");
        jsObj.addProperty("photo_url", photo_url);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<RegisterRP> call = apiService.getRegister(API.toBase64(jsObj.toString()));
        call.enqueue(new Callback<RegisterRP>() {
            @Override
            public void onResponse(@NotNull Call<RegisterRP> call, @NotNull Response<RegisterRP> response) {

                try {
                    RegisterRP registerRP = response.body();
                    assert registerRP != null;

                    if (registerRP.getStatus().equals("1")) {

                        if (registerRP.getSuccess().equals("1")) {

                            method.editor.putBoolean(method.prefLogin, true);
                            method.editor.putString(method.profileId, registerRP.getUser_id());
                            method.editor.putString(method.userEmail, registerRP.getEmail());
                            method.editor.putString(method.loginType, type);
                            method.editor.commit();

                            Toast.makeText(LoginActivity.this, registerRP.getMsg(), Toast.LENGTH_SHORT).show();

                            if (Method.goToLogin()) {
                                Events.Login loginNotify = new Events.Login(registerRP.getName(), registerRP.getEmail(), registerRP.getUser_image());
                                GlobalBus.getBus().post(loginNotify);
                                Method.login(false);
                                onBackPressed();
                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finishAffinity();
                            }

                        } else {
                            failLogin(type);
                            method.alertBox(registerRP.getMsg());
                        }

                    } else {
                        failLogin(type);
                        method.alertBox(registerRP.getMessage());
                    }

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<RegisterRP> call, @NotNull Throwable t) {
                // Log error here since request failed
                Log.e("fail_api", t.toString());
                progressDialog.dismiss();
                method.alertBox(getResources().getString(R.string.failed_try_again));
            }
        });
    }

    private void failLogin(String type) {
        if (type.equals("google")) {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        method.editor.putBoolean(method.prefLogin, false);
                        method.editor.commit();
                    });
        } else {
            LoginManager.getInstance().logOut();
        }
    }

    private void forgetPassword(String femail) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        JsonObject jsonObject = (JsonObject) new Gson().toJsonTree(new API(LoginActivity.this));
        jsonObject.addProperty("email", femail);
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<DataRP> call = apiService.getForgotPass(API.toBase64(jsonObject.toString()));
        call.enqueue(new Callback<DataRP>() {
            @Override
            public void onResponse(Call<DataRP> call, Response<DataRP> response) {

                try {

                    DataRP dataRP = response.body();
                    assert dataRP != null;

                    if (dataRP.getSuccess().equals("1")) {
                        et_fpass_email.setText("");
                    }
                    method.alertBox(dataRP.getMsg());

                } catch (Exception e) {
                    Log.d("exception_error", e.toString());
                    method.alertBox(getResources().getString(R.string.failed_try_again));
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