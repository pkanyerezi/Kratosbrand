package com.brand.Kratos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brand.Kratos.MainHomeFrag;
import com.brand.Kratos.R;
import com.brand.Kratos.model.Categories.Data;
import com.brand.Kratos.model.HomeModel;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.github.lzyzsd.randomcolor.RandomColor;

import java.util.List;


public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.MyViewHolder>{

    Context context;


    private List<Data> category_list;


    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView name;
        ImageView image;
        MaterialLetterIcon letter_budge;
        LinearLayout category_item_container_id;
        public MyViewHolder(View view) {
            super(view);

            category_item_container_id=(LinearLayout) view.findViewById(R.id.category_item_container_id);
            name=(TextView)view.findViewById(R.id.txt1);
            image=(ImageView)view.findViewById(R.id.image1);
            letter_budge=(MaterialLetterIcon)view.findViewById(R.id.letter_budge);


        }

    }


    public RoundAdapter(Context mainActivityContacts, List<Data> category_list) {
        this.category_list = category_list;
        this.context = mainActivityContacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);


        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Data lists = category_list.get(position);
        RandomColor randomColor = new RandomColor();
        int color = randomColor.randomColor();

        holder.name.setText(lists.getName());
        holder.letter_budge.setLetter(lists.getName().toString());
        holder.letter_budge.setShapeColor(color);
        holder.category_item_container_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category_id = lists.getCode();
               MainHomeFrag.loadcontent(category_id,MainHomeFrag.page, MainHomeFrag.limit ,MainHomeFrag.code ,MainHomeFrag.type ,MainHomeFrag.search ,MainHomeFrag.sort, MainHomeFrag.direction);

                MainHomeFrag.swipeRefreshLayout.setRefreshing(true);
            }
        });
        //holder.image.setImageResource(lists.getImage());

    }



    @Override
    public int getItemCount() {
        return category_list.size();

    }

}


