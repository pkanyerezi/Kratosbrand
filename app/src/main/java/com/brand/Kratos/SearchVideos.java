package com.brand.Kratos;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.brand.Kratos.adapter.CategoriesAdapter;
import com.brand.Kratos.adapter.CustomSuggestionsAdapter;
import com.brand.Kratos.adapter.SearchAdapter;
import com.brand.Kratos.adapter.SportsAdapter;
import com.brand.Kratos.adapter.TagsAdapter;
import com.brand.Kratos.adapter.TrendingHome;
import com.brand.Kratos.adapter.TrendingSearch;
import com.brand.Kratos.adapter.VideosAdapter;
import com.brand.Kratos.model.Categories.ResponseCategories;
import com.brand.Kratos.model.VideoContent.Data;
import com.brand.Kratos.model.VideoContent.ResponseContent;
import com.brand.Kratos.model.sports.ResponseSports;
import com.brand.Kratos.model.tags.ResponseTags;
import com.brand.Kratos.network.api;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.nicolettilu.hiddensearchwithrecyclerview.HiddenSearchWithRecyclerView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sakout.mehdi.StateViews.StateView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class SearchVideos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MaterialSearchBar search_liveo;
    SearchAdapter searchAdapter;
    CategoriesAdapter categoriesAdapter;
   // SwipeRefreshLayout swipeRefreshLayout_search;
   // RecyclerView recycler_searching;
    RecyclerView recycler_categories;
    private List<Data> suggestions = new ArrayList<>();
    private CustomSuggestionsAdapter customSuggestionsAdapter;
    SearchAdapter searchVideos;

    public static int page = 1;
    public static int limit = 20;
    public static String category ="";
    public static String code = "";
    public static String type = "VIDEO";
    public static String search ="";
    public static String sort = ",";
    public static String direction = "DESC";

    public static RecyclerView recycler_trending_vids;
    public static RecyclerView recycler_category;
    private CategoriesAdapter eAdapter;
    public static SearchAdapter trendingHome;
    public static Context context;

    public static  RecyclerView recycler_sports;
    public static SportsAdapter sportsAdapter;

    public static  RecyclerView recycler_trending;
    public static SearchAdapter fAdapter;
    public static TrendingSearch trending_vids;
    public static LinearLayout categories_linear_container_id;
    public static RelativeLayout search_results_container;
    public static SwipeRefreshLayout swipeRefreshLayout_search;

    TagsAdapter tagshome;
    public SearchVideos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchVideos.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchVideos newInstance(String param1, String param2) {
        SearchVideos fragment = new SearchVideos();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_videos, container, false);
        swipeRefreshLayout_search = v.findViewById(R.id.swipeRefreshLayout_search);
        categories_linear_container_id = v.findViewById(R.id.categories_linear_container_id);
        recycler_category = v.findViewById(R.id.recycler_category);
        recycler_trending_vids = v.findViewById(R.id.recycler_trending_vids);
        recycler_sports = v.findViewById(R.id.recycler_sports);
        search_results_container = v.findViewById(R.id.search_results_container);
       // recycler_searching = v.findViewById(R.id.recycler_searching);
        search_liveo = v.findViewById(R.id.search_liveo);
        LayoutInflater inflater_txt = (LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        customSuggestionsAdapter = new CustomSuggestionsAdapter(inflater_txt);
        //recycler_trending = v.findViewById(R.id.recycler_trending);
/*
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 4);
        recycler_sports.setLayoutManager(mGridLayoutManager);
*/
        RecyclerView.LayoutManager elayoutManager1 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recycler_sports.setLayoutManager(elayoutManager1);
        recycler_sports.setItemAnimator(new DefaultItemAnimator());
        //search_liveo.setMaxSuggestionCount(2);
        search_liveo.setHint("Find video..");
        search_liveo.enableSearch();
        RecyclerView.LayoutManager flayoutManager = new LinearLayoutManager(getContext());

       // recycler_searching.setLayoutManager(flayoutManager);
       // recycler_searching.setNestedScrollingEnabled(false);
        //swipeRefreshLayout_search.setEnabled(false);
        //swipeRefreshLayout_search.setRefreshing(true);
        swipeRefreshLayout_search.setEnabled(true);
        swipeRefreshLayout_search.setRefreshing(false);
        RecyclerView.LayoutManager elayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recycler_category.setLayoutManager(elayoutManager);
        recycler_category.setItemAnimator(new DefaultItemAnimator());

        recycler_trending_vids.setLayoutManager(layoutManager);
        recycler_trending_vids.setItemAnimator(new DefaultItemAnimator());
        context=getActivity();

        recycler_trending = v.findViewById(R.id.recycler_trending);
        // RecyclerView.LayoutManager flayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        flayoutManager = new LinearLayoutManager(getContext());
        recycler_trending.setLayoutManager(flayoutManager);
        categories_linear_container_id.setVisibility(View.VISIBLE);
        search_results_container.setVisibility(View.GONE);
        loadsport();
       // loadcategories();
        tags_videos();
        trending_videos(category,page, 40 ,code ,type ,search ,sort, direction);

        // categories_linear_container_id.setVisibility(View.VISIBLE);
      //  trending_videos(category,page, 40 ,code ,type ,search ,sort, direction);
       // loadcontent(category,page, limit ,code ,type ,search ,sort, direction);
        search_liveo.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("LOG_TAG", getClass().getSimpleName() + " text changed " + search_liveo.getText());
                // send the entered text to our filter and let it manage everything



                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (search_liveo.getText().length() > 3) {
                             search_results_container.setVisibility(View.VISIBLE);

                               swipeRefreshLayout_search.setRefreshing(true);
                             swipeRefreshLayout_search.setRefreshing(true);
                             categories_linear_container_id.setVisibility(View.GONE);
                            loadcontent(category, page, limit, code, type, search_liveo.getText(), sort, direction);
                            //    customSuggestionsAdapter.getFilter().filter(search_liveo.getText());
                        }else{
                             categories_linear_container_id.setVisibility(View.VISIBLE);
                             search_results_container.setVisibility(View.GONE);

                        }
                        }
                    }, 1000);

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        return v;
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

                    tagshome = new TagsAdapter(context,response.body().getData());
                    recycler_category.setAdapter(tagshome);




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
    public void loadcategories(){
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
        Call<ResponseCategories> call = apiCallInterface.getCategories("");
        call.enqueue(new Callback<ResponseCategories>() {
            @Override
            public void onResponse(Call<ResponseCategories> call, Response<ResponseCategories> response) {
                // Toasty.success(mcontext, "Getting Logs", Toast.LENGTH_SHORT, true).show();

                if(response.isSuccessful()){
                    //  Toasty.success(mcontext, ""+response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    //    adapter = new WalletsTransactionsAdapter(mcontext,R.layout.fragment_transaction_log_item ,response.body().getData().getTransactions());

                    // adapter = new MyRecyclerAdapter(getApplicationContext(),feedsList);
                    //  my_wallet_logs.setAdapter(adapter);
                    eAdapter = new CategoriesAdapter(getActivity(),response.body().getData());
                    recycler_category.setAdapter(eAdapter);
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
            public void onFailure(Call<ResponseCategories> call, Throwable t) {
                //Toasty.error(mcontext, "Invalid Response.Connection Failure", Toast.LENGTH_SHORT, true).show();

            }
        });

    }
    public static void trending_videos(String category, final int page_no, int limit , String code , String type, String search, String sort, String direction) {

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
        Call<ResponseContent> call = apiCallInterface.getContent(category, page_no ,limit ,code ,type, search ,sort, direction);
        call.enqueue(new Callback<ResponseContent>() {
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {
                // Toasty.success(mcontext, "Getting Logs", Toast.LENGTH_SHORT, true).show();

                if(response.isSuccessful()){
                    //  Toasty.success(mcontext, ""+response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    //    adapter = new WalletsTransactionsAdapter(mcontext,R.layout.fragment_transaction_log_item ,response.body().getData().getTransactions());

                    // adapter = new MyRecyclerAdapter(getApplicationContext(),feedsList);
                    //  my_wallet_logs.setAdapter(adapter);
                    System.out.println("out pagexxx12"+page_no);
                    if(page_no >1){
                        System.out.println(page_no+"in pagexxx12"+page_no);
                        //  listOfItems.clear();
                        // 2. Notify the adapter of the update
                        trending_vids.swap(response.body().getData());
                        //fAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                        // 3. Reset endless scroll listener when performing a new search
                        // scrollListener.resetState();
                    }else{
                        trending_vids = new TrendingSearch(context,response.body().getData());
                        recycler_trending_vids.setAdapter(trending_vids);
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

            }
        });
    }

    public static void loadcontent(String category, final int page_no, int limit , String code , String type, String search, String sort, String direction){

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
                swipeRefreshLayout_search.setRefreshing(false);
                if(response.isSuccessful()){
                    //  Toasty.success(mcontext, ""+response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    //    adapter = new WalletsTransactionsAdapter(mcontext,R.layout.fragment_transaction_log_item ,response.body().getData().getTransactions());

                    // adapter = new MyRecyclerAdapter(getApplicationContext(),feedsList);
                    //  my_wallet_logs.setAdapter(adapter);
                    System.out.println("out pagexxx12"+page_no);


                       // fAdapter = new SearchAdapter(context,response.body().getData());
                        fAdapter = new SearchAdapter(context,response.body().getData());
                        recycler_trending.setAdapter(fAdapter);




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
            //    loader_id.setVisibility(View.GONE);
                swipeRefreshLayout_search.setRefreshing(false);
               // categories_linear_container_id.setVisibility(View.VISIBLE);
            }
        });
    }


    public static void loadsport(){

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
        Call<ResponseSports> call = apiCallInterface.getSports();
        call.enqueue(new Callback<ResponseSports>() {
            @Override
            public void onResponse(Call<ResponseSports> call, Response<ResponseSports> response) {
                 Toasty.success(context, "Getting sports", Toast.LENGTH_SHORT, true).show();

                if(response.isSuccessful()){
                    //  Toasty.success(mcontext, ""+response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    //    adapter = new WalletsTransactionsAdapter(mcontext,R.layout.fragment_transaction_log_item ,response.body().getData().getTransactions());

                    // adapter = new MyRecyclerAdapter(getApplicationContext(),feedsList);
                    //  my_wallet_logs.setAdapter(adapter);



                    // fAdapter = new SearchAdapter(context,response.body().getData());
                    sportsAdapter = new SportsAdapter(context,response.body().getData());
                    recycler_sports.setAdapter(sportsAdapter);




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
            public void onFailure(Call<ResponseSports> call, Throwable t) {
                Toasty.error(context, "Invalid Response"+t.getMessage(), Toast.LENGTH_SHORT, true).show();
                //    loader_id.setVisibility(View.GONE);
                swipeRefreshLayout_search.setRefreshing(false);
                // categories_linear_container_id.setVisibility(View.VISIBLE);
            }
        });
    }

}
