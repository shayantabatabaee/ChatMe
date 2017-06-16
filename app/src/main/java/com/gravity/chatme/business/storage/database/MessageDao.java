package com.gravity.chatme.business.storage.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.gravity.chatme.business.model.Message;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;


@Dao
public interface MessageDao {

    @Query("SELECT * FROM message ORDER BY message_time DESC LIMIT 10")
    List<Message> getAllMessages();

    @Query("SELECT * FROM message WHERE message_time < :messageTime ORDER BY message_time DESC LIMIT 10")
    List<Message> getOnScrolledMessages(long messageTime);

    @Insert(onConflict = IGNORE)
    void insertMessage(Message message);

    @Insert(onConflict = IGNORE)
    void insertMessage(List<Message> messages);

}
