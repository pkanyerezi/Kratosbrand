package com.brand.Kratos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.brand.Kratos.adapter.RoundAdapter;
import com.brand.Kratos.adapter.ViewPagerAdapter;
import com.brand.Kratos.model.HomeModel;
import com.brand.Kratos.model.VideoContent.Data;
import com.brand.Kratos.model.VideoContent.ResponseContent;
import com.brand.Kratos.model.VideoDetails.ReesponseContentDetails;
import com.brand.Kratos.network.api;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainContainer extends AppCompatActivity {
    private ArrayList<HomeModel> homeListModelClassArrayList5;
    private RecyclerView recyclerView5;
    private RoundAdapter eAdapter;

    SharedPreferences pref;
   public static String token;
   public static String email;
   public static String full_name;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MenuItem prevMenuItem;
   /* FragmentWallet fragmentWallet;
    FragmentNavigationDrawer fragmentNavigationDrawer;
    FragmentHome fragmentHome;*/
    MainHomeFrag mainHomeFrag;
    HomeVideos homeVideos;
    SettingsFrag settingsFrag;
    TrendingVideos trendingVideos;
    SearchVideos searchVideos;
    LiveVideos watchLive;
    ViewPagerAdapter viewPagerAdapter;
    public static FragmentManager fragmentManager;
   public static Context context;
    public static DialogFragment dialogFragment;
    public static DialogFragment dialogcomments;
    public static  FragmentTransaction ft;
    public static  String vide_code;
    int expiry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
      /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
      //  loadFragment(new MainHomeFrag());
        fragmentManager = getSupportFragmentManager();
        context = getApplicationContext();

        ft = fragmentManager.beginTransaction();
        Fragment prev =fragmentManager.findFragmentByTag("dialog");
        if (prev != null) {
           ft .remove(prev);
        }
       ft .addToBackStack(null);
        dialogcomments= new CommentsDialog();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        // Create an object of page transformer
        viewPager.setOffscreenPageLimit(4);
        BookFlipPageTransformer bookFlipPageTransformer = new BookFlipPageTransformer();

// Enable / Disable scaling while flipping. If true, then next page will scale in (zoom in). By default, its true.
        bookFlipPageTransformer.setEnableScale(true);

// The amount of scale the page will zoom. By default, its 5 percent.
        bookFlipPageTransformer.setScaleAmountPercent(10f);

// Assign the page transformer to the ViewPager.
        viewPager.setPageTransformer(true, bookFlipPageTransformer);
        pref = getApplicationContext().getSharedPreferences("Settings_variables", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        token = pref.getString("token", "");
        email = pref.getString("email", "");
        expiry = pref.getInt("expiry", 0);
        boolean tags_view = pref.getBoolean("tags_view", false);
        full_name = pref.getString("full_name", "");
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
      /*  viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/
        setupViewPager(viewPager);

        if(!tags_view){
            Intent intent = new Intent(context, MyTags.class);
            context.startActivity(intent);
        }
        final String videocode = getIntent().getStringExtra("videocode");
        final boolean deep_link = getIntent().getBooleanExtra("deep_link",false);
      /*  if (SplashScreen.main_intent != null && SplashScreen.main_intent.getData() != null && (SplashScreen.main_intent.getData().getScheme().equals("https"))) {
            Uri data = SplashScreen.main_intent.getData();

            String id = data.getQueryParameter("id");
            System.out.println("prefxxx"+id);

            loadcontent_detals(id);
        }*/
      CountDownTimer start = new CountDownTimer(50, 100) { //40000 milli seconds is total time, 1000 milli seconds is time interval

                public void onTick(long millisUntilFinished) {
                }

                    public void onFinish() {
                        System.out.println("xxxxxx "+videocode+"xxxxxx "+deep_link);
                      if(videocode!= null){
                          loadcontent_detals(videocode);
                      }

            }
        }.start();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    //toolbar.setTitle("Shop");
                   /* fragment = new MainHomeFrag();
                    loadFragment(fragment);
                    return true;*/
                    viewPager.setCurrentItem(0);
                    break;
               /* case R.id.navigation_gifts:
                   // toolbar.setTitle("My Gifts");
                    fragment = new HomeVideos();
                    loadFragment(fragment);
                    return true;
                    viewPager.setCurrentItem(1);
                    break;*/
                case R.id.navigation_cart:
                  //  toolbar.setTitle("Cart");
                  /*  fragment = new HomeVideos();
                    loadFragment(fragment);
                    return true;*/
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.navigation_live:
                  //  toolbar.setTitle("Cart");
                  /*  fragment = new HomeVideos();
                    loadFragment(fragment);
                    return true;*/
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.navigation_profile:
                   // toolbar.setTitle("Profile");
                  /*  fragment = new SettingsFrag();
                    loadFragment(fragment);
                    return true;*/
                    viewPager.setCurrentItem(3);
                    break;
            }

            return false;
        }
    };
