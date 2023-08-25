package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

public class NewsViewActivity extends AppCompatActivity {

    AppCompatTextView newsTitleTVID,linkTVID,contentTVID;
    AppCompatImageView newsIVID,likeIVID,unLikeIVID;
    String imageUrl,title,link,content;
    RelativeLayout backRLID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        Intent intent = getIntent();
        if (intent != null) {
            imageUrl = intent.getStringExtra("image");
            title = intent.getStringExtra("title");
            link = intent.getStringExtra("link");
            content = intent.getStringExtra("content");
        }

        newsTitleTVID = findViewById(R.id.newsTitleTVID);
        linkTVID = findViewById(R.id.linkTVID);
        contentTVID = findViewById(R.id.contentTVID);
        newsIVID = findViewById(R.id.newsIVID);
        likeIVID = findViewById(R.id.likeIVID);
        unLikeIVID = findViewById(R.id.unLikeIVID);
        backRLID = findViewById(R.id.backRLID);

        Glide.with(this).load(imageUrl).into(newsIVID);
        newsTitleTVID.setText(title);
        linkTVID.setText(link);
        contentTVID.setText(content);
        backRLID.setOnClickListener(view -> {
            finish();
        });

        unLikeIVID.setOnClickListener(view1 -> {
            likeIVID.setVisibility(View.VISIBLE);
            unLikeIVID.setVisibility(View.GONE);
        });

    }
}