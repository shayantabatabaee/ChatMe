package com.gravity.chatme.business.storage.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.gravity.chatme.business.model.Message;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;


@Dao
public interface MessageDao {

    @Query("SELECT * FROM message")
    List<Message> getAllMessages();

    @Insert(onConflict = IGNORE)
    void insertMessage(Message message);
}
