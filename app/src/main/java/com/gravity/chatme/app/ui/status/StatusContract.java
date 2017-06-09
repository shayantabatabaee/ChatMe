package com.gravity.chatme.app.ui.status;

import com.gravity.chatme.business.model.User;

import java.util.ArrayList;

public interface StatusContract {

    interface view {
        void displayData(ArrayList<User> users);
    }

    interface presenter {
        void retrieveData();
    }

}
