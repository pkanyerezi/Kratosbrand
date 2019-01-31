package com.brand.Kratos;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.brand.Kratos.adapter.RoundAdapter;
import com.brand.Kratos.adapter.TagsAdapter;
import com.brand.Kratos.adapter.TagsHomeAdapter;
import com.brand.Kratos.adapter.TendingVideosAdapter;
import com.brand.Kratos.adapter.TrendingHome;
import com.brand.Kratos.adapter.VideosAdapter;
import com.brand.Kratos.model.Categories.ResponseCategories;
import com.brand.Kratos.model.HomeModel;
import com.brand.Kratos.model.VideoContent.Data;
import com.brand.Kratos.model.VideoContent.ResponseContent;
import com.brand.Kratos.model.tags.ResponseTags;
import com.brand.Kratos.network.api;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainHomeFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<HomeModel> homeListModelClassArrayList5;
    public static RecyclerView recyclerView5;
    private RoundAdapter eAdapter;

    private ArrayList<HomeModel> homeListModelClassArrayList6;
    public static  RecyclerView recyclerView6;
    public static  VideosAdapter fAdapter;

    CustomLinearScroll categories_container_id;
    public static SwipeRefreshLayout swipeRefreshLayout;
    String name []={"Westlife","Love story","Laila main\n" +
            "laila","High End","Dum mar\n" +
            "o dum","I love you","lonely","Westlife"};
    Integer image[]={R.drawable.ic_launcher_background,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background
            ,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background,R.drawable.ic_launcher_background};

    //category page limit code type search sort direction
    public static int page = 1;
    public static int limit = 20;
    public static String category ="";
    public static String code = "";
    public static String type = "VIDEO";
    public static String search ="";
    public static String sort = ",";
    public static String direction = "DESC";
    RecyclerView.LayoutManager flayoutManager;
    LinearLayout progress_container_id;
    public static  ProgressBar progressBar;
    public static  ProgressBar loader_id;
    public static Context context;
    public static TrendingHome trendingHome;
    private EndlessRecyclerViewScrollListener scrollListener;
    public static ShimmerFrameLayout mShimmerViewContainer;
    static SkeletonScreen skeletonScreen;
    TagsHomeAdapter tagshome;
    public MainHomeFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainHomeFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static MainHomeFrag newInstance(String param1, String param2) {
        MainHomeFrag fragment = new MainHomeFrag();
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
        View v = inflater.inflate(R.layout.fragment_main_home, container, false);
        ////////////////
        //circular view with horizontal Recycler for the Genres list with same model as above......
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        categories_container_id = v.findViewById(R.id.categories_container_id);
        recyclerView5 = v.findViewById(R.id.recycler5);
        RecyclerView.LayoutManager elayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView5.setLayoutManager(elayoutManager);
        recyclerView5.setItemAnimator(new DefaultItemAnimator());
        context=getActivity();
       /* mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();
        homeListModelClassArrayList5 = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            HomeModel beanClassForRecyclerView_contacts = new HomeModel(name[i],image[i]);
            homeListModelClassArrayList5.add(beanClassForRecyclerView_contacts);
        }*/




        ////////////////
        //circular view with horizontal Recycler for the Genres list with same model as above......
        recyclerView6 = v.findViewById(R.id.recycler6);
       // RecyclerView.LayoutManager flayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
         flayoutManager = new LinearLayoutManager(getContext());
        recyclerView6.setLayoutManager(flayoutManager);
        skeletonScreen = Skeleton.bind(recyclerView6)
                .adapter(fAdapter)
                .angle(20)
                .frozen(false)
                .duration(1200)
                .count(10)
                .load(R.layout.placeholder)
                .show();
        //recyclerView6.setItemAnimator(new DefaultItemAnimator());

       /* homeListModelClassArrayList6 = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            HomeModel beanClassForRecyclerView_contacts = new HomeModel(name[i],image[i]);
            homeListModelClassArrayList6.add(beanClassForRecyclerView_contacts);
        }
        fAdapter = new VideosAdapter(getActivity(),homeListModelClassArrayList6);
        recyclerView6.setAdapter(fAdapter);
*/
    /*    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) categories_container_id.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());*/

         progress_container_id = (LinearLayout)v.findViewById(R.id.progress_container_id);
         progressBar = (ProgressBar)v.findViewById(R.id.spin_kit);
        loader_id = (ProgressBar)v.findViewById(R.id.loader_id);
        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
       // loadcategories();

        tags_videos();
       // trending_videos(category,page, 40 ,code ,type ,search ,sort, direction);

        loadcontent_recommended(page, limit , direction);

        //loadcontent(category,page, limit ,code ,type ,search ,sort, direction);
       // swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setEnabled(false);
        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) flayoutManager) {
            @Override
            public void onLoadMore(int pages, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
             System.out.println("pagexxx"+page);
               int page_number = page+pages;
                System.out.println(page_number+" pagexxxcheck "+page+" pages "+pages);
                if(page_number > 1){
                    System.out.println("pagexxx toload"+page_number);
                    loadNextDataFromApi(page_number);
                }

            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView6.addOnScrollListener(scrollListener);
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

                    tagshome = new TagsHomeAdapter(context,response.body().getData());
                    recyclerView5.setAdapter(tagshome);




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
                    eAdapter = new RoundAdapter(getActivity(),response.body().getData());
                    recyclerView5.setAdapter(eAdapter);
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
                            trendingHome.swap(response.body().getData());
                            //fAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                            // 3. Reset endless scroll listener when performing a new search
                            // scrollListener.resetState();
                        }else{
                            trendingHome = new TrendingHome(context,response.body().getData());
                            recyclerView5.setAdapter(trendingHome);
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
                loader_id.setVisibility(View.GONE);
                skeletonScreen.hide();
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
                       fAdapter.swap(response.body().getData());
                       //fAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                            // 3. Reset endless scroll listener when performing a new search
                      // scrollListener.resetState();
                   }else{
                      // mShimmerViewContainer.setVisibility(View.GONE);
                       System.out.println("loading ....l");
                       fAdapter = new VideosAdapter(context,response.body().getData());
                       recyclerView6.setAdapter(fAdapter);
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
                loader_id.setVisibility(View.GONE);
                MainHomeFrag.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public static void loadcontent_recommended(final int page_no, int limit,String direction){

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
        Call<ResponseContent> call = apiCallInterface.getHomeContent( page_no ,limit  ,true, direction);
        call.enqueue(new Callback<ResponseContent>() {
            @Override
            public void onResponse(Call<ResponseContent> call, Response<ResponseContent> response) {
                // Toasty.success(mcontext, "Getting Logs", Toast.LENGTH_SHORT, true).show();
                loader_id.setVisibility(View.GONE);
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
                       fAdapter.swap(response.body().getData());
                       //fAdapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                            // 3. Reset endless scroll listener when performing a new search
                      // scrollListener.resetState();
                   }else{
                      // mShimmerViewContainer.setVisibility(View.GONE);
                       System.out.println("loading ....l");
                       fAdapter = new VideosAdapter(context,response.body().getData());
                       recyclerView6.setAdapter(fAdapter);
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
                loader_id.setVisibility(View.GONE);
                MainHomeFrag.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void loadNextDataFromApi(int offset) {
        loadcontent(category,offset, limit ,code ,type ,search ,sort, direction);
        loadcontent_recommended(offset, limit , direction);
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request              including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }

}
