package com.brand.Kratos.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.brand.Kratos.CommentsDialog;
import com.brand.Kratos.LogJsonInterceptor;
import com.brand.Kratos.MainContainer;
import com.brand.Kratos.R;
import com.brand.Kratos.VideoDetails;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Medium;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Regular;
import com.brand.Kratos.model.VideoContent.Data;
import com.brand.Kratos.model.VideoLogAction.ResponseVideoLogAction;
import com.brand.Kratos.network.api;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class LiveVideosAdaptor extends RecyclerView.Adapter<LiveVideosAdaptor.MyViewHolder>{

    Context context;

    Date date = null;
    private List<Data> videolist;


    public class MyViewHolder extends RecyclerView.ViewHolder {



        MyTextView_Roboto_Regular duration_id;
        MyTextView_Roboto_Regular textrecycler;
        MyTextView_Roboto_Medium views_id;
        ImageView thumbnail_id;

        Button share_video;
        Button likes_tx_id;
        Button dislikes_txt_id;
        Button comment_btn;
        RelativeTimeTextView timestamp;
        public MyViewHolder(View view) {
            super(view);

            duration_id=(MyTextView_Roboto_Regular)view.findViewById(R.id.duration_id);
            textrecycler=(MyTextView_Roboto_Regular)view.findViewById(R.id.textrecycler);
            views_id=(MyTextView_Roboto_Medium)view.findViewById(R.id.views_id_txt);
            thumbnail_id=(ImageView)view.findViewById(R.id.thumbnail_id);
            share_video=(Button)view.findViewById(R.id.share_video);
            likes_tx_id=(Button)view.findViewById(R.id.likes_tx_id);
            dislikes_txt_id=(Button)view.findViewById(R.id.dislikes_txt_id);
            comment_btn=(Button)view.findViewById(R.id.comment_btn);
            timestamp=(RelativeTimeTextView) view.findViewById(R.id.timestamp);


        }

    }


    public LiveVideosAdaptor(Context mainActivityContacts, List<Data> videolist) {
        this.videolist = videolist;
        this.context = mainActivityContacts;
    }

    @Override
    public LiveVideosAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);


        return new LiveVideosAdaptor.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(final LiveVideosAdaptor.MyViewHolder holder, int position) {
        final Data lists = videolist.get(position);
        final String video_id = lists.getCode();
        final String video_url = lists.getVideoUrl();
        String video_image_url = lists.getImageUrl();
        final String video_description = lists.getDescription();
        final String video_title = lists.getTitle();
        final String video_views = lists.getViews().toString();
        final String video_category = lists.getCategory().getName().toString();

        final String video_category_id = lists.getCategory().getCode().toString();
        final String likes_no = lists.getLikes().toString();
        final String dislike_no = lists.getDislikes().toString();
        final String added_date = lists.getDateAdded().toString();
        final String video_duration= secToTime(lists.getDuration());
        System.out.println("inside the dislike_no"+dislike_no);
        MainContainer.vide_code = video_id;
        holder.duration_id.setText(video_duration);
        holder.textrecycler.setText(video_title);
        holder.likes_tx_id.setText(lists.getLikes()+"");
        holder.dislikes_txt_id.setText(lists.getDislikes()+"");
        holder.comment_btn.setText("Comments");
        final int likes = lists.getLikes()+1;
        final int dislikes = lists.getDislikes()+1;
        holder.likes_tx_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.likes_tx_id.setText(likes+"");
                log_action(lists.getCode().toString(),"LIKE","ANDROID");
            }
        });
        holder.dislikes_txt_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.dislikes_txt_id.setText(dislikes+"");
                log_action(lists.getCode().toString(),"DISLIKE","ANDROID");

            }
        });
        holder.share_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.kratosbrand.com/?id="+video_id+"  "+video_title+" \n"+
                        // sendIntent.putExtra(Intent.EXTRA_TEXT, ""+video_title+"dowwnload Kratos app from https://www.kratosbrand.com/ to watch the full video on"+
                        lists.getDescription()); // Simple text and URL to share
                sendIntent.setType("text/plain");
                //  this.startActivity(sendIntent);
                context.startActivity(sendIntent);
            }
        });
        holder.comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fragmentManager = getSupportFragmentManager();
                CommentsDialog dialogcomments= new CommentsDialog();
                FragmentTransaction ft = MainContainer.fragmentManager.beginTransaction();
                Fragment prev =MainContainer.fragmentManager.findFragmentByTag("dialog");
                if (prev != null) {
                    ft .remove(prev);
                }
                ft .addToBackStack(null);
                dialogcomments.show(ft , "dialog");
            }
        });
        // holder.voting_down_badge.setBadgeCount("0= Dislikes");
        //  holder.voting_up_badge.setText(likes_no+" Likes");
        //  holder.voting_down_badge.setText(dislike_no+" Dislikes");
        holder.views_id.setText(video_category +" . " +video_views+ " ");
        //  holder.views_id.setText(video_category +" . " +video_views+ "");
        // holder.views_id.setText(lists.getTitle());
        //String dtStart = "2010-10-15T09:27:37Z";
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String[] parts = added_date.split("\\+");
        String dtStart = parts[0];

        //  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);


        long longmilliseconds=9877;
        try {
            //  Toast.makeText(mContext, "there ttt...date" +date, Toast.LENGTH_SHORT).show();
            Date dating = format.parse(added_date);
            date = dating;
            System.out.println("datex"+dating);
            longmilliseconds = date.getTime();
            // Toast.makeText(mContext, "there date inside" +longmilliseconds, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.timestamp.setReferenceTime(date.getTime());
        Picasso.get()
                .load(video_image_url)
                .placeholder(R.drawable.youtube )
                // .resize(50, 50)
                // .centerCrop()
                .fit().centerCrop()
                .into(holder.thumbnail_id);
        // holder.thumbnail_id.setImageResource(lists.getImage());
        final Date finalDate = date;
        holder.thumbnail_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoDetails.class);
                intent.putExtra("description",video_description);
                intent.putExtra("title",video_title);
                intent.putExtra("video_url",video_url);
                intent.putExtra("video_views",video_views);
                intent.putExtra("video_category_id",video_category_id);
                intent.putExtra("video_duration_x",video_duration);
                intent.putExtra("video_category",video_category);
                intent.putExtra("likes_no",likes_no);
                intent.putExtra("dislike_no_txt",dislike_no);
                intent.putExtra("added_date_txt", added_date);
                intent.putExtra("video_code_id", video_id);
                context.startActivity(intent);
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
    public void swap(List list){
        if (videolist != null) {
            //videolist.clear();
            videolist.addAll(list);
        }
        else {
            videolist = list;
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return videolist.size();

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



