package com.brand.Kratos.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.brand.Kratos.CategoryVideos;
import com.brand.Kratos.MainContainer;
import com.brand.Kratos.R;
import com.brand.Kratos.VideoDetails;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Bold;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Medium;
import com.brand.Kratos.model.sports.Data;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.MyViewHolder>{

    Context mcontext;


    private List<Data> videolist;


    public class MyViewHolder extends RecyclerView.ViewHolder {



        MyTextView_Roboto_Bold sports_tite;
        ImageView image_thumbnail;
        MaterialLetterIcon letter_budge;
        LinearLayout sports_container_id;
        public MyViewHolder(View view) {
            super(view);

            sports_tite=(MyTextView_Roboto_Bold)view.findViewById(R.id.sports_title);
            image_thumbnail=(ImageView)view.findViewById(R.id.image_thumbnail);
            letter_budge=(MaterialLetterIcon)view.findViewById(R.id.letter_budge);
            sports_container_id=(LinearLayout) view.findViewById(R.id.sports_container_id);


        }

    }


    public SportsAdapter(Context mainActivityContacts, List<Data> videolist) {
        this.videolist = videolist;
        this.mcontext = mainActivityContacts;
    }

    @Override
    public SportsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sports_item, parent, false);


        return new SportsAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(SportsAdapter.MyViewHolder holder, int position) {
        final Data lists = videolist.get(position);
        final String name = lists.getName();
        final int id = lists.getId();

        holder.sports_tite.setText(name);
        holder.sports_tite.setVisibility(View.VISIBLE);
        if(id == 1){
            //rugby
            holder.image_thumbnail.setImageResource(R.drawable.r_rugby);
        }else if(id == 4){
            //soccer
            holder.image_thumbnail.setImageResource(R.drawable.r_football);
        }else if(id == 3){
            //basketball
            holder.image_thumbnail.setImageResource(R.drawable.r_basketball);
        }else{
        holder.letter_budge.setLetter(name);
        holder.image_thumbnail.setVisibility(View.GONE);
        holder.letter_budge.setVisibility(View.VISIBLE);
        }

      /*  Picasso.get()
                .load(video_image_url)
                .placeholder(R.drawable.youtube )
                // .resize(50, 50)
                // .centerCrop()
                .fit().centerCrop()
                .into(holder.image_thumbnail);*/
        holder.sports_container_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = MainContainer.fragmentManager.beginTransaction();
                Fragment prev =MainContainer.fragmentManager.findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                MainContainer.dialogFragment = new CategoryVideos(lists.getId().toString(),lists.getName(),false);
                MainContainer.dialogFragment.show(ft, "dialog");
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


