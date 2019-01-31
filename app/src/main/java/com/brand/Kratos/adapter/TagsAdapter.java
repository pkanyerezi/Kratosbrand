package com.brand.Kratos.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.brand.Kratos.MainContainer;
import com.brand.Kratos.R;
import com.brand.Kratos.TagsList;
import com.brand.Kratos.VideoDetails;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Bold;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Medium;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Regular;
import com.brand.Kratos.model.tags.Data;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.github.lzyzsd.randomcolor.RandomColor;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.MyViewHolder>{

    Context mcontext;


    private List<Data> tags_list;


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


    public TagsAdapter(Context mainActivityContacts, List<Data> tags_list) {
        this.tags_list = tags_list;
        this.mcontext = mainActivityContacts;
    }

    @Override
    public TagsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_item_video, parent, false);


        return new TagsAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(TagsAdapter.MyViewHolder holder, int position) {
        final Data lists = tags_list.get(position);
        RandomColor randomColor = new RandomColor();
        int color = randomColor.randomColor();

        holder.category_name_id.setText(lists.getName());
        holder.category_name_letter.setText(lists.getHashTag());
        holder.category_container_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainContainer.context, TagsList.class);
                intent.putExtra("hashtag",lists.getHashTag());
                intent.putExtra("hashtag_name",lists.getName());
                intent.putExtra("hashtag_id",lists.getId()+"");
                MainContainer.context.startActivity(intent);
            /*    FragmentTransaction ft = MainContainer.fragmentManager.beginTransaction();
                Fragment prev =MainContainer.fragmentManager.findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                MainContainer.dialogFragment = new CategoryVideos(lists.getCode(),lists.getName(),true);
                MainContainer.dialogFragment.show(ft, "dialog");*/
            }
        });
        //holder.image.setImageResource(lists.getImage());

    }



    @Override
    public int getItemCount() {
        return tags_list.size();

    }

}


