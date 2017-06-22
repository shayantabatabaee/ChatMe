package com.gravity.chatme.business.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity(primaryKeys = {"username"})
public class User {

    private String username;

    @ColumnInfo(name = "user_img")
    private String userImgUrl;
    @ColumnInfo(name = "email")
    private String email;

    @Ignore
    private String userToken;
    @Ignore
    private long lastSeen;
    @Ignore
    private boolean online;
    @Ignore
    private boolean typing;

    public User() {
    }

    public User(String username, String email, String userToken, String userImgUrl) {
        this.email = email;
        this.username = username;
        this.userToken = userToken;
        this.userImgUrl = userImgUrl;
        this.lastSeen = new Date().getTime();
        this.online = true;
        this.typing = false;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean isTyping() {
        return typing;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userImgUrl", userImgUrl);
        result.put("userToken", userToken);
        return result;
    }


}
