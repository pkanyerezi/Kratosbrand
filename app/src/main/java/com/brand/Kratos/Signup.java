package com.brand.Kratos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.brand.Kratos.customfonts.Button_SF_Pro_Display_Semibold;
import com.brand.Kratos.customfonts.EditText_SF_Pro_Display_Medium;
import com.brand.Kratos.customfonts.TextViewSFProDisplayRegular;
import com.brand.Kratos.model.Registration.CallbackResponseRegistration;
import com.brand.Kratos.network.api;
import com.google.gson.Gson;
import com.rilixtech.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Signup extends AppCompatActivity {
    TextViewSFProDisplayRegular login_btn;
    EditText_SF_Pro_Display_Medium confirm_password_txt_id;
    EditText_SF_Pro_Display_Medium email_txt_id;
    EditText_SF_Pro_Display_Medium password_txt_id;
    EditText_SF_Pro_Display_Medium name_txt_id;
    EditText_SF_Pro_Display_Medium phone_txt_id;
    Button_SF_Pro_Display_Semibold signup_btn;
    CountryCodePicker country_code_sp;
    String Full_phone;
    JSONObject signup_details;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        country_code_sp = (CountryCodePicker) findViewById(R.id.country_code_sp);
        confirm_password_txt_id = (EditText_SF_Pro_Display_Medium) findViewById(R.id.confirm_password_txt_id);
        email_txt_id = (EditText_SF_Pro_Display_Medium) findViewById(R.id.email_txt_id);
        password_txt_id = (EditText_SF_Pro_Display_Medium) findViewById(R.id.password_txt_id);
        name_txt_id = (EditText_SF_Pro_Display_Medium) findViewById(R.id.name_txt_id);
        phone_txt_id = (EditText_SF_Pro_Display_Medium) findViewById(R.id.phone_txt_id);
        login_btn = (TextViewSFProDisplayRegular) findViewById(R.id.login_txt_id);
        signup_btn = (Button_SF_Pro_Display_Semibold) findViewById(R.id.signup_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                Signup.this.startActivity(intent);
            }
        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String confirm_password_sp = confirm_password_txt_id.getText().toString();
                String email_txt = email_txt_id.getText().toString();
                final String password_sp = password_txt_id.getText().toString();
                final String phone_txt_id_txt = phone_txt_id.getText().toString();
                String full_name_sp = name_txt_id.getText().toString();
                String CountryCodePicker_txt = country_code_sp.getSelectedCountryCode().toString();
                final String country_code_name = country_code_sp.getSelectedCountryNameCode();
                Full_phone  = CountryCodePicker_txt+phone_txt_id_txt;
                PhoneNumberUtil util = null;
                if (util == null) {
                    util = PhoneNumberUtil.createInstance(getApplicationContext());
                }
                Phonenumber.PhoneNumber phoneNumber = null;
                try {
                    phoneNumber = util.parse(Full_phone , ""+country_code_name);
                    System.out.println("xxx"+util.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
                } catch (NumberParseException e) {
                    e.printStackTrace();
                }
                System.out.println("xxx"+phoneNumber);
                Full_phone = util.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                boolean isValid = util.isValidNumber(phoneNumber);

                if (full_name_sp.isEmpty() || full_name_sp.length() < 4 ){

                    Toasty.error(Signup.this, "Provide a valid name.", Toast.LENGTH_SHORT, true).show();

                }else if(!isValidEmail(email_txt) ){
                    Toasty.error(Signup.this, "Provide a valid email.", Toast.LENGTH_SHORT, true).show();

                }else if(!isValid ){
                    Toasty.error(Signup.this, "Provide a valid phone number.", Toast.LENGTH_SHORT, true).show();


                }else if(email_txt.isEmpty() || email_txt.length() < 4 ){
                    Toasty.error(Signup.this, "Provide a valid email.", Toast.LENGTH_SHORT, true).show();

                }else if(password_sp.isEmpty() || password_sp.length() < 4 ){
                    Toasty.error(Signup.this, "Provide a valid password.", Toast.LENGTH_SHORT, true).show();


                }else if(password_sp.isEmpty()|| !password_sp.equals(confirm_password_sp) ){

                    Toasty.error(Signup.this, "Passwords don't match", Toast.LENGTH_SHORT, true).show();

                }else {
                    signup_btn.setText("Loading...");
                    signup_btn.setEnabled(false);
                    signup_details = new JSONObject();
                    try {
                        signup_details.put("name", full_name_sp);
                        signup_details.put("phone_number", "+"+CountryCodePicker_txt+phone_txt_id_txt);
                        signup_details.put("password", password_sp);
                        signup_details.put("email", email_txt);
                        signup_details.put("platform", "ANDROID");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Map<String, String> requestBody = new HashMap<>();
                    requestBody.put("full_name", full_name_sp);
                    requestBody.put("phone_number", "+"+CountryCodePicker_txt+phone_txt_id_txt);
                    requestBody.put("password", password_sp);
                    requestBody.put("email", email_txt);
                    requestBody.put("platform", "ANDROID");


             /*   Intent intent = new Intent(Signup.this, Login.class);
                Signup.this.startActivity(intent);*/

                    // set header through OkHttpClient if API is authenticate API.
                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                    httpClient.addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request request = chain.request().newBuilder()
                                    //.addHeader("timestamp", "153138130")
                                    //.addHeader("authentication_key", "QJTpP/7rai7D7KF2RcNK=")
                                    .build();
                            return chain.proceed(request);
                        }
                    });
                    httpClient.interceptors().add(new LogJsonInterceptor());
                    Retrofit retrofit = new Retrofit.Builder()
                            // .baseUrl("http://188.166.157.18:3000/")
                            .baseUrl(api.BaseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build())
                            .build();
                    final Gson gson = new Gson();
                    api apiCallInterface = retrofit.create(api.class);
                    // Call<ResponseSignup> call = apiCallInterface.getResponseSignup(gson.toJson(signup_details.toString()));
                    Call<CallbackResponseRegistration> call = apiCallInterface.getsignupResponse(requestBody);
                    // call.enqueue(new Callback<CountryResponse>() {
                    System.out.println("signup_details"+gson.toJson(signup_details.toString()));

                    // call.enqueue(new Callback<CountryResponse>() {
                    System.out.println("signup_details"+gson.toJson(signup_details.toString()));
                    call.enqueue(new Callback<CallbackResponseRegistration>() {
                        @Override
                        public void onResponse(Call<CallbackResponseRegistration> call, Response<CallbackResponseRegistration> response) {
                            signup_btn.setText("Create Account");
                            signup_btn.setEnabled(true);
                            if (response.isSuccessful()) {
                                Log.d("SuccessfulRequest",response.body().getMessage());
                                String msg = response.body().getMessage();

                                System.out.println("signup_details"+msg+"_"+response.body().getCode());

                                if(response.body().getCode() != 200){
                                    Toasty.error(Signup.this, ""+msg, Toast.LENGTH_SHORT, true).show();

                                }else{
                                    pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putBoolean("target_view_status", false);
                                    editor.putBoolean("login_status", true);
                                    editor.putBoolean("verification_status", false);
                                    editor.putString("phone","+"+country_code_name+phone_txt_id_txt);
                                    editor.putString("password",password_sp);
                                    editor.putString("token","");
                                    editor.putString("name","");

                                    editor.commit();
                                    Toasty.success(Signup.this, "Successful"+msg, Toast.LENGTH_SHORT, true).show();
                                    Intent intent = new Intent(Signup.this, Login.class);
                                    Signup.this.startActivity(intent);
                                    finish();
                                }
                            }
                            else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toasty.error(Signup.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toasty.error(Signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                            //  Log.d("Res",response.body().toString());


                        }

                        @Override
                        public void onFailure(Call<CallbackResponseRegistration> call, Throwable t) {
                            System.out.println("signup_details"+t.getMessage());
                            signup_btn.setText("Signup");
                            signup_btn.setEnabled(true);
                            Toasty.error(Signup.this, "Invalid Response.Connection Failure", Toast.LENGTH_SHORT, true).show();

                        }
                    });

                }
            }
        });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
