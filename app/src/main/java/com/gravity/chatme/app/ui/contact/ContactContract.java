package com.gravity.chatme.app.ui.contact;


import com.gravity.chatme.business.model.User;

public interface ContactContract {

    interface view {
        void displayData(User user);

        void displayData(long lastSeen, boolean online);
    }

    interface presenter {
        void attachView(ContactActivity view);

        void detachView();

        void getData(String username);
    }

}
