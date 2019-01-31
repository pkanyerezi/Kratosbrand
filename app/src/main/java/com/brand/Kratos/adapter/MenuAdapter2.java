package com.brand.Kratos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.Kratos.R;
import com.brand.Kratos.model.MenuModel2;

import java.util.List;


public class MenuAdapter2 extends RecyclerView.Adapter<MenuAdapter2.MyViewHolder>{

    Context context;


    private List<MenuModel2> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView musicName,musicArtist;
        ImageView movieImage;

        public MyViewHolder(View view) {
            super(view);

            musicName=(TextView)view.findViewById(R.id.musicName);
            musicArtist=(TextView)view.findViewById(R.id.musicArtist);
            movieImage=(ImageView)view.findViewById(R.id.movieImage);


        }

    }


    public MenuAdapter2(Context mainActivityContacts, List<MenuModel2> offerList) {
        this.OfferList = offerList;
        this.context = mainActivityContacts;
    }

    @Override
    public MenuAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_new_release_recycler, parent, false);


        return new MenuAdapter2.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MenuModel2 lists = OfferList.get(position);
        holder.musicName.setText(lists.getMusicName());
        holder.musicArtist.setText(lists.getMusicArtist());
        holder.movieImage.setImageResource(lists.getMovieName());
    }





    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}
