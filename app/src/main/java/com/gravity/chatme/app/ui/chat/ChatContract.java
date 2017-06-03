package com.gravity.chatme.app.ui.chat;

import com.gravity.chatme.business.model.Message;

import java.util.ArrayList;

public interface ChatContract {

    interface View {

        void displayMessages(ArrayList<Message> messages);

        void displayMessage(Message message);

        void showWelcomeMessage(String username);
    }

    interface Presenter {

        void sendMessage(String messageContent);

        void retrieveMessage();

        void getWelcomeMessage();

        String getCurrentUser();
    }
}
