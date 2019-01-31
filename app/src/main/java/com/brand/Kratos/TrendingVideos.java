package com.brand.Kratos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.brand.Kratos.adapter.RoundAdapter;
import com.brand.Kratos.adapter.TendingVideosAdapter;
import com.brand.Kratos.adapter.VideosAdapter;
import com.brand.Kratos.model.Categories.ResponseCategories;
import com.brand.Kratos.model.VideoContent.ResponseContent;
import com.brand.Kratos.network.api;

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


public class TrendingVideos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static int page = 1;
    public static int limit = 20;
    public static String category ="";
    public static String code = "";
    public static String type = "VIDEO";
    public static String search ="";
    public static String sort = ",";
    public static String direction = "";
    RecyclerView.LayoutManager flayoutManager;
    LinearLayout progress_container_id;
    public static ProgressBar progressBar;
    public static  ProgressBar loader_id;
    public static Context context;
    private EndlessRecyclerViewScrollListener scrollListener;
    TendingVideosAdapter trendingVideosAdapter;
    RecyclerView recycler_trending;
    SwipeRefreshLayout swipeRefreshLayout_trend;
    private RoundAdapter eAdapter;
    public TrendingVideos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrendingVideos.
     */
    // TODO: Rename and change types and number of parameters
    public static TrendingVideos newInstance(String param1, String param2) {
        TrendingVideos fragment = new TrendingVideos();
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
        View v = inflater.inflate(R.layout.fragment_trending_videos, container, false);
        // Inflate the layout for this fragment
        swipeRefreshLayout_trend = v.findViewById(R.id.swipeRefreshLayout_trend);
        recycler_trending = v.findViewById(R.id.recycler_trending);
        flayoutManager = new LinearLayoutManager(getContext());
        recycler_trending.setLayoutManager(flayoutManager);
        swipeRefreshLayout_trend.setRefreshing(true);
        loadcontent(category,page, limit ,code ,type ,search ,sort, direction);
        return v;
    }

    public  void loadcontent(String category, final int page_no, int limit , String code , String type, String search, String sort, String direction){

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
        Call<ResponseContent> call = apiCallInterface.getContent(category, page_no ,limit ,code ,type, search ,sort, direction);
        call.enqueue(new Callback<ResponseContent>() {
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {
                // Toasty.success(mcontext, "Getting Logs", Toast.LENGTH_SHORT, true).show();
                swipeRefreshLayout_trend.setRefreshing(false);
                if(response.isSuccessful()){
                    //  Toasty.success(mcontext, ""+response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    //    adapter = new WalletsTransactionsAdapter(mcontext,R.layout.fragment_transaction_log_item ,response.body().getData().getTransactions());

                    // adapter = new MyRecyclerAdapter(getApplicationContext(),feedsList);
                    //  my_wallet_logs.setAdapter(adapter);
                    System.out.println("out pagexxx12"+page_no);
                    MainHomeFrag.swipeRefreshLayout.setRefreshing(false);
                    if(page_no >1){
                        System.out.println(page_no+"in pagexxx12"+page_no);
                        //  listOfItems.clear();
                        // 2. Notify the adapter of the update
                        trendingVideosAdapter.swap(response.body().getData());
                        //fAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                        // 3. Reset endless scroll listener when performing a new search
                        // scrollListener.resetState();
                    }else{
                        trendingVideosAdapter = new TendingVideosAdapter(context,response.body().getData());
                        recycler_trending.setAdapter(trendingVideosAdapter);
                    }



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

           swipeRefreshLayout_trend.setRefreshing(false);
            }
        });
    }

    public void loadNextDataFromApi(int offset) {
        loadcontent(category,offset, limit ,code ,type ,search ,sort, direction);

        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request              including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }
}
