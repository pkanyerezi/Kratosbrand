package com.brand.Kratos;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.brand.Kratos.adapter.CategoryVideosAdapter;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Regular;
import com.brand.Kratos.model.PostTags.TagSubscribe;
import com.brand.Kratos.model.VideoContent.ResponseContent;
import com.brand.Kratos.network.api;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TagsList extends AppCompatActivity {
    RecyclerView recycler_tags_vids;
    SwipeRefreshLayout swipeRefreshLayout_tags_vids;
    CategoryVideosAdapter trending_search;
    Integer page = 1;
    Integer limit = 20;
    String category ="";
    String code = "";
    String type = "VIDEO";
    String search ="";
    String sort = ",";
    String direction = "";
    String category_name="";
    String category_id ="";
    int tag_id =0;
    ImageView closing_icon_id;
    MyTextView_Roboto_Regular title_id;
    SwipeButton subscribe_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        recycler_tags_vids = (RecyclerView) findViewById(R.id.recycler_tags_vids);
        subscribe_btn = (SwipeButton) findViewById(R.id.subscribe_btn);
        setSupportActionBar(toolbar);
        String hashtag = getIntent().getStringExtra("hashtag");
        final String hashtag_name = getIntent().getStringExtra("hashtag_name");
        final String hashtag_id = getIntent().getStringExtra("hashtag_id");

        System.out.println("xxx"+hashtag_name+"_"+hashtag_id+"_"+hashtag);
       toolbar_layout.setTitle(hashtag);
       // toolbar.setTitle(hashtag);
       // getActionBar().setTitle(hashtag);
      //  getActionBar().setTitle(category_name);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_video_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        swipeRefreshLayout_tags_vids = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout_tags_vids);
        // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        swipeRefreshLayout_tags_vids.setRefreshing(true);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recycler_tags_vids.setLayoutManager(mGridLayoutManager);

        mGridLayoutManager.setAutoMeasureEnabled( true );
        loadcontent(category,page, limit ,code ,type ,search ,sort, direction, Integer.parseInt(hashtag_id));
        subscribe_btn.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                String json = "{" +
                        "\"tags\": [";
                    json += " {" +
                            "\"id\":" + hashtag_id +
                            " }";
                        json += ",";




                String json3 = json + " ]," +
                        "\"platform\": \"ANDROID\"}";
                JsonObject convertedObject = new Gson().fromJson(json3, JsonObject.class);

                Log.d("tagsxx", convertedObject.toString());
                tags_subscription(convertedObject);
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

                    System.out.println("subscribexxx"+response.body().getMessage());
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



    public void loadcontent(String category,int page,int limit ,String code ,String type,String search,String sort,String direction,int tag_id){

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
        Call<ResponseContent> call = apiCallInterface.getContentForTag(category,page, limit ,code ,type ,search ,sort, direction,tag_id);
        call.enqueue(new Callback<ResponseContent>() {
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {
                // Toasty.success(mcontext, "Getting Logs", Toast.LENGTH_SHORT, true).show();
                swipeRefreshLayout_tags_vids.setRefreshing(false);
                if(response.isSuccessful()){
                    //  Toasty.success(mcontext, ""+response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    //    adapter = new WalletsTransactionsAdapter(mcontext,R.layout.fragment_transaction_log_item ,response.body().getData().getTransactions());


                    trending_search = new CategoryVideosAdapter(getApplication(),response.body().getData());
                    recycler_tags_vids.setAdapter(trending_search);
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
            public void onFailure(Call<ResponseContent> call, Throwable t) {
                //Toasty.error(mcontext, "Invalid Response.Connection Failure", Toast.LENGTH_SHORT, true).show();
                swipeRefreshLayout_tags_vids.setRefreshing(false);
            }
        });
    }


}
