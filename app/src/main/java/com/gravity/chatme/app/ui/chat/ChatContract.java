package com.gravity.chatme.app.ui.chat;

import com.gravity.chatme.business.model.Message;

import java.util.ArrayList;

public interface ChatContract {

    interface View {

        void displayMessages(ArrayList<Message> messages);

        void displayMessage(Message message);

        void showWelcomeMessage(String username);

        void loadNavHeader(String username, String email, String urlProfileImg);

        void startActivity();
    }

    interface Presenter {

        void getNavHeader();

        void sendMessage(String messageContent);

        void retrieveLocalMessage();

        void fetchRemoteMessage();

        void getOnScrolledMessages(long firstMessageTime);

        void getWelcomeMessage();

        String getCurrentUser();

        void signOut();


    }
}
