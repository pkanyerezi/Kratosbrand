package com.brand.Kratos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.brand.Kratos.customfonts.EditText_SF_Pro_Display_Medium;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Medium;
import com.brand.Kratos.model.Login.CallbackResponseLogin;
import com.brand.Kratos.network.api;
import com.rilixtech.CountryCodePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
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


public class ConfirmPhone extends AppCompatActivity {
    MyTextView_Roboto_Medium username_id;
    CountryCodePicker country_code_sp;
    EditText_SF_Pro_Display_Medium phone_txt_id;
    CircleImageView image_thumbnail;
    MyTextView_Roboto_Medium next_id;
    MyTextView_Roboto_Medium user_email_id;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_phone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        username_id = (MyTextView_Roboto_Medium) findViewById(R.id.username_id);
        user_email_id = (MyTextView_Roboto_Medium) findViewById(R.id.user_email_id);
        country_code_sp = (CountryCodePicker) findViewById(R.id.country_code_sp);
        phone_txt_id = (EditText_SF_Pro_Display_Medium) findViewById(R.id.phone_txt_id);
        image_thumbnail = (CircleImageView) findViewById(R.id.image_thumbnail);
        next_id = (MyTextView_Roboto_Medium) findViewById(R.id.next_id);
        setSupportActionBar(toolbar);

        final String social_type = getIntent().getStringExtra("type");
        final String email_social = getIntent().getStringExtra("email").replace('"',' ').replace(" ","");
        final String social_token = getIntent().getStringExtra("social_token");
        final String full_name = getIntent().getStringExtra("full_name").replace('"',' ').replace(" ","");
        String photourl = getIntent().getStringExtra("photourl");
        final String access_id = getIntent().getStringExtra("access_id");
        username_id.setText(full_name.replace('"',' ').replace(" ",""));
        user_email_id.setText(email_social.replace('"',' ').replace(" ",""));
        System.out.println("photourl"+photourl+" email_social "+email_social+" social_type "+social_type);
        Picasso.get()
                .load(photourl.toString().replace('"',' ').replace(" ",""))
                .placeholder(R.drawable.imageplaceholder )

                // .resize(50, 50)
                // .centerCrop()
                .fit().centerCrop()
                .into(image_thumbnail);
        next_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String code = country_code_sp.getSelectedCountryCode();
            String phone = phone_txt_id.getText().toString();
            final String country_code_name = country_code_sp.getSelectedCountryNameCode();
            String Full_phone =code+""+phone;
                //Full_phone  = CountryCodePicker_txt+phone_txt_id_txt;
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
                if(!isValid){
                    Toasty.error(ConfirmPhone.this, "Provide a valid phone number.", Toast.LENGTH_SHORT, true).show();

                }else {
                    Map<String, String> requestBody = new HashMap<>();
                    requestBody.put("email", email_social);
                    requestBody.put("password", "password");
                    requestBody.put("type", social_type.replace(" ",""));
                    requestBody.put("platform", "ANDROID");
                    requestBody.put("full_name", full_name);
                    requestBody.put("phone_number", Full_phone);
                    requestBody.put("access_id", access_id);
                    requestBody.put("access_token", social_token);

                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                    httpClient.addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                            Request request = chain.request().newBuilder()
                                    //.addHeader("timestamp", "153138130")
                                    .addHeader("Authorize", "x-access-token: eyJh...24k")
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
                    api apiCallInterface = retrofit.create(api.class);
                    Call<CallbackResponseLogin> call = apiCallInterface.getloginResponse(requestBody);

                    final String finalFull_phone = Full_phone;
                    call.enqueue(new Callback<CallbackResponseLogin>() {
                        @Override
                        public void onResponse(Call<CallbackResponseLogin> call, Response<CallbackResponseLogin> response) {
                            next_id.setText("Confirm");
                            next_id.setEnabled(true);
                            if(response.isSuccessful()){
                                next_id.setText("Confirm");
                                next_id.setEnabled(true);

                                pref = getApplicationContext().getSharedPreferences("Settings_variables", 0); // 0 - for private mode
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putBoolean("login_status", true);
                                editor.putBoolean("intro_screen", true);
                                editor.putBoolean("tags_view", false);
                                editor.putBoolean("social_login", true);
                                editor.putString("email",email_social);
                                editor.putString("type", social_type.replace(" ",""));
                                editor.putString("phone_number", finalFull_phone);
                                editor.putString("password","password");
                                editor.putString("access_id", access_id);
                                editor.putString("access_token", social_token);
                                editor.putString("token",response.body().getToken());
                                editor.putInt("expiry",response.body().getExpiresIn());
                                editor.putString("full_name",response.body().getDisplayName());
                                editor.commit();
                                finish();
                                Intent intent2 = new Intent("finish_activity");
                                sendBroadcast(intent2);
                                Intent intent = new Intent(ConfirmPhone.this, MainContainer.class);
                                ConfirmPhone.this.startActivity(intent);

                            }else{
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toasty.error(ConfirmPhone.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toasty.error(ConfirmPhone.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                //Toasty.error(LoginTrue.this, "Unauthorized access", Toast.LENGTH_SHORT, true).show();


                            }

                        }

                        @Override
                        public void onFailure(Call<CallbackResponseLogin> call, Throwable t) {
                            Toasty.error(ConfirmPhone.this, "Invalid Response.Connection Failure", Toast.LENGTH_SHORT, true).show();

                            next_id.setText("Confirm");
                            next_id.setEnabled(true);
                        }
                    });
                }


                }

        });


    }

}
