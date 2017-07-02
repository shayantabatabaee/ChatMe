package com.gravity.chatme.app.ui.chat;

import com.gravity.chatme.business.model.Message;

import java.util.ArrayList;

public interface ChatContract {

    interface View {
        void loadNavHeader(String username, String email, String urlProfileImg);

        void startActivity();

        void displayMemberNumber(long number);

        void displayTyping(String typingContent);

        void displayData(int level);
    }

    interface Presenter {

        void detachView();

        void attachView(ChatActivity view);

        void getData();

        void sendData(String content);

        void getScrolledData(long firstTime);

        void updateStatus(boolean online, long lastSeen);

        void setIsTyping(boolean typing);

        void signOut();

        ArrayList<Message> getMessageList();

    }
}
