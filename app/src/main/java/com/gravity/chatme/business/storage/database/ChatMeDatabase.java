package com.gravity.chatme.business.storage.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.gravity.chatme.business.model.Message;

@Database(entities = {Message.class}, version = 1)
public abstract class ChatMeDatabase extends RoomDatabase {
    public abstract MessageDao messageDao();
}
