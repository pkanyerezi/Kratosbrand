package com.brand.Kratos;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.brand.Kratos.adapter.TendingVideosAdapter;
import com.brand.Kratos.model.Login.CallbackResponseLogin;
import com.brand.Kratos.model.VideoContent.Data;
import com.brand.Kratos.model.VideoContent.ResponseContent;
import com.brand.Kratos.network.api;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//@DeepLink("https://www.kratosbrand.com/{id}/{info}")
//@DeepLink({"foo://example.com/deepLink/{id}", "foo://example.com/anotherDeepLink"})
public class SplashScreen extends AppCompatActivity {
    SharedPreferences pref;
    public static Intent main_intent;
    String prefix;
    public static Intent intent_deep;
    AlertDialog.Builder builder1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        // = getIntent();
        CountDownTimer start = new CountDownTimer(3000, 100) { //40000 milli seconds is total time, 1000 milli seconds is time interval

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                pref = getApplicationContext().getSharedPreferences("Settings_variables", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
    /*
    *   "email": "patrickkanyerezi@gmail.com",
  "password": "10u11136ps",
  "type": "BASIC",
  "platform": "ANDROID",
  "full_name": "string",
  "phone_number": "string",
  "access_id": "string",
  "access_token": "string"*/
                boolean logged_in = pref.getBoolean("login_status", false);
                boolean intro_screen = pref.getBoolean("intro_screen", false);
                boolean social_login = pref.getBoolean("social_login", false);
                String email = pref.getString("email", "");
                String password = pref.getString("password", "");
                String type = pref.getString("type", "BASIC");
                String platform = pref.getString("platform", "ANDROID");
                String full_name = pref.getString("full_name", "");
                String access_id = pref.getString("access_id", "");
                String access_token = pref.getString("access_token", "");
                String phone_number = pref.getString("phone_number", "");
                System.out.println(" logged_in " + logged_in + " intro_screen " + intro_screen+" "+email+" "+password+" "+type);
                if (!intro_screen) {
                    Intent intent = new Intent(SplashScreen.this, IntroActivity.class);
                    // Intent intent = new Intent(SplashScreen.this, LoginTrue.class);
                    SplashScreen.this.startActivity(intent);
                    finish();
                } else if (social_login && logged_in && intro_screen) {

                    login(email, password, type, platform, full_name, phone_number, access_id, access_token);

                } else if (!social_login && logged_in && intro_screen) {

                    login(email, password, type, platform, full_name, phone_number, access_id, access_token);

                    /* Intent intent = new Intent(SplashScreen.this, MainContainer.class);

                    SplashScreen.this.startActivity(intent);
                    finish();*/
                } else if (!logged_in) {
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    // Intent intent = new Intent(SplashScreen.this, LoginTrue.class);
                    SplashScreen.this.startActivity(intent);
                    finish();
                }
               // Uri uri = getIntent().getData();


            }
        }.start();

    }
    public void login(final String email, final String password, final String type, final String platform, final String full_name, final String phone_number, final String access_id, final String access_token){

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("type", type);
        requestBody.put("platform", platform);
        requestBody.put("full_name", full_name);
        requestBody.put("phone_number", phone_number);
        requestBody.put("access_id", access_id);
        requestBody.put("access_token", access_token);
System.out.println("xxxxx"+requestBody.toString());
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

        call.enqueue(new Callback<CallbackResponseLogin>() {
            @Override
            public void onResponse(Call<CallbackResponseLogin> call, Response<CallbackResponseLogin> response) {

                if(response.isSuccessful()){
                    pref = getApplicationContext().getSharedPreferences("Settings_variables", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("login_status", true);
                    editor.putBoolean("tags_view", true);
                    editor.putBoolean("intro_screen", true);
                    editor.putString("email",email.replace('"', ' '));
                    editor.putString("password",password);
                    editor.putString("type",type);
                    editor.putString("token",response.body().getToken());
                    editor.putInt("expiry",response.body().getExpiresIn());
                    editor.putString("full_name",response.body().getDisplayName().replace('"', ' '));
                    editor.commit();
                    finish();
                    main_intent = getIntent();
                    if (main_intent != null && main_intent.getData() != null && (main_intent.getData().getScheme().equals("https"))) {
                        Uri data = main_intent.getData();

                        String id = data.getQueryParameter("id");
                        System.out.println("prefxxx"+id);
                        Intent intent = new Intent(SplashScreen.this, MainContainer.class);
                        intent.putExtra("videocode",id);
                        intent.putExtra("deep_link",true);
                        SplashScreen.this.startActivity(intent);
                        //loadcontent_detals(id);
                    }else {
                        //System.out.println("param_id"+uri.getQuery());
                        Intent intent = new Intent(SplashScreen.this, MainContainer.class);
                        SplashScreen.this.startActivity(intent);
                    }

                }else{
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toasty.error(SplashScreen.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toasty.error(SplashScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    //Toasty.error(LoginTrue.this, "Unauthorized access", Toast.LENGTH_SHORT, true).show();


                }

            }

            @Override
            public void onFailure(Call<CallbackResponseLogin> call, Throwable t) {
                Toasty.error(SplashScreen.this, "Invalid Response.Connection Failure", Toast.LENGTH_SHORT, true).show();
                alert(email, password, type, platform, full_name, phone_number, access_id, access_token);

            }
        });
    }

    String secToTime(int sec) {
        int seconds = sec % 60;
        int minutes = sec / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            if( hours >= 24) {
                int days = hours / 24;
                return String.format("%d days %02d:%02d:%02d", days,hours%24, minutes, seconds);
            }
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("00:%02d:%02d", minutes, seconds);
    }

    public void alert(final String email, final String password, final String type, final String platform, final String full_name, final String phone_number, final String access_id, final String access_token){
        builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Connection Failure");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Try again",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        login(email, password, type, platform, full_name, phone_number, access_id, access_token);

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
