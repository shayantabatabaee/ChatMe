package com.gravity.chatme.business.model;


import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String userToken;

    private String userImgUrl;

    public User() {
    }

    public User(String userToken, String userImgUrl) {
        this.userToken = userToken;
        this.userImgUrl = userImgUrl;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public String getUserToken() {
        return userToken;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userImgUrl", userImgUrl);
        result.put("userToken", userToken);
        return result;
    }


}
