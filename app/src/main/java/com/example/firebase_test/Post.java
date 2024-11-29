package com.example.firebase_test;

import java.io.Serializable;

public class Post implements Serializable {
    String key;
    String title;
    String body;

    public Post() {
    }

    public Post(String key, String title, String body) {
        this.key = key;
        this.title = title;
        this.body = body;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
