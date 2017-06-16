package com.gravity.chatme.business.storage.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.gravity.chatme.business.model.User;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Insert(onConflict = IGNORE)
    void insertUser(User user);

    @Insert(onConflict = REPLACE)
    void insertUser(List<User> user);

    @Query("SELECT * FROM user WHERE username == :username")
    User getUser(String username);
}
