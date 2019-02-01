package com.brand.Kratos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.brand.Kratos.customfonts.Button_SF_Pro_Display_Semibold;
import com.brand.Kratos.customfonts.EditText_Roboto_Regular;
import com.brand.Kratos.customfonts.EditText_SF_Pro_Display_Medium;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Regular;
import com.brand.Kratos.customfonts.TextViewSFProDisplayRegular;
import com.brand.Kratos.model.Login.CallbackResponseLogin;
import com.brand.Kratos.model.VideoContent.Data;
import com.brand.Kratos.model.VideoContent.ResponseContent;
import com.brand.Kratos.network.api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pkb.sociallogin.SocialLoginActivity;
import com.shaishavgandhi.loginbuttons.FacebookButton;
import com.shaishavgandhi.loginbuttons.GoogleButton;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
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

public class Login extends AppCompatActivity {
    Button_SF_Pro_Display_Semibold login_btn_id;
    TextViewSFProDisplayRegular signup_btn_id;

    EditText_SF_Pro_Display_Medium password_txt_id;
    EditText_SF_Pro_Display_Medium username_txt_id;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public static String TYPE_FACENOOK="facebook";
    public static String TYPE_GOOGLE="google";
    public static String TYPE_TWITTER="twitter";
    public static int FACEBOOK_REQUEST_CODE=5001;
    public static int GOOGLE_REQUEST_CODE=5002;
    public static int TWITTER_REQUEST_CODE=5003;

    public static int RESPONCE_CODE=5000;
    public static String RESPONCE_KEY="responce";
    public static String TYPE_KEY="type";
    public static String TWITTER_CONSUMER="twitterConsumerKey";
    public static String TWITTER_SECRETE="twitterConsumerSecret";

    GoogleButton google_linear_id;
    FacebookButton facebook_linear_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password_txt_id = (EditText_SF_Pro_Display_Medium) findViewById(R.id.password_txt_id);
        username_txt_id = (EditText_SF_Pro_Display_Medium) findViewById(R.id.username_txt_id);
        login_btn_id = (Button_SF_Pro_Display_Semibold) findViewById(R.id.login_btn_id);
        signup_btn_id = (TextViewSFProDisplayRegular) findViewById(R.id.signup_btn_id);
        google_linear_id = (GoogleButton) findViewById(R.id.google_linear_id);
        facebook_linear_id = (FacebookButton) findViewById(R.id.facebook_linear_id);

