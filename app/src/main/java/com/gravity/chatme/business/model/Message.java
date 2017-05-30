package com.gravity.chatme.business.model;

import java.util.Date;

public class Message {

    private String messageContent;
    private Long messageTime;
    private String messageUser;

    public Message() {
        messageTime = new Date().getTime();
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

