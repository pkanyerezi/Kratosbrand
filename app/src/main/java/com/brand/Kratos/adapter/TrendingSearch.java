package com.brand.Kratos.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brand.Kratos.MainContainer;
import com.brand.Kratos.R;
import com.brand.Kratos.VideoDetails;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Medium;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Regular;
import com.brand.Kratos.model.VideoContent.Data;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrendingSearch extends RecyclerView.Adapter<TrendingSearch.MyViewHolder>{

    Context mcontext;


    private List<Data> videolist;


    public class MyViewHolder extends RecyclerView.ViewHolder {



        MyTextView_Roboto_Regular textrecycler;
        MyTextView_Roboto_Regular duration_id;
        ImageView thumbnail_id;

        public MyViewHolder(View view) {
            super(view);

            textrecycler=(MyTextView_Roboto_Regular)view.findViewById(R.id.textrecycler);
            duration_id=(MyTextView_Roboto_Regular)view.findViewById(R.id.duration_id);
            thumbnail_id=(ImageView)view.findViewById(R.id.thumbnail_id);


        }

    }


    public TrendingSearch(Context mainActivityContacts, List<Data> videolist) {
        this.videolist = videolist;
        this.mcontext = mainActivityContacts;
    }

    @Override
    public TrendingSearch.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_trending, parent, false);


        return new TrendingSearch.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(TrendingSearch.MyViewHolder holder, int position) {
        Data lists = videolist.get(position);
        final String video_url = lists.getVideoUrl();
        final String video_code_id = lists.getCode();
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
        String[] parts = video_title.split(" ");
        String lastWord = parts[parts.length - 2]+""+parts[parts.length - 1];
        holder.textrecycler.setText(video_title);
        //  holder.views_id.setText(video_category +" . " +video_views+ "");
         holder.duration_id.setText(video_duration);
        Picasso.get()
                .load(video_image_url)
                .placeholder(R.drawable.youtube )
                // .resize(50, 50)
                // .centerCrop()
                .fit().centerCrop()
                .into(holder.thumbnail_id);
        // holder.thumbnail_id.setImageResource(lists.getImage());
        holder.thumbnail_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainContainer.context, VideoDetails.class);
                intent.putExtra("description",video_description);
                intent.putExtra("title",video_title);
                intent.putExtra("video_url",video_url);
                intent.putExtra("video_views",video_views);
                intent.putExtra("video_category_id",video_category_id);
                intent.putExtra("video_duration_x",video_duration);
                intent.putExtra("video_category",video_category);
                intent.putExtra("likes_no",likes_no);
                intent.putExtra("dislike_no_txt",dislike_no);
                intent.putExtra("added_date_txt",added_date);
                intent.putExtra("video_code_id",video_code_id);
                MainContainer.context.startActivity(intent);
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