        pref = getApplicationContext().getSharedPreferences("Settings_variables", 0); // 0 - for private mode
         editor = pref.edit();
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                    // DO WHATEVER YOU WANT.
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));
        google_linear_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(), SocialLoginActivity.class);
                in.putExtra(TYPE_KEY,TYPE_GOOGLE);
                startActivityForResult(in,GOOGLE_REQUEST_CODE);
            }
        });
        facebook_linear_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(), SocialLoginActivity.class);
                in.putExtra(TYPE_KEY,TYPE_FACENOOK);
                startActivityForResult(in,FACEBOOK_REQUEST_CODE);
            }
        });
        signup_btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                Login.this.startActivity(intent);
            }
        });
        login_btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("logged_in", true);
                editor.putBoolean("intro_screen", true);
                editor.commit();
             final String usename_txt = username_txt_id.getText().toString();
             final String password_txt = password_txt_id.getText().toString();

                login_btn_id.setText("Loading...");
                login_btn_id.setEnabled(false);
                if (!isValidEmail(usename_txt) ){
                    /*provide a valid username*/
                    // FancyToast.makeText(LoginTrue.this,"Provide a valid username",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                    Toasty.error(Login.this, "Provide a valid emal.", Toast.LENGTH_SHORT, true).show();
                    login_btn_id.setText("Login");
                    login_btn_id.setEnabled(true);
                }else if(password_txt.isEmpty() || password_txt.length() < 4 ){
                    /*provide a valid password*/
                    //  FancyToast.makeText(LoginTrue.this,"Provide a valid password",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();
                    Toasty.error(Login.this, "Provide a valid password", Toast.LENGTH_SHORT, true).show();
                    login_btn_id.setText("Login");
                    login_btn_id.setEnabled(true);
                }else{
                    Map<String, String> requestBody = new HashMap<>();
                    requestBody.put("email", usename_txt);
                    requestBody.put("password", password_txt);
                    requestBody.put("type", "BASIC");
                    requestBody.put("platform", "ANDROID");

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
                            login_btn_id.setText("Login");
                            login_btn_id.setEnabled(true);
                            if(response.isSuccessful()){
                                login_btn_id.setText("Login");
                                login_btn_id.setEnabled(true);

                                    pref = getApplicationContext().getSharedPreferences("Settings_variables", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putBoolean("login_status", true);
                                    editor.putBoolean("intro_screen", true);
                                    editor.putBoolean("tags_view", false);
                                    editor.putString("email",usename_txt);
                                    editor.putString("password",password_txt);
                                    editor.putString("token",response.body().getToken());
                                    editor.putInt("expiry",response.body().getExpiresIn());
                                    editor.putString("full_name",response.body().getDisplayName());
                                    editor.commit();
                                    finish();
                                if (SplashScreen.main_intent != null && SplashScreen.main_intent.getData() != null && (SplashScreen.main_intent.getData().getScheme().equals("https"))) {
                                    Uri data = SplashScreen.main_intent.getData();

                                    String id = data.getQueryParameter("id");
                                    System.out.println("prefxxx"+id);

                                    Intent intent = new Intent(Login.this, MainContainer.class);
                                    intent.putExtra("videocode",id);
                                    intent.putExtra("deep_link",true);
                                    Login.this.startActivity(intent);
                                }else {
                                    Intent intent = new Intent(Login.this, MainContainer.class);
                                    Login.this.startActivity(intent);
                                }
                            }else{
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toasty.error(Login.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toasty.error(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                //Toasty.error(LoginTrue.this, "Unauthorized access", Toast.LENGTH_SHORT, true).show();


                            }

                        }

                        @Override
                        public void onFailure(Call<CallbackResponseLogin> call, Throwable t) {
                            Toasty.error(Login.this, "Invalid Response.Connection Failure", Toast.LENGTH_SHORT, true).show();

                            login_btn_id.setText("Login");
                            login_btn_id.setEnabled(true);
                        }
                    });
                }





            }
        });


    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        pref = getApplicationContext().getSharedPreferences("Settings_variables", 0);
        Boolean phone_settings = pref.getBoolean("phone_settings", false);
        String phone = pref.getString("phone_number", "");
        if(requestCode==FACEBOOK_REQUEST_CODE){
            if(resultCode==RESPONCE_CODE){
                Log.d("Facebook RESPONCE",""+data.getExtras().getString(RESPONCE_KEY));
                String facebook_data = data.getExtras().getString(RESPONCE_KEY);
                JsonElement jelem = gson.fromJson(facebook_data, JsonElement.class);
                JsonObject jobj = jelem.getAsJsonObject();
              //  System.out.println("my_first_name"+jobj.get("first_name"));
                String success = jobj.get("success").toString().replace('"',' ').replace(" ","");
                System.out.println("success"+jobj.get("success").toString().replace('"',' ').replace(" ",""));
                if(success.equals("false")){
                    String errr_txt=jobj.get("error").toString();
                    Toasty.error(Login.this, ""+errr_txt, Toast.LENGTH_SHORT, true).show();

                }else{
                    String first_name = jobj.get("first_name").toString();
                    String last_name = jobj.get("last_name").toString();
                    String id = jobj.get("id").toString();
                    String email_social = jobj.get("email").toString();
                    String social_token = jobj.get("token").toString();
                    String expireon = jobj.get("expireon").toString();
                    editor.putBoolean("login_status", true);
                    editor.putBoolean("intro_screen", true);
                    editor.putBoolean("tags_view", false);
                    editor.putBoolean("social_login", false);
                    editor.putBoolean("has_photo", true);
                    editor.putString("type", "FACEBOOK");
                    editor.putString("photourl", "https://graph.facebook.com/"+id+"/picture?type=normal");
                    editor.putString("email",email_social);
                    editor.putString("social_token",social_token);
                    editor.putString("expiry_social",expireon);
                    editor.putString("full_name",first_name+" "+last_name);
                    editor.commit();
                    if(phone_settings){
                        login(email_social, "password", "FACEBOOK","ANDROID", first_name+" "+last_name, phone, id,id);

                    }else {
                        Intent intent = new Intent(Login.this, ConfirmPhone.class);
                        intent.putExtra("type", "FACEBOOK");
                        intent.putExtra("email", email_social);
                        intent.putExtra("social_token", social_token);
                        intent.putExtra("access_id", id);
                        intent.putExtra("full_name", first_name + " " + last_name);
                        intent.putExtra("photourl", "https://graph.facebook.com/" + id + "/picture?type=normal");

                        Login.this.startActivity(intent);
                    }
                }

           /*{"id":"2120338634676511","first_name":"Patrick","last_name":"Kanyerezi",
           "email":"patrickkanyerax@yahoo.com","userid":"2120338634676511",
           "token":"EAAZAbQQD8ZB08BAOunhXEibbp8QYsGQclik3UqxT1ArLvlgKn2E4dDZCWp5OLxaEgjDT2WgnKZBGbbmUqTRpRdGjnOOshBOIzs4fc10l2LuPUKUzcEQYU70neyexBhH7gvrkIwhUvDUWWAZCX6TrDOIWr2sLZAkTY07GYJZC301tgZDZD",
           "expireon":"Fri Mar 29 14:40:03 GMT+03:00 2019","success":"true"}*/
            }
        }
        if(requestCode==GOOGLE_REQUEST_CODE){
            if(resultCode==RESPONCE_CODE) {
                Log.d("GOOGLE RESPONCE", "" + data.getExtras().getString(RESPONCE_KEY));
                String google_data = data.getExtras().getString(RESPONCE_KEY);
                JsonElement jelem = gson.fromJson(google_data, JsonElement.class);
                JsonObject jobj = jelem.getAsJsonObject();
                String displayname = jobj.get("displayname").toString();
                String email_google = jobj.get("email").toString();
                String photourl = jobj.get("photourl").toString();
                String id = jobj.get("id").toString();
                editor.putBoolean("login_status", true);
                editor.putBoolean("intro_screen", true);
                editor.putBoolean("tags_view", false);
                editor.putBoolean("social_login", false);
                editor.putBoolean("has_photo", true);
                editor.putString("photourl", photourl);
                editor.putString("type", "GOOGLE");
                editor.putString("email", email_google);
                editor.putString("social_token", id);
                editor.putString("access_id", id);
                editor.putString("full_name", displayname);
                editor.commit();
                if(phone_settings){
                    login(email_google, "password", "GOOGLE","ANDROID", displayname, phone, id,id);


                    }else {
                    Intent intent = new Intent(Login.this, ConfirmPhone.class);
                    intent.putExtra("type", "GOOGLE ");
                    intent.putExtra("email", email_google);
                    intent.putExtra("social_token", id);
                    intent.putExtra("access_id", id);
                    intent.putExtra("full_name", displayname);
                    intent.putExtra("photourl", photourl);

                    Login.this.startActivity(intent);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void login(final String email, final String password, final String type, final String platform, final String full_name, final String phone_number, final String access_id, final String access_token) {

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("type", type.replace(" ",""));
        requestBody.put("platform", "ANDROID");
        requestBody.put("full_name", full_name);
        requestBody.put("phone_number", phone_number);
        requestBody.put("access_id", access_id);
        requestBody.put("access_token", access_token);

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
                    editor.putBoolean("intro_screen", true);
                    editor.putBoolean("tags_view", false);
                    editor.putBoolean("social_login", true);
                    editor.putBoolean("phone_settings", true);
                    editor.putString("email",email);
                    editor.putString("type", type.replace(" ",""));
                    editor.putString("phone_number", phone_number);
                    editor.putString("password","password");
                    editor.putString("access_id", access_id);
                    editor.putString("access_token", access_token);
                    editor.putString("token",response.body().getToken());
                    editor.putInt("expiry",response.body().getExpiresIn());
                    editor.putString("full_name",response.body().getDisplayName());
                    editor.commit();
                    finish();
                    Intent intent2 = new Intent("finish_activity");
                    sendBroadcast(intent2);
                    Intent intent = new Intent(Login.this, MainContainer.class);
                    Login.this.startActivity(intent);

                }else{
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toasty.error(Login.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toasty.error(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    //Toasty.error(LoginTrue.this, "Unauthorized access", Toast.LENGTH_SHORT, true).show();


                }

            }

            @Override
            public void onFailure(Call<CallbackResponseLogin> call, Throwable t) {
                Toasty.error(Login.this, "Invalid Response.Connection Failure", Toast.LENGTH_SHORT, true).show();


            }
        });
    }


}
