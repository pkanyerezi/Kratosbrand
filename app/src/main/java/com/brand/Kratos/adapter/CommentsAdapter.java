package com.brand.Kratos.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.brand.Kratos.model.Comments.Data;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.github.lzyzsd.randomcolor.RandomColor;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>{

    Context mcontext;

    Date date_co = null;
    private List<Data> videolist;
    SharedPreferences pref;

    public class MyViewHolder extends RecyclerView.ViewHolder {



        MaterialLetterIcon letter_budge;
        MyTextView_Roboto_Regular name_id;
        MyTextView_Roboto_Regular comment_txt_id;
        RelativeTimeTextView timestamp;
        CircleImageView user_thumbnail;

        public MyViewHolder(View view) {
            super(view);

            letter_budge=(MaterialLetterIcon)view.findViewById(R.id.letter_budge);
            name_id=(MyTextView_Roboto_Regular)view.findViewById(R.id.name_id);
            comment_txt_id=(MyTextView_Roboto_Regular)view.findViewById(R.id.comment_txt_id);
            timestamp=(RelativeTimeTextView)view.findViewById(R.id.timestamp);
            user_thumbnail=(CircleImageView)view.findViewById(R.id.user_thumbnail);


        }

    }


    public CommentsAdapter(Context mainActivityContacts, List<Data> videolist) {
        this.videolist = videolist;
        this.mcontext = mainActivityContacts;
    }

    @Override
    public CommentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);


        return new CommentsAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(CommentsAdapter.MyViewHolder holder, int position) {
        Data lists = videolist.get(position);
        final String comment = lists.getBody();
        final String user = lists.getAuthor().replace('"',' ');
        final String comment_date = lists.getDate();
        holder.name_id.setText(user);
        holder.comment_txt_id.setText(comment);

        RandomColor randomColor = new RandomColor();
        int color = randomColor.randomColor();
        holder.letter_budge.setShapeColor(color);

        String[] parts = comment_date.split("\\+");
        SimpleDateFormat format = new SimpleDateFormat("DD-MM-YYYY HH:ss", Locale.US);
       // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
//"DD-MM-YYYY HH:ss"

        long longmilliseconds=9877;
        try {
            //  Toast.makeText(mContext, "there ttt...date" +date, Toast.LENGTH_SHORT).show();
            Date dating = format.parse(comment_date);
            date_co = dating;
            System.out.println("datex"+date_co);
            longmilliseconds = date_co.getTime();
            // Toast.makeText(mContext, "there date inside" +longmilliseconds, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("datecomment"+date_co.getTime());
        holder.timestamp.setReferenceTime(date_co.getTime());
        pref = mcontext.getSharedPreferences("Settings_variables", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        String full_name = pref.getString("full_name", "").replace('"',' ');
        String login_type = pref.getString("type", "");
        String photourl = pref.getString("photourl", "").replace('"',' ');
        if(user.replaceAll("\\s+","").equals(full_name.replaceAll("\\s+",""))){


            Picasso.get()
                    .load(photourl.toString().replace('"',' ').replace(" ",""))
                    .placeholder(R.drawable.imageplaceholder )

                    // .resize(50, 50)
                    // .centerCrop()
                    .fit().centerCrop()
                    .into(holder.user_thumbnail);
            holder.user_thumbnail.setVisibility(View.VISIBLE);
            holder.letter_budge.setVisibility(View.GONE);
        }else{
            holder.letter_budge.setLetter(user);
            holder.user_thumbnail.setVisibility(View.GONE);
            holder.letter_budge.setVisibility(View.VISIBLE);
        }
      /*  Picasso.get()
                .load(video_image_url)
                .placeholder(R.drawable.youtube )
                // .resize(50, 50)
                // .centerCrop()
                .fit().centerCrop()
                .into(holder.image_thumbnail);*/
        // holder.thumbnail_id.setImageResource(lists.getImage());
       /* holder.image_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


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


