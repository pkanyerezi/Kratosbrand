package com.brand.Kratos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brand.Kratos.adapter.CategoryVideosAdapter;
import com.brand.Kratos.adapter.RelatedVideosAdapter;
import com.brand.Kratos.adapter.TrendingSearch;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Regular;
import com.brand.Kratos.model.VideoContent.Data;
import com.brand.Kratos.model.VideoContent.ResponseContent;
import com.brand.Kratos.network.api;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

@SuppressLint("ValidFragment")
public class CategoryVideos extends BottomSheetDialogFragment {
    RecyclerView recycler_category_vids;
    SwipeRefreshLayout swipeRefreshLayout_category_vids;
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
    ImageView closing_icon_id;
    MyTextView_Roboto_Regular title_id;
    boolean is_category;
    @SuppressLint("ValidFragment")
    public CategoryVideos(String cat_id, String cat_name,boolean is_cat) {
        // Required empty public constructor
        category_name = cat_name;
        category_id=cat_id;
        is_category=is_cat;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_category_videos, container, false);

       // setContentView(R.layout.activity_category_videos);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        closing_icon_id = (ImageView) v.findViewById(R.id.closing_icon_id);
        title_id = (MyTextView_Roboto_Regular) v.findViewById(R.id.title_id);
        //setSupportActionBar(toolbar);
      //  String category_name = getIntent().getStringExtra("category_name");
        //String category_id = getIntent().getStringExtra("category_id");
      /*  toolbar.setTitle(category_name);
      //  getActionBar().setTitle(category_name);
       // toolbar.setNavigationIcon(R.drawable.ic_arrow_back_video_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
        title_id.setText(category_name);
        closing_icon_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainContainer.dialogFragment.dismiss();
            }
        });
        recycler_category_vids = (RecyclerView) v.findViewById(R.id.recycler_category_vids);
        swipeRefreshLayout_category_vids = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout_category_vids);
       // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        category=category_id;
        swipeRefreshLayout_category_vids.setRefreshing(true);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        recycler_category_vids.setLayoutManager(mGridLayoutManager);
        mGridLayoutManager.setAutoMeasureEnabled( true );
        if(is_category){
            loadcontent(category,page, limit ,code ,type ,search ,sort, direction);
        }else{
            loadsportscontent(page, limit ,code ,type ,search ,sort, direction, Integer.valueOf(category));
        }

        return v;
    }
    public void loadcontent(String category,int page,int limit ,String code ,String type,String search,String sort,String direction){

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
        Call<ResponseContent> call = apiCallInterface.getContent(category,page, limit ,code ,type ,search ,sort, direction);
        call.enqueue(new Callback<ResponseContent>() {
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {
                // Toasty.success(mcontext, "Getting Logs", Toast.LENGTH_SHORT, true).show();
                swipeRefreshLayout_category_vids.setRefreshing(false);
                if(response.isSuccessful()){
                    //  Toasty.success(mcontext, ""+response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    //    adapter = new WalletsTransactionsAdapter(mcontext,R.layout.fragment_transaction_log_item ,response.body().getData().getTransactions());


                    trending_search = new CategoryVideosAdapter(getContext(),response.body().getData());
                    recycler_category_vids.setAdapter(trending_search);
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
                swipeRefreshLayout_category_vids.setRefreshing(false);
            }
        });
    }




    public void loadsportscontent(int page,int limit ,String code ,String type,String search,String sort, String direction,Integer sports){

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
        Call<ResponseContent> call = apiCallInterface.getSportsContent(page, limit ,code ,type ,search ,sort, direction,sports);
        call.enqueue(new Callback<ResponseContent>() {
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {
                // Toasty.success(mcontext, "Getting Logs", Toast.LENGTH_SHORT, true).show();
                swipeRefreshLayout_category_vids.setRefreshing(false);
                if(response.isSuccessful()){
                    //  Toasty.success(mcontext, ""+response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    //    adapter = new WalletsTransactionsAdapter(mcontext,R.layout.fragment_transaction_log_item ,response.body().getData().getTransactions());


                    trending_search = new CategoryVideosAdapter(getContext(),response.body().getData());
                    recycler_category_vids.setAdapter(trending_search);
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
                swipeRefreshLayout_category_vids.setRefreshing(false);
            }
        });
    }
}
