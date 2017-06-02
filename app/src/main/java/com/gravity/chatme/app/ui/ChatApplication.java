package com.gravity.chatme.app.ui;

import android.app.Application;

public class ChatApplication extends Application {

    private static ChatApplication sInstance;

    public ChatApplication() {
        sInstance = this;
    }

    public static ChatApplication getInstance() {
        return sInstance;
    }

}
