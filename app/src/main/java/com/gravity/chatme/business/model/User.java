package com.gravity.chatme.business.model;


import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String userToken;

    private String userImgUrl;

    private String username;

    private String email;

    private long lastSeen;

    private boolean online;

    public User() {
    }

    public User(String username, String email, String userToken, String userImgUrl) {
        this.email = email;
        this.username = username;
        this.userToken = userToken;
        this.userImgUrl = userImgUrl;
        this.lastSeen = new Date().getTime();
        this.online = true;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public String getUserToken() {
        return userToken;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public boolean isOnline() {
        return online;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userImgUrl", userImgUrl);
        result.put("userToken", userToken);
        return result;
    }


}
