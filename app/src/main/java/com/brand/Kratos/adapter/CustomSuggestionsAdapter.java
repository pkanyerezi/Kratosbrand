package com.brand.Kratos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import com.brand.Kratos.R;
import com.brand.Kratos.VideoDetails;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Medium;
import com.brand.Kratos.customfonts.MyTextView_Roboto_Regular;
import com.brand.Kratos.model.VideoContent.Data;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class CustomSuggestionsAdapter extends SuggestionsAdapter<Data, CustomSuggestionsAdapter.SuggestionHolder> {
    Context context;
    public CustomSuggestionsAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public int getSingleViewHeight() {
        return 80;
    }

    @Override
    public SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.related_videos_item, parent, false);
        return new SuggestionHolder(view);
    }

    @Override
    public void onBindSuggestionHolder(Data suggestion, SuggestionHolder holder, int position) {

        final String video_url = suggestion.getVideoUrl();
        String video_image_url = suggestion.getImageUrl();
        final String video_description = suggestion.getDescription();
        final String video_title = suggestion.getTitle();
        final String video_views = suggestion.getViews().toString();
        final String video_category = suggestion.getCategory().getName().toString();

        final String video_category_id = suggestion.getCategory().getCode().toString();
        final String likes_no = suggestion.getLikes().toString();
        final String dislike_no = suggestion.getDislikes().toString();
        final String added_date = suggestion.getDateAdded().toString();
        final String video_duration= secToTime(suggestion.getDuration());

        holder.duration_id.setText(video_duration);
        holder.textrecycler.setText(video_title);
        holder.views_id_txt.setText(video_category +" . " +video_views+ " Views");
       // holder.views_id.setText(video_category +" . " +video_views+ "");
        // holder.views_id.setText(lists.getTitle());
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
                Intent intent = new Intent(context, VideoDetails.class);
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
                context.startActivity(intent);
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String term = constraint.toString();
                if(term.isEmpty())
                    suggestions = suggestions_clone;
                else {
                    suggestions = new ArrayList<>();
                    for (Data item: suggestions_clone)
                        if(item.getTitle().toLowerCase().contains(term.toLowerCase()))
                            suggestions.add(item);
                }
                results.values = suggestions;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                suggestions = (ArrayList<Data>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    static class SuggestionHolder extends RecyclerView.ViewHolder{
        protected TextView title;
        protected MyTextView_Roboto_Medium textrecycler;
        protected MyTextView_Roboto_Medium duration_id;
        protected MyTextView_Roboto_Regular views_id_txt;
        protected ImageView thumbnail_id;

        public SuggestionHolder(View itemView) {
            super(itemView);
            textrecycler = (MyTextView_Roboto_Medium) itemView.findViewById(R.id.textrecycler);
            duration_id = (MyTextView_Roboto_Medium) itemView.findViewById(R.id.duration_id);
            views_id_txt = (MyTextView_Roboto_Regular) itemView.findViewById(R.id.views_id_txt);
            thumbnail_id = (ImageView) itemView.findViewById(R.id.thumbnail_id);
        }
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