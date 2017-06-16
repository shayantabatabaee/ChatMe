package com.gravity.chatme.business.storage.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.gravity.chatme.business.model.Message;
import com.gravity.chatme.business.model.User;

@Database(entities = {Message.class, User.class}, version = 1)
public abstract class ChatMeDatabase extends RoomDatabase {

    private static ChatMeDatabase sInstance;

    public abstract MessageDao messageDao();

    public abstract UserDao userDao();

    public static ChatMeDatabase getDatabase(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), ChatMeDatabase.class, "chat-me").allowMainThreadQueries()
                    .build();
        }
        return sInstance;
    }
}
