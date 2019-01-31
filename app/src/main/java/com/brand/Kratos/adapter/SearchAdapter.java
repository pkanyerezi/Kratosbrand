package com.brand.Kratos.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brand.Kratos.R;
import com.brand.Kratos.VideoDetails;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Medium;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Regular;
import com.brand.Kratos.model.VideoContent.Data;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{

    Context context;
    Date date = null;


    private List<Data> videolist;


    public class MyViewHolder extends RecyclerView.ViewHolder {



        MyTextView_Roboto_Medium duration_id;
        MyTextView_Roboto_Medium textrecycler;
        MyTextView_Roboto_Regular views_id;
        LinearLayout related_video_container;
        ImageView thumbnail_id;

        public MyViewHolder(View view) {
            super(view);

            related_video_container=(LinearLayout) view.findViewById(R.id.related_video_container);
            duration_id=(MyTextView_Roboto_Medium)view.findViewById(R.id.duration_id);
            textrecycler=(MyTextView_Roboto_Medium)view.findViewById(R.id.textrecycler);
            views_id=(MyTextView_Roboto_Regular)view.findViewById(R.id.views_id_txt);
            thumbnail_id=(ImageView)view.findViewById(R.id.thumbnail_id);


        }

    }


    public SearchAdapter(Context mainActivityContacts, List<Data> videolist) {
        this.videolist = videolist;
        this.context = mainActivityContacts;
    }

    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_video_item, parent, false);


        return new SearchAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(SearchAdapter.MyViewHolder holder, int position) {
        System.out.println("loading ....lxx");
        Data lists = videolist.get(position);
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

        //published_date


        // VideoDetails.published_date.setText("Published on "+added_date);
        holder.duration_id.setText(" "+video_duration);
        holder.textrecycler.setText(video_title);
        holder.views_id.setText(video_category +" . " +video_views+ " Views  ");
        //  holder.views_id.setText(video_category +" . " +video_views+ "");
        // holder.views_id.setText(lists.getTitle());
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
        System.out.println("datexxx123"+video_url);
        Picasso.get()
                .load(video_image_url)
                .placeholder(R.drawable.youtube )

                // .resize(50, 50)
                // .centerCrop()
                .fit().centerCrop()
                .into(holder.thumbnail_id);
        // holder.thumbnail_id.setImageResource(lists.getImage());
        holder.related_video_container.setOnClickListener(new View.OnClickListener() {
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
                context.startActivity(intent);
            }
        });

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
