package com.gravity.chatme.app.ui.chat;

import com.gravity.chatme.business.model.Message;

import java.util.ArrayList;

public interface ChatContract {

    interface View {

        void displayLowerData(ArrayList<Message> messages);

        void displayUpperData(ArrayList<Message> messages);

        void displayData(Message message);

        void loadNavHeader(String username, String email, String urlProfileImg);

        void startActivity();

        void displayMemberNumber(long number);
    }

    interface Presenter {

        void getData();

        void sendData(String content);

        void getScrolledData(long firstTime);

        void updateStatus(boolean online, long lastSeen);

        void signOut();

        String getCurrentUser();


    }
}
