package com.example.myapplication.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.NewsArticlesMainResponse;
import com.example.myapplication.model.NewsArticlesResponse;

import java.util.ArrayList;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsVH> {

    ArrayList<NewsArticlesResponse> newsArticlesResponses = new ArrayList<>();
    Activity activity;
    NewsFeedClickListener newsFeedClickListener;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public NewsFeedAdapter(ArrayList<NewsArticlesResponse> newsArticlesResponses, Activity activity, NewsFeedClickListener newsFeedClickListener,SharedPreferences sharedPreferences,SharedPreferences.Editor editor) {
        this.newsArticlesResponses = newsArticlesResponses;
        this.activity = activity;
        this.newsFeedClickListener = newsFeedClickListener;
        this.sharedPreferences = sharedPreferences;
        this.editor = editor;
    }

    @NonNull
    @Override
    public NewsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_feed_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsVH holder, int position) {

        NewsArticlesResponse response = newsArticlesResponses.get(position);



        try {
            Glide.with(activity).load(newsArticlesResponses.get(position).getUrlToImage()).into(holder.newsIVID);
            holder.newsTitleTVID.setText(response.getAuthor());
            holder.linkTVID.setText(response.getUrl());
            holder.contentTVID.setText(response.getContent());
            holder.linkTVID.setOnClickListener(view -> newsFeedClickListener.onNewsFeedClick(response,holder,view,position));
            holder.likeIVID.setOnClickListener(view -> newsFeedClickListener.onNewsFeedClick(response,holder,view,position));
            holder.unLikeIVID.setOnClickListener(view -> newsFeedClickListener.onNewsFeedClick(response,holder,view,position));
            holder.viewBtn.setOnClickListener(view -> newsFeedClickListener.onNewsFeedClick(response,holder,view,position));
            holder.linkRLID.setOnClickListener(view -> newsFeedClickListener.onNewsFeedClick(response,holder,view,position));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response.isFavorite()) {
            holder.likeIVID.setVisibility(View.VISIBLE);
            holder.unLikeIVID.setVisibility(View.GONE);
        } else {
            holder.likeIVID.setVisibility(View.GONE);
            holder.unLikeIVID.setVisibility(View.VISIBLE);
        }

        holder.unLikeIVID.setOnClickListener(view1 -> {
            holder.likeIVID.setVisibility(View.VISIBLE);
            holder.unLikeIVID.setVisibility(View.GONE);
            boolean newFavoriteState = !response.isFavorite();
            response.setFavorite(newFavoriteState);
            saveFavoriteState(response.getTitle(), newFavoriteState);

            notifyItemChanged(position);
        });

    }

    private void saveFavoriteState(String articleId, boolean newFavoriteState) {

        editor.putBoolean(articleId, newFavoriteState);
        editor.apply();

    }

    @Override
    public int getItemCount() {
        return newsArticlesResponses.size();
    }

    public class NewsVH extends RecyclerView.ViewHolder{

        public AppCompatTextView newsTitleTVID,linkTVID,contentTVID;
        public AppCompatImageView newsIVID,likeIVID,unLikeIVID;
        public RelativeLayout linkRLID;
        public AppCompatButton viewBtn;
        public NewsVH(@NonNull View itemView) {
            super(itemView);

            newsTitleTVID = itemView.findViewById(R.id.newsTitleTVID);
            linkTVID = itemView.findViewById(R.id.linkTVID);
            contentTVID = itemView.findViewById(R.id.contentTVID);
            newsIVID = itemView.findViewById(R.id.newsIVID);
            likeIVID = itemView.findViewById(R.id.likeIVID);
            unLikeIVID = itemView.findViewById(R.id.unLikeIVID);
            viewBtn = itemView.findViewById(R.id.viewBtn);
            linkRLID = itemView.findViewById(R.id.linkRLID);
        }
    }

    public interface NewsFeedClickListener {
        void onNewsFeedClick(NewsArticlesResponse response,NewsVH newsVH,View view, int position);
    }
}
