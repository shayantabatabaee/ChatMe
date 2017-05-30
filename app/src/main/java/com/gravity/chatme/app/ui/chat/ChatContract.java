package com.gravity.chatme.app.ui.chat;

import com.gravity.chatme.business.model.Message;

public interface ChatContract {

    interface View {
        void displayMessages(Message message);
    }

    interface Presenter {

        void sendMessage(String messageContent);

        void retrieveMessage();
    }
}
