package com.gravity.chatme.business.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Message {

    @PrimaryKey
    private String uid;

    @ColumnInfo(name = "message_time")
    private Long messageTime;
    @ColumnInfo(name = "message_content")
    private String messageContent;
    @ColumnInfo(name = "message_user")
    private String messageUser;

    public Message() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }
}

