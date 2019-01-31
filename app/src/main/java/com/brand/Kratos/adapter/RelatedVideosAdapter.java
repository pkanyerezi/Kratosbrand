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
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RelatedVideosAdapter extends RecyclerView.Adapter<RelatedVideosAdapter.MyViewHolder>{

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


    public RelatedVideosAdapter(Context mainActivityContacts, List<Data> videolist) {
        this.videolist = videolist;
        this.context = mainActivityContacts;
    }

    @Override
    public RelatedVideosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.related_videos_item, parent, false);


        return new RelatedVideosAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(RelatedVideosAdapter.MyViewHolder holder, int position) {
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
        System.out.println("datexxx123"+date);
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
               /* Intent intent = new Intent(context, VideoDetails.class);
                context.startActivity(intent);*/
               VideoDetails.playerView.setup(new PlayerConfig.Builder()
                        .file(""+video_url)
                        .autostart(true)
                        .build());
                VideoDetails.autoreply_id.setChecked(false);
                VideoDetails.views_id_txt_id.setText(video_views +" Views  ");
                VideoDetails.video_title_id.setText(video_title +"");
                VideoDetails.duration_id.setText(video_duration +"");
                VideoDetails.categoty_id.setText(""+video_category);
                VideoDetails.dislikes_txt_id.setText(dislike_no+" Dislikes");
                VideoDetails.likes_tx_id.setText(likes_no+" Likes");
                VideoDetails.letter_budge.setLetter(video_category);
                VideoDetails.share_btn.setText("Share");
                VideoDetails.timestamp.setReferenceTime(date.getTime());
                VideoDetails.comment_btn.setText("234 Comments");
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
