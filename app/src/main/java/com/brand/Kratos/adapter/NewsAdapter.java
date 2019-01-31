package com.brand.Kratos.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.Kratos.R;
import com.brand.Kratos.model.NewsModel;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private Context context;
    private List<NewsModel> newsModelList;

    public NewsAdapter(Context context, List<NewsModel> newsModelList) {
        this.context = context;
        this.newsModelList = newsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.newsimage1.setImageResource(newsModelList.get(position).getImage1());
        holder.newsimage2.setImageResource(newsModelList.get(position).getImage2());
        holder.newstitle.setText(newsModelList.get(position).getTitle());
        holder.newsname.setText(newsModelList.get(position).getName());
        holder.newstime.setText(newsModelList.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return newsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newsimage1,newsimage2;
        TextView newstitle,newsname,newstime;
        public ViewHolder(View itemView) {
            super(itemView);
            newsimage1 = itemView.findViewById(R.id.newsimage1);
            newsimage2 = itemView.findViewById(R.id.newsimage2);
            newstitle = itemView.findViewById(R.id.newstitle);
            newsname = itemView.findViewById(R.id.newsname);
            newstime = itemView.findViewById(R.id.newstime);
        }
    }
}