public void logout_(){
    finish();
}
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void onResume() {

        super.onResume();

     /*  long time_now =  System.currentTimeMillis();
       long difference = time_now-expiry;
       System.out.println("time_now"+time_now+"expiry"+expiry+"difference"+difference);
       if(time_now <= expiry){
           finish();
           Intent intent = new Intent(MainContainer.this, SplashScreen.class);
           MainContainer.this.startActivity(intent);
       }else{

       }*/
    }
    @Override
    public void onBackPressed() {
        // do something on back.
        System.out.println("current item"+viewPager.getCurrentItem());
        if(viewPager.getCurrentItem() == 0){
            finish();
        }else if(viewPager.getCurrentItem() == 1){
            viewPager.setCurrentItem(0);
        }else if(viewPager.getCurrentItem() == 2){
            viewPager.setCurrentItem(1);

        }else if(viewPager.getCurrentItem() == 3){
            viewPager.setCurrentItem(2);
        }else{
            //viewPager.setCurrentItem(0);
            finish();
        }

        return;
    }
    private void setupViewPager(ViewPager viewPager) {

        mainHomeFrag = new MainHomeFrag();
        settingsFrag = new SettingsFrag();
        searchVideos = new SearchVideos();
        //trendingVideos = new TrendingVideos();
        watchLive = new LiveVideos();
        viewPagerAdapter.addFragment(mainHomeFrag);
       // viewPagerAdapter.addFragment(trendingVideos);
        viewPagerAdapter.addFragment( searchVideos);
        viewPagerAdapter.addFragment( watchLive);
        viewPagerAdapter.addFragment( settingsFrag);
        viewPager.setAdapter(viewPagerAdapter);
    }

    public  void loadcontent_detals( String code ){

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
        Call<ReesponseContentDetails> call = apiCallInterface.getContent_details(code );
        call.enqueue(new Callback<ReesponseContentDetails>() {
            @Override
            public void onResponse(Call<ReesponseContentDetails> call, Response<ReesponseContentDetails> response) {
                 Toasty.success(getApplicationContext(), "details video", Toast.LENGTH_SHORT, true).show();

                if(response.isSuccessful()){

                    final String video_id = response.body().getData().getCode();
                    final String video_url = response.body().getData().getVideoUrl();
                    String video_image_url = response.body().getData().getImageUrl();
                    final String video_description = response.body().getData().getDescription();
                    final String video_title = response.body().getData().getTitle();
                    final String video_views = response.body().getData().getViews().toString();
                    final String video_category = response.body().getData().getCategory().getName().toString();

                    final String video_category_id = response.body().getData().getCategory().getCode().toString();
                    final String likes_no = response.body().getData().getLikes().toString();
                    final String dislike_no = response.body().getData().getDislikes().toString();
                    final String added_date = response.body().getData().getDateAdded().toString();
                    final String video_duration= secToTime(response.body().getData().getDuration());
                    Intent intent = new Intent(MainContainer.this, VideoDetails.class);
                    intent.putExtra("description",video_description);
                    intent.putExtra("title",video_title);
                    intent.putExtra("video_url",video_url);
                    intent.putExtra("video_views",video_views);
                    //video_description=video_description&video_title=video_title&video_url=video_url&video_views=video_views&video_category_id=video_category_id&video_duration=video_duration&video_category=video_category
                    intent.putExtra("video_category_id",video_category_id);
                    intent.putExtra("video_duration_x",video_duration);
                    intent.putExtra("video_category",video_category);
                    intent.putExtra("likes_no",likes_no);
                    intent.putExtra("dislike_no_txt",dislike_no);
                    intent.putExtra("added_date_txt", added_date);
                    intent.putExtra("video_code_id", video_id);
                    MainContainer.this.startActivity(intent);


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
            public void onFailure(Call<ReesponseContentDetails> call, Throwable t) {
                Toasty.error(getApplicationContext(), "Invalid Response.Connection Failure", Toast.LENGTH_SHORT, true).show();
                System.out.println("xxxxxx"+t.getMessage());
                //swipeRefreshLayout_trend.setRefreshing(false);
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
}