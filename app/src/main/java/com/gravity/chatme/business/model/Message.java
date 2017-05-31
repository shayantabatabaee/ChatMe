package com.gravity.chatme.business.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Message {

    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }

    @PrimaryKey
    private Long messageTime;
    @ColumnInfo(name = "message_content")
    private String messageContent;
    @ColumnInfo(name = "message_user")
    private String messageUser;

    public Message() {

    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Long getMessageTime() {
        return messageTime;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }
}

