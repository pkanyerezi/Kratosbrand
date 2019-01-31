package com.brand.Kratos;

import android.content.Intent;
import android.content.res.Configuration;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.brand.Kratos.adapter.RelatedVideosAdapter;
import com.brand.Kratos.adapter.VideosAdapter;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Medium;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Regular;
import com.brand.Kratos.model.VideoContent.Data;
import com.brand.Kratos.model.VideoContent.ResponseContent;
import com.brand.Kratos.model.VideoLogAction.ResponseVideoLogAction;
import com.brand.Kratos.network.api;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.longtailvideo.jwplayer.JWPlayerFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.suke.widget.SwitchButton;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoDetails extends AppCompatActivity implements VideoPlayerEvents.OnFullscreenListener{
    RelatedVideosAdapter relatedVideosAdapter;
    RecyclerView related_items_recycler;
    public static JWPlayerView playerView;

    //category page limit code type search sort direction
    Integer page = 1;
    Integer limit = 20;
    String category ="";
    String code = "";
    String type = "VIDEO";
    String search ="";
    String sort = ",";
    String direction = "";
    SwipeRefreshLayout swipeRefreshLayout;
    public static SwitchButton autoreply_id;
    List<PlaylistItem> playlist;

    public static MyTextView_Roboto_Medium video_title_id;
    public static MyTextView_Roboto_Regular views_id_txt_id;
    public static MyTextView_Roboto_Regular published_date;
    public static  MyTextView_Roboto_Medium duration_id;
    public static  MyTextView_Roboto_Medium categoty_id;

    public static RelativeTimeTextView timestamp;

   public static Button dislikes_txt_id;
    public static Button likes_tx_id;
    public static Button share_btn;
    public static Button comment_btn;
    public static MaterialLetterIcon letter_budge;
    public static MyTextView_Roboto_Regular title_id;
    LinearLayout details_container_id;
    Date date = null;
    ImageView video_back_btn;
    ImageView show_details_container;
    boolean toggle = false;
    Toolbar toolbar;
    LinearLayout vid_detals;
    RelativeLayout related_videos_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_testing_video_details);
        setContentView(R.layout.activity_video_details);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        vid_detals = (LinearLayout) findViewById(R.id.vid_detals);
        related_videos_id = (RelativeLayout) findViewById(R.id.related_videos_id);
       // setSupportActionBar(toolbar);
        playlist = new ArrayList<PlaylistItem>();
        show_details_container = (ImageView) findViewById(R.id.show_details_container);
        video_back_btn = (ImageView) findViewById(R.id.video_back_btn);
        letter_budge = (MaterialLetterIcon) findViewById(R.id.letter_budge);
        share_btn = (Button) findViewById(R.id.share_btn);
        comment_btn = (Button) findViewById(R.id.comment_btn);
        dislikes_txt_id = (Button) findViewById(R.id.dislikes_txt_id);
        likes_tx_id = (Button) findViewById(R.id.likes_tx_id);
        timestamp = (RelativeTimeTextView) findViewById(R.id.timestamp);
        categoty_id = (MyTextView_Roboto_Medium) findViewById(R.id.categoty_id);
        video_title_id = (MyTextView_Roboto_Medium) findViewById(R.id.video_title_id);
        duration_id = (MyTextView_Roboto_Medium) findViewById(R.id.duration_id);
        published_date = (MyTextView_Roboto_Regular) findViewById(R.id.published_date);
        views_id_txt_id = (MyTextView_Roboto_Regular) findViewById(R.id.views_id_txt_id);
        autoreply_id = (SwitchButton) findViewById(R.id.autoreply_id);
         playerView = (JWPlayerView) findViewById(R.id.playerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        details_container_id = (LinearLayout) findViewById(R.id.details_container_id);
        title_id = (MyTextView_Roboto_Regular) findViewById(R.id.title_id);
        final String description = getIntent().getStringExtra("description");
        final String title = getIntent().getStringExtra("title");
        final String video_url = getIntent().getStringExtra("video_url");
        String video_views = getIntent().getStringExtra("video_views");
        String video_category_id = getIntent().getStringExtra("video_category_id");
        String video_duration = getIntent().getStringExtra("video_duration_x");
        String likes_no = getIntent().getStringExtra("likes_no");
        String dislike_no_t = getIntent().getStringExtra("dislike_no_txt");
        String added_date_txt = getIntent().getStringExtra("added_date_txt");
        String video_category = getIntent().getStringExtra("video_category");
        final String video_code_id = getIntent().getStringExtra("video_code_id");
        System.out.println("xxxxxx"+video_url);
        views_id_txt_id.setText(video_views +" Views  ");
        video_title_id.setText(title +"");
        duration_id.setText(video_duration +"");
        categoty_id.setText(""+video_category);
        dislikes_txt_id.setText(dislike_no_t+" Dislikes");
        likes_tx_id.setText(likes_no+" Likes");
        letter_budge.setLetter(video_category);
        share_btn.setText("Share");

        comment_btn.setText("Comments");
        title_id.setText(category);
        log_action(video_code_id.toString(),"VIEW","ANDROID");
        final int likes = Integer.parseInt(likes_no) + 1;
        final int dislikes = Integer.parseInt(dislike_no_t) + 1;
        dislikes_txt_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dislikes_txt_id.setText(likes+"");
                log_action(video_code_id.toString(),"DISLIKE","ANDROID");

            }
        });
        likes_tx_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likes_tx_id.setText(dislikes+"");
                log_action(video_code_id.toString(),"LIKE","ANDROID");

            }
        });
        details_container_id.setVisibility(View.VISIBLE);
        //ic_keyboard_arrow_up_black_24dp
        show_details_container.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

        toggle = true;
        show_details_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if(!toggle){
                  details_container_id.setVisibility(View.VISIBLE);
                  //ic_keyboard_arrow_up_black_24dp
                  show_details_container.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

                  toggle = true;
              }else{
                  toggle = false;
                  details_container_id.setVisibility(View.GONE);
                  //ic_keyboard_arrow_up_black_24dp
                  show_details_container.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);

              }
            }
        });
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);


        long longmilliseconds=9877;
        try {
            //  Toast.makeText(mContext, "there ttt...date" +date, Toast.LENGTH_SHORT).show();
            Date  dating = format.parse(added_date_txt);
            date = dating;
            System.out.println("datex"+dating);
            longmilliseconds = date.getTime();
            // Toast.makeText(mContext, "there date inside" +longmilliseconds, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timestamp.setReferenceTime(date.getTime());
        category = video_category_id;
        PlaylistItem video = new PlaylistItem(""+video_url);
        playlist.add(video);



        related_items_recycler = (RecyclerView) findViewById(R.id.related_items_recycler);
        RecyclerView.LayoutManager flayoutManager = new LinearLayoutManager(VideoDetails.this);

        related_items_recycler.setLayoutManager(flayoutManager);
        related_items_recycler.setNestedScrollingEnabled(false);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        video_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  String message = video_url+" Shared from the Kratos APP."+title;
                String message = "https://www.kratosbrand.com/?id="+video_code_id+"  "+title+" \n"+description;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,message);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, " Shared from the Kratos APP");
                startActivity(Intent.createChooser(shareIntent, "Share..."));
            }
        });
        if(autoreply_id.isChecked()){

        }
        autoreply_id.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
            }
        });
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment prev =MainContainer.fragmentManager.findFragmentByTag("dialog");
                if (prev != null) {
                    fragmentTransaction.remove(prev);
                }
                fragmentTransaction.addToBackStack(null);
                DialogFragment comments= new CommentsDialog();
                comments.show(fragmentTransaction, "dialog");
            }
        });
        loadcontent(category,page, limit ,code ,type ,search ,sort, direction);

    }

    @Override
    protected void onResume() {
        // Let JW Player know that the app has returned from the background
        super.onResume();
        playerView.onResume();
    }

    @Override
    protected void onPause() {
        // Let JW Player know that the app is going to the background
        playerView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // Let JW Player know that the app is being destroyed
        playerView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // Set fullscreen when the device is rotated to landscape
        playerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Exit fullscreen when the user pressed the Back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (playerView.getFullscreen()) {
                playerView.setFullscreen(false, false);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * Handles JW Player going to and returning from fullscreen by hiding the ActionBar
     *
     * @param fullscreen true if the player is fullscreen
     */
    @Override
    public void onFullscreen(boolean fullscreen) {
       // ActionBar actionBar = getSupportActionBar();

            if (fullscreen) {
                /*
                 * Because we are using a coordinator layout, which has setFitsSystemWindows set
                 * to 'true' by default, we need to disable this when the JW Player View goes into
                 * fullscreen. If we would not do this, the activity will not properly hide the
                 * system UI.
                 */
               // actionBar.hide();
                 vid_detals.setVisibility(View.GONE);
                 related_videos_id.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
            } else {
                toolbar.setVisibility(View.VISIBLE);
                vid_detals.setVisibility(View.VISIBLE);
                related_videos_id.setVisibility(View.VISIBLE);
            }


    }
  /*  @Override
    public void onFullscreen(boolean state) {
        if (state) {
            getActionBar().hide();
            findViewById(R.id.custom_ui).setVisibility(View.VISIBLE);
        } else {
            getActionBar().show();
        }


        //((CoordinatorLayout)findViewById({ID_OF_ROOT_LAYOUT})).setFitsSystemWindows(!state);
    }*/
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
                swipeRefreshLayout.setRefreshing(false);
                if(response.isSuccessful()){
                    //  Toasty.success(mcontext, ""+response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                    //    adapter = new WalletsTransactionsAdapter(mcontext,R.layout.fragment_transaction_log_item ,response.body().getData().getTransactions());

                    // adapter = new MyRecyclerAdapter(getApplicationContext(),feedsList);
                    //  my_wallet_logs.setAdapter(adapter);
                    List<Data> videolist =response.body().getData();
                    for (int i=0; i<videolist.size(); i++) {
                        String vid_url = videolist.get(i).getVideoUrl();

                        PlaylistItem video23 = new PlaylistItem(""+vid_url);
                        playlist.add(video23);
                    }
                    playerView.setup(new PlayerConfig.Builder()
                            .playlist(playlist)
                            .mute(false)
                            .controls(true)
                            .autostart(true)
                            .build());
                    relatedVideosAdapter = new RelatedVideosAdapter(VideoDetails.this,response.body().getData());
                    related_items_recycler.setAdapter(relatedVideosAdapter);
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
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void log_action(String code,String action,String platform){

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("action", action);
        requestBody.put("code", code);
        requestBody.put("platform", platform);
        // requestBody.put("platform", "ANDROID");

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

        Call<ResponseVideoLogAction> call = apiCallInterface.getResponseVideoLogAction(requestBody);
        call.enqueue(new Callback<ResponseVideoLogAction>() {
            @Override
            public void onResponse(Call<ResponseVideoLogAction> call, Response<ResponseVideoLogAction> response) {
                // Toasty.success(mcontext, "Getting Logs", Toast.LENGTH_SHORT, true).show();

                if(response.isSuccessful()){
                    System.out.println("videox action"+response.body().getMessage());
                }else{

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        //Toasty.error(mcontext, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        // Toasty.error(mcontext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseVideoLogAction> call, Throwable t) {
                //Toasty.error(mcontext, "Invalid Response.Connection Failure", Toast.LENGTH_SHORT, true).show();

            }
        });
    }

}
