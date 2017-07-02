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
    @ColumnInfo(name = "message_sent")
    private boolean messageSent;

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

    public boolean isMessageSent() {
        return messageSent;
    }

    public void setMessageSent(boolean messageSent) {
        this.messageSent = messageSent;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj instanceof Message) {
            return ((Message) obj).getUid().equals(this.getUid());
        }

        return false;
    }
}

