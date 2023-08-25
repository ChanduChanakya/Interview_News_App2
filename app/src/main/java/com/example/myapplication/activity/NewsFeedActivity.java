package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.NewsFeedAdapter;
import com.example.myapplication.model.NewsArticlesMainResponse;
import com.example.myapplication.model.NewsArticlesResponse;
import com.example.myapplication.service.ApiClient;
import com.example.myapplication.service.ApiInterface;
import com.example.myapplication.service.AppConstants;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFeedActivity extends AppCompatActivity implements NewsFeedAdapter.NewsFeedClickListener {

    RecyclerView newsFeedRVID;
    ProgressBar pb;
    NewsArticlesMainResponse newsArticlesMainResponse;
    ArrayList<NewsArticlesResponse> newsArticlesResponses = new ArrayList<>();
    LinearLayoutManager layoutManager;
    NewsFeedAdapter adapter;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        newsFeedRVID = findViewById(R.id.newsFeedRVID);
        pb = findViewById(R.id.pb);

        getNewsFeed();
    }

    private ArrayList<NewsArticlesResponse> loadArticlesWithFavorites(ArrayList<NewsArticlesResponse> articles) {
        for (NewsArticlesResponse article : articles) {
            boolean isFavorite = sharedPreferences.getBoolean(article.getTitle(), false);
            article.setFavorite(isFavorite);
        }
        return articles;
    }

    private void getNewsFeed() {


        pb.setVisibility(View.VISIBLE);
        newsFeedRVID.setVisibility(View.GONE);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<NewsArticlesMainResponse> call = apiInterface.getNewsArticles("tesla","2023-07-25","publishedAt", AppConstants.API_KEY);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<NewsArticlesMainResponse>() {
            @Override
            public void onResponse(Call<NewsArticlesMainResponse> call, Response<NewsArticlesMainResponse> response) {

                if (response.body() != null && response.code() == 200){

                    Log.e("response==>","" + "Success");

                    pb.setVisibility(View.GONE);
                    newsFeedRVID.setVisibility(View.VISIBLE);

                    newsArticlesMainResponse = response.body();
                    newsArticlesResponses = newsArticlesMainResponse.getArticles();
                    newsArticlesResponses = loadArticlesWithFavorites(newsArticlesResponses);

                    if (newsArticlesResponses.size() > 0){

                        layoutManager = new LinearLayoutManager(NewsFeedActivity.this,LinearLayoutManager.VERTICAL,false);
                        newsFeedRVID.setLayoutManager(layoutManager);

                        adapter = new NewsFeedAdapter(newsArticlesResponses,NewsFeedActivity.this,NewsFeedActivity.this,sharedPreferences,editor);
                        newsFeedRVID.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsArticlesMainResponse> call, Throwable t) {

                Log.e("response==>","" + "Failure");
            }
        });
    }

    @Override
    public void onNewsFeedClick(NewsArticlesResponse response, NewsFeedAdapter.NewsVH newsVH, View view, int position) {


        newsVH.viewBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(NewsFeedActivity.this,NewsViewActivity.class);
            intent.putExtra("image",response.getUrlToImage());
            intent.putExtra("title",response.getAuthor());
            intent.putExtra("link",response.getUrl());
            intent.putExtra("content",response.getContent());
            startActivity(intent);
        });

        newsVH.likeIVID.setOnClickListener(view1 -> {
            newsVH.likeIVID.setVisibility(View.GONE);
            newsVH.unLikeIVID.setVisibility(View.VISIBLE);
        });


        newsVH.linkTVID.setOnClickListener(view1 -> {

            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();

            Uri webpage = Uri.parse(response.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(intent);


        });



    }
}