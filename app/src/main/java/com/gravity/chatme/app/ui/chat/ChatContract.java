package com.gravity.chatme.app.ui.chat;

import com.gravity.chatme.business.model.Message;

import java.util.ArrayList;

public interface ChatContract {

    interface View {
        void displayMessages(ArrayList<Message> messageList);
    }

    interface Presenter {

        void sendMessage(String messageContent);

        void retrieveMessage();
    }
}
