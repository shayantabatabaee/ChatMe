package com.gravity.chatme.app.ui.chat;

import com.gravity.chatme.business.model.Message;

import java.util.ArrayList;

public interface ChatContract {

    interface View {

        void displayMessages(ArrayList<Message> messages);

        void displayMessage(Message message);

        void loadNavHeader(String username, String email, String urlProfileImg);

        void startActivity();

        void displayMemberNumber(long number);
    }

    interface Presenter {

        void getNavHeader();

        void sendMessage(String messageContent);

        void retrieveLocalMessage();

        void fetchRemoteMessage();

        void getOnScrolledMessages(long firstMessageTime);

        void updateStatus(boolean online, long lastSeen);

        void getMemberNumber();

        String getCurrentUser();

        void signOut();


    }
}
