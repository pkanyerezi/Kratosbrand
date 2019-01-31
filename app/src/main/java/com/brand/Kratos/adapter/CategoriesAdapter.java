package com.brand.Kratos.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brand.Kratos.CategoryVideos;
import com.brand.Kratos.CommentsDialog;
import com.brand.Kratos.MainContainer;
import com.brand.Kratos.MainHomeFrag;
import com.brand.Kratos.R;
import com.brand.Kratos.VideoDetails;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Bold;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Medium;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Regular;
import com.brand.Kratos.model.Categories.Data;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.github.lzyzsd.randomcolor.RandomColor;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>{

    Context context;


    private List<Data> category_list;


    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView name;
        ImageView image;
        MaterialLetterIcon letter_budge;
        LinearLayout category_item_container_id;
        MyTextView_Roboto_Regular category_name_id;
        MyTextView_Roboto_Bold category_name_letter;
        CardView category_container_id;
        public MyViewHolder(View view) {
            super(view);

            category_name_id=(MyTextView_Roboto_Regular) view.findViewById(R.id.category_name_id);
            category_name_letter=(MyTextView_Roboto_Bold) view.findViewById(R.id.category_name_letter);
            category_container_id=(CardView) view.findViewById(R.id.category_container_id);



        }

    }


    public CategoriesAdapter(Context mainActivityContacts, List<Data> category_list) {
        this.category_list = category_list;
        this.context = mainActivityContacts;
    }

    @Override
    public CategoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_item_video, parent, false);


        return new CategoriesAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(CategoriesAdapter.MyViewHolder holder, int position) {
        final Data lists = category_list.get(position);
        RandomColor randomColor = new RandomColor();
        int color = randomColor.randomColor();

        holder.category_name_id.setText(lists.getName());
        holder.category_name_letter.setText(lists.getName().substring(0, 1));
        holder.category_container_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(context, CategoryVideos.class);
                intent.putExtra("category_name",lists.getName());
                intent.putExtra("category_id",lists.getCode());
                context.startActivity(intent);*/
                FragmentTransaction ft = MainContainer.fragmentManager.beginTransaction();
                Fragment prev =MainContainer.fragmentManager.findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                MainContainer.dialogFragment = new CategoryVideos(lists.getCode(),lists.getName(),true);
                MainContainer.dialogFragment.show(ft, "dialog");
            }
        });
        //holder.image.setImageResource(lists.getImage());

    }



    @Override
    public int getItemCount() {
        return category_list.size();

    }

}


