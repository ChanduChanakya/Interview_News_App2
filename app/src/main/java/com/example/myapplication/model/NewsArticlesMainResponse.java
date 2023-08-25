package com.example.myapplication.model;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsArticlesMainResponse implements Serializable {

    private String status;
    private int totalResults;
    ArrayList<NewsArticlesResponse>  articles = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<NewsArticlesResponse> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<NewsArticlesResponse> articles) {
        this.articles = articles;
    }
}
