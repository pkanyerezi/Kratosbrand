package com.brand.Kratos.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brand.Kratos.MainContainer;
import com.brand.Kratos.R;
import com.brand.Kratos.TagsList;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Bold;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Medium;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Regular;
import com.brand.Kratos.model.tags.Data;
import com.github.ivbaranov.mli.MaterialLetterIcon;
import com.github.lzyzsd.randomcolor.RandomColor;

import java.util.List;

public class TagsHomeAdapter extends RecyclerView.Adapter<TagsHomeAdapter.MyViewHolder>{

    Context mcontext;


    private List<Data> tags_list;


    public class MyViewHolder extends RecyclerView.ViewHolder {




        MaterialLetterIcon letter_budge;
        LinearLayout tags_container;
        MyTextView_Roboto_Medium tags_tite;
        public MyViewHolder(View view) {
            super(view);

            letter_budge=(MaterialLetterIcon) view.findViewById(R.id.letter_budge);
            tags_container=(LinearLayout) view.findViewById(R.id.tags_container);
            tags_tite=(MyTextView_Roboto_Medium) view.findViewById(R.id.tags_tite);



        }

    }


    public TagsHomeAdapter(Context mainActivityContacts, List<Data> tags_list) {
        this.tags_list = tags_list;
        this.mcontext = mainActivityContacts;
    }

    @Override
    public TagsHomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_tags, parent, false);


        return new TagsHomeAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(TagsHomeAdapter.MyViewHolder holder, int position) {
        final Data lists = tags_list.get(position);
        RandomColor randomColor = new RandomColor();
        //int color = randomColor.randomColor();
        int color[] = randomColor.random(RandomColor.Color.ORANGE, position+1);
        String[] parts = lists.getName().split(" ");
//        String lastWord = parts[parts.length - 2]+""+parts[parts.length - 1];
        holder.letter_budge.setLetter(lists.getName());
        holder.tags_tite.setText("#"+parts[0]);
        holder.letter_budge.setShapeColor(color[position]);
        holder.tags_container.setOnClickListener(new View.OnClickListener() {
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


