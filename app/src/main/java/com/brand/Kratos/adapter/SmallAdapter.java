package com.brand.Kratos.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.Kratos.R;
import com.brand.Kratos.model.HomeModel;

import java.util.List;


public class SmallAdapter extends RecyclerView.Adapter<SmallAdapter.MyViewHolder>{

        Context context;


private List<HomeModel> OfferList;


public class MyViewHolder extends RecyclerView.ViewHolder {



    TextView name;
    ImageView image;

    public MyViewHolder(View view) {
        super(view);

        name=(TextView)view.findViewById(R.id.txt3);
        image=(ImageView)view.findViewById(R.id.image3);


    }

}


    public SmallAdapter(Context mainActivityContacts, List<HomeModel> offerList) {
        this.OfferList = offerList;
        this.context = mainActivityContacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler1, parent, false);


        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HomeModel lists = OfferList.get(position);

        holder.name.setText(lists.getName());
        holder.image.setImageResource(lists.getImage());

    }



    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}

