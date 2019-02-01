package com.brand.Kratos;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.Kratos.customfonts.MyTextView_Roboto_Medium;
import com.brand.Kratos.model.PostTags.Tag;
import com.brand.Kratos.model.PostTags.TagSubscribe;
import com.brand.Kratos.model.PostTags.TagsArray;
import com.brand.Kratos.model.PostTags.TagsCollection;
import com.brand.Kratos.model.tags.Data;
import com.brand.Kratos.model.tags.ResponseTags;
import com.brand.Kratos.model.tags.SelectTag;
import com.brand.Kratos.network.api;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

public class MyTags extends AppCompatActivity {

    private LinearLayout vericaltlinear, linearLayoutHorizontal, llMusic;
    TextView tvMusic;

    private ArrayList<SelectTag> musicNamesArrayList = new ArrayList<>();
    MyTextView_Roboto_Medium next_id;
     JSONObject jsonObj;
     JSONArray id_array;
     JSONObject pnObj;
    JSONObject obj;
    Tag tag;
    TagsArray tagsArray ;
    Gson gson1;
    ArrayList<JSONObject> tags_push_id;
    List<String> my_tags;
    SharedPreferences pref;
    boolean tags_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tags);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         next_id = (MyTextView_Roboto_Medium) findViewById(R.id.next_id);
        setSupportActionBar(toolbar);

        pref = getApplicationContext().getSharedPreferences("Settings_variables", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
         tags_view = pref.getBoolean("tags_view", false);
        vericaltlinear = (LinearLayout) findViewById(R.id.vericaltlinear);
         jsonObj = new JSONObject();

        try {
            jsonObj.put("platform", "ANDROID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
         id_array = new JSONArray();


        //setData();
        tags_videos();
        gson1 = new Gson();

        my_tags = new ArrayList<>();



        next_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(my_tags.size() > 2) {
                    String json = "{" +
                            "\"tags\": [";
                    for (int i = 0; i < my_tags.size(); i++) {
                        json += " {" +
                                "\"id\":" + my_tags.get(i) +
                                " }";
                        if (i < my_tags.size() - 1) {
                            json += ",";
                        }
                    }


                    String json3 = json + " ]," +
                            "\"platform\": \"ANDROID\"}";


                    Log.d("tags", json3 + "");
                    Log.d("tags", my_tags + "");
                    JsonObject convertedObject = new Gson().fromJson(json3, JsonObject.class);

                    Log.d("tagsxx", convertedObject.toString());
                    tags_subscription(convertedObject);

                editor.putBoolean("tags_view",true);
                editor.commit();

              /*  }else{

                    Toasty.warning(getApplicationContext(), "Select atleast 3 tags", Toast.LENGTH_LONG).show();
                }*/
            }
        });

    }

    @Override
    public void onBackPressed(){

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setData(final List<Data> tags) {

        vericaltlinear.removeAllViews();

        ///////////
        //for every 3rd position a new horizontal linear will be created...............

        for (int i = 0; i < musicNamesArrayList.size(); i++) {

            if (i == 0 || i % 2 == 0) {

                linearLayoutHorizontal = new LinearLayout(MyTags.this);
                linearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams layoutForOuter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayoutHorizontal.setLayoutParams(layoutForOuter);

            }

            final View hiddenInfo = getLayoutInflater().inflate(R.layout.list_tags_genre, null);
            tvMusic = (TextView) hiddenInfo.findViewById(R.id.tvMusic);
            llMusic = (LinearLayout) hiddenInfo.findViewById(R.id.llMusic);

            tvMusic.setText(musicNamesArrayList.get(i).getName());

            //////////////
            //condition where if the option is selected, the background will become yellow and on unselecting it background will turn into white.
            if (musicNamesArrayList.get(i).isSelected() == true) {
                llMusic.setBackgroundResource(R.drawable.rectangle_button_yellow_rounded);

                Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
                tvMusic.setTypeface(font);
                llMusic.setElevation(6);

            } else {
                llMusic.setBackgroundResource(R.drawable.rectangle_white_gray_rounded);
                Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
                tvMusic.setTypeface(font);

            }

            linearLayoutHorizontal.setGravity(View.TEXT_ALIGNMENT_CENTER);
            linearLayoutHorizontal.addView(hiddenInfo);

            hiddenInfo.setTag(i);
            if (linearLayoutHorizontal.getParent() != null)
                ((ViewGroup) linearLayoutHorizontal.getParent()).removeView(linearLayoutHorizontal); // <- fix

            vericaltlinear.addView(linearLayoutHorizontal);

            /* Tag post_tag = new Tag();
                post_tag.setId(2);
            Tag post_tag2 = new Tag();
                post_tag2.setId(4);
            Tag post_tag3 = new Tag();
                post_tag3.setId(6);
            List<Tag> my_tags = new ArrayList<>();
                my_tags.add(0,post_tag);
                my_tags.add(1,post_tag2);
                my_tags.add(2,post_tag3);

            TagsArray tagsArray= new TagsArray();
                    tagsArray.setPlatform("ANDROID");
                    tagsArray.setPlatform(String.valueOf(my_tags));

            String jsonText = new Gson().toJson(tagsArray);
            System.out.println("tags check"+jsonText);*/

             tags_push_id = new <JSONObject>ArrayList();
            JSONObject oJsonInner = new JSONObject();
            try {
                oJsonInner.put("id","8");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tags_push_id.add(oJsonInner);
            hiddenInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(musicNamesArrayList.get((Integer) v.getTag()).getName()+"xxx"+musicNamesArrayList.get((Integer) v.getTag()).getId());

                        int tag_id = musicNamesArrayList.get((Integer) v.getTag()).getId();
                    if (musicNamesArrayList.get((Integer) v.getTag()).isSelected() == true) {
                        musicNamesArrayList.get((Integer) v.getTag()).setSelected(false);
                        my_tags.remove(""+tag_id);
                    } else {
                        musicNamesArrayList.get((Integer) v.getTag()).setSelected(true);
                       // my_tags.add(""+tag_id);
                       // my_tags.add(0,""+tag_id);
                        my_tags.add(""+tag_id);


                    }


                    setData(tags);
                }
            });

        }

    }

    public void tags_videos() {

        // public  void loadcontent(String category, final int page_no, int limit , String code , String type, String search, String sort, String direction){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        //.addHeader("timestamp", "153138130")
                        .addHeader("Authorization", MainContainer.token)
                        .build();
                return chain.proceed(request);
            }
        });
        httpClient.interceptors().add(new LogJsonInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                //  .baseUrl("http://128.199.33.32:3020/api/")
                .baseUrl(api.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        api apiCallInterface = retrofit.create(api.class);
        Call<ResponseTags> call = apiCallInterface.getTags(MainContainer.token);
        call.enqueue(new Callback<ResponseTags>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<ResponseTags> call, Response<ResponseTags> response) {
                // Toasty.success(mcontext, "Getting Logs", Toast.LENGTH_SHORT, true).show();

                if(response.isSuccessful()){
                    //  Toasty.success(mcontext, ""+response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    //    adapter = new WalletsTransactionsAdapter(mcontext,R.layout.fragment_transaction_log_item ,response.body().getData().getTransactions());

                    for (int i = 0; i < response.body().getData().size(); i++) {

                        SelectTag musicNames = new SelectTag(response.body().getData().get(i).getName(), false,response.body().getData().get(i).getId());
                        musicNamesArrayList.add(musicNames);
                    }

                    setData(response.body().getData());



                }else{

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //Toasty.error(mcontext, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        // Toasty.error(mcontext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                /*currenciesAdapter= new CurrenciesAdapter(getContext(),categories);
                spinner.setAdapter(currenciesAdapter);*/
            }

            @Override
            public void onFailure(Call<ResponseTags> call, Throwable t) {
                //Toasty.error(mcontext, "Invalid Response.Connection Failure", Toast.LENGTH_SHORT, true).show();

            }
        });
    }
    public void tags_subscription(JsonObject tags_json) {

        // public  void loadcontent(String category, final int page_no, int limit , String code , String type, String search, String sort, String direction){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        //.addHeader("timestamp", "153138130")
                        .addHeader("Authorization", MainContainer.token)
                        .build();
                return chain.proceed(request);
            }
        });
        httpClient.interceptors().add(new LogJsonInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                //  .baseUrl("http://128.199.33.32:3020/api/")
                .baseUrl(api.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        api apiCallInterface = retrofit.create(api.class);
        Call<TagSubscribe> call = apiCallInterface.getTagsSubscription(tags_json);
        call.enqueue(new Callback<TagSubscribe>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<TagSubscribe> call, Response<TagSubscribe> response) {
                // Toasty.success(mcontext, "Getting Logs", Toast.LENGTH_SHORT, true).show();

                if(response.isSuccessful()){
                    //  Toasty.success(mcontext, ""+response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    //    adapter = new WalletsTransactionsAdapter(mcontext,R.layout.fragment_transaction_log_item ,response.body().getData().getTransactions());

                    System.out.println("messagxx"+response.body().getMessage());
                    finish();

                }else{

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //Toasty.error(mcontext, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        // Toasty.error(mcontext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                /*currenciesAdapter= new CurrenciesAdapter(getContext(),categories);
                spinner.setAdapter(currenciesAdapter);*/
            }

            @Override
            public void onFailure(Call<TagSubscribe> call, Throwable t) {
                //Toasty.error(mcontext, "Invalid Response.Connection Failure", Toast.LENGTH_SHORT, true).show();

            }
        });
    }


}