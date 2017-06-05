package com.gravity.chatme.app;

import android.app.Application;

public class ChatApplication extends Application {

    public static boolean isInBackground = true;

    private static ChatApplication sInstance;

    public ChatApplication() {
        sInstance = this;
    }

    public static ChatApplication getInstance() {
        return sInstance;
    }

}
