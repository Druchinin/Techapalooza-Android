package com.consultica.techapalooza.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class News {
    public String id;
    public String title;
    public String content;
    public String date;
    public String image;
    public String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public class NewsResponse {
        Data data;

        class Data {
            List<News> news;
        }

        public List<News> getAllNews(){
            return data.news;
        }
    }
}
